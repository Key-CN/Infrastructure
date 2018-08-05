package io.keyss.infrastructure;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.orhanobut.logger.Logger;

import io.keyss.keytools.utils.KeySPUtil;

/**
 * @author MrKey
 */
public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.b_main_act).setOnClickListener(v -> startActivity(new Intent(this, TestActivity.class)));

        toLog("status: " + Environment.getExternalStorageState() + "   path: " + Environment.getExternalStorageDirectory());
    }

    public void start(View view) {

        KeySPUtil.save("abc");
        Logger.e(KeySPUtil.get());
    }

    public void stop(View view) {
        new Thread(() -> {
            KeySPUtil.init(this);
            KeySPUtil.save("cba");
            Logger.e(KeySPUtil.get());
        }).start();
    }

    public void toLog(String msg) {
        Log.e("aaa", "" + msg);
    }
}
