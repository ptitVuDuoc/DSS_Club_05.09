package com.example.admin.dss_project.ultility;

import java.util.ArrayList;
import java.util.List;

public class Statistic {

    public static final String token =  "3DF89654-86D7-457D-9D81-FBD71043BE61";

    public static final int KEY_EDIT_ACCOUNT = 1;
    public static final int KEY_CHANGE_PASS = 2;
    public static  String DAY_START_REGISTER_SIREA ;
    public static  String DAY_START_REGISTER_GIFT ;
    public static  String DAY_END_REGISTER_SIREA ;
    public static  String DAY_END_REGISTER_GIFT ;
    public static final String TOPIC = "Dss_Project";

    public static List<String> listSeria ;

    public static void initDataList(){
        listSeria = new ArrayList<>();
    }

    public static void addListSeria(String s){
        listSeria.add(s);
    }

    public static void removeListSeria(){
        listSeria.clear();
    }

    public static void removeItemListSeria(int position){
        listSeria.remove(position);
    }

    public static List<String> getListSeria() {
        return listSeria;
    }
}
