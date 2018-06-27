package io.keyss.keytools.observer;

import com.orhanobut.logger.Logger;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * @author 钢铁侠
 * Time: 2018/6/15 19:59
 * Description:
 */
public abstract class JustNextObserver<T> implements Observer<T> {
    @Override
    public void onSubscribe(Disposable d) {

    }

    @Override
    public abstract void onNext(T t);

    @Override
    public void onError(Throwable e) {
        Logger.w("Error: " + e);
    }

    @Override
    public void onComplete() {
        Logger.d("JustNextObserver Complete");
    }
}
