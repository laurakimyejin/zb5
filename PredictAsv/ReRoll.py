# -*- coding: utf-8 -*-

from flask import Flask, request, jsonify
from flask_restx import Api, Resource
from keras.utils import pad_sequences
from keras.models import load_model
import pickle as pk
import re
import codecs
import mariadb

#받은 text를 리스트에 담아야 model이 돌아갑니다
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


app = Flask(__name__)
api = Api(app)


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


if __name__ == "__main__":
    app.run(debug=False, host="0.0.0.0", port=5000)
