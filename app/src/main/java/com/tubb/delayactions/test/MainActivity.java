package com.tubb.delayactions.test;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.tubb.delayactions.ActionUnit;
import com.tubb.delayactions.ActionUnitListener;
import com.tubb.delayactions.CoreAction;
import com.tubb.delayactions.DelayActions;

public class MainActivity extends AppCompatActivity {
    private ActionUnitListener mListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mListener = new ActionUnitListener(){

            @Override
            public void onStart() {
                Log.d("delayactions", "onStart");
            }

            @Override
            public void onFinish() {
                Log.d("delayactions", "onFinish");
            }
        };
        DelayActions.instance().registerActionUnitListener(OrderDetailCoreAction.class, mListener);
    }

    public void viewClick(View view) {
        switch (view.getId()) {
            case R.id.btn_order_detail:
                CoreAction coreAction = new OrderDetailCoreAction(this);
                ActionUnit unit = DelayActions.instance().createActionUnit(coreAction)
                        .addPremiseAction(new LoginPremiseAction(this))
                        .addPremiseAction(new DiscountPremiseAction(this));
                DelayActions.instance().post(unit);
                break;
            case R.id.btn_clear_login:
                UserInfoCache.setLogin(this, false);
                break;
            case R.id.btn_clear_discount:
                UserInfoCache.setDiscount(this, false);
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // must unregister the listener, otherwise will be leak the activity instance
        DelayActions.instance().unregisterActionUnitListener(OrderDetailCoreAction.class, mListener);
    }
}
