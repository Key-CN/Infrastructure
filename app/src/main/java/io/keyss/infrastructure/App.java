package io.keyss.infrastructure;

import android.app.Application;
import android.view.Gravity;

import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;

import io.keyss.keytools.utils.KeyToastUtil;

/**
 * @author 钢铁侠
 * Time: 2018/6/26 14:45
 * Description:
 */
public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Logger.addLogAdapter(new AndroidLogAdapter());

        KeyToastUtil.initToast(this, 0.8f, R.drawable.base_shape_toast_normal, Gravity.CENTER_HORIZONTAL | Gravity.TOP, 0, 500);
    }
}
