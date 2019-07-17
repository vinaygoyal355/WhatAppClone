package com.example.whatappclone;

import android.app.Application;
import com.parse.Parse;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("mH64BtQPQL4tszX7YRIpcyXbHQwCSKpX7Hy74c7l")
                .clientKey("qEdE6HA8gnmlOLVhWdb1ysGHdzQ0kC16W2HJmhJa")
                .server("https://parseapi.back4app.com/")
                .build());
    }
}
