package io.keyss.keytools.utils;

import android.content.Context;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Author : hikobe8@github.com
 * Time : 2018/3/26 下午10:25
 * Description : Toast 工具类
 */

public class ToastUtil {

    public static void show(Context context, String text) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }

    private static Toast toast = null;

    private static TextView toastText = null;

    private ToastUtil() {
    }

    public static void initToast() {
        /*toast = new Toast();
        View toastView = View.inflate(TeacherApp.getApplication(), R.layout.toast_normal, null);
        toastView.setAlpha(0.9f);
        toastText = toastView.findViewById(R.id.tv_toast);
        toast.setView(toastView);
        toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.TOP, 0, 500);
        toast.setDuration(Toast.LENGTH_SHORT);*/
    }

    /**
     * Created by Key on 2016/12/21 13:53
     * email: MrKey.K@gmail.com
     * description:超级连弹土司
     *
     * @param s 弹出的字符串
     */
    public static void getToast(String s) {
        if (toast == null) {
            initToast();
        }
        //((TextView) toast.getView().findViewById(R.id.tv_toast)).setText(s);
        toastText.setText(s);
        toast.show();
    }
}
