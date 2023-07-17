# -*- coding: utf-8 -*-

#C:\Users\smoke\OneDrive\바탕 화면\project\YhdbPrj3213\PredictAsv
#pip install tensorflow
#pip install keras
#pip install mariadb
#pip install librosa
#pip install flask
#pip install flask_restx
#pip install SpeechRecognition
#pip install pydub

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

#테스트 모드를 할때(단독실행)는 m4a폴더를 만들어주어야 한다.
TEST_MODE = True #서버랑 연동할때는 False
M4A_PATH = "./m4a"

#wav파일과 stt를 위한 잘린wav파일들을 저장하는 장소 만들어주기
SPLITWAV_PATH = "./cut_wav"
WAV_PATH = "./convert_wav"
m4a_filename = ""

#진행상황과 결과값을 주기위한 url설정
portnumber = "http://127.0.0.1:9966"

#log찍으려고 하는것(난이도 설정가능함)
logging.basicConfig(level=logging.INFO)


#내부 기능들이 돌아가면서 얼만큼 작업이 되고 있는지 확인시켜주기
def notify_file_received(user_id, declaration):
    url = portnumber + f"/api/progress/{user_id}/{declaration}"
    payload = {"progress": "25%"}
    response = requests.post(url, json=payload)
    if response.status_code == 200:
        print("File received successfully (25%)")
    else:
        print("Failed file received")


#읽어온 m4a파일을 wav로 변환시켜주고 포멧시켜주기
def m4a_wav_convert(path, WAV_PATH):
    logging.info("m4a_wav_convert")
    encoded_path = urllib.parse.unquote(path)
    encoded_path = re.sub('가-힣ㄱ-ㅎ', "", encoded_path)
    wav_filename = os.path.basename(encoded_path).replace('.m4a', '.wav')
    wav_dst = os.path.join(WAV_PATH, wav_filename)
    m4a_src = AudioSegment.from_file(encoded_path, format="m4a", encoding="utf-8")
    m4a_src.export(wav_dst, format="wav")
    return wav_dst


#변환된 wav파일을 읽어와서 mfcc(파형분석)화 시키기
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


#내부 기능들이 돌아가면서 얼만큼 작업이 되고 있는지 확인시켜주기
def notify_wav_conversion(user_id, declaration):
    url = portnumber + f"/api/progress/{user_id}/{declaration}"
    payload = {"progress": "50%"}
    response = requests.post(url, json=payload)
    if response.status_code == 200:
        print("WAV conversion successfully (50%)")
    else:
        print("Failed WAV conversion")


#wav파일을 잘라서 저장하기 위한 함수처리(cut_wav함수와 같이 작동)
def trim_audio_data(wav_filename, start_time=0.0, sec=120):
    logging.info("trim_audio_data")
    sample_rate = 44100
    audio_array, sample_rate = librosa.load(wav_filename, sr=sample_rate)
    audio_splitted = audio_array[start_time * sample_rate:sample_rate * (sec + start_time)]
    sf.write(
        os.path.join(SPLITWAV_PATH, os.path.basename(wav_filename.replace(".wav", f"_{str(start_time).zfill(5)}.wav"))),
        audio_splitted, sample_rate)
    return


#stt를 하기 위해서 wav파일을 120초씩 잘라주고 저장(너무 길면 stt안해줌)
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


#잘린wav 파일들을 리스트에 담기
def get_datalist(wav_filename):
    logging.info("get_datalist")
    basename = os.path.basename(wav_filename)
    data_list = [i for i in os.listdir(SPLITWAV_PATH) if i.startswith(os.path.splitext(basename)[0])]
    return data_list


#리스트에 담긴 파일들을 읽어와서 stt변환을 시켜서 다른 빈리스트에 담아두기([1,2,3]이런식으로)
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


#내부 기능들이 돌아가면서 얼만큼 작업이 되고 있는지 확인시켜주기
def notify_stt_conversion(user_id, declaration):
    url = portnumber + f"/api/progress/{user_id}/{declaration}"
    payload = {"progress": "75%"}
    response = requests.post(url, json=payload)
    if response.status_code == 200:
        print("STT conversion successfully (75%)")
    else:
        print("Failed STT conversion")


#리스트에 담아둔 stt텍스트들을 하나로 합치기([1,2,3]-->[123]이런식으로)
def concatenate_texts(text_list):
    concatenated_text = ' '.join(text_list)
    return concatenated_text


#model폴더에서 tokenizer_pre.pickle와 model_pre.h5 그리고 mfcc.h5불러오기
with open("./model/tokenizer_pre.pickle", "rb") as f:
    tokenizer1 = pk.load(f)
# model1 = load_model("./model/model_pre.h5")
# model2 = load_model("./model/mfcc.h5")
model1 = load_model(r"C:\model_pre.h5")
model2 = load_model(r"C:\mfcc.h5")


#tokenizer_pre.pickle와 model_pre.h5를 가지고 STT했던 텍스트 돌리고 결과값1 얻기
#결과값이 1이면 보이스피싱/ 0이면 보이스피싱 아님
def predict1(string):
    logging.info("voicemodel")
    real_sequences1 = tokenizer1.texts_to_sequences([string])
    real_seq1 = pad_sequences(real_sequences1, maxlen=1000, truncating="pre")
    result1 = model1.predict(real_seq1)

    if (result1 >= 0.35):
        detect = 1
    else:
        detect = 0
    return detect


#mfcc.h5를 가지고 mfcc화 했던 wav돌리고 결과값2 얻기
#결과값이 1이면 변조됨/ 0이면 변조된거 아님
def predict2(X_data, y_data):
    logging.info("mfccmodel")
    result2 = model2.evaluate(x=X_data, y=y_data)[1]

    if (result2 >= 0.5):
        result_mfcc = 1
    else:
        result_mfcc = 0
    return result_mfcc


#내부 기능들이 돌아가면서 얼만큼 작업이 되고 있는지 확인시켜주기
def notify_prediction(user_id, declaration):
    url = portnumber + f"/api/progress/{user_id}/{declaration}"
    payload = {"progress": "100%"}
    response = requests.post(url, json=payload)
    if response.status_code == 200:
        print("Prediction successfully")
    else:
        print("Failed prediction")


#flask를 api로 설정(객체생성)
app = Flask(__name__)
api = Api(app)


#POST요청을 받은 뒤 기능작동
@api.route("/api/VoiClaReq/<string:user_id>/<string:declaration>", methods=["POST"])
class HelloWorld(Resource):
    def post(self, user_id, declaration):
        global SPLITWAV_PATH, WAV_PATH, portnumber, m4a_filename, M4A_PATH, TEST_MODE
        if request.method == 'POST':
            #spring서버에서 저장한 파일경로를 읽어서 m4a파일 찾기
            if TEST_MODE:
                file = request.files["file"]
                m4a_filename = os.path.join(M4A_PATH, file.filename)
                file.save(m4a_filename)
            else:
                file = request.form["file"]
            notify_file_received(user_id, declaration)
            if TEST_MODE:
                wav_filename = m4a_wav_convert(m4a_filename, WAV_PATH)
            else:
                wav_filename = m4a_wav_convert(file, WAV_PATH)
            FileName = wav_filename.replace(WAV_PATH, "")
            notify_wav_conversion(user_id, declaration)
            cut_wav(wav_filename)
            data_list = get_datalist(wav_filename)
            stt_result_list = transcribe_audio(data_list)
            notify_stt_conversion(user_id, declaration)
            text_final = concatenate_texts(stt_result_list)
            prediction = predict1(text_final)
            X_data, y_data = wav_mfcc(wav_filename)
            result_mfcc = predict2(X_data, y_data)
            notify_prediction(user_id, declaration)

            data = {
                'voiceResult': prediction,
                'mfccResult': result_mfcc
            }

            #마지막 결과값 2개를 url주소로 넘겨주기(비동기라 return안받는다고 함)
            url = portnumber + f"/api/progress/{user_id}/{declaration}"
            requests.post(url, json=data)


            #여기서부터는 기능들이 다 돌아가고 난뒤 DB에 저장하는 내용
            declaration = re.sub("[^0-9]", "", declaration)
            # conn = mariadb.connect(
            #   user="root",
            #   password="hkit301301",
            #   host="182.229.34.184",
            #   port=3306,
            #   database="301project",
            # )
            #
            # cursor = conn.cursor()
            # query = f"""INSERT INTO voicedata(user_id,declaration,audio_file,content,disdata,created_date,mfcc) VALUES('{user_id}','{declaration}','{FileName}','{text_final}','{prediction}',NOW(),'{result_mfcc}')"""
            # cursor.execute(query)
            # conn.commit()
            # cursor.close()
            # conn.close()

            if TEST_MODE:
                for filename1 in os.listdir(M4A_PATH):
                    file_path1 = os.path.join(M4A_PATH, filename1)
                    if os.path.isfile(file_path1):
                        os.remove(file_path1)
                        print(f"{filename1} 파일이 삭제되었습니다.")

            #120초씩 잘랐던 파일들을 삭제하는 내용(용량줄이기)
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
