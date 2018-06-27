package io.keyss.keytools.widget.dialog;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import io.keyss.keytools.R;
import io.keyss.keytools.utils.KeyToastUtil;

public class RxDialogLoading extends RxDialog {

    private ProgressBar mLoadingView;
    private View mDialogContentView;
    private TextView mTextView;

    public RxDialogLoading(Context context, int themeResId) {
        super(context, themeResId);
        initView(context);
    }

    public RxDialogLoading(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        initView(context);
    }

    public RxDialogLoading(Context context) {
        super(context);
        initView(context);
    }

    public RxDialogLoading(Activity context) {
        super(context);
        initView(context);
    }

    public RxDialogLoading(Context context, float alpha, int gravity) {
        super(context, alpha, gravity);
        initView(context);
    }

    private void initView(Context context) {
        mDialogContentView = LayoutInflater.from(context).inflate(R.layout.base_dialog_loading, null);
        mLoadingView = mDialogContentView.findViewById(R.id.loading_view);
        mTextView = mDialogContentView.findViewById(R.id.name);
        setContentView(mDialogContentView);
    }

    public void setLoadingText(CharSequence charSequence) {
        mTextView.setText(charSequence);
    }

    public void setLoadingColor(Drawable d) {
        //mLoadingView.setColor(color);
        mLoadingView.setIndeterminateDrawable(d);
    }

    public void cancel(RxCancelType code, String str) {
        cancel();
        switch (code) {
            case normal:
                KeyToastUtil.showToast(str);
                break;
            case error:
                KeyToastUtil.showToast(str);
                break;
            case success:
                KeyToastUtil.showToast(str);
                break;
            case info:
                KeyToastUtil.showToast(str);
                break;
            default:
                KeyToastUtil.showToast(str);
                break;
        }
    }

    public void cancel(String str) {
        cancel();
        KeyToastUtil.showToast(str);
    }

    public ProgressBar getLoadingView() {
        return mLoadingView;
    }

    public View getDialogContentView() {
        return mDialogContentView;
    }

    public TextView getTextView() {
        return mTextView;
    }

    public enum RxCancelType {normal, error, success, info}
}
