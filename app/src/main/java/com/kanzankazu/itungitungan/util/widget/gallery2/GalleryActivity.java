package com.kanzankazu.itungitungan.util.widget.gallery2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.LinearLayout;

import com.kanzankazu.itungitungan.R;
import com.kanzankazu.itungitungan.util.Utils;

import java.util.ArrayList;

public class GalleryActivity extends AppCompatActivity {

    private GalleryAdapter rvGalleryAdapter;
    private RecyclerView rvGalleryfvbi;
    private CustomScrollView nsvGalleryfvbi;
    private SwipeRefreshLayout srlGalleryfvbi;
    private LinearLayout llLoadModeGalleryfvbi;

    private Toolbar toolbar;
    private ArrayList<GalleryImageModel> data = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        initComponent();
        initContent();
        initListener();
    }

    private void initComponent() {
        rvGalleryfvbi = (RecyclerView) findViewById(R.id.rvGallery);
        nsvGalleryfvbi = (CustomScrollView) findViewById(R.id.nsvGallery);
        srlGalleryfvbi = (SwipeRefreshLayout) findViewById(R.id.srlGallery);
        llLoadModeGalleryfvbi = (LinearLayout) findViewById(R.id.llLoadModeGallery);
    }

    private void initContent() {

        initToolbar("Gallery");

        rvGalleryAdapter = new GalleryAdapter(GalleryActivity.this, data, position -> moveToGalleryDetail(position));
        //rvGalleryAdapter.setHasStableIds(true);
        rvGalleryfvbi.setNestedScrollingEnabled(false);
        rvGalleryfvbi.setAdapter(rvGalleryAdapter);
        //rvGalleryfvbi.setHasFixedSize(true);
        //rvGalleryfvbi.setItemViewCacheSize(50);
        //rvGalleryfvbi.setDrawingCacheEnabled(true);
        //rvGalleryfvbi.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        rvGalleryfvbi.setLayoutManager(new GridLayoutManager(this, 3));

        getGalleryData();
    }

    private void initListener() {
        srlGalleryfvbi.setOnRefreshListener(() -> getGalleryData());

        nsvGalleryfvbi.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener) (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
            if (scrollY > oldScrollY) {
                Log.d("Lihat", "initListener GalleryActivity : " + "Scroll DOWN");
            }
            if (scrollY < oldScrollY) {
                Log.d("Lihat", "initListener GalleryActivity : " + "Scroll UP");
            }
            if (scrollY == 0) {
                Log.d("Lihat", "initListener GalleryActivity : " + "TOP SCROLL");
            }
            if (scrollY == (v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight())) {

            }

            /*int measuredHeight = homeIVEventBackgroundfvbi.getMeasuredHeight();
            int measuredHeight1 = homeLLWriteSomethingfvbi.getMeasuredHeight();
            int measuredHeight2 = 0;
            for (int i = 0; i < 4; i++) {
                measuredHeight2 = measuredHeight2 + recyclerView.getChildAt(i).getMeasuredHeight();
            }*/

            /*if (scrollY > measuredHeight + measuredHeight1 + measuredHeight2) {
                homeFABHomeUpfvbi.setVisibility(View.VISIBLE);
            } else {
                homeFABHomeUpfvbi.setVisibility(View.GONE);
            }*/
        });
    }

    private void moveToGalleryDetail(int position) {
        Intent intent = new Intent(GalleryActivity.this, GalleryDetailActivity.class);
        intent.putExtra("models", data);
        intent.putExtra("posData", position);
        startActivity(intent);

    }

    public void initToolbar(String title) {
        toolbar = findViewById(R.id.toolbar);
        Utils.setupAppToolbarForActivity(this, toolbar, title);
    }

    private void getGalleryData() {
        if (srlGalleryfvbi.isRefreshing()) {
            srlGalleryfvbi.setRefreshing(false);
        } else {

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return super.onOptionsItemSelected(item);
    }

}
