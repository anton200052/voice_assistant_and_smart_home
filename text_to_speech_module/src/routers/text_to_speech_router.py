from fastapi import APIRouter, Request, HTTPException
from fastapi.responses import StreamingResponse
from src.infrastructure.text_to_speech_service import text_to_speech

router = APIRouter()


@router.post("/text-to-speech")
async def text_to_speech_router(request: Request):
    data = await request.json()

    if not data or 'text' not in data:
        raise HTTPException(status_code=400, detail="Missing 'text' in request body")

    text = data.get('text')
    audio_file = await text_to_speech(text)
    return StreamingResponse(audio_file, media_type="audio/mpeg", headers={
        "Content-Disposition": 'attachment; filename="audio.mp3"'
    })
