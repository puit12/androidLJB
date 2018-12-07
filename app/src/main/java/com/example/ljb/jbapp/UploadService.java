package com.example.ljb.jbapp;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * Created by LJB on 2018-08-13.
 */

public interface UploadService {
    @FormUrlEncoded
    @POST("/test")
    Call<ResponseBody> upload(
            @Field("description") RequestBody description,
            @Field("id") String text);

    @FormUrlEncoded
    @POST("/returnresult")
    Call<ResponseBody> resultSentenceResponse(
            @Field("action") String text);

    @FormUrlEncoded
    @POST("/returncount")
    Call<ResponseBody> resultWordCountResponse(
            @Field("action") String text);

}
