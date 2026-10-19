from fastapi import FastAPI, Header
import httpx

app = FastAPI()

@app.get("/gateway")

async def gateway(
    authorization: str = Header(None)
):

    headers = {
        "Authorization": authorization
    }

    async with httpx.AsyncClient() as client:

        response = await client.get(
            "http://localhost:8080/validate",
            headers=headers
        )

    return response.json()