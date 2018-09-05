package com.dss.dssClub.retrofit;

import com.dss.dssClub.model.ChangePass;
import com.dss.dssClub.model.CheckExistsQRcode;
import com.dss.dssClub.model.CheckInQRcode;
import com.dss.dssClub.model.DetailGift;
import com.dss.dssClub.model.DetailNotifi;
import com.dss.dssClub.model.EditAcc;
import com.dss.dssClub.model.ForgotPass;
import com.dss.dssClub.model.GetListGiftRegisterd;
import com.dss.dssClub.model.GetListNotifi;
import com.dss.dssClub.model.GetListRegitedSeria;
import com.dss.dssClub.model.GetScoresAcc;
import com.dss.dssClub.model.GetSizeNotifi;
import com.dss.dssClub.model.Gift;
import com.dss.dssClub.model.ListRegisterSeria;
import com.dss.dssClub.model.PostRawRegiterSeria;
import com.dss.dssClub.model.Register;
import com.dss.dssClub.model.RegisterDeviceID;
import com.dss.dssClub.model.RewardGift;
import com.dss.dssClub.model.SendCodeOTP;
import com.dss.dssClub.model.SeriInfo;
import com.dss.dssClub.model.User;
import com.dss.dssClub.model.ValidateOTP;
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

    @POST("RegisterGiftWithQuantity")
    Call<RewardGift> postRawJSONRewradMoreGift(@Body JsonObject locationPost);

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

    @POST("RegisterDeviceID ")
    Call<RegisterDeviceID> postRawJSONRegisterDeviceID (@Body JsonObject locationPost);

}
