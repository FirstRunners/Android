package com.android.firstlearners.learners.model;


import com.android.firstlearners.learners.model.data.ResponseMessage;
import com.android.firstlearners.learners.model.data.ResponseMypage;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface MypageApi {

    @GET("mypage/main")
    Call<ResponseMypage> requestMypage(@Header("user_token") String user_token);

    @FormUrlEncoded
    @POST("mypage/count")
    Call<ResponseMessage> changeCount(@Header("user_token") String user_token, @Field("study_count") int study_count);

    @DELETE("mypage/signout")
    Call<ResponseMessage> deleteUser(@Header("user_token") String user_token);

    @DELETE("mypage/studyout")
    Call<ResponseMessage> studyOut(@Header("user_token") String user_token);

    @FormUrlEncoded
    @POST("mypage/period")
    Call<ResponseMessage> studyPeriod(@Header("user_token") String user_token, @Field("study_start") String study_start,
                                      @Field("study_end") String study_end);
}
