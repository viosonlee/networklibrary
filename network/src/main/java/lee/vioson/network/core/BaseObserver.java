package lee.vioson.network.core;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created by viosonlee
 * on 2017/10/30.
 * for 基本回调
 */

public abstract class BaseObserver<T> implements Observer<BaseResponse<T>> {
    private static final String TAG = "BaseObserver";
    private LoadingListener loadingListener;

    public BaseObserver() {
    }

    public BaseObserver(LoadingListener loadingListener) {
        this.loadingListener = loadingListener;
    }

    @Override
    public void onSubscribe(Disposable d) {
        if (loadingListener != null) {
            loadingListener.onStartLoading();
        }
    }

    @Override
    public void onNext(BaseResponse<T> response) {
        try {
            if (response.isOk()) {
                onHandleSuccess(response.getData());
            } else onHandleError(new BaseApiException(response));
        } catch (Exception e) {
            onHandleError(new BaseApiException(-999, e.getMessage()));//其他错误
        }
    }

    protected void onHandleError(BaseApiException e) {
        DebugLog.e(TAG, e.getMessage());
    }


    protected abstract void onHandleSuccess(T data);

    @Override
    public void onComplete() {
        DebugLog.d(TAG, "onComplete");
        if (loadingListener != null) {
            loadingListener.onDismissLoading();
        }
    }

    @Override
    public void onError(Throwable e) {
        onHandleError(new BaseApiException(-999, e.getMessage()));
    }

    public  interface LoadingListener {
        void onStartLoading();

        void onDismissLoading();
    }
}
