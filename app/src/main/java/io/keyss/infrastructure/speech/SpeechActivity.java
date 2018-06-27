package io.keyss.infrastructure.speech;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.BindView;
import io.keyss.infrastructure.R;
import io.keyss.keytools.activity.BaseActivity;

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

    int[] resIds = {R.layout.speech_1, R.layout.speech_2, R.layout.speech_3, R.layout.speech_4, R.layout.speech_5,
            R.layout.speech_6, R.layout.speech_7, R.layout.speech_8, R.layout.speech_9, R.layout.speech_10,
            R.layout.speech_11};
    View[] resViews;

    @Override
    protected void initLayout(@Nullable Bundle savedInstanceState) {
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

    public void 演示() {

    }
}
