# -*- coding: utf-8 -*-

import os
import numpy as np
import pandas as pd
import tensorflow as tf
import mariadb
from tensorflow.keras.preprocessing.text import Tokenizer
from tensorflow.keras.preprocessing.sequence import pad_sequences
from sklearn.model_selection import train_test_split

#model update를 위한 DB연결
conn = mariadb.connect(
    user="root",
    password="hkit301301",
    host="182.229.34.184",
    port=3306,
    database="301project"
)

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


