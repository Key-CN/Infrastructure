package io.keyss.infrastructure;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.orhanobut.logger.Logger;

import io.keyss.keytools.activity.BaseActivity;
import io.keyss.keytools.utils.KeySPUtil;

/**
 * @author MrKey
 */
public class TestActivity extends BaseActivity {

    @Override
    protected void onActivityInitialized() {

    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_test;
    }

    @Override
    protected void initLayout(@Nullable Bundle savedInstanceState) {

    }

    @Override
    protected void initTitle() {

    }

    public void read(View view) {
        KeySPUtil.save("abc");
        Logger.e(KeySPUtil.get());
    }

    public void child(View view) {
        new Thread(() -> {
            KeySPUtil.save("cba");
            Logger.e(KeySPUtil.get());
        }).start();
    }
}
