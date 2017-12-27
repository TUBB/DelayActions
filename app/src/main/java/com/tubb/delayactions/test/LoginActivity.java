package com.tubb.delayactions.test;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.tubb.delayactions.DelayActions;
import com.tubb.delayactions.PremiseActionListener;

/**
 * Created by tubingbing on 2017/12/18.
 */

public class LoginActivity extends AppCompatActivity {
    private PremiseActionListener mListener;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mListener = new PremiseActionListener() {
            @Override
            public void onFinish() {
                finish();
            }
        };
        DelayActions.instance().registerPremiseActionFinishedListener(LoginPremiseAction.class, mListener);
    }

    public void viewClick(View view) {
        UserInfoCache.setLogin(this, true);
        DelayActions.instance().notifyLoop();
    }

    @Override
    protected void onStop() {
        super.onStop();
        // must unregister the listener, otherwise will be leak the activity instance
        DelayActions.instance().unregisterPremiseActionFinishedListener(LoginPremiseAction.class, mListener);
    }
}
