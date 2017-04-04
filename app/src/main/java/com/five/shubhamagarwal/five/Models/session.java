package com.five.shubhamagarwal.five.Models;

/**
 * Created by shubhamagrawal on 05/04/17.
 */

public class session {
    String token;
    String sessionId;
    String apiKey;

    public session(String token, String sessionId, String apiKey) {
        this.token = token;
        this.sessionId = sessionId;
        this.apiKey = apiKey;
    }

    public session() {
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }
}
