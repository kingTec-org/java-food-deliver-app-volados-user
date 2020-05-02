package gofereats.utils;
/**
 * @package com.trioangle.gofereats
 * @subpackage utils
 * @category ImageUtils
 * @author Trioangle Product Team
 * @version 1.0
 **/

import android.content.Context;
import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import javax.inject.Inject;

import gofereats.configs.AppController;
import gofereats.configs.SessionManager;

/*****************************************************************
 ImageUtils
 ****************************************************************/
public class ImageUtils {

    public static int sCorner = 5;
    public static int sMargin = 1;

    public @Inject
    SessionManager sessionManager;

    public ImageUtils() {
        AppController.getAppComponent().inject(this);
    }

    public void loadGalleryImage(Context context, ImageView imageView, String url, int size) {
        try {
            if (!TextUtils.isEmpty(url)) {
                Glide.with(context).load(url).override(size, size).centerCrop().diskCacheStrategy(DiskCacheStrategy.ALL)

                        .into(imageView);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadGalleryImage(Context context, ImageView imageView, String url, boolean isHeightPixel, int s) {
        int size = s;
        try {
            int width = context.getResources().getDisplayMetrics().widthPixels;
            if (!isHeightPixel) {
                size = width - (int) (12 * context.getResources().getDisplayMetrics().density);
            }
            if (!TextUtils.isEmpty(url)) {
                Glide.with(context).load(url).override(width, size)
                        //.error(R.color.white)
                        .diskCacheStrategy(DiskCacheStrategy.ALL).into(imageView);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadImage(Context context, ImageView imageView, String url) {
        try {
            if (!TextUtils.isEmpty(url)) {
                /*Glide.with(context)
                        .load(url)
                        //.placeholder(R.color.text_light_gray)
                        //.error(R.color.text_light_gray)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(imageView);*/
                Glide.with(context).load(url).diskCacheStrategy(DiskCacheStrategy.ALL).into(imageView);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadImageCurve(Context context, ImageView imageView, String url, int image_id) {
        try {
            if (!TextUtils.isEmpty(url)) {
                /*Glide.with(context)
                        .load(url)
                        //.placeholder(R.color.text_light_gray)
                        //.error(R.color.text_light_gray)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(imageView);*/
                Glide.with(context).load(url).bitmapTransform(new RoundedCornersTransformation(context, sCorner, sMargin)).diskCacheStrategy(DiskCacheStrategy.ALL).into(imageView);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadSliderImage(Context context, ImageView imageView, String url) {
        if (!TextUtils.isEmpty(url)) {
            Glide.with(context).load(url)
                    //.centerCrop()
                    // .error(R.color.black)
                    .diskCacheStrategy(DiskCacheStrategy.ALL).into(imageView);
        }
    }

    public void loadCircleImage(Context context, ImageView imageView, String url) {
        try {
            if (!TextUtils.isEmpty(url)) {
                Glide.with(context).load(url)
                        //.override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                        //.placeholder(R.color.gray_color)
                        //.error(R.color.gray_color)
                        .diskCacheStrategy(DiskCacheStrategy.ALL).into(imageView);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
