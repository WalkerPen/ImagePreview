package com.pen.imagepreview;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.pen.imagepreview.libs.ExitScaleJumpBean;
import com.pen.imagepreview.libs.ImagePreviewActivity;
import com.pen.imagepreview.libs.ExitAlphaJumpBean;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView mImage;
    private ArrayList<String> mImages = new ArrayList<>();
    private ImageView mImage2;
    private ImageView mImage3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        mImages.add("http://192.168.1.109:8080/pen/biting.jpg");
        mImages.add("http://192.168.1.109:8080/pen/biting2.jpg");
        mImages.add("http://192.168.1.109:8080/pen/biting4.jpg");

        mImage = (ImageView) findViewById(R.id.image);
        mImage2 = (ImageView) findViewById(R.id.image2);
        mImage3 = (ImageView) findViewById(R.id.image3);
        mImage.setOnClickListener(this);
        mImage2.setOnClickListener(this);
        mImage3.setOnClickListener(this);

        Glide.with(this).load(mImages.get(0)).into(mImage);
        Glide.with(this).load(mImages.get(1)).into(mImage2);
        Glide.with(this).load(mImages.get(2)).into(mImage3);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.image:
                startScale(0);
                break;
            case R.id.image2:
                startScale(1);
                break;
            case R.id.image3:
                startScale(2);
                break;
        }
    }

    private void startScale(int index) {
        Intent intent = new Intent(this, ImagePreviewActivity.class);
        /*ExitAlphaJumpBean jumpBean = new ExitAlphaJumpBean();
        jumpBean.images = mImages;
        jumpBean.index = 0;
        jumpBean.x = mImage.getX();
        jumpBean.y = mImage.getY();
        jumpBean.width = mImage.getWidth();
        jumpBean.height = mImage.getHeight();*/
        ExitScaleJumpBean jumpBean = new ExitScaleJumpBean();
        jumpBean.index = index;
        jumpBean.images = new ArrayList<>();
        ExitScaleJumpBean.ImageInfo imageInfo1 = new ExitScaleJumpBean.ImageInfo();
        imageInfo1.x = mImage.getX();
        imageInfo1.y = mImage.getY();
        imageInfo1.width = mImage.getWidth();
        imageInfo1.height = mImage.getHeight();
        imageInfo1.image = mImages.get(0);
        jumpBean.images.add(imageInfo1);

        ExitScaleJumpBean.ImageInfo imageInfo2 = new ExitScaleJumpBean.ImageInfo();
        imageInfo2.setView(mImage2);
        imageInfo2.image = mImages.get(1);
        jumpBean.images.add(imageInfo2);

        ExitScaleJumpBean.ImageInfo imageInfo3 = new ExitScaleJumpBean.ImageInfo();
        imageInfo3.setView(mImage3);
        imageInfo3.image = mImages.get(2);
        jumpBean.images.add(imageInfo3);

        intent.putExtra("data", jumpBean);
        startActivity(intent);
        overridePendingTransition(0, 0);
    }
}
