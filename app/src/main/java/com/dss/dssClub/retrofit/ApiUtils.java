package com.dss.dssClub.retrofit;

import android.util.Log;

import com.dss.dssClub.retrofit.APIRegisterUser;
import com.dss.dssClub.retrofit.RetrofitClient;

public class ApiUtils {

    private ApiUtils() {}

    public static final String BASE_URL = "https://app.dahua.vn:4433/DssApi.svc/Web/";

    public static APIRegisterUser getAPIService() {
        return RetrofitClient.getClient(BASE_URL).create(APIRegisterUser.class);
    }
}
