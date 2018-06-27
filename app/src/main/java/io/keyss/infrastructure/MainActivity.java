package io.keyss.infrastructure;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.orhanobut.logger.Logger;

import io.keyss.infrastructure.speech.SpeechActivity;
import io.keyss.keytools.utils.KeyAudioRecoderUtil;

public class MainActivity extends AppCompatActivity {

    private KeyAudioRecoderUtil audioRecoderUtil;
    private String startRecord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.b_main_act).setOnClickListener(v -> startActivity(new Intent(this, SpeechActivity.class)));

        audioRecoderUtil = new KeyAudioRecoderUtil(Environment.getExternalStorageDirectory() + "/aKey/");

    }

    public void start(View view) {
        startRecord = audioRecoderUtil.startRecord();
        Logger.e("路径: " + startRecord);
    }

    public void stop(View view) {
        audioRecoderUtil.stopRecord();
    }

    public void play(View view) {
        if (startRecord != null) {
            MediaPlayer.create(this, Uri.parse(startRecord)).start();
        }
    }
}
