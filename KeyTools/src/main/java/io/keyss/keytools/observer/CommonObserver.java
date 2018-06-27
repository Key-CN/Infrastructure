package io.keyss.keytools.observer;

import com.google.gson.JsonSyntaxException;
import com.orhanobut.logger.Logger;

import java.net.ConnectException;
import java.net.SocketException;
import java.util.concurrent.TimeoutException;

import io.keyss.keytools.utils.KeyToastUtil;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import retrofit2.HttpException;

public class CommonObserver<T> implements Observer<T> {

    private Disposable d;

    public CommonObserver() {
    }

    @Override
    public void onSubscribe(Disposable d) {
        this.d = d;
    }

    @Override
    public void onNext(T t) {

    }

    @Override
    public void onError(Throwable e) {
        if (e instanceof Error) {
            KeyToastUtil.showToast(e.getMessage());
        } else {
            if (e instanceof HttpException ||
                    e instanceof ConnectException ||
                    e instanceof TimeoutException ||
                    e instanceof SocketException) {

                KeyToastUtil.showToast("网络异常，请稍后重试！");

            } else if (e instanceof JsonSyntaxException) {

                Logger.w("普通的observer  okhttp---" + "response json parse error : " + e.getMessage());

            } else {

                KeyToastUtil.showToast("网络连接失败，请稍后再试！");

            }
        }
        Logger.w("普通的observer onError: " + e);
    }

    @Override
    public void onComplete() {
        Logger.i("普通的observer onComplete");
    }

    public boolean isDisposed() {
        return d == null || d.isDisposed();
    }

    public void dispose() {
        if (!isDisposed()) {
            d.dispose();
        }
    }

}
