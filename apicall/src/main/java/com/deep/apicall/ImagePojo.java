package com.deep.apicall;

import androidx.annotation.NonNull;

public class ImagePojo {

    String imageUrl;
    String imageNameWithExtensions;
    String imageName;

    public ImagePojo() {
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getImageNameWithExtensions() {
        return imageNameWithExtensions;
    }

    public void setImageNameWithExtensions(String imageNameWithExtensions) {
        this.imageNameWithExtensions = imageNameWithExtensions;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    @NonNull
    @Override
    public String toString() {
        return "ImagePojo{" +
                "imageUrl='" + imageUrl + '\'' +
                ", imageNameWithExtensions='" + imageNameWithExtensions + '\'' +
                ", imageName='" + imageName + '\'' +
                '}';
    }
}
