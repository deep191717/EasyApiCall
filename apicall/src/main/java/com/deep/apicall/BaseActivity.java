package com.deep.apicall;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.OpenableColumns;
import android.view.View;
import android.widget.ImageView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;

import com.deep.apicall.ImagePojo;
import com.deep.apicall.FileUtils;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BaseActivity extends SubBaseActivity {

    ImageView profile_image;
    public List<ImagePojo> profileImageList;
    boolean image = true;

    @Override
    protected void onStart() {
        super.onStart();
        profileImageList = new ArrayList<>();
    }

    public void setImage(boolean image) {
        this.image = image;
    }

    public void checkCaptureImagePermission(ImageView profile_image) {
        this.profile_image = profile_image;
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            captureImagePermission.launch(Manifest.permission.READ_MEDIA_IMAGES);
        }else {
            captureImagePermission.launch(android.Manifest.permission.READ_EXTERNAL_STORAGE);
        }
    }


    private void captureImage() {
        try {
            String[] mimeTypes = {"image/jpg", "image/jpeg", "image/png"};
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
            selectImage.launch(intent);
        } catch (Exception e) {
            showSnackbar(findViewById(android.R.id.content), "Photo not found.");
        }
    }

    ActivityResultLauncher<String> captureImagePermission = registerForActivityResult(new ActivityResultContracts.RequestPermission(),
            result -> {
                if (result) {
                    captureImage();
                } else {
                    showSnackbar(findViewById(android.R.id.content), "READ EXTERNAL STORAGE permission needed to upload your profile.");
                }
            });

    ActivityResultLauncher<Intent> selectImage = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null) {
                            ImagePojo imagePojo = new ImagePojo();
                            imagePojo.setImageUrl(FileUtils.getPath(BaseActivity.this, data.getData()));
                            imagePojo.setImageName(getNameWithoutExtension(getFileName(data.getData())));
                            imagePojo.setImageNameWithExtensions(getFileName(data.getData()));
                            profileImageList.add(imagePojo);
                            onImageSelected(profileImageList);
                            if (image) {
                                profile_image.setImageURI(data.getData());
                            }
                        } else {
                            showSnackbar(findViewById(android.R.id.content), "Image not selected.");
                        }
                    }
                }
            });

    private void showSnackbar(View viewById, String s) {
        Snackbar.make(viewById,s,Snackbar.LENGTH_SHORT).show();
    }

    private String getNameWithoutExtension(String fileName) {
        String[] name = fileName.split("\\.");
        return name[0];
    }

    private String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            try (Cursor cursor = getContentResolver().query(uri, null, null, null, null)) {
                if (cursor != null && cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                    if (index >= 0) {
                        result = cursor.getString(index);
                    }
                }
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }

    public List<String> stringToArray(String string){
        String temp = string.replace("[","").replace("]","");
        String[] stringArray = temp.split(",");
        return new ArrayList<>(Arrays.asList(stringArray));
    }


    @Override
    public void onImageSelected(List<ImagePojo> profileImageList) {

    }



}
