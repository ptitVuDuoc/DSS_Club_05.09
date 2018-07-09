package com.example.admin.dss_project.retrofit;

import com.example.admin.dss_project.model.ChangePass;
import com.example.admin.dss_project.model.CheckExistsQRcode;
import com.example.admin.dss_project.model.CheckInQRcode;
import com.example.admin.dss_project.model.DetailGift;
import com.example.admin.dss_project.model.DetailNotifi;
import com.example.admin.dss_project.model.EditAcc;
import com.example.admin.dss_project.model.ForgotPass;
import com.example.admin.dss_project.model.GetListGiftRegisterd;
import com.example.admin.dss_project.model.GetListNotifi;
import com.example.admin.dss_project.model.GetListRegitedSeria;
import com.example.admin.dss_project.model.GetScoresAcc;
import com.example.admin.dss_project.model.GetSizeNotifi;
import com.example.admin.dss_project.model.Gift;
import com.example.admin.dss_project.model.ListRegisterSeria;
import com.example.admin.dss_project.model.PostRawRegiterSeria;
import com.example.admin.dss_project.model.Register;
import com.example.admin.dss_project.model.RewardGift;
import com.example.admin.dss_project.model.SendCodeOTP;
import com.example.admin.dss_project.model.SeriInfo;
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

    @POST("ChangePassword")
    Call<ChangePass> postRawJSONChangePass(@Body JsonObject locationPost);

    @POST("UpdateUserInfo")
    Call<EditAcc> postRawJSONEditAcc(@Body JsonObject locationPost);

    @POST("RegisterSeria")
    Call<SeriInfo> postRawJSONRegisterSeria(@Body JsonObject locationPost);

    @POST("GetListSeriaRegistered")
    Call<GetListRegitedSeria> postRawJSONGetListRegistedSeria(@Body JsonObject locationPost);

    @POST("GetListGift")
    Call<Gift> postRawJSONGetListGift(@Body JsonObject locationPost);

    @POST("RegisterGift")
    Call<RewardGift> postRawJSONRewradGift(@Body JsonObject locationPost);

    @POST("GetGiftInfo")
    Call<DetailGift> postRawJSONDetailGift(@Body JsonObject locationPost);

    @POST("GetScoreUser")
    Call<GetScoresAcc> postRawJSONGetScoresAcc(@Body JsonObject locationPost);

    @POST("GetListGiftRegistered")
    Call<GetListGiftRegisterd> postRawJSONGetListGiftRegistered(@Body JsonObject locationPost);

    @POST("RegisterArraySeria")
    Call<ListRegisterSeria> postRawJSONListRegisterSeria(@Body PostRawRegiterSeria postRawRegiterSeria);

    @POST("CheckExistsQRcode")
    Call<CheckExistsQRcode> postRawJSONCheckExistsQRcode(@Body JsonObject locationPost);

    @POST("CheckInQRcode")
    Call<CheckInQRcode> postRawJSONCheckQRcode(@Body JsonObject locationPost);

    @POST("GetListNotification")
    Call<GetListNotifi> postRawJSONGetListNotifi(@Body JsonObject locationPost);

    @POST("GetCountNotification")
    Call<GetSizeNotifi> postRawJSONGetCountNotification(@Body JsonObject locationPost);

    @POST("GetNotificationInfo")
    Call<DetailNotifi> postRawJSONGetInfoNotifi(@Body JsonObject locationPost);

}
