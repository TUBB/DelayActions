package com.tubb.delayactions;

import static com.tubb.delayactions.EmptyUtils.checkNotNull;

/**
 * DelayActions usage interface
 * Created by tubingbing on 2017/12/18.
 */

public final class DelayActions {
    private final static DelayActions INSTANCE = new DelayActions();
    private ActionUnitDispatcher mDispatcher;

    private DelayActions() {
        mDispatcher = new ActionUnitDispatcher();
    }

    public static DelayActions instance() {
        return INSTANCE;
    }

    /**
     * Dispatch the ActionUnit
     * @param unit ActionUnit
     */
    public void post(ActionUnit unit) {
        checkNotNull(unit);
        mDispatcher.dispatch(unit);
    }

    /**
     * Create ActionUnit for CoreAction
     * @param coreAction the target action
     * @return ActionUnit
     */
    public ActionUnit createActionUnit(CoreAction coreAction) {
        return DefaultActionUnit.create(coreAction);
    }

    /**
     * Notify next premise action check
     */
    public void notifyLoop() {
        mDispatcher.loop();
    }
}
