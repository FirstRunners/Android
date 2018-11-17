package com.android.firstlearners.learners.model.data;

public class ResponseMypage {
    public boolean status;
    public String message;
    public Mypage result;

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Mypage getResult() {
        return result;
    }

    public void setResult(Mypage result) {
        this.result = result;
    }
}

