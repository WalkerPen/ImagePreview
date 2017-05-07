package com.pen.imagepreview.libs;

import android.view.View;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Pen on 2017/5/7.
 */

public class ExitScaleJumpBean implements Serializable{
    public int index;

    public List<ImageInfo> images;

    public static class ImageInfo implements Serializable{
        public int width;
        public int height;
        public float x;
        public float y;
        public Object image;

        public void setImage(Object image) {
            this.image = image;
        }

        public void setView(View view) {
            this.width = view.getWidth();
            this.height = view.getHeight();
            this.x = view.getX();
            this.y = view.getY();
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

        public Object getImage() {
            return image;
        }
    }
}
