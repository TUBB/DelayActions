package com.tubb.delayactions;

import java.util.List;

import io.reactivex.Observable;

/**
 * The user action unit
 * Created by tubingbing on 2017/12/16.
 */

public interface ActionUnit {
    CoreAction getCoreAction();

    List<PremiseAction> getPremiseActions();

    ActionUnit addPremiseAction(PremiseAction premiseAction);

    Observable<Boolean> checkAllPremiseActions();
}
