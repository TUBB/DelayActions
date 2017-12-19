package com.tubb.delayactions.test;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by tubingbing on 2017/12/18.
 */

public class UserInfoCache {
    public static boolean isLogin(Context context) {
        SharedPreferences sp = getSharedPreferences(context);
        return sp.getBoolean("login", false);
    }

    private static SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences("user_info", Context.MODE_PRIVATE);
    }

    public static boolean isHasDiscount(Context context) {
        SharedPreferences sp = getSharedPreferences(context);
        return sp.getBoolean("discount", false);
    }
    public static void setLogin(Context context, boolean login) {
        SharedPreferences sp = getSharedPreferences(context);
        sp.edit().putBoolean("login", login).apply();
    }
    public static void setDiscount(Context context, boolean discount) {
        SharedPreferences sp = getSharedPreferences(context);
        sp.edit().putBoolean("discount", discount).apply();
    }
}
