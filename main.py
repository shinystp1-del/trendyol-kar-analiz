
import os
from datetime import datetime, timezone

import httpx
from fastapi import FastAPI, HTTPException
from fastapi.middleware.cors import CORSMiddleware
from pydantic import BaseModel, Field

REEF_API_KEY = os.getenv("REEF_API_KEY", "")

REEF_URL = "https://api.reefapi.com/trendyol/v1/search"

app = FastAPI(title="Trendyol Kar Analiz API")

app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],
    allow_methods=["*"],
    allow_headers=["*"],
)

class SearchRequest(BaseModel):
    query: str = Field(min_length=2)
    max_pages: int = 2
    price: float | None = None

@app.get("/")
async def home():
    return {"message": "Trendyol Kar Analiz API aktif"}

@app.get("/health")
async def health():
    return {
        "status": "ok",
        "api_configured": bool(REEF_API_KEY)
    }

@app.post("/search")
async def search_products(request: SearchRequest):

    if not REEF_API_KEY:
        raise HTTPException(
            status_code=503,
            detail="REEF_API_KEY ayarlanmamış"
        )

    payload = {
        "query": request.query,
        "max_pages": request.max_pages
    }

    if request.price is not None:
        payload["price"] = request.price

    try:
        async with httpx.AsyncClient(timeout=45) as client:
            response = await client.post(
                REEF_URL,
                headers={"x-api-key": REEF_API_KEY},
                json=payload
            )

        if response.status_code >= 400:
            raise HTTPException(
                status_code=502,
                detail="ReefAPI bağlantı hatası"
            )

        return {
            "data": response.json(),
            "fetched_at": datetime.now(
                timezone.utc
            ).isoformat()
        }

    except httpx.RequestError:
        raise HTTPException(
            status_code=502,
            detail="ReefAPI bağlantısı başarısız"
        )

