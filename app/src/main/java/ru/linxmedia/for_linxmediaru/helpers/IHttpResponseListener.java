package ru.linxmedia.for_linxmediaru.helpers;



public interface IHttpResponseListener<T> {
    void onSuccess(T object, String message, String response);
    void onFail(String message);
    void onException(String message);
}
