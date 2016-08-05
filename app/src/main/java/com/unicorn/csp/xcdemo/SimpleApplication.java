package com.unicorn.csp.xcdemo;

import android.content.Context;
import android.support.multidex.MultiDexApplication;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.marswin89.marsdaemon.DaemonClient;
import com.marswin89.marsdaemon.DaemonConfigurations;
import com.unicorn.csp.xcdemo.daemon.Receiver1;
import com.unicorn.csp.xcdemo.daemon.Receiver2;
import com.unicorn.csp.xcdemo.daemon.Service2;
import com.unicorn.csp.xcdemo.volley.SimpleVolley;

import cat.ereza.customactivityoncrash.CustomActivityOnCrash;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.service.PushService;


public class SimpleApplication extends MultiDexApplication {

    private static SimpleApplication instance;

    public static SimpleApplication getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {

        super.onCreate();
        instance = this;
        SimpleVolley.init(instance);
        JPushInterface.init(instance);
        JPushInterface.setDebugMode(true);
        Fresco.initialize(this);
        CustomActivityOnCrash.install(this);


    }
    private DaemonClient mDaemonClient;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        mDaemonClient = new DaemonClient(createDaemonConfigurations());
        mDaemonClient.onAttachBaseContext(base);
    }



    private DaemonConfigurations createDaemonConfigurations(){
        DaemonConfigurations.DaemonConfiguration configuration1 = new DaemonConfigurations.DaemonConfiguration(
                "com.unicorn.csp.xcdemo:process1",
                PushService.class.getCanonicalName(),
                Receiver1.class.getCanonicalName());
        DaemonConfigurations.DaemonConfiguration configuration2 = new DaemonConfigurations.DaemonConfiguration(
                "com.unicorn.csp.xcdemo:process2",
                Service2.class.getCanonicalName(),
                Receiver2.class.getCanonicalName());
        DaemonConfigurations.DaemonListener listener = new MyDaemonListener();
        //return new DaemonConfigurations(configuration1, configuration2);//listener can be null
        return new DaemonConfigurations(configuration1, configuration2, listener);
    }

    class MyDaemonListener implements DaemonConfigurations.DaemonListener {
        @Override
        public void onPersistentStart(Context context) {
        }

        @Override
        public void onDaemonAssistantStart(Context context) {
        }

        @Override
        public void onWatchDaemonDaed() {
        }
    }
}
