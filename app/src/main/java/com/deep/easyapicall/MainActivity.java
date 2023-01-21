package com.deep.easyapicall;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.deep.apicall.Api;
import com.deep.apicall.RequestMethod;
import com.deep.apicall.Response;

import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Api.with(this).setRequestMethod(RequestMethod.GET).call("https://google.com/", new Response() {
            @Override
            public void onSuccess(JSONObject jsonObject) {
                super.onSuccess(jsonObject);
            }

            @Override
            public void onFailed(int code, String exception) {
                super.onFailed(code, exception);
            }
        });
    }
}