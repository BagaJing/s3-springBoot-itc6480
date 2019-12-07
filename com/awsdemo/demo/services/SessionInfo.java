package com.awsdemo.demo.services;

public class SessionInfo {
    private String session;
    private String accessToken;
    private String challengeResult;

    public SessionInfo(String session, String accessToken, String challengeResult) {
        this.session = session;
        this.accessToken = accessToken;
        this.challengeResult = challengeResult;
    }

    public String getSession() {
        return session;
    }

    public void setSession(String session) {
        this.session = session;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getChallengeResult() {
        return challengeResult;
    }

    public void setChallengeResult(String challengeResult) {
        this.challengeResult = challengeResult;
    }
}
