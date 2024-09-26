from flask import jsonify
from src.infrastructure.recognition_starter import isRecognitionThreadAlive


def health_check():
    if isRecognitionThreadAlive():
        return jsonify({'state': 'UP'}), 200
    else:
        return jsonify({'state': 'DOWN'}), 200
