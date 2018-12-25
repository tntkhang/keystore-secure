package com.github.khangtran.keystoresecuredemo;

import android.app.Application;

/**
 * Created by KhangTran on 12/26/17.
 */

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        // Config RealmEncryptionHelper
//        RealmEncryptionHelper realmEncryptionHelper = RealmEncryptionHelper.init(this, getString(R.string.app_name));
//
//        // Config Realm
//        Realm.init(this);
//        RealmConfiguration config = new RealmConfiguration.Builder()
//                    .name("realm_encrypt.realm")
//                    .encryptionKey(realmEncryptionHelper.getEncryptKey()) // Call realmEncryptionHelper to get encrypt key for encrypting
//                    .build();
//        Realm.setDefaultConfiguration(config);
    }
}
