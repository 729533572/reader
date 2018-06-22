package com.yn.reader.util;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;

/**
 * 加载图片
 * Created by sunxy on 2018/2/8.
 */

public class GlideUtils {
//    public static void load(Context context, String url, ImageView imageView) {
//        Glide.with(context).load(url).transform(new CenterCrop(context), new GlideRoundTransform(context, 5)).into(imageView);
//    }
//
//    public static void load(Context context, int resId, ImageView imageView) {
//        Glide.with(context).load(resId).transform(new CenterCrop(context), new GlideRoundTransform(context, 5)).into(imageView);
//    }

    public static void load(Context context, String url, ImageView imageView, int holdPlaceResId) {
        Glide.with(context).load(url).transform(new CenterCrop(context), new GlideRoundTransform(context, 5)).placeholder(holdPlaceResId).into(imageView);
    }

    public static void load(Context context, String url, ImageView imageView){
        Glide.with(context).load(url).into(imageView);
    }
    public static void load(Context context, int resId, ImageView imageView){
        Glide.with(context).load(resId).into(imageView);
    }

}
