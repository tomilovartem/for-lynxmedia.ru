package ru.linxmedia.for_linxmediaru.helpers;



import android.os.AsyncTask;
import android.os.Handler;
import android.support.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;



import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class HttpHelper {

    private static class ResponseResult<T> extends AsyncTask<String, Void, String>{



        public ResponseResult(IHttpResponseListener<T> responseListener, Class<T> clazz) {
            this.responseListener = responseListener;
            this.clazz = clazz;
        }

        private IHttpResponseListener<T> responseListener = null;

        public void setClazz(Class<T> clazz) {
            this.clazz = clazz;
        }

        private Class<T> clazz;

        public void setResponseListener(IHttpResponseListener responseListener) {
            this.responseListener = responseListener;
        }

        private Handler handler = new Handler();

        @Override
        protected String doInBackground(String... url) {
            try{
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        .url(url[0])
                        .build();
                final Response response = client.newCall(request).execute();
                if(response.isSuccessful()) {
                    GsonBuilder builder = new GsonBuilder();
                    Gson gson = builder.create();
                    final String json = response.body().string();
                    T obj = null;
                    try{
                        obj =  gson.fromJson(json, clazz);
                    }
                    catch(final Exception ex){
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                if(responseListener!=null) responseListener.onException(ex.getMessage());
                            }
                        });
                        return null;
                    }
                    final T finalObj = obj;
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            if(responseListener!=null) responseListener.onSuccess(finalObj, response.message(), json);
                        }
                    });


                }
                else{
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            if(responseListener!=null) responseListener.onFail(response.message());
                        }
                    });


                }
            }
            catch (final Exception ex){
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if(responseListener!=null) responseListener.onException(ex.getMessage());
                    }
                });


            }
            return null;

        }



    }



    public static void Get(String url, Class clazz,  IHttpResponseListener responseListener, @Nullable String... params){


        String full_url = url;
        String responseStr = "";
        if(params!=null){
            full_url = String.format(url, params);
        }
        Object[] arg = new String[]{full_url};
        ResponseResult responseResult = new ResponseResult(responseListener, clazz);
        responseResult.execute(arg);
    }
}
