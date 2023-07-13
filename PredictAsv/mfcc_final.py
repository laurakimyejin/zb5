#필요한 모델들 임포트

import librosa
import numpy as np
from sklearn.model_selection import train_test_split
import os
import keras
import tensorflow as tf
from keras.layers import Dense

#-------------------------mfcc 활용-----------------------
RATE = 16000  # 샘플레이트, 본인 wav파일 샘플레이트 숫자 * 1000, 보통 오디오작업할때 44.1KHz이면 우리가 음악을 듣는 데 필요한 가청주파수 대역을 모두 커버함
              # 우리 프로젝트는 16KHz로 사용
DATA_PATH = r"C:\Users\HKIT\PycharmProjects\realproject\project\dataset1"         #데이터셋 폴더 경로 지정

X_data = []
y_data = []

for dataset in os.listdir(DATA_PATH):    #폴더 찾아가기
    folder_path = os.path.join(DATA_PATH, dataset)

    if os.path.isdir(folder_path):
        label = int(dataset)  # 폴더명을 정수로 변환하여 클래스 레이블로 사용
        files = os.listdir(folder_path)
        print(len(files), "파일")      #몇개의 파일이 있는지 확인
        for filename in files:
            file_path = os.path.join(folder_path, filename)

            if filename.endswith('.wav'):          #mfcc알고리즘 적용
                y, sr = librosa.load(file_path)    # y : time domain audio signal
                                                   # sr = sampling rate
                mfcc = librosa.feature.mfcc(y=y, sr=sr, n_mfcc=13, hop_length=int(sr * 0.01), n_fft=int(sr * 0.02)).T     #n_mfcc: mfcc coefficient 개수

                X_data.append(mfcc)
                for i in range(len(mfcc)):
                    y_data.append([0, 1] if label else [1, 0])  # [[0, 1], [0, 1], [1, 0], [1, 0]] 리스트 생성

X_data = np.concatenate(X_data)
y_data = np.array(y_data)

print("X_data :", X_data.shape)
print("Y_data :", y_data.shape)
X_train, X_test, y_train, y_test = train_test_split(X_data, y_data)      #모델에 학습할 train, test 설정

#---------------------모델 학습하기 -------------------------
adam = keras.optimizers.Adam(learning_rate=0.01)
model = tf.keras.Sequential()
model.add(Dense(units=256, activation='relu'))      # Dense층 나누기
model.add(Dense(units=128, activation='relu'))
model.add(Dense(units=2, activation='sigmoid'))
model.compile(loss="categorical_crossentropy", optimizer=adam, metrics=["accuracy"])
history = model.fit(X_train, y_train, epochs=20, batch_size=64, validation_data=(X_test, y_test))  #모델 훈련, epochs(훈련횟수)는 많을수록 좋음

#-------------------실제 테스트 부분---------------------------
y, sr = librosa.load(r"C:\Users\HKIT\Desktop\audio.wav")   #테스트 할 wav파일 경로
X_test = librosa.feature.mfcc(y=y, sr=sr, n_mfcc=13, hop_length=int(sr*0.01),n_fft=int(sr*0.02)).T
label = [0 for i in range(2)]
label[0] = 1.0     #학습한 모델의 레이블 지정, 0과 1이 존재하며 0을 정답으로 지정함
Y_test = []
for i in range(len(X_test)):
    Y_test.append(label)

#-----------------테스트한 wav파일이 변조된지 변조가 안된지 수치로 확인-------------------
model.evaluate(x=X_test, y=np.array(Y_test))

#------------------모델 저장-----------------
model.save('mfcc.h5')