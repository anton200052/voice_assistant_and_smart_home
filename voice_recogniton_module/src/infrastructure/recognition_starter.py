import threading
from src.infrastructure.recognition_state import RecognitionState
from src.infrastructure.vosk_recognizer import start_recognizer

state = RecognitionState()
recognition_thread = None


def start_recognition():
    global recognition_thread
    if recognition_thread is None or not recognition_thread.is_alive():
        state.start()
        recognition_thread = threading.Thread(target=start_recognizer, args=(state,))
        recognition_thread.start()


def isRecognitionThreadAlive():
    global recognition_thread
    return recognition_thread is not None and recognition_thread.is_alive()
