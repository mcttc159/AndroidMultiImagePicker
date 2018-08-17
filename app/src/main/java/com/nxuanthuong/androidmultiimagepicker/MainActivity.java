package com.nxuanthuong.androidmultiimagepicker;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.nxuanthuong.androidmultiimagepicker.adapters.GalleryItemAdapter;
import com.nxuanthuong.androidmultiimagepicker.models.Picture;
import com.nxuanthuong.androidmultiimagepicker.utils.ConstantDataManager;
import com.nxuanthuong.androidmultiimagepicker.utils.Libraries;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerViewGallery;
    private ArrayList<Picture> pictures;
    GalleryItemAdapter adapter;
    Handler handler;

    private ImageView imageViewButtonSend;
    private TextView textViewSelectedCount;
    private ConstraintLayout constraintLayoutSend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        imageViewButtonSend = findViewById(R.id.button_send);
        textViewSelectedCount = findViewById(R.id.textViewSeletedCount);
        constraintLayoutSend = findViewById(R.id.layoutSend);

        ImageView imageViewSendDetail = findViewById(R.id.button_send);
        imageViewSendDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentDetail = new Intent(MainActivity.this, ListImageSelectedActivity.class);

                ArrayList<Picture> picturesSelected = adapter.getAllPictureSelected();
                intentDetail.putParcelableArrayListExtra("listpicture", picturesSelected);

                startActivity(intentDetail);
            }
        });

        pictures = new ArrayList<>();
        recyclerViewGallery = findViewById(R.id.recyclerViewGallery);
        recyclerViewGallery.setLayoutManager(new GridLayoutManager(this, 3));
        adapter = new GalleryItemAdapter(this, pictures, new GalleryItemAdapter.ItemSelectedChangeListener() {
            @Override
            public void onItemSelectedChange(int number) {
                if (number > 0) {
                    constraintLayoutSend.setVisibility(View.VISIBLE);
                    textViewSelectedCount.setText(number + "");
                } else {
                    constraintLayoutSend.setVisibility(View.GONE);
                }
            }
        });
        recyclerViewGallery.setAdapter(adapter);

        handler = new Handler();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            //lay hinh tu camera
            Libraries.requestPermissionStorage(MainActivity.this);
        } else {
            new Thread() {

                @Override
                public void run() {
                    Looper.prepare();
                    handler.post(new Runnable() {

                        @Override
                        public void run() {
                            pictures.clear();
                            pictures.addAll(Picture.getGalleryPhotos(MainActivity.this));
                            adapter.notifyDataSetChanged();
                            //imageListRecyclerAdapter.addAll(getGalleryPhotos());
                            //checkImageStatus();
                        }
                    });
                    Looper.loop();
                }


            }.start();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == ConstantDataManager.PERMISSION_REQUEST_CODE_EXTERNAL_STORAGE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                new Thread() {

                    @Override
                    public void run() {
                        Looper.prepare();
                        handler.post(new Runnable() {

                            @Override
                            public void run() {
                                pictures.clear();
                                pictures.addAll(Picture.getGalleryPhotos(MainActivity.this));
                                adapter.notifyDataSetChanged();
                                //imageListRecyclerAdapter.addAll(getGalleryPhotos());
                                //checkImageStatus();
                            }
                        });
                        Looper.loop();
                    }
                }.start();

            } else {
                //deny
                Libraries.requestPermissionStorageDeny(MainActivity.this);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
