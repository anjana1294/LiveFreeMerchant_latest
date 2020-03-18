package com.livefree.merchant.util

import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

object RxUtils {

    fun dispose(disposable: Disposable?) {
        if (disposable != null) {
            if (!disposable.isDisposed) {
                disposable.dispose()
            }
        }
    }

    fun dispose(compositeDisposable: CompositeDisposable) {
        if (!compositeDisposable.isDisposed) {
            compositeDisposable.dispose()
        }
    }
}