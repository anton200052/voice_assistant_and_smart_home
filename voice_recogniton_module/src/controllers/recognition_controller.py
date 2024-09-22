from flask import jsonify
from src.services.recognition_service import start_recognition
from src.services.recognition_service import stop_recognition


def start_recognition_route():
    start_recognition()
    return jsonify({'message': 'Запускаем распознавание речи'}), 200


def stop_recognition_route():
    stop_recognition()
    return jsonify({'message': 'Останавливаем распознавание речи'}), 200
