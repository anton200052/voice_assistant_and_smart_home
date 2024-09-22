from flask import jsonify
from src.services.health_service import get_health_status


def health_check():
    status, code = get_health_status()
    return jsonify({'status': status}), 200
