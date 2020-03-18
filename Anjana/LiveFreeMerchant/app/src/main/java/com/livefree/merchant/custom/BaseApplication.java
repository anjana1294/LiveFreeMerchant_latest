package com.livefree.merchant.custom;

import android.support.multidex.MultiDexApplication;
import io.reactivex.disposables.Disposable;

public class BaseApplication extends MultiDexApplication {
    private Disposable disposable;

    @Override
    public void onCreate() {
        super.onCreate();

      /*  if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree() {
                @Override
                protected void log(int priority, String tag, @NonNull String msg, Throwable t) {
                    super.log(priority, "lynn_grocerystore_logs_" + tag, msg, t);
                }
            });
        } else {
            Fabric.with(this, new Crashlytics());

            Timber.plant(new CrashlyticsTrees());
        }

        if(disposable == null) {
            disposable = ReactiveNetwork.observeNetworkConnectivity(this)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .unsubscribeOn(Schedulers.io())
                    .subscribe(new Consumer<Connectivity>() {
                        @Override
                        public void accept(Connectivity connectivity) throws Exception {
                            if (connectivity.getState() != NetworkInfo.State.CONNECTED) {
                                EventBus.getDefault().post(new NetworkEventBusMessage(EVENT_CONNECTIVITY_LOST));
                            } else {
                                EventBus.getDefault().post(new NetworkEventBusMessage(EVENT_CONNECTIVITY_CONNECTED));
                            }
                        }
                    });
        }
*/
      //  JodaTimeAndroid.init(this);
    }
}
