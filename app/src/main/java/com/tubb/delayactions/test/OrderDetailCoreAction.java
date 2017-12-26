package com.tubb.delayactions.test;

import android.app.Activity;
import android.content.Intent;

import com.tubb.delayactions.CoreAction;

/**
 * Created by tubingbing on 2017/12/26.
 */

class OrderDetailCoreAction implements CoreAction {

    private Activity mActivity;

    OrderDetailCoreAction(Activity activity) {
        mActivity = activity;
    }

    @Override
    public void execute() {
        mActivity.startActivity(new Intent(mActivity, OrderDetailActivity.class));
    }
}
