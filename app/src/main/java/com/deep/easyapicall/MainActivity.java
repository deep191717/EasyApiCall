package com.deep.easyapicall;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.deep.apicall.Api;
import com.deep.apicall.BaseActivity;
import com.deep.apicall.RequestMethod;
import com.deep.apicall.Response;

import org.json.JSONObject;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Api.with("MainActivity","https://google.com/")
                .setRequestMethod(RequestMethod.GET)
                .execute("h", new Response() {
            @Override
            public void onSuccess(JSONObject jsonObject) {
                super.onSuccess(jsonObject);
            }

            @Override
            public void onFailed(int code, String exception) {
                super.onFailed(code, exception);
            }
        });

        setCrop(true);
        setCapture(true);
        checkCaptureImagePermission(findViewById(R.id.img));
    }
}