import threading
from src.infrastructure.recognition_state import RecognitionState
from src.infrastructure.recognizer import start_recognizer

state = RecognitionState()
recognition_thread = None


def start_recognition():
    global recognition_thread
    if recognition_thread is None or not recognition_thread.is_alive():
        state.start()
        recognition_thread = threading.Thread(target=start_recognizer, args=(state,))
        recognition_thread.start()


def stop_recognition():
    global recognition_thread
    state.stop()
    if recognition_thread is not None and recognition_thread.is_alive():
        recognition_thread.join()
        recognition_thread = None
