let oauthFlow = false;

export const OAuthFlow = {
  start() {
    oauthFlow = true;
  },
  end() {
    oauthFlow = false;
  },
  isActive() {
    return oauthFlow;
  },
};
