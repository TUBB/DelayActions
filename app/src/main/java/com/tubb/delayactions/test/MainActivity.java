package com.tubb.delayactions.test;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.tubb.delayactions.ActionUnit;
import com.tubb.delayactions.CoreAction;
import com.tubb.delayactions.DelayActions;

public class MainActivity extends AppCompatActivity implements CoreAction {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void viewClick(View view) {
        switch (view.getId()) {
            case R.id.btn_order_detail:
                ActionUnit unit = DelayActions.instance().createActionUnit(this)
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
    public void execute() {
        startActivity(new Intent(this, OrderDetailActivity.class));
    }
}
