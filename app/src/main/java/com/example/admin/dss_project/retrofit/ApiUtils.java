package com.example.admin.dss_project.retrofit;

import android.util.Log;

public class ApiUtils {

    private ApiUtils() {}

    public static final String BASE_URL = "http://113.190.254.198:7777/DssApi.svc/Web/";

    public static APIRegisterUser getAPIService() {
        return RetrofitClient.getClient(BASE_URL).create(APIRegisterUser.class);
    }
}
