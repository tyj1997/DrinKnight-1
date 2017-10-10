package com.kwong.drinknight.utils;

import com.kwong.drinknight.home_page.DrinkData;
import com.kwong.drinknight.user_data_page.UserData;

import java.util.List;

/**
 * Created by 锐锋 on 2017/10/5.
 */

public class Global {

    public static String SERVER_URL = "http://10.206.18.173:8089";
    //public static final String SERVER_URL = "http://10.206.18.173:8089";
    //public static final String SERVER_URL = "http://10.206.13.81:8089";
    //public static final String SERVER_URL = "http://10.206.13.71:8000";
    public static UserData userData = new UserData();//当前用户信息，全局变量
    public static List<DrinkData> drinkDataList;
    public static String suggestedVolumeDose;
    public static String suggestedNextTime;
    public static double sumdrink;
    public static  float volumeDose = 0;

}
