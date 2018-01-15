package com.tubb.delayactions;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.List;

import io.reactivex.Observable;

/**
 * The user action unit
 * Created by tubingbing on 2017/12/16.
 */

public interface ActionUnit {
    @NonNull
    CoreAction getCoreAction();

    @Nullable
    List<PremiseAction> getPremiseActions();

    @NonNull
    ActionUnit addPremiseAction(PremiseAction premiseAction);

    @Nullable
    Observable<Boolean> checkAllPremiseActions();
}
