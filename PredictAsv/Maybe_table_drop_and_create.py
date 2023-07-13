# -*- coding: utf-8 -*-

#이 파일은 만약에 table을 지우고 다시 생성시 데이터들을 넣어주기 위한 코드들
#text2라는 폴더에 이전에 만들어준 파일들(학습에 쓰인 파일들)이 있어야 합니다.
#tqdm은 진행상황 보고 싶어서(어디까지 들어가고있나)쓰이는 모듈

# %pip install faker 이것 외에도 필요한 모듈들 설치하기
from faker import Faker
import random
import mariadb
from tqdm import tqdm

#모든 txt파일들을 불러와서 객체에 담아준다
with open("./text2/0.txt", encoding="utf-8") as f:
    txt1 = f.read()
with open("./text2/1.txt", encoding="utf-8") as f:
    txt2 = f.read()
with open("./text2/1_.txt", encoding="utf-8") as f:
    txt3 = f.read()
with open("./text2/shopping_data.txt", encoding="cp949") as f:
    txt4 = f.read()

#현재는 모든 txt들이 하나하나가 한 덩어리로 있기에 떼어주기 위한 코드들
#여기서 content,disdata,mfcc가 생김
text_list = [(i, 0, 0) for i in txt1.split("\n")]
text_list += [(i, 1, 0) for i in txt2.split("\n")]
text_list += [(i, 1, 1) for i in txt3.split("\n")]
text_list += [(i, 0, 0) for i in txt4.split("\n")]

#''이나 ""이 들어가있으면 오류가 나기에 없애주고 시작한다
txt = [(i[0].replace("'", ""), i[1], i[2]) for i in text_list]

#Faker와 random이라는 모듈을 통해 임시로
#user_id,declaration,audio_file,created_date들을 생성해줄거임
faker = Faker("ko-kr")
user_list = [faker.profile()["username"] for i in range(10)]
phone_list = ["010" + str(random.randint(11111111, 99999999)) for i in range(4523)]
file_list = [str(random.randint(111, 9999)) + ".wav" for i in range(4523)]
date_list = [f"{random.randint(2001, 2023)}-{str(random.randint(1, 12)).zfill(2)}-{str(random.randint(1, 28)).zfill(2)}" for _ in range(4523)]

#DB를 연결시켜주기
conn = mariadb.connect(
            user="root",
            password="hkit301301",
            host="182.229.34.184",
            port=3306,
            database="301project",
            )

cursor = conn.cursor()

#위에서 만들어진 txt를 마음대로 섞어준다
#안하면 너무 정갈하게 0인 범위와 1인 범위가 만들어짐으로
random.shuffle(txt)

#섞인 txt(content,disdata)와 user_id,declaration,audio_file,created_date를
#잘 넣어주면 됨
for idx, i in tqdm(enumerate(txt)):
    query = f"""INSERT INTO voicedata(user_id,declaration,audio_file,content,disdata,created_date,mfcc) VALUES('{user_list[idx % 10]}','{phone_list[idx % 10]}','{file_list[idx]}','{i[0]}', '{i[1]}', '{date_list[idx]}','{i[2]}')"""
    cursor.execute(query)
    conn.commit()

#다하고 나면 닫아줘야 이후에 오류가 생기지 않음
cursor.close()
conn.close()
#닫기까지 하고 나서는 잘 담겨져있는지 확인하기
#위에서 insert한 칼럼들 외에는 null로 표시가 되어있으니
