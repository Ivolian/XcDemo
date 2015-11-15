package com.unicorn.csp.xcdemo.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

import java.io.File;
import java.io.FileOutputStream;
import java.util.UUID;

public class ImageUtils {

    public static Uri getRandomPhotoUri() {
        String filePath = ConfigUtils.getBaseDirPath() + File.separator + UUID.randomUUID().toString() + ".jpg";
        return Uri.fromFile(new File(filePath));
    }

    public static BitmapFactory.Options getFactoryOptions(final int width, final int height, final String imagePath) {

        BitmapFactory.Options factoryOptions = new BitmapFactory.Options();

        factoryOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(imagePath, factoryOptions);

        final int imageWidth = factoryOptions.outWidth;
        final int imageHeight = factoryOptions.outHeight;

        int scaleFactor = Math.min(imageWidth / width, imageHeight / height);

        factoryOptions.inJustDecodeBounds = false;
        factoryOptions.inSampleSize = scaleFactor;

        return factoryOptions;
    }

    public static String compressPhoto(String photoPath) {

        String compressPhotoPath = photoPath.substring(0, photoPath.length() - 4) + "_compress.jpg";
        Bitmap bitmap = BitmapFactory.decodeFile(photoPath);
        File file = new File(compressPhotoPath);
        try {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 20, new FileOutputStream(file));
        } catch (Exception e) {
            //
        }
        return compressPhotoPath;
    }

}