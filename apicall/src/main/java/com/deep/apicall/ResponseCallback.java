package com.deep.apicall;

public interface ResponseCallback<T> {
    void onSuccess(T response);
    void onFailed(int statusCode, String errorMessage);
}

