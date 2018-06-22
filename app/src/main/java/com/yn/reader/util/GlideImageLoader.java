package com.yn.reader.util;

import android.app.Activity;
import android.net.Uri;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.lzy.imagepicker.loader.ImageLoader;

import java.io.File;

/**
 * Created : lts .
 * Date: 2018/1/3
 * Email: lts@aso360.com
 */

public class GlideImageLoader implements ImageLoader {


    @Override
    public void displayImage(Activity activity, String path, ImageView imageView, int width, int height) {
        Glide
                .with(activity)
                .load(Uri.fromFile(new File(path)))
                .into(imageView);
    }

    @Override
    public void displayImagePreview(Activity activity, String path, ImageView imageView, int width, int height) {

    }

    @Override
    public void clearMemoryCache() {

    }
}
