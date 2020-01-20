package com.kanzankazu.itungitungan.util.widget.gallery2;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;

import com.kanzankazu.itungitungan.R;
import com.kanzankazu.itungitungan.util.NetworkUtil;
import com.kanzankazu.itungitungan.util.PictureUtil;

import java.util.ArrayList;

public class GalleryDetailActivity extends AppCompatActivity {

    private static final int REQ_CODE_COMMENT = 123;

    private ArrayList<GalleryImageModel> models = new ArrayList<>();
    private int posData;
    private Toolbar toolbar;
    private ViewPager mViewPager;
    private GalleryDetailPagerAdapter mGalleryDetailPagerAdapter;
    private GalleryImageModel model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        initComponent();
        initParam();
        initContent();
        initListener();

    }

    /*
    menu_activity
    *
    <menu xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto">
    <item
        android:id="@+id/action_close"
        android:icon="@drawable/ic_clear"
        android:title="Close"
        app:showAsAction="always" />
    </menu>
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    private void initComponent() {
        toolbar = findViewById(R.id.toolbar);
        mViewPager = findViewById(R.id.vp_gallery_detail);
    }

    private void initParam() {
        Bundle bundle = getIntent().getExtras();
        if (bundle!=null){
            models = getIntent().getParcelableArrayListExtra("models");
            posData = getIntent().getIntExtra("posData", 0);
        }

    }

    private void initContent() {
        setSupportActionBar(toolbar);

        ArrayList<String> imageList = new ArrayList<>();
        for (int i = 0; i < models.size(); i++) {
            GalleryImageModel q = models.get(i);
            imageList.add(NetworkUtil.isConnected(getApplicationContext()) ? q.getImage_posted() : TextUtils.isEmpty(q.getImage_posted_local()) ? q.getImage_posted() : !PictureUtil.isFileExists(q.getImage_posted_local()) ? q.getImage_posted() : q.getImage_posted_local());
        }

        mGalleryDetailPagerAdapter = new GalleryDetailPagerAdapter(GalleryDetailActivity.this, imageList);
        mViewPager.setPageTransformer(true, new DepthPageTransformer());
        mViewPager.setAdapter(mGalleryDetailPagerAdapter);
        mViewPager.setOffscreenPageLimit(2);
        mViewPager.setCurrentItem(posData);

        updateUi(posData);
    }

    private void updateUi(int posData) {

    }

    private void initListener() {
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                updateUi(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }
}
