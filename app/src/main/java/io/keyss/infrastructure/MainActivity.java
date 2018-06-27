package io.keyss.infrastructure;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ProgressBar;

import io.keyss.infrastructure.speech.SpeechActivity;
import io.keyss.keytools.widget.dialog.RxDialogLoading;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.b_main_act).setOnClickListener(v -> startActivity(new Intent(this, SpeechActivity.class)));

    }
}
