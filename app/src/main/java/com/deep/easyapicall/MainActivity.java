package com.deep.easyapicall;

import android.os.Bundle;

import com.deep.apicall.Api;
import com.deep.apicall.BaseActivity;
import com.deep.apicall.Environment;
import com.deep.apicall.RequestMethod;
import com.deep.apicall.Response;

import org.json.JSONObject;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Api.with("MainActivity", "https://rapidrasoi.in/")
                .setEnvironment(Environment.DEBUG)
                .setRequestMethod(RequestMethod.POST)
                .setSubFolder("api")
                .setPerm("city_id", "10")
                .setPerm("name", "")
                .execute("get_college.php", new Response() {
                    @Override
                    public void onSuccess(JSONObject jsonObject) {
                        super.onSuccess(jsonObject);
                    }

                    @Override
                    public void onFailed(int code, String exception, Environment environment) {
                        super.onFailed(code, exception, environment);
                    }

                });

//        setCrop(true);
//        setCapture(true);
//        checkCaptureImagePermission(findViewById(R.id.img));
    }
}