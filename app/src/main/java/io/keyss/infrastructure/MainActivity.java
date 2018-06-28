package io.keyss.infrastructure;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import io.keyss.infrastructure.speech.SpeechActivity;
import io.keyss.keytools.utils.KeyAudioRecorderUtil;

public class MainActivity extends AppCompatActivity {

    private KeyAudioRecorderUtil audioRecorderUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.b_main_act).setOnClickListener(v -> startActivity(new Intent(this, SpeechActivity.class)));

        audioRecorderUtil = new KeyAudioRecorderUtil(this,Environment.getExternalStorageDirectory() + "/aKey/");
    }

    public void start(View view) {
        audioRecorderUtil.startRecord();
    }

    public void stop(View view) {
        audioRecorderUtil.stopRecord();
    }

    public void play(View view) {

    }
}
