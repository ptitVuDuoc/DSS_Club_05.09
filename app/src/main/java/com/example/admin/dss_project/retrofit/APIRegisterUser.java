package com.example.admin.dss_project.retrofit;

import com.example.admin.dss_project.model.ForgotPass;
import com.example.admin.dss_project.model.Register;
import com.example.admin.dss_project.model.SendCodeOTP;
import com.example.admin.dss_project.model.User;
import com.example.admin.dss_project.model.ValidateOTP;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIRegisterUser {
//    @POST("RegisterUser")
//    @FormUrlEncoded
//    Call<RegisterUser> savePost(@Field("TokenKey") String TokenKey,
//                                @Field("SoDienThoai") String SoDienThoai,
//                                @Field("HoVaTen") String HoVaTen,
//                                @Field("TenDaiLy") String TenDaiLy,
//                                @Field("Email") String Email,
//                                @Field("DiaChi") String DiaChi,
//                                @Field("NgaySinh") String NgaySinh,
//                                @Field("MatKhau") String MatKhau);

    @Headers({
            "Accept: application/json",
            "Content-Type: application/json"
    })
    @POST("LoginUser")
    Call<User> postRawJSONLogin(@Body JsonObject locationPost);

    @POST("RegisterUser")
    Call<Register> postRawJSONRegister(@Body JsonObject locationPost);

    @POST("ResetPassword")
    Call<ForgotPass> postRawJSONForGotPass(@Body JsonObject locationPost);

    @POST("ValidateOTP")
    Call<ValidateOTP> postRawJSONSendOtp(@Body JsonObject locationPost);

    @POST("SendCodeOTP")
    Call<SendCodeOTP> postRawJSONGetCodeOTP(@Body JsonObject locationPost);

    @POST("GetUserInfo")
    Call<User> postRawJSONGetUserInfo(@Body JsonObject locationPost);

}
