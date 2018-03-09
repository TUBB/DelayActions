package com.tubb.delayactions;

import android.support.annotation.NonNull;

import io.reactivex.Observable;

/**
 * The premise action interface
 * Created by tubingbing on 2017/12/16.
 */

public interface PremiseAction {
    /**
     * The action premise checking is async or sync
     * @return sync or async
     */
    boolean isPremiseCheckAsync();

    /**
     * The premise action finished or not
     * @return RxJava
     */
    @NonNull
    Observable<Boolean> finished();

    /**
     * Execute the premise action
     */
    void execute();
}
