package com.tubb.delayactions;

import java.util.LinkedList;
import java.util.List;

import io.reactivex.functions.Consumer;

/**
 * Created by tubingbing on 2017/12/18.
 */

class ActionUnitDispatcher {
    private List<ActionUnit> actionUnitList = new LinkedList<>();

    void dispatch(ActionUnit unit) {
        actionUnitList.add(unit);
        loop();
    }

    void loop() {
        for (final ActionUnit actionUnit : actionUnitList) {
            actionUnit
                    .checkAllPremiseActions()
                    .observeOn(SchedulerProvider.ui())
                    .subscribe(new Consumer<Boolean>() {
                        @Override
                        public void accept(Boolean finished) throws Exception {
                            // is these code run in for loop?
                            handleActionUnit(actionUnit);
                        }
                    });
        }
    }

    private void handleActionUnit(ActionUnit actionUnit) {
        List<PremiseAction> premiseActions = actionUnit.getPremiseActions();
        if (premiseActions.size() == 0) { // all premise actions finished
            actionUnitList.remove(actionUnit);
            actionUnit.getCoreAction().execute();
        } else {
            PremiseAction premiseAction = premiseActions.get(0);
            premiseAction.execute();
        }
    }
}
