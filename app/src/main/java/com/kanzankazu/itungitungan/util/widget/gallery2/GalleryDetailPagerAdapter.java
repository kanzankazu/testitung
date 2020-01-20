package com.kanzankazu.itungitungan.util.widget.gallery2;

import android.app.Activity;
import android.graphics.Bitmap;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.github.chrisbanes.photoview.PhotoView;
import com.kanzankazu.itungitungan.util.InputValidUtil;

import java.io.File;
import java.util.ArrayList;

/*
 implementation 'com.github.chrisbanes:PhotoView:2.0.0'
 **/
public class GalleryDetailPagerAdapter extends PagerAdapter {
    ArrayList<String> imagePaths;
    private Activity mActivity;

    public GalleryDetailPagerAdapter(Activity activity, ArrayList<String> imagePaths) {
        this.mActivity = activity;
        this.imagePaths = imagePaths;
    }

    @Override
    public int getCount() {
        return this.imagePaths.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public View instantiateItem(ViewGroup container, int position) {

        /*inflater = (LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View viewLayout = inflater.inflate(R.layout.layout_fullscreen_image, container, false);*/

        PhotoView photoView = new PhotoView(container.getContext());
        Glide.with(mActivity)
                .load(InputValidUtil.isLinkUrl(imagePaths.get(position)) ? imagePaths.get(position) : new File(imagePaths.get(position)))
                .asBitmap()
                .fitCenter()
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        photoView.setImageBitmap(resource);
                    }
                });
        container.addView(photoView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);

        return photoView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}
