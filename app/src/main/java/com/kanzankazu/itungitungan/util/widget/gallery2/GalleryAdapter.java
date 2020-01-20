package com.kanzankazu.itungitungan.util.widget.gallery2;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.kanzankazu.itungitungan.R;
import com.kanzankazu.itungitungan.util.PictureUtil;

import java.util.ArrayList;
import java.util.List;

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.ViewHolder> {

    private final Activity activity;
    private GalleryAdapterListener galleryAdapterListener;
    private List<GalleryImageModel> models = new ArrayList<>();

    public GalleryAdapter(Activity activity, List<GalleryImageModel> models, GalleryAdapterListener galleryAdapterListener) {
        this.activity = activity;
        this.models = models;
        this.galleryAdapterListener = galleryAdapterListener;
    }

    @NonNull
    @Override
    public GalleryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_list_gallery_grid, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull GalleryAdapter.ViewHolder holder, int position) {
        GalleryImageModel model = models.get(position);

        new Handler().postDelayed(() -> {
            //code here
            Glide.with(activity)
                    .load(model.getImage_posted())
                    .asBitmap()
                    .error(R.mipmap.ic_launcher)
                    .placeholder(R.drawable.ic_photo)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .dontAnimate()
                    .into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                            Bitmap resizeImageBitmap = PictureUtil.resizeImageBitmap(resource, 270);
                            holder.gallery_img_fvbi.setImageBitmap(resizeImageBitmap);
                        }
                    });
        }, 2000);

        holder.gallery_img_fvbi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                galleryAdapterListener.onDetailClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return models.size();
    }

    public void setData(List<GalleryImageModel> datas) {
        if (datas.size() > 0) {
            models.clear();
            models = datas;
        } else {
            models = datas;
        }
        notifyDataSetChanged();
    }

    public void addDatas(List<GalleryImageModel> datas) {
        models.addAll(datas);
        notifyItemRangeInserted(models.size(), datas.size());
    }

    public interface GalleryAdapterListener {
        void onDetailClick(int position);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageView gallery_img_fvbi;

        ViewHolder(View itemView) {
            super(itemView);
            gallery_img_fvbi = (ImageView) itemView.findViewById(R.id.gallery_img);
        }
    }
}
