# auth_utils.py
import os
import jwt
from fastapi import HTTPException, status
from dotenv import load_dotenv
load_dotenv()


# ✅ Spring Boot 의 jwt.secret 문자열 그대로 가져오기
JWT_SECRET = os.getenv("JWT_SECRET", "")
JWT_ALGORITHM = os.getenv("JWT_ALGORITHM", "HS256")

def decode_jwt(token: str):
    try:
        # Spring의 Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8)) 와 동일
        secret_bytes = JWT_SECRET.encode("utf-8")

        payload = jwt.decode(
            token,
            secret_bytes,
            algorithms=[JWT_ALGORITHM]
        )
        #print("✅ [DEBUG] JWT payload:", payload)
        return payload

    except jwt.ExpiredSignatureError:
        print("❌ [DEBUG] Token has expired")
        raise HTTPException(status_code=status.HTTP_401_UNAUTHORIZED, detail="Token expired")
    except jwt.InvalidSignatureError:
        print("❌ [DEBUG] Invalid token signature")
        raise HTTPException(status_code=status.HTTP_401_UNAUTHORIZED, detail="Invalid signature")
    except jwt.DecodeError:
        print("❌ [DEBUG] Token decode error")
        raise HTTPException(status_code=status.HTTP_401_UNAUTHORIZED, detail="Invalid token format")
    except Exception as e:
        print("❌ [DEBUG] JWT decode error:", str(e))    
        raise HTTPException(status_code=status.HTTP_401_UNAUTHORIZED, detail=str(e))
