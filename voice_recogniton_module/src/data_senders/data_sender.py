import requests


#1
def sendRecognizedText(text):
    url = 'http://localhost:8080/recognition/recognized-text'
    data = {'text': text}

    try:
        response = requests.post(url, json=data, timeout=5)

        if response.status_code == 200:
            print("Успех")
        else:
            print(f"Ошибка: {response.status_code}")

    except requests.exceptions.ConnectionError:
        print("Ошибка: сервер не запущен или недоступен.")

    except requests.exceptions.Timeout:
        print("Ошибка: превышено время ожидания ответа от сервера.")

    except requests.exceptions.RequestException as e:
        print(f"Произошла ошибка: {e}")