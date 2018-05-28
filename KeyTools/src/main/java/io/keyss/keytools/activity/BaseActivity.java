package io.keyss.keytools.activity;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Looper;
import android.os.MessageQueue;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;


import com.orhanobut.logger.Logger;

import butterknife.ButterKnife;
import butterknife.OnClick;
import io.keyss.keytools.R;

/**
 * Created by Key on 2018/5/15.
 */
public abstract class BaseActivity extends AppCompatActivity {

    protected BaseActivity mContext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        int contentViewId = getContentViewId();
        if (contentViewId != 0) {
            setContentView(contentViewId);
            ButterKnife.bind(this);
            View view = findViewById(R.id.ib_back_title);
            if (view != null) {
                // 后退键
                view.setOnClickListener(v -> onBackPressed());
                // 初始化导航栏
                initTitle();
            }
        }

        mContext = this;
        //mRxPermissions = new RxPermissions(this);
        //mProgressDialog = new RxDialogLoading(mContext);

        // 竖屏显示，不能转动
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


        // 初始化本地数据和布局
        initLayout(savedInstanceState);

        Looper.myQueue().addIdleHandler(() -> {
            Logger.e("Looper.myQueue().addIdleHandler() -> new MessageQueue.IdleHandler(){}");
            onActivityInitialized();
            return false; //false 表示只监听一次IDLE事件,之后就不会再执行这个函数了.
        });
    }

    /**
     * 界面初始化完成
     */
    protected abstract void onActivityInitialized();

    /**
     * 布局文件
     */
    protected abstract int getContentViewId();

    /**
     * 初始化界面
     */
    protected abstract void initLayout(@Nullable Bundle savedInstanceState);

    /**
     * title 名字 设置
     * ((TextView) findViewById(R.id.tv_name_title)).setText("亲子圈");
     * ((TextView) findViewById(R.id.tv_function_title)).setText("新增");
     * @OnClick(R.id.tv_function_title)
     * public void onRightButtonClick(View view)
     * findViewById(R.id.tv_function_title).setVisibility(View.GONE);
     * findViewById(R.id.ib_back_title).setVisibility(View.GONE);
     */
    protected abstract void initTitle();
}
