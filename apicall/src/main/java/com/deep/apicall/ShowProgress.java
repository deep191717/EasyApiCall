package com.deep.apicall;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;


public class ShowProgress {

    AlertDialog alertDialog;
    Context context;

    public static ShowProgress init(Context context){
        return new ShowProgress(context);
    }

    public ShowProgress(Context context) {
        this.context = context;
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(false);
        builder.setView(LayoutInflater.from(context).inflate(R.layout.show_progress,null,false));
        alertDialog = builder.create();
        alertDialog.getWindow().setBackgroundDrawableResource(R.drawable.transperent_background);
        alertDialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
    }

    public void Show(){
        if (alertDialog!=null && !alertDialog.isShowing()) {
            alertDialog.show();
        }else {
            Toast.makeText(context, "Progress is already showing", Toast.LENGTH_SHORT).show();
        }
    }

    public void Dismiss(){
        if (alertDialog!=null && alertDialog.isShowing()) {
            alertDialog.dismiss();
        }else {
            Toast.makeText(context, "Progress Not showing", Toast.LENGTH_SHORT).show();
        }
    }

}
