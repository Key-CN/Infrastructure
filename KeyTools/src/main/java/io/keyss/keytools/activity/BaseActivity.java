package io.keyss.keytools.activity;

import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.orhanobut.logger.Logger;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.keyss.keytools.R;
import io.keyss.keytools.utils.KeyActivityUtil;

/**
 * @author Key
 * Time: 2018/6/27 11:59
 * Description: ButterKnife版 最底层基类
 */
public abstract class BaseActivity extends AppCompatActivity {

    protected BaseActivity mContext;
    private Unbinder unbinder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        KeyActivityUtil.addActivity(mContext = this);
        int contentViewId = getContentViewId();
        if (contentViewId != 0) {
            setContentView(contentViewId);
            unbinder = ButterKnife.bind(this);
            View view = findViewById(R.id.ib_back_title);
            if (view != null) {
                // 后退键
                view.setOnClickListener(v -> onBackPressed());
                // 初始化导航栏
                initTitle();
            }
        }
        //mRxPermissions = new RxPermissions(this);
        //mProgressDialog = new RxDialogLoading(mContext);

        // 竖屏显示，不能转动，建议设在第二层
        //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


        // 初始化本地数据和布局
        initLayout(savedInstanceState);

        Looper.myQueue().addIdleHandler(() -> {
            Logger.e("Looper.myQueue().addIdleHandler() -> new MessageQueue.IdleHandler() { 后台空闲线程 }");
            onActivityInitialized();
            //false 表示只监听一次IDLE事件,之后就不会再执行这个函数了. true则会一直执行
            return false;
        });
    }

    /**
     * 界面初始化完成，只执行一次
     * 也可以用onWindowFocusChanged等待渲染完成,比这个方法先运行，但此方法多次执行
     * 按需选择
     * onStart - onResume - onWindowFocusChanged: true - Looper.myQueue().addIdleHandler()
     * - (进入后台)onWindowFocusChanged: false - onPause - onStop
     * 返回时：onStart - onResume - onWindowFocusChanged: true
     */
    protected abstract void onActivityInitialized();

    /**
     * 布局文件
     *
     * @return Layout的ID
     */
    protected abstract int getContentViewId();

    /**
     * 初始化界面
     *
     * @param savedInstanceState onCreate中传下来的
     */
    protected abstract void initLayout(@Nullable Bundle savedInstanceState);

    /**
     * title 名字 设置
     * ((TextView) findViewById(R.id.tv_name_title)).setText("亲子圈");
     * ((TextView) findViewById(R.id.tv_function_title)).setText("新增");
     * ;@OnClick(R.id.tv_function_title) public void onRightButtonClick(View view)
     * findViewById(R.id.tv_function_title).setVisibility(View.GONE);
     * findViewById(R.id.ib_back_title).setVisibility(View.GONE);
     */
    protected abstract void initTitle();

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (unbinder != null) {
            unbinder.unbind();
        }
        KeyActivityUtil.removeActivity(this);
    }
}
