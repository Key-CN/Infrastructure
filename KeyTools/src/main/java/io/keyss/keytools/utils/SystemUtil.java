package io.keyss.keytools.utils;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * @author Key
 * Time: 2018/8/6 21:38
 * Description: 设置系统类的工具
 */
public class SystemUtil {
    /**
     * 设置系统时间
     *
     * @param context context
     * @param time    2016-05-26-19-28-32
     */
    public static void setSystemTime(@NonNull Context context, String time) {
        Intent intent = new Intent();
        intent.setAction("ACTION_UPDATE_TIME");
        KeyCommonUtil.remoteLogE("当前将要设置的时间: " + time);
        intent.putExtra("cmd", time);
        context.sendBroadcast(intent, null);
    }

    public static String getNetworkTimeNew(String url) {
        String nowStr = "2019-01-30-15-28-32";
        try {
            URLConnection conn = new URL(url).openConnection();
            conn.connect();
            Date now = new Date(conn.getDate());
            nowStr = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss", Locale.SIMPLIFIED_CHINESE).format(now);
        } catch (IOException e) {
            e.printStackTrace();
        }
        KeyCommonUtil.remoteLogE("当前获取到的时间: " + nowStr);
        return nowStr;
    }

    public static String getNetworkTimeNew() {
        return getNetworkTimeNew("http://aliyun.com");
    }

    public static void getNetworkTimeAndSet(Context context) {
        setSystemTime(context, getNetworkTimeNew());
    }
}
