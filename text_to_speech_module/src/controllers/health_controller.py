from flask import jsonify


def handle_health_check():
    return jsonify({'state': 'UP'}), 200
