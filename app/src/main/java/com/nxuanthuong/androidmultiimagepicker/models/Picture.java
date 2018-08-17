package com.nxuanthuong.androidmultiimagepicker.models;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;

public class Picture {
    private String path;
    private int selectCount;
    //để biets vị trí nào trong adapter để refresh đúng index đó
    private int position;

    public Picture() {
    }

    public Picture(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getSelectCount() {
        return selectCount;
    }

    public void setSelectCount(int selectCount) {
        this.selectCount = selectCount;
    }


    @Override
    public boolean equals(Object obj) {
        //return super.equals(obj);
        return this.getSelectCount()==((Picture)obj).getSelectCount();
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public static ArrayList<Picture> getGalleryPhotos(Context context) {
        ArrayList<Picture> pictures = new ArrayList<>();

        Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        String[] columns = {MediaStore.Images.Media.DATA, MediaStore.Images.Media._ID};
        String orderBy = MediaStore.Images.Media._ID;

        Cursor cursorPhotos = context.getContentResolver().query(uri, columns, null, null, orderBy);
        if (cursorPhotos != null && cursorPhotos.getCount() > 0) {
            while (cursorPhotos.moveToNext()) {
                Picture picture=new Picture();

                int indexPath=cursorPhotos.getColumnIndex(MediaStore.MediaColumns.DATA);
                picture.setPath(cursorPhotos.getString(indexPath));

                Log.d("Duongdananh",picture.getPath());
                pictures.add(picture);
            }
        }
        Collections.reverse(pictures);
        return pictures;
    }
}
