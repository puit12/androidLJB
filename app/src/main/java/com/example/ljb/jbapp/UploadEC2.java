package com.example.ljb.jbapp;

import android.app.Notification;
import android.content.Context;
import android.os.Message;
import android.widget.Toast;


import java.io.IOException;
import java.util.concurrent.TimeUnit;
import android.os.Handler;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
/**
 * Created by LJB on 2018-07-13.
 */

public class UploadEC2 {
    Retrofit retrofit;
    UploadService service;
    String resultLine;

    protected UploadEC2(){
        final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .readTimeout(50, TimeUnit.SECONDS)
                .connectTimeout(10, TimeUnit.SECONDS)
                .build();
        retrofit = new Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl("awsurl:5000")
                .build();

        service = retrofit.create(UploadService.class);
    }

    protected void uploadToEC2(final Context context, String textLine) {

        RequestBody descrip = RequestBody.create(MediaType.parse("multipart/form-data"), "descrip");
        String realText = textLine;
        Call<ResponseBody> call = service.upload(descrip, realText);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                ResponseBody res = response.body();
                try {
                    String resultLine = res.string();
                    Toast.makeText(context, resultLine, Toast.LENGTH_LONG).show();
                }
               catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    protected void resultSentenceFromEC2(final Context context, final Handler handler, String textLine) {
        String realText = textLine;

        Call<ResponseBody> call = service.resultSentenceResponse(realText);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                ResponseBody res = response.body();
                try {
                    resultLine = res.string();
                    Message msg = Message.obtain(handler,1);
                    msg.sendToTarget();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
            }
        });
    }

    protected void resultWordCountFromEC2(final Context context, final Handler handler, String textLine) {
        String realText = textLine;

        Call<ResponseBody> call = service.resultWordCountResponse(realText);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                ResponseBody res = response.body();
                try {
                    resultLine = res.string();
                    Message msg = Message.obtain(handler,2);
                    msg.sendToTarget();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
            }
        });
    }

    protected String returnResult(){
        return resultLine;
    }

}