package com.notesdea.articles.model;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.util.Log;

/**
 * Created by notes on 2016/12/6.
 */

//网络相关的工具类
public class NetworkUtils {
    //判断网络是否可用
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) context.getSystemService(Activity.CONNECTIVITY_SERVICE);
        Network[] networks = connectivityManager.getAllNetworks();
        for (int i = 0; i < networks.length; i++) {
            NetworkInfo info = connectivityManager.getNetworkInfo(networks[i]);
            Log.d("NetworkUtils", info.getTypeName() + info.isConnectedOrConnecting());
            if (info.isConnectedOrConnecting()) {
                return true;
            }
        }
        return false;
    }
}
