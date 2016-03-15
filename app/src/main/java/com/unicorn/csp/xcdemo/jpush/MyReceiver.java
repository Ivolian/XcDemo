package com.unicorn.csp.xcdemo.jpush;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.unicorn.csp.xcdemo.activity.technician.MainActivity;
import com.unicorn.csp.xcdemo.utils.ToastUtils;

import org.json.JSONObject;

import cn.jpush.android.api.JPushInterface;

/**
 * 自定义接收器
 * <p/>
 * 如果不定义这个 Receiver，则：
 * 1) 默认用户会打开主界面
 * 2) 接收不到自定义消息
 */
public class MyReceiver extends BroadcastReceiver {
    private static final String TAG = "JPush";

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
//		Log.d(TAG, "[MyReceiver] onReceive - " + intent.getAction() + ", extras: " + printBundle(bundle));

        if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
            String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
            Log.d(TAG, "[MyReceiver] 接收Registration Id : " + regId);
            //send the Registration Id to your server...

        } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
            Log.d(TAG, "[MyReceiver] 接收到推送下来的自定义消息: " + bundle.getString(JPushInterface.EXTRA_MESSAGE));
            ToastUtils.show("hehe");

//        	processCustomMessage(context, bundle);

        } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {


            Log.d(TAG, "[MyReceiver] 接收到推送下来的通知");
            int notifactionId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
            Log.d(TAG, "[MyReceiver] 接收到推送下来的通知的ID: " + notifactionId);

        } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
            Log.d(TAG, "[MyReceiver] 用户点击打开了通知");
            //打开自定义的Activity

            try {

                String jsonString = bundle.getString(JPushInterface.EXTRA_EXTRA);
                JSONObject jsonObject = new JSONObject(jsonString);
                String type = jsonObject.getString("type");
                Class activityClass = null;
                int currentItem = 0;
                switch (type) {
                    case "Issue":
                    case "Distribute":
                        activityClass = MainActivity.class;
                        currentItem = 0;
                        break;
                    case "IssueTimeout":
                        activityClass = com.unicorn.csp.xcdemo.activity.chief.MainActivity.class;
                        currentItem = 0;
                        break;
                    case "Review":
                        activityClass = com.unicorn.csp.xcdemo.activity.chief.MainActivity.class;
                        currentItem = 1;
                        break;

                }


                Intent intent1 = new Intent(context, activityClass);
                intent1.putExtra("currentItem", currentItem);
//                i.putExtras(bundle);
                intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(intent1);
            } catch (Exception e) {
                //
            }

        } else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent.getAction())) {
            Log.d(TAG, "[MyReceiver] 用户收到到RICH PUSH CALLBACK: " + bundle.getString(JPushInterface.EXTRA_EXTRA));
            //在这里根据 JPushInterface.EXTRA_EXTRA 的内容处理代码，比如打开新的Activity， 打开一个网页等..

        } else if (JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent.getAction())) {
            boolean connected = intent.getBooleanExtra(JPushInterface.EXTRA_CONNECTION_CHANGE, false);
            Log.w(TAG, "[MyReceiver]" + intent.getAction() + " connected state change to " + connected);
        } else {
            Log.d(TAG, "[MyReceiver] Unhandled intent - " + intent.getAction());
        }
    }


}
