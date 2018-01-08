package com.tubb.delayactions.test;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;

import com.tubb.delayactions.PremiseAction;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;

/**
 * Created by tubingbing on 2017/12/18.
 */

public class DiscountPremiseAction implements PremiseAction {
    Activity mContext;
    public DiscountPremiseAction(Activity context) {
        mContext = context;
    }

    @Override
    public boolean isPremiseCheckAsync() {
        return true;
    }

    @NonNull
    @Override
    public Observable<Boolean> finished() {
        return Observable.create(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(ObservableEmitter<Boolean> e) throws Exception {
                boolean isHasDiscount = UserInfoCache.isHasDiscount(mContext);
                e.onNext(isHasDiscount);
            }
        });
    }

    @Override
    public void execute() {
        mContext.startActivity(new Intent(mContext, DiscountActivity.class));
    }
}
