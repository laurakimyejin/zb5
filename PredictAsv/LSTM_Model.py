import os
import numpy as np
import pandas as pd
from tensorflow.keras.preprocessing.text import Tokenizer
from tensorflow.keras.preprocessing.sequence import pad_sequences
from sklearn.model_selection import train_test_split
import tensorflow as tf
import mariadb

#------MariaDB연결


conn = mariadb.connect(user = "root", password = "hkit301301", host = "182.229.34.184", port = 3306, database = "301project")

#DB메인코드

sql = "select * from flask;" #실행할 SQL문
 # 커서로 sql문 실행
conn.commit(sql)
conn.close()

#--------------경로지정--------------------
os.chdir(r"C:\Users\HKIT\PycharmProjects\realproject\project")

encoding = "UTF-8"  # "cp949" # "euc-kr"  # "ANSI"  # "ascii"

with open("0.txt", encoding=encoding) as f: a = f.read()
with open("1.txt", encoding=encoding) as f: b = f.read()
with open("1_.txt", encoding=encoding) as f: c = f.read()
with open(r"new_쇼핑.txt", encoding=encoding) as f: d = f.read()

a = [(i, 0) for i in a.split("\n")[:-1]]  # 일상 레이블은 0
b = [(i, 1) for i in b.split("\n")[:-1]]  # 스팸 레이블을 1
c = [(i, 1) for i in c.split("\n")[:-1]]  # 스팸 1
d = [(i, 0) for i in d.split("\n")[:-1]]  # 일상 0

data = a + b + c + d  #모델에 집어 넣을 데이터 셋을 data변수 안에 저장

X = [i[0] for i in data]
y = np.array([i[1] for i in data])

df = pd.DataFrame({"document": X, "label": y})

#-------------토크나이저 설정---------
#토크나이저 설정을 해야 문자 형식의 데이터를 모델이 학습 가능해짐
MAX_LEN = 1000    #최대 길이 설정
TRUNC = "pre"     #Trunc방식이 pre랑 post방식이 있는데 pre는 앞에서부터, post는 뒤에서부터 자르는 정도. 어디서 부터 자르냐에 따라 결과치가 달라짐
                  #우리 모델은 pre방식을 사용함

train_input, val_input, train_target, val_target = train_test_split(df["document"], df["label"], test_size=0.4, stratify=df["label"])

tokenizer = Tokenizer()
tokenizer.fit_on_texts(train_input)
train_sequences = tokenizer.texts_to_sequences(train_input)
train_seq = pad_sequences(train_sequences, maxlen=MAX_LEN, truncating=TRUNC)
val_sequences = tokenizer.texts_to_sequences(val_input)
val_seq = pad_sequences(val_sequences, maxlen=MAX_LEN, truncating=TRUNC)

#--------------모델 층 나누기(모델제작)-----------
model2 = tf.keras.Sequential()
model2.add(tf.keras.layers.Embedding(input_dim=100000, output_dim=64, input_length=MAX_LEN))   #뉴런수(imput_dim)를 10만으로 설정. 우리 데이터 셋이 많다보니 최소 10만을 줘야함
model2.add(tf.keras.layers.Bidirectional(tf.keras.layers.LSTM(2, return_sequences=True)))      #LSTM알고리즘 적용하여 모델 층 나누기
model2.add(tf.keras.layers.Bidirectional(tf.keras.layers.LSTM(2, return_sequences=False)))
model2.add(tf.keras.layers.Dropout(rate=0.3))                                                  #Dropout 설정
model2.add(tf.keras.layers.Dense(1, activation="sigmoid"))

#----------모델 학습------------
model2.compile(loss="binary_crossentropy", optimizer="adam", metrics=["accuracy"])
history = model2.fit(train_seq, train_target, epochs=50, batch_size=32, validation_data=(val_seq, val_target))    #학습횟수(epochs)가 크면 좋지만 학습시간이 느려져서 50정도로만 줌


# 저장할 폴더 경로
folder_path = r'C:\Users\HKIT\PycharmProjects\realproject\project\h5'

# 저장할 파일 이름
file_name = 'modelVer.h5'

# 이미 있는 파일들의 목록 가져오기
existing_files = os.listdir(folder_path)

# 같은 이름의 파일이 이미 존재하는지 확인
if file_name in existing_files:
    # 다른 이름으로 저장할 파일의 번호 결정
    next_number = 1
    while True:
        next_file_name = f'{file_name.split(".")[0]}_{next_number}.h5'
        if next_file_name not in existing_files:
            break
        next_number += 1
else:
    # 같은 이름의 파일이 없을 경우 그대로 저장
    next_file_name = file_name

# 저장할 파일의 경로 생성
next_file_path = os.path.join(folder_path, next_file_name)

# 모델 저장
model2.save(next_file_path)







