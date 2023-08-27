package com.deep.apicall;

import androidx.appcompat.app.AppCompatActivity;
import com.deep.apicall.ImagePojo;

import java.util.List;

public abstract class SubBaseActivity extends AppCompatActivity {
    public abstract void onImageSelected(List<ImagePojo> profileImageList);
}
