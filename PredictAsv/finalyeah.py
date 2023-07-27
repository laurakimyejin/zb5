# -*- coding: utf-8 -*-

from flask import Flask, request, jsonify
from flask_restx import Api, Resource
from keras.utils import pad_sequences
from keras.models import load_model
from pydub import AudioSegment
import speech_recognition as sr
import soundfile as sf
import pickle as pk
import numpy as np
import urllib.parse
import requests
import mariadb
import librosa
import math
import os
import re
import logging
# Reroll을 위한 추가 구문
import codecs
# ModelUpdate를 위한 추가 구문
import pandas as pd
import tensorflow as tf
from tensorflow.keras.preprocessing.text import Tokenizer
from tensorflow.keras.preprocessing.sequence import pad_sequences
from sklearn.model_selection import train_test_split

#################################finalyeah.py####################################

# 테스트 모드를 할때(단독실행)는 m4a폴더를 만들어주어야 한다.
TEST_MODE = True  # 스프링에 연동할때 False
UBUNTO_PASS = False  # 파이썬 파일이 WSL에서 구동될때

M4A_PATH = "./m4a"

# wav파일과 stt를 위한 잘린wav파일들을 저장하는 장소 만들어주기
SPLITWAV_PATH = "./cut_wav"
WAV_PATH = "./convert_wav"
m4a_filename = ""

# 진행상황과 결과값을 주기위한 url설정
portnumber = "http://182.229.34.184:5502"  # 바꾸세요 ^^7
# portnumber = "http://127.0.0.1:5502"  # 바꾸세요 ^^7

# log찍으려고 하는것(난이도 설정가능함)
logging.basicConfig(level=logging.INFO)


def notify_progress(idx, declaration, progress, work):
    url = portnumber + f"/api/progress/{idx}/{declaration}"
    payload = {"progress": f"{progress}"}
    response = requests.post(url, json=payload)

    if response.status_code == 200:
        print(progress + work + "successfully")
    else:
        print(progress + work + "Failed")


# 읽어온 m4a파일을 wav로 변환시켜주고 포멧시켜주기
def m4a_wav_convert(path, WAV_PATH):
    logging.info("m4a_wav_convert")
    encoded_path = urllib.parse.unquote(path)
    encoded_path = re.sub('가-힣ㄱ-ㅎ', "", encoded_path)
    wav_filename = os.path.basename(encoded_path).replace('.m4a', '.wav')
    wav_dst = os.path.join(WAV_PATH, wav_filename)
    m4a_src = AudioSegment.from_file(encoded_path, format="m4a", encoding="utf-8")
    m4a_src.export(wav_dst, format="wav")
    return wav_dst


# 변환된 wav파일을 읽어와서 mfcc(파형분석)화 시키기
def wav_mfcc(wav_dst):
    logging.info("wav_mfcc")
    X_data = []
    y_data = []
    y, sr = librosa.load(wav_dst)
    mfcc = librosa.feature.mfcc(y=y, sr=sr, n_mfcc=13, hop_length=int(sr * 0.01), n_fft=int(sr * 0.02)).T
    X_data.append(mfcc)
    for _ in range(len(X_data[0])):
        y_data.append([0, 1] if mfcc.shape[0] > 0 else [1, 0])
    y_data = np.array(y_data)
    return X_data, y_data


# wav파일을 잘라서 저장하기 위한 함수처리(cut_wav함수와 같이 작동)
def trim_audio_data(wav_filename, start_time=0.0, sec=120):
    logging.info("trim_audio_data")
    sample_rate = 44100
    audio_array, sample_rate = librosa.load(wav_filename, sr=sample_rate)
    audio_splitted = audio_array[start_time * sample_rate:sample_rate * (sec + start_time)]
    sf.write(
        os.path.join(SPLITWAV_PATH, os.path.basename(wav_filename.replace(".wav", f"_{str(start_time).zfill(5)}.wav"))),
        audio_splitted, sample_rate)
    return


# stt를 하기 위해서 wav파일을 120초씩 잘라주고 저장(너무 길면 stt안해줌)
def cut_wav(wav_filename):
    logging.info("cut_wav")
    f = sf.SoundFile(wav_filename)
    total_sec = f.frames // f.samplerate
    split_sec = 120
    interval_count = math.ceil(total_sec / split_sec)
    for i in range(interval_count):
        if i * split_sec > total_sec:
            break
        trim_audio_data(wav_filename, i * split_sec, split_sec)
    return


# 잘린wav 파일들을 리스트에 담기
def get_datalist(wav_filename):
    logging.info("get_datalist")
    basename = os.path.basename(wav_filename)
    data_list = [i for i in os.listdir(SPLITWAV_PATH) if i.startswith(os.path.splitext(basename)[0])]
    return data_list


# 리스트에 담긴 파일들을 읽어와서 stt변환을 시켜서 다른 빈리스트에 담아두기([1,2,3]이런식으로)
def transcribe_audio(data_list):
    logging.info("transcribe_audio")
    text_list = []
    for i in data_list:
        r = sr.Recognizer()
        with sr.AudioFile(os.path.join(SPLITWAV_PATH, i)) as source:
            audio = r.record(source)
        text = r.recognize_google(audio, language='ko-KR')
        text_list.append(text)
    return text_list


# 리스트에 담아둔 stt텍스트들을 하나로 합치기([1,2,3]-->[123]이런식으로)
def concatenate_texts(text_list):
    concatenated_text = ' '.join(text_list)
    return concatenated_text


# model폴더에서 tokenizer_pre.pickle와 model_pre.h5 그리고 mfcc.h5불러오기
with open("./model/tokenizer_pre.pickle", "rb") as f:
    tokenizer1 = pk.load(f)
model1 = load_model("./model/model_pre.h5")
model2 = load_model("./model/mfcc.h5")


# tokenizer_pre.pickle와 model_pre.h5를 가지고 STT했던 텍스트 돌리고 결과값1 얻기
# 결과값이 1이면 보이스피싱/ 0이면 보이스피싱 아님
def predict1(string):
    logging.info("voicemodel")
    real_sequences1 = tokenizer1.texts_to_sequences([string])
    real_seq1 = pad_sequences(real_sequences1, maxlen=1000, truncating="pre")
    result1 = model1.predict(real_seq1)

    return result1

    # if (result1 >= 0.35):
    #    detect = 1
    # else:
    #    detect = 0
    # return detect


# mfcc.h5를 가지고 mfcc화 했던 wav돌리고 결과값2 얻기
# 결과값이 1이면 변조됨/ 0이면 변조된거 아님
def predict2(X_data, y_data):
    logging.info("mfccmodel")
    result2 = model2.evaluate(x=X_data, y=y_data)[1]

    if (result2 >= 0.5):
        result_mfcc = 1
    else:
        result_mfcc = 0
    return result_mfcc


# flask를 api로 설정(객체생성)
app = Flask(__name__)
api = Api(app)

##################################ReRoll.py##################################
def reroll():
    text_read = []

    with open("./model/tokenizer_pre.pickle", "rb") as f:
        tokenizer1 = pk.load(f)
    model1 = load_model("./model/model_pre.h5")


    def predict(string):
        real_sequences1 = tokenizer1.texts_to_sequences([string])
        real_seq1 = pad_sequences(real_sequences1, maxlen=1000, truncating="pre")
        result1 = model1.predict(real_seq1)

        if (result1 >= 0.35):
            detect = 1
        else:
            detect = 0
        return detect

#############################ModelUpdate.py##################################

def modelupdate():
    #한글이 깨져서 불러올 수도 있으니 UTF-8로 인코딩해주고 결과값1과 함께 불러오기
    cursor = conn.cursor()
    query = "SELECT CONVERT(content USING UTF8),disdata from voicedata"
    cursor.execute(query)
    result = cursor.fetchall()
    cursor.close()
    conn.close()

    #불러온 텍스트를 X에 결과값1을 y에 각각 리스트로 담고 y는 float64로 타입을 변경을 시켜줘야 됨
    X = [i[0] for i in result]
    y = np.array([i[1] for i in result]).astype('float64')
    df = pd.DataFrame({"document": X, "label": y})

    #Tokenizer설정
    MAX_LEN = 1000
    TRUNC = "pre"
    train_input, val_input, train_target, val_target = train_test_split(df["document"], df["label"], test_size=0.4, stratify=df["label"])
    tokenizer = Tokenizer()
    tokenizer.fit_on_texts(train_input)
    train_sequences = tokenizer.texts_to_sequences(train_input)
    train_seq = pad_sequences(train_sequences, maxlen=MAX_LEN, truncating=TRUNC)
    val_sequences = tokenizer.texts_to_sequences(val_input)
    val_seq = pad_sequences(val_sequences, maxlen=MAX_LEN, truncating=TRUNC)


    #model설정==만들기
    model = tf.keras.Sequential()
    model.add(tf.keras.layers.Embedding(input_dim=100000, output_dim=64, input_length=MAX_LEN))
    model.add(tf.keras.layers.Bidirectional(tf.keras.layers.LSTM(2, return_sequences=True)))
    model.add(tf.keras.layers.Bidirectional(tf.keras.layers.LSTM(2, return_sequences=False)))
    model.add(tf.keras.layers.Dropout(rate=0.3))
    model.add(tf.keras.layers.Dense(1, activation="sigmoid"))
    model.compile(loss="binary_crossentropy", optimizer="adam", metrics=["accuracy"])
    model.fit(train_seq, train_target, epochs=50, batch_size=32, validation_data=(val_seq, val_target))

    #저장할 model이름과 폴더 지정
    folder_path = './model'
    file_name = 'modelVer.h5'
    existing_files = os.listdir(folder_path)

    #같은 이름으로 저장을 할시 덮어씌우기를 하기때문에 이름을 다르게 해주기 위한 코드
    #무작정 modelupdate를 하게 되면 성능이 저하된 상태로 덮어씌우기가 되니 주의할 점이라서
    if file_name in existing_files:
        next_number = 1
        while True:
            next_file_name = f'{file_name.split(".")[0]}_{next_number}.h5'
            if next_file_name not in existing_files:
                break
            next_number += 1
    else:
        next_file_name = file_name

    #이름과 경로를 설정해서 model저장하기
    next_file_path = os.path.join(folder_path, next_file_name)
    model.save(next_file_path)

######################################################################################################
# Reroll - api
@api.route("/api/text/<int:idx>/<string:declaration>", methods=["POST"])
class HelloWorld(Resource):
    def post(self, idx, declaration):
        global text_read
        declaration = re.sub("[^0-9]", "", declaration)
        text = request.form.get('text')
        decoded_text = codecs.decode(text.encode(), 'utf-8')
        text_read.append(decoded_text)
        prediction = predict(text_read)

        data = {
            'idx': idx,
            'phone': declaration,
            'result': prediction
        }

        conn = mariadb.connect(
            user="root",
            password="hkit301301",
            host="182.229.34.184",
            port=3306,
            database="301project",
        )
        cursor = conn.cursor()
        query = f"""UPDATE voicedata SET reroll='{prediction}' where idx='{idx}' and declaration='{declaration}'"""
        cursor.execute(query)
        conn.commit()
        cursor.close()
        conn.close()
        return jsonify(data)


# ModelUpdate - api
@api.route("/api/modelupdate/<int:idx>/<string:declaration>", methods=["POST"])
class HelloWorld(Resource):
    def post(self, idx, declaration):


# Finalyeah - api POST요청을 받은 뒤 기능작동
@api.route("/api/VoiClaReq/<int:idx>/<string:declaration>", methods=["POST"])
class HelloWorld(Resource):
    def post(self, idx, declaration):

        global SPLITWAV_PATH, WAV_PATH, portnumber, m4a_filename, M4A_PATH, TEST_MODE
        if request.method == 'POST':
            # print("Dddddddddddddddddddddd")
            # spring서버에서 저장한 파일경로를 읽어서 m4a파일 찾기
            # from은 spring과 연결을 할때쓰기, files는 단독실행시
            notify_progress(idx, declaration, "0%", "--Post")
            # print("zzzzzzzzzzzzzzzzzzzzzz")
            if TEST_MODE:
                file = request.files["file"]
                m4a_filename = os.path.join(M4A_PATH, file.filename)
                file.save(m4a_filename)
            else:
                file = request.form["file"]
            # notify_file_received(idx, declaration)
            if TEST_MODE:
                wav_filename = m4a_wav_convert(m4a_filename, WAV_PATH)
            else:
                if UBUNTO_PASS:
                    file = "/mnt/c" + file[2:]
                wav_filename = m4a_wav_convert(file, WAV_PATH)
            notify_progress(idx, declaration, "10%", "--File_receive")

            FileName = wav_filename.replace(WAV_PATH, "")
            notify_progress(idx, declaration, "20%", "--wav_filename.replace")

            cut_wav(wav_filename)
            notify_progress(idx, declaration, "30%", "--cut_wav")

            data_list = get_datalist(wav_filename)
            notify_progress(idx, declaration, "40%", "--get_datalist")

            stt_result_list = transcribe_audio(data_list)
            notify_progress(idx, declaration, "50%", "--transcribe_audio")

            text_final = concatenate_texts(stt_result_list)
            notify_progress(idx, declaration, "60%", "--concatenate_texts")

            prediction = predict1(text_final)
            notify_progress(idx, declaration, "70%", "--voice_complete")

            X_data, y_data = wav_mfcc(wav_filename)
            notify_progress(idx, declaration, "80%", "--wav_mfcc")

            result_mfcc = predict2(X_data, y_data)
            notify_progress(idx, declaration, "90%", "--mfcc_complete")
            persent = prediction

            if (prediction >= 0.35):
                detect = 1
            else:
                detect = 0

            notify_progress(idx, declaration, "100%", "--all_work_complete")

            data = {
                'progress': '100%',
                'voiceResult': detect,
                'mfccResult': result_mfcc
            }

            # 마지막 결과값 2개를 url주소로 넘겨주기(비동기라 return안받는다고 함)
            url = portnumber + f"/api/progress/{idx}/{declaration}"
            requests.post(url, json=data)

            # 여기서부터는 기능들이 다 돌아가고 난뒤 DB에 저장하는 내용
            declaration = re.sub("[^0-9]", "", declaration)
            conn = mariadb.connect(
                user="root",
                password="1234",
                host="127.0.0.1",
                port=3306,
                database="sample",
            )

            cursor = conn.cursor()
            #  query = f"""INSERT INTO voicedata(idx,declaration,audio_file,content,disdata,created_date,mfcc) VALUES('{idx}','{declaration}','{FileName}','{text_final}','{prediction}',NOW(),'{result_mfcc}')"""
            # query = f"""INSERT INTO voicedata(idx,declaration,audio_file,content,disdata,created_date,mfcc) VALUES('{idx}','{declaration}','{FileName}','{text_final}','{prediction}',NOW(),'{result_mfcc}')"""

            query = f"""INSERT INTO voicedata (idx, declaration, audio_file, content, disdata, created_date, modified_date, reroll, mfcc, admindata, persent ) VALUES ( '{idx}','{declaration}','{FileName}','{text_final}','{prediction}',NOW(),'NOW()','null','{result_mfcc}',null,'{persent}')"""

            cursor.execute(query)
            conn.commit()
            cursor.close()
            conn.close()

            if TEST_MODE:
                for filename1 in os.listdir(M4A_PATH):
                    file_path1 = os.path.join(M4A_PATH, filename1)
                    if os.path.isfile(file_path1):
                        os.remove(file_path1)
                        print(f"{filename1} 파일이 삭제되었습니다.")

            # 120초씩 잘랐던 파일들을 삭제하는 내용(용량줄이기)
            for filename2 in os.listdir(SPLITWAV_PATH):
                file_path2 = os.path.join(SPLITWAV_PATH, filename2)
                if os.path.isfile(file_path2):
                    os.remove(file_path2)
                    print(f"{filename2} 파일이 삭제되었습니다.")

            return jsonify(data)


if __name__ == "__main__":
    if not os.path.exists(WAV_PATH):
        os.mkdir(WAV_PATH)
        pass
    if not os.path.exists(SPLITWAV_PATH):
        os.mkdir(SPLITWAV_PATH)
        pass
    if TEST_MODE:
        if not os.path.exists(M4A_PATH):
            os.mkdir(M4A_PATH)
            pass
    app.run(debug=False, host="0.0.0.0", port=9966)
