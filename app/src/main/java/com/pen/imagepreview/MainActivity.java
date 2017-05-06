package com.pen.imagepreview;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.pen.imagepreview.libs.ImagePreviewActivity;
import com.pen.imagepreview.libs.JumpBean;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView mImage;
    private ArrayList<String> mImages = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        mImages.add("http://192.168.1.109:8080/pen/biting.jpg");
        mImages.add("http://192.168.1.109:8080/pen/biting2.jpg");
        mImages.add("http://192.168.1.109:8080/pen/biting3.jpg");

        mImage = (ImageView) findViewById(R.id.image);
        mImage.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.image:
                Intent intent = new Intent(this, ImagePreviewActivity.class);
                JumpBean jumpBean = new JumpBean();
                jumpBean.mImages = mImages;
                jumpBean.x = mImage.getX();
                jumpBean.y = mImage.getY();
                jumpBean.width = mImage.getWidth();
                jumpBean.height = mImage.getHeight();
                intent.putExtra("data", jumpBean);
                startActivity(intent);
                overridePendingTransition(0, 0);
                break;
        }
    }
}
