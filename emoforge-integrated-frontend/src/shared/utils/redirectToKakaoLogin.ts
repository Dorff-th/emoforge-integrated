const KAKAO_AUTH_URL = `https://kauth.kakao.com/oauth/authorize?client_id=${
  import.meta.env.VITE_KAKAO_REST_API_KEY
}&redirect_uri=${
  import.meta.env.VITE_KAKAO_REDIRECT_URI
}&response_type=code`;

export const redirectToKakaoLogin = () => {
  window.location.href = KAKAO_AUTH_URL;
};
