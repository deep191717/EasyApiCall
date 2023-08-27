package com.deep.easyapicall;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.deep.apicall.Api;
import com.deep.apicall.RequestMethod;
import com.deep.apicall.Response;

import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Api.with("MainActivity","https://google.com/").setRequestMethod(RequestMethod.GET).call("h", new Response() {
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