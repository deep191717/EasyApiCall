package com.deep.apicall;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.net.ssl.HttpsURLConnection;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;

public class Api {

    private String BASE_URL = "";
    private String method;
    private ShowProgress showProgress;
    private ShowNoInternet showNoInternet;
    private boolean canShowProgress = false;


    public Api setRequestMethod(RequestMethod requestMethod) {
        if (requestMethod == RequestMethod.GET) {
            method = "GET";
        } else if (requestMethod == RequestMethod.POST) {
            method = "POST";
        }
        return this;
    }

    Activity activity;
    Context context;
    String TAG;

    public static Api with(String tag) {
        return new Api(tag);
    }
    public static Api with(Activity activity) {
        return new Api(activity, activity.getClass().getSimpleName());
    }

    public static Api with(Context activity) {
        return new Api(activity, activity.getClass().getSimpleName());
    }

    public static Api with(Activity activity, String baseUrl) {
        return new Api(activity, activity.getClass().getSimpleName(), baseUrl);
    }

    private RequestBody body;
    private HashMap<String, String> perms = null;

    public Api(String TAG) {
        this.TAG = TAG;
    }
    public Api(Activity activity, String TAG) {
        this.activity = activity;
        this.TAG = TAG;
        this.showProgress = ShowProgress.init(activity);
        this.showNoInternet = ShowNoInternet.init(activity);
    }

    public Api(Context context, String TAG) {
        this.context = context;
        this.TAG = TAG;
        this.showProgress = ShowProgress.init(context);
        this.showNoInternet = ShowNoInternet.init(context);
    }

    public Api(Activity activity, String TAG, String baseUrl) {
        this.activity = activity;
        this.TAG = TAG;
        this.BASE_URL = baseUrl;
        this.showProgress = ShowProgress.init(activity);
        this.showNoInternet = ShowNoInternet.init(activity);
    }

    @Deprecated
    public Api setPerms(String key, String value) {
        if (perms == null) {
            perms = new HashMap<>();
        }
        perms.put(key, value);
        return this;
    }

    public Api canShowProgress(boolean canShowProgress) {
        this.canShowProgress = canShowProgress;
        return this;
    }

    private MediaType type = MultipartBody.FORM;

    public Api setPermsType(MediaType type) {
        this.type = type;
        return this;
    }

    MultipartBody.Builder builder;

    public Api setPerm(String key, String value) {
        if ("POST".equals(method)) {
            if (builder == null) {
                builder = new MultipartBody.Builder().setType(type);
            }
            Log.e(TAG, "setPerm: " + key + " = " + value);
            builder.addFormDataPart(key, value);
        } else if ("GET".equals(method)) {
            setPerms(key, value);
        }
        return this;
    }

    public Api setPerm(Object object) {
        if (builder == null) {
            builder = new MultipartBody.Builder().setType(type);
        }
        for (Field field : object.getClass().getDeclaredFields()) {
            for (Method method : object.getClass().getDeclaredMethods()) {
                if (method.getName().startsWith("get") && method.getName().length() == (field.getName().length() + 3) && method.getName().toLowerCase().endsWith(field.getName().toLowerCase())) {
                    try {
                        Object value = method.invoke(object);
                        Log.e(TAG, "setPrem: " + field.getName() + " = " + value);
                        builder.addFormDataPart(field.getName(), value == null ? "" : (String) value);
                    } catch (IllegalAccessException | InvocationTargetException | ClassCastException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return this;
    }


    public Api setPerm(String key, String fileName, String url) {
        if (builder == null) {
            builder = new MultipartBody.Builder().setType(type);
        }
        builder.addFormDataPart(key, fileName, RequestBody.create(MediaType.parse("application/octet-stream"),
                new File(url)));
        return this;
    }

    @Deprecated
    private void setPerms() {
        if (body == null) {
            MultipartBody.Builder builder = new MultipartBody.Builder().setType(type);
            for (Map.Entry<String, String> entry : perms.entrySet()) {
                if (entry.getKey() != null && entry.getValue() != null) {
                    builder.addFormDataPart(entry.getKey(), entry.getValue());
                }
                Log.e(TAG, "setPerms: " + entry.getKey() + " = " + entry.getValue());
            }
            body = builder.build();
        }
    }


    public Api setSubFolder(String subFolder) {
        BASE_URL = BASE_URL + subFolder + "/";
        return this;
    }

    public void showProgress() {
        if (canShowProgress && showProgress!=null) {
            showProgress.Show();
        }
    }

    public void dismissProgress() {
        if (canShowProgress && showProgress!=null) {
            showProgress.Dismiss();
        }
    }

    public Class<?> dataTo(String jsonData,Class<?> pojo){
        try{
            return (Class<?>) new Gson().fromJson(jsonData,pojo);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public void showInternet() {
        if (showNoInternet!=null) {
            showNoInternet.Show();
        }
    }

    public void dismissInternet() {
        if (showNoInternet!=null) {
            showNoInternet.Dismiss();
        }
    }

    public void call(String url, @NonNull Response response) {
        dismissInternet();
        if (checkInternet()) {

            if (response.getContext() == null) {
                if (activity != null) {
                    response.with(activity);
                }
                if (context != null) {
                    response.with(context);
                }
            }

            if ("GET".equals(method)) {
                url = embedUrl(url);
            } else {

                if (perms != null && !perms.isEmpty()) {
                    setPerms();
                } else {
                    if (body == null) {
                        if (builder == null) {
                            //builder = new MultipartBody.Builder().setType(type);
                            body = RequestBody.create(MediaType.parse("text/plain"), "");
                        } else {
                            body = builder.build();
                        }
                    }
                }
            }
            String finalUrl = url;
            showProgress();
            ExecutorService executor = Executors.newSingleThreadExecutor();
            Handler handler = new Handler(Looper.getMainLooper());
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    try {

//                String PROXY_SERVER_HOST = "promotionwala.co.in";
//                int PROXY_SERVER_PORT = 8088;
//                Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(PROXY_SERVER_HOST, PROXY_SERVER_PORT));

                        OkHttpClient client = new OkHttpClient().newBuilder()
                                .build();
                        //MediaType mediaType = MediaType.parse("text/plain");

                        Log.e(TAG, "call: " + BASE_URL + finalUrl);

                        Request request = new Request.Builder()
                                .url(BASE_URL + finalUrl)
                                .method(method, body)
                                .build();
                        okhttp3.Response res = client.newCall(request).execute();
                        //Log.e(TAG, "call: "+res.headers());

//                List<String> Cookielist = res.headers().values("Set-Cookie");
//                for (String s : Cookielist){
//                    Log.e(TAG, "call: "+s );
//                }
//                String v = (Cookielist .get(0).split(";"))[0];
//                Log.e(TAG, "call: "+jsessionid );

                        int responseCode = res.code();
                        StringBuilder sBuilder1 = new StringBuilder();

                        if (responseCode == HttpsURLConnection.HTTP_OK) {
                            String line;
                            ResponseBody responseBody = res.body();

                            try {
                                BufferedReader br = new BufferedReader(new InputStreamReader(responseBody.byteStream()));
                                while ((line = br.readLine()) != null) {
                                    sBuilder1.append(line).append("\n");
                                }
                                Log.e(TAG, "call: " + sBuilder1);
                                if (!sBuilder1.toString().trim().isEmpty()) {
                                    Object json = new JSONTokener(sBuilder1.toString()).nextValue();
                                    if (json instanceof JSONObject) {
                                        JSONObject jsonObject = new JSONObject(sBuilder1.toString());
                                        if (jsonObject.has("res")) {
                                            String success = jsonObject.getString("res");
                                            String message = jsonObject.getString("msg");
                                            if (success.matches("success")) {
                                                Log.e(TAG, "call: in");
                                                String data = jsonObject.getString("data");
                                                Object json1 = new JSONTokener(data).nextValue();
                                                if (json1 instanceof JSONObject) {
                                                    dismissProgress();
                                                    handler.post(() -> response.onSuccess((JSONObject) json1));
                                                } else if (json1 instanceof JSONArray) {
                                                    dismissProgress();
                                                    handler.post(() -> response.onSuccess((JSONArray) json1));
                                                }
                                                dismissProgress();
                                                handler.post(() -> response.onSuccess(data));

                                            } else {
                                                dismissProgress();
                                                handler.post(() -> response.onFailed(responseCode, message));
                                            }
                                        }else {
                                            dismissProgress();
                                            handler.post(() -> response.onSuccess((JSONObject) json));
                                        }
                                    } else if (json instanceof JSONArray) {
                                        dismissProgress();
                                        handler.post(() -> response.onSuccess((JSONArray) json));
                                    } else {
                                        dismissProgress();
                                        handler.post(() -> response.onSuccess(sBuilder1.toString()));
                                    }
                                } else {

                                    dismissProgress();
                                    handler.post(() -> response.onFailed(responseCode, "No Request Found For this Data."));

                                }
                            } catch (Exception e) {
                                e.printStackTrace();

                                dismissProgress();
                                handler.post(() -> response.onFailed(responseCode, "No Request Found For this Data. due to exception"));

                            }

                        } else {

                            dismissProgress();
                            handler.post(() -> response.onFailed(responseCode, res.message().isEmpty() ? "Page Not Found" : res.message()));

                        }

                    } catch (Exception e) {
                        e.printStackTrace();

                        dismissProgress();
                        handler.post(() -> response.onFailed(500, e.getMessage()));

                    }
                }
            });


        } else {
            Log.e(TAG, "call: no internet ");
            showNoInternet(response);
        }
    }

    private void showNoInternet(Response response) {
        if (activity != null) {
            if (response.getContext() != null) {
                response.onFailed(0, "No Internet Connection");
            } else {
                if (activity != null) {
                    response.with(activity);
                    response.onFailed(0, "No Internet Connection");
                }
            }
        }
        showInternet();
    }

    private boolean checkInternet() {
        String status = null;
        Context con;
        if (context == null) {
            con = activity;
        } else {
            con = context;
        }
        if (context==null){
            Log.e(TAG, "checkInternet: UNABLE TO CHECK this is background process");
            return true;
        }
        ConnectivityManager cm = (ConnectivityManager) con.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null) {
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
                status = "Wifi enabled";
                Log.e(TAG, "checkInternet: " + status);
                return true;
            } else if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                status = "Mobile data enabled";
                Log.e(TAG, "checkInternet: " + status);
                return true;
            }
        } else {
            status = "No internet is available";
            Log.e(TAG, "checkInternet: " + status);
            return false;
        }

        return false;
    }

    private String embedUrl(String url) {
        StringBuilder u = new StringBuilder();
        u.append(url);
        if (perms != null && perms.entrySet().size() > 0) {
            u.append("?");
            for (Map.Entry<String, String> entry : perms.entrySet()) {
                u.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
            }
        }
        return u.toString();
    }

}
