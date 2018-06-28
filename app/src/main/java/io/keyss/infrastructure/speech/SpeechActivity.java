package io.keyss.infrastructure.speech;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Random;

import butterknife.BindView;
import io.keyss.infrastructure.R;
import io.keyss.keytools.activity.BaseActivity;
import io.keyss.keytools.widget.like.RxHeartLayout;

public class SpeechActivity extends BaseActivity {

    @BindView(R.id.vp_speech_act)
    ViewPager viewPager;
    private LayoutInflater factory;

    @Override
    protected void onActivityInitialized() {
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_speech;
    }

    int[] resIds = {R.layout.speech_0, R.layout.speech_1, R.layout.speech_2, R.layout.speech_3, R.layout.speech_4, R.layout.speech_5,
            R.layout.speech_6, R.layout.speech_6_1, R.layout.speech_6_2, R.layout.speech_6_3, R.layout.speech_6_4,
            R.layout.speech_6_5, R.layout.speech_6_6, R.layout.speech_7, R.layout.speech_8, R.layout.speech_9, R.layout.speech_10,
            R.layout.speech_11, R.layout.speech_12, R.layout.speech_13, R.layout.speech_14, R.layout.speech_15,
            R.layout.speech_9999, R.layout.speech_99999};
    View[] resViews;

    @Override
    protected void initLayout(@Nullable Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT >= 19) {
            //for new api versions.
            View decorView = getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);
        }

        factory = LayoutInflater.from(this);

        resViews = new View[resIds.length];

        viewPager.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return resIds.length;
            }

            @NonNull
            @Override
            public Object instantiateItem(@NonNull ViewGroup container, int position) {
                container.addView(resViews[position] != null ? resViews[position] : (resViews[position] = factory.inflate(resIds[position], null)));
                if (position == resIds.length - 1) {
                    mRxHeartLayout = resViews[position].findViewById(R.id.heart_layout);
                }
                return resViews[position];
            }

            @Override
            public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
                return view == object;
            }

            @Override
            public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
                container.removeView(resViews[position]);
            }
        });
    }

    @Override
    protected void initTitle() {

    }

    RxHeartLayout mRxHeartLayout;
    private Random random = new Random();

    public void like(View view) {
        if (mRxHeartLayout != null) {
            mRxHeartLayout.post(new Runnable() {
                @Override
                public void run() {
                    int rgb = Color.rgb(random.nextInt(255), random.nextInt(255), random.nextInt(255));
                    mRxHeartLayout.addHeart(rgb);
                }
            });
        }
    }
}
