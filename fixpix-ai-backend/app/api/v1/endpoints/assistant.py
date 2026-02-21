from fastapi import APIRouter, HTTPException
from pydantic import BaseModel
from typing import List, Dict, Optional, Any
import logging

from app.engine.assistant.service import chat_service

router = APIRouter()
logger = logging.getLogger(__name__)

class Message(BaseModel):
    role: str
    content: str

class ChatRequest(BaseModel):
    messages: List[Message]

class ChatResponse(BaseModel):
    role: str
    content: str
    tool_call: Optional[Dict[str, Any]] = None

from fastapi.responses import StreamingResponse
import asyncio

@router.get("/stream")
async def stream_chat(message: str, session_id: str):
    """
    Stream response via SSE.
    """
    async def event_generator():
        try:
            async for token in chat_service.chat_stream_generator(message, session_id):
                yield f"data: {token}\n\n"
            yield "data: [DONE]\n\n"
        except Exception as e:
            yield f"data: [Error: {str(e)}]\n\n"

    return StreamingResponse(event_generator(), media_type="text/event-stream")

@router.post("/chat", response_model=ChatResponse)
async def chat(request: ChatRequest):
    """
    Chat with FixPix Assistant.
    """
    try:
        # Convert pydantic models to dicts
        msgs = [m.model_dump() for m in request.messages]
        response = await chat_service.process_message(msgs)
        return response
    except Exception as e:
        logger.error(f"Chat failed: {e}")
        raise HTTPException(status_code=500, detail="Assistant failed to respond")
