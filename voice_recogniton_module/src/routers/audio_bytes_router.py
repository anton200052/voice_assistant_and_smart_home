from fastapi import APIRouter, WebSocket
from starlette.websockets import WebSocketDisconnect, WebSocketState
from src.services.speech_recognizer_service import initialize_recognizer
from src.services.speech_recognizer_service import recognize_data_chunk
import json

router = APIRouter()


@router.websocket("/recognize/audio/chunk")
async def websocket_endpoint(websocket: WebSocket):
    await websocket.accept()

    try:
        config_json = await wait_for_config_json(websocket)
        if config_json is None:
            return

        sample_rate = config_json.get("sample_rate")
        recognizer = await initialize_recognizer(sample_rate)
        await process_audio_bytes(recognizer, websocket)

    except WebSocketDisconnect:
        print("Клиент отключился.")

    except Exception as e:
        print("Ошибка:", e)

    finally:
        if websocket.client_state == WebSocketState.CONNECTED:
            try:
                await websocket.close()
            except RuntimeError:
                print("WebSocket уже был закрыт.")


async def wait_for_config_json(websocket):
    try:
        config_message = await websocket.receive_text()
        config_json = await validate_config_msg(config_message, websocket)
        return config_json

    except (json.JSONDecodeError, KeyError):
        await websocket.close(code=1003, reason="Invalid first request format. Required: JSON config")
        return None


async def validate_config_msg(config_msg, websocket):
    try:
        config_json = json.loads(config_msg)
    except json.JSONDecodeError:
        await websocket.close(code=1003, reason="Invalid JSON format")
        return None

    config = config_json.get("config")
    if not config or "sample_rate" not in config:
        await websocket.close(
            code=1003,
            reason="Invalid request format. Required: { \"config\" : { \"sample_rate\" : \"16000\" } }"
        )
        return None

    sample_rate = config.get("sample_rate")
    if not isinstance(sample_rate, int):
        await websocket.close(code=1003, reason="Sample rate must be an integer")
        return None

    return config


async def process_audio_bytes(recognizer, websocket):
    while True:
        data = await websocket.receive_bytes()
        if not isinstance(data, bytes):
            await websocket.close(code=1003, reason="Expected binary audio data")
            return

        result = await recognize_data_chunk(recognizer, data)
        if result:
            recognized_text = json.loads(result).get('text', "")
            await websocket.send_text(recognized_text)
