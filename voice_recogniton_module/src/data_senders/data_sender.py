import requests


def sendRecognizedText(text):
    url = 'http://localhost:8080/recognized-text'
    data = {
        'text': text,
    }

    response = requests.post(url, json=data)

    if response.status_code == 200:
        print("Ответ:", response.json())
    else:
        print("Ошибка: {response.status_code}")
