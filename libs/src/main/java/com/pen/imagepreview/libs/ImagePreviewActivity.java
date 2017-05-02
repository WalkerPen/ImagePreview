package com.pen.imagepreview.libs;

import android.graphics.Color;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.pen.imagepreview.libs.circleindicator.CircleIndicator;

import java.util.ArrayList;
import java.util.List;

public class ImagePreviewActivity extends AppCompatActivity {

    public static final String KEY_IMAGE_ARRAY = "key_image_array";
    public static final String KEY_INDEX = "key_index";

    private ViewPager mViewPager;
    private ArrayList<String> mImages;
    private CircleIndicator mIndicator;
    private int mIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_preview);

        initData();
        initView();
    }

    private void initData() {
        mImages = getIntent().getStringArrayListExtra(KEY_IMAGE_ARRAY);
        mIndex = getIntent().getIntExtra(KEY_INDEX, 0);
    }

    private void initView() {
        mViewPager = (ViewPager) findViewById(R.id.view_pager);
        mIndicator = (CircleIndicator) findViewById(R.id.indicator);
        MyPagerAdapter adapter = new MyPagerAdapter();
        mViewPager.setAdapter(adapter);
        mIndicator.setViewPager(mViewPager);

    }

    private class MyPagerAdapter extends PagerAdapter{
        @Override
        public int getCount() {
            return mImages.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView imageView = new ImageView(container.getContext());
            imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            Glide.with(ImagePreviewActivity.this)
                    .load(R.drawable.biting)
                    .into(imageView);
            container.addView(imageView);
            return imageView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }
}
