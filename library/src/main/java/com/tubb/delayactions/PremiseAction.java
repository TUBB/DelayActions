package com.tubb.delayactions;

import android.support.annotation.NonNull;

import io.reactivex.Observable;

/**
 * The premise action interface
 * Created by tubingbing on 2017/12/16.
 */

public interface PremiseAction {
    /**
     * If the action premise checking is async
     * @return sync or async
     */
    boolean isPremiseCheckAsync();

    /**
     * The premise action onFinish or not
     * @return RxJava
     */
    @NonNull
    Observable<Boolean> finished();

    /**
     * Execute the premise action
     */
    void execute();
}
