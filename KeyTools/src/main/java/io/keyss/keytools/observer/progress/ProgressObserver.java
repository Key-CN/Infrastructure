package io.keyss.keytools.observer.progress;

import android.content.Context;

import com.orhanobut.logger.Logger;

import java.net.ConnectException;
import java.net.SocketException;
import java.util.concurrent.TimeoutException;

import io.keyss.keytools.utils.KeyToastUtil;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import retrofit2.HttpException;

/**
 * Author : hikobe8@github.com
 * Time : 2018/3/28 下午4:32
 * Description :
 */

public class ProgressObserver<T> implements Observer<T>, ProgressDialogHandler.ProgressCancelListener {
    private static final String TAG = "ProgressObserver";
    private ObserverOnNextListener<T> listener;
    private ProgressDialogHandler mProgressDialogHandler;
    private Context context;
    private Disposable d;

    public ProgressObserver(Context context, ObserverOnNextListener<T> listener) {
        this.listener = listener;
        this.context = context;
        mProgressDialogHandler = new ProgressDialogHandler(context, this, false);
    }

    private void showProgressDialog() {
        if (mProgressDialogHandler != null) {
            mProgressDialogHandler.obtainMessage(ProgressDialogHandler.SHOW_PROGRESS_DIALOG).sendToTarget();
        }
    }

    private void dismissProgressDialog() {
        if (mProgressDialogHandler != null) {
            mProgressDialogHandler.obtainMessage(ProgressDialogHandler.DISMISS_PROGRESS_DIALOG)
                    .sendToTarget();
            mProgressDialogHandler = null;
        }
    }

    @Override
    public void onSubscribe(Disposable d) {
        this.d = d;
        showProgressDialog();
    }

    @Override
    public void onNext(T t) {
        listener.onNext(t);
    }

    @Override
    public void onError(Throwable e) {
        dismissProgressDialog();
        listener.onError(e);
        if (e instanceof Error) {
            try {
                Integer.valueOf(e.getMessage());
            } catch (NumberFormatException e1) {
                KeyToastUtil.showToast(e.getMessage());
            }
        } else {
            if (e instanceof HttpException
                    || e instanceof ConnectException || e instanceof TimeoutException || e instanceof SocketException) {
                KeyToastUtil.showToast("网络异常，请稍后重试!");
            } else {
                KeyToastUtil.showToast("网络连接失败，请稍后再试！");
            }
        }
        Logger.w("加载动画的observer onError: " + e);
    }

    @Override
    public void onComplete() {
        dismissProgressDialog();
        Logger.i("加载动画的observer onComplete: ");
    }

    public boolean isDisposed() {
        return d == null || d.isDisposed();
    }

    public void dispose() {
        if (!isDisposed())
            d.dispose();
        dismissProgressDialog();
    }

    @Override
    public void onCancelProgress() {
        //如果处于订阅状态，则取消订阅
        if (!d.isDisposed()) {
            d.dispose();
        }
    }
}
