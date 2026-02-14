# auth_dependency.py
from fastapi import Request, HTTPException, status, Depends
from core.auth_utils import decode_jwt

def verify_jwt_from_cookie(request: Request):
    token = request.cookies.get("access_token")

    print("🔍 Cookies in request:", request.cookies)

    if not token:
        raise HTTPException(
            status_code=status.HTTP_401_UNAUTHORIZED,
            detail="Missing access_token cookie"
        )
    
    payload = decode_jwt(token)
    #return payload  # { member_uuid, username, role, exp ... }
    return {
        "member_uuid": payload.get("sub"),
        "role": payload.get("role"),
        "claims": payload
    }

def require_role(required_role: str):
    def inner(user=Depends(verify_jwt_from_cookie)):
        role = user.get("role")
        if role != required_role:
            raise HTTPException(
                status_code=status.HTTP_403_FORBIDDEN,
                detail=f"Access denied: role '{role}' cannot access this resource"
            )
        return user
    return inner
