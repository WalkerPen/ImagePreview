package com.pen.imagepreview.libs;

import android.view.View;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Pen on 2017/5/6.
 */

public class ExitAlphaJumpBean implements Serializable{
    public ArrayList images;
    public int index;
    public int width;
    public int height;
    public float x;
    public float y;

    public void setImages(ArrayList images) {
        this.images = images;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public void setView(View view) {
        this.width = view.getWidth();
        this.height = view.getHeight();
        this.x = view.getX();
        this.y = view.getY();
    }

    public ArrayList getImages() {
        return images;
    }

    public int getIndex() {
        return index;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }
}
