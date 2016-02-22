package com.netease.commonlibrary.base;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.netease.commonlibrary.R;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by zhouchangping on 2015/12/29.
 */
public abstract class BaseZcpActivity extends Activity{

        private View contentView;

        private  boolean isShowToolbar=true;

        public final static String TOKEN="token";

        public Toast mToast;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            IntentFilter filter = new IntentFilter();
            filter.addAction("finish");
            registerReceiver(mFinishReceiver, filter);
        }

        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
            return super.onCreateOptionsMenu(menu);
        }
        @Override
        public void finish() {
            super.finish();
            overridePendingTransition(R.anim.base_slide_left_in, R.anim.base_slide_right_out);
        }
        @Override
        public void startActivityForResult(Intent intent, int requestCode) {
            super.startActivityForResult(intent, requestCode);
            overridePendingTransition(R.anim.base_slide_right_in, R.anim.base_slide_left_out);
        }
        @Override
        public void startActivity(Intent intent) {
            super.startActivity(intent);
            overridePendingTransition(R.anim.base_slide_right_in, R.anim.base_slide_left_out);
        }
        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            return super.onOptionsItemSelected(item);
        }

        /**
         * @param force if false, only try to hide when IMM is accepting text
         */
        public void hideKeyBoard(boolean force) {
            final InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm.isAcceptingText() || force) {
                Timer timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        if (imm != null) {
                            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                        }
                    }
                }, 200);
            }
        }

        private BroadcastReceiver mFinishReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                if ("finish".equals(intent.getAction())) {
                    finish();
                }
            }
        };

        @Override
        protected void onDestroy() {
            super.onDestroy();
            unregisterReceiver(mFinishReceiver);
        }

}
