package com.tubb.delayactions;

import java.util.HashMap;
import java.util.Map;

import static com.tubb.delayactions.EmptyUtils.checkNotNull;

/**
 * DelayActions usage interface
 * Created by tubingbing on 2017/12/18.
 */

public final class DelayActions {
    private final static DelayActions INSTANCE = new DelayActions();
    private ActionUnitDispatcher mDispatcher;
    private Map<Class<? extends PremiseAction>, PremiseActionListener> mPaListenerMap = new HashMap<>(2);
    private Map<Class<? extends CoreAction>, ActionUnitListener> mAuListenerMap = new HashMap<>(2);

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
    public synchronized void post(ActionUnit unit) {
        checkNotNull(unit);
        if (mDispatcher.getActionUnitList().contains(unit)) return;
        ActionUnitListener listener = mAuListenerMap.get(unit.getCoreAction().getClass());
        if (!EmptyUtils.isNull(listener)) {
            listener.onStart();
        }
        mDispatcher.dispatch(unit);
    }

    /**
     * Create ActionUnit for CoreAction
     * @param coreAction the target action
     * @return ActionUnit
     */
    public synchronized ActionUnit createActionUnit(CoreAction coreAction) {
        checkNotNull(coreAction);
        return ActionUnitImpl.create(coreAction);
    }

    /**
     * Notify next premise action check
     */
    public synchronized void notifyLoop() {
        mDispatcher.loop();
    }

    public synchronized void registerPremiseActionFinishedListener(Class<? extends PremiseAction> clazz,
                                                      PremiseActionListener listener) {
        checkNotNull(clazz);
        checkNotNull(listener);
        if (mPaListenerMap.containsKey(clazz)) {
            PremiseActionListener oldListener = mPaListenerMap.get(clazz);
            if (oldListener != listener) {
                // just update
                mPaListenerMap.put(clazz, listener);
            }
        } else {
            mPaListenerMap.put(clazz, listener);
        }
    }

    public synchronized void unregisterPremiseActionFinishedListener(Class<? extends PremiseAction> clazz,
                                                      PremiseActionListener listener) {
        checkNotNull(clazz);
        checkNotNull(listener);
        mPaListenerMap.remove(clazz);
    }

    public synchronized void registerActionUnitListener(Class<? extends CoreAction> clazz,
                                                                   ActionUnitListener listener) {
        checkNotNull(clazz);
        checkNotNull(listener);
        if (mAuListenerMap.containsKey(clazz)) {
            ActionUnitListener oldListener = mAuListenerMap.get(clazz);
            if (oldListener != listener) {
                // just update
                mAuListenerMap.put(clazz, listener);
            }
        } else {
            mAuListenerMap.put(clazz, listener);
        }
    }

    public synchronized void unregisterActionUnitListener(Class<? extends CoreAction> clazz,
                                                          ActionUnitListener listener) {
        checkNotNull(clazz);
        checkNotNull(listener);
        mAuListenerMap.remove(clazz);
    }

    Map<Class<? extends PremiseAction>, PremiseActionListener> getPaListenerMap() {
        return mPaListenerMap;
    }

    Map<Class<? extends CoreAction>, ActionUnitListener> getAuListenerMap() {
        return mAuListenerMap;
    }
}
