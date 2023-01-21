package com.deep.apicall;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import androidx.appcompat.app.AlertDialog;

public class ShowNoInternet {

    AlertDialog alertDialog;
    Context context;

    public static ShowNoInternet init(Context context){
        return new ShowNoInternet(context);
    }

    public ShowNoInternet(Context context) {
        this.context = context;
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(LayoutInflater.from(context).inflate(R.layout.no_internet,null,false));
        alertDialog = builder.create();
        alertDialog.getWindow().setBackgroundDrawableResource(R.drawable.transperent_background);
        alertDialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
    }

    public void Show(){
        if (alertDialog!=null && !alertDialog.isShowing()) {
            alertDialog.show();
        }else {
            //Toast.makeText(context, "No Internet Alert is already showing", Toast.LENGTH_SHORT).show();
        }
    }

    public void Dismiss(){
        if (alertDialog!=null && alertDialog.isShowing()) {
            alertDialog.dismiss();
        }else {
            //Toast.makeText(context, "No Internet Alert Not showing", Toast.LENGTH_SHORT).show();
        }
    }

}
