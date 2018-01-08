package com.tubb.delayactions;

import android.support.annotation.NonNull;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

import static com.tubb.delayactions.EmptyUtils.checkNotNull;
import static com.tubb.delayactions.EmptyUtils.isNull;

/**
 * The core action call unit
 * Created by tubingbing on 2017/12/16.
 */

final class ActionUnitImpl implements ActionUnit {

    private CoreAction coreAction;
    private List<PremiseAction> premiseActions = new LinkedList<>();

    private ActionUnitImpl(CoreAction coreAction) {
        this.coreAction = coreAction;
    }

    static ActionUnitImpl create(CoreAction coreAction) {
        return new ActionUnitImpl(coreAction);
    }

    @Override
    public CoreAction getCoreAction() {
        return coreAction;
    }

    @Override
    public List<PremiseAction> getPremiseActions() {
        return premiseActions;
    }

    @Override
    public ActionUnit addPremiseAction(PremiseAction premiseAction) {
        premiseActions.add(premiseAction);
        return this;
    }

    @Override
    public Observable<Boolean> checkAllPremiseActions() {
        Observable<Boolean> chainObservable = null;
        for (final PremiseAction premiseAction : premiseActions) {
            if (isNull(chainObservable)) {
                chainObservable = getFinishedObservable(premiseAction);
                if (premiseAction.isPremiseCheckAsync()) {
                    chainObservable = chainObservable.subscribeOn(SchedulerProvider.io());
                } else {
                    chainObservable = chainObservable.subscribeOn(SchedulerProvider.ui());
                }
            } else {
                chainObservable = chainObservable.flatMap( new Function<Boolean, ObservableSource<Boolean>>() {
                    @Override
                    public ObservableSource<Boolean> apply(Boolean finished) throws Exception {
                        return flatPremiseAction(finished, premiseAction);
                    }
                });
                if (premiseAction.isPremiseCheckAsync()) {
                    chainObservable = chainObservable.subscribeOn(SchedulerProvider.io());
                } else {
                    chainObservable = chainObservable.subscribeOn(SchedulerProvider.ui());
                }
            }
        }
        if (chainObservable != null && premiseActions.size() > 0) {
            final PremiseAction lastPremiseAction = premiseActions.get(premiseActions.size() - 1);
            chainObservable = chainObservable.doOnNext(new Consumer<Boolean>() {
                @Override
                public void accept(Boolean finished) throws Exception {
                    if (finished) {
                        notifyPremiseActionFinishedListener(lastPremiseAction);
                        premiseActions.remove(lastPremiseAction);
                    }
                }
            });
            if (lastPremiseAction.isPremiseCheckAsync()) {
                chainObservable = chainObservable.subscribeOn(SchedulerProvider.io());
            } else {
                chainObservable = chainObservable.subscribeOn(SchedulerProvider.ui());
            }
        }
        return chainObservable;
    }

    private void notifyPremiseActionFinishedListener(PremiseAction premiseAction) {
        Map<Class<? extends PremiseAction>, PremiseActionListener> pafListenerMap = DelayActions.instance().getPaListenerMap();
        PremiseActionListener listener = pafListenerMap.get(premiseAction.getClass());
        if (!EmptyUtils.isNull(listener)) {
            listener.onFinish();
        }
    }

    @NonNull
    private Observable<Boolean> getFinishedObservable(PremiseAction premiseAction) {
        Observable<Boolean> chainObservable = checkNotNull(premiseAction.finished());
        if (premiseAction.isPremiseCheckAsync()) {
            chainObservable = chainObservable.subscribeOn(SchedulerProvider.io());
        } else {
            chainObservable = chainObservable.subscribeOn(SchedulerProvider.ui());
        }
        return chainObservable;
    }

    @NonNull
    private ObservableSource<Boolean> flatPremiseAction(Boolean finished, PremiseAction premiseAction) {
        int preIndex = premiseActions.indexOf(premiseAction) - 1;
        if (preIndex >= 0) {
            if (finished) {
                notifyPremiseActionFinishedListener(premiseActions.get(preIndex));
                premiseActions.remove(preIndex);
            }
        }
        return getFinishedObservable(premiseAction);
    }
}
