package com.tubb.delayactions.test;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.tubb.delayactions.DelayActions;

/**
 * Created by tubingbing on 2017/12/18.
 */

public class LoginActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void viewClick(View view) {
        UserInfoCache.setLogin(this, true);
        DelayActions.instance().notifyLoop();
        finish();
    }
}
