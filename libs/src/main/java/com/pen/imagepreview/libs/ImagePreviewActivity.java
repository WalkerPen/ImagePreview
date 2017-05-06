package com.pen.imagepreview.libs;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.BitmapTypeRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.pen.imagepreview.libs.circleindicator.CircleIndicator;

import java.util.ArrayList;

public class ImagePreviewActivity extends AppCompatActivity {


    private ViewPager mViewPager;
    private ArrayList<String> mImages;
    private CircleIndicator mIndicator;
    private int mIndex;
    private JumpBean mJumpBean;
    private int mMaxWidth; //ViewPager的放大后宽度
    private int mMinWidth; //ViewPager的缩小后宽度
    private int mMaxHeight; //ViewPager的放大后高度
    private int mMinHeight; //ViewPager的缩小后高度
    private float mMaxX;
    private float mMinX;
    private float mMaxY;
    private float mMinY;
    private RelativeLayout mRlContainer;
    private View mViewLeft;
    private View mViewTop;
    private View mViewRight;
    private View mViewBottom;
    private boolean mHigher;//true表示图片比ImageView相对要高，false表示相对要宽
    private View mViewBackground;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_preview);

        initData();
        initView();
        initScale();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private void startEnterAnimator() {
        ViewCompat.setX(mRlContainer, mJumpBean.x);
        ViewCompat.setY(mRlContainer, mJumpBean.y);
        mRlContainer.getLayoutParams().width = mMinWidth;
        mRlContainer.getLayoutParams().height = mMinHeight;
        mRlContainer.requestLayout();

        ObjectAnimator animatorX = ObjectAnimator.ofFloat(mRlContainer, "X", mMinX, mMaxX);
        ObjectAnimator animatorY = ObjectAnimator.ofFloat(mRlContainer, "Y", mMinY, mMaxY);
        ValueAnimator animatorAlpha = ValueAnimator.ofInt(Color.WHITE, Color.BLACK);
        animatorAlpha.setEvaluator(new ArgbEvaluator());
        animatorAlpha.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int alpha = (int) animation.getAnimatedValue();
                mViewBackground.setBackgroundColor(alpha);
                mViewLeft.setBackgroundColor(alpha);
                mViewTop.setBackgroundColor(alpha);
                mViewRight.setBackgroundColor(alpha);
                mViewBottom.setBackgroundColor(alpha);
            }
        });
        ValueAnimator animatorWidth = ValueAnimator.ofInt(mMinWidth, mMaxWidth);
        animatorWidth.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int width = (int) animation.getAnimatedValue();
                mRlContainer.getLayoutParams().width = width;
//                Log.d("width", width + "");
                mRlContainer.requestLayout();
            }
        });
        ValueAnimator animatorHeight = ValueAnimator.ofInt(mMinHeight, mMaxHeight);
        animatorHeight.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int height = (int) animation.getAnimatedValue();
//                Log.d("height", height + "");
                mRlContainer.getLayoutParams().height = height;
                mRlContainer.requestLayout();
            }
        });
        ValueAnimator animatorShield = null;
        if(mHigher) {
            animatorShield = ValueAnimator.ofInt((mMinHeight - mJumpBean.height)/2, 0);
        }else {
            animatorShield = ValueAnimator.ofInt((mMinWidth - mJumpBean.width)/2, 0);
        }
        animatorShield.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int distance = (int) animation.getAnimatedValue();
                //如果图片比ImageView相对更高，那么缩放上下的view
                if(mHigher) {
                    mViewTop.getLayoutParams().height = distance;
                    mViewBottom.getLayoutParams().height = distance;
                }else {
                    mViewLeft.getLayoutParams().width = distance;
                    mViewRight.getLayoutParams().width = distance;
                }
                mRlContainer.requestLayout();
            }
        });
        AnimatorSet set = new AnimatorSet();
        set.play(animatorX).with(animatorY).with(animatorWidth).with(animatorAlpha).with(animatorShield).with(animatorHeight);
        set.setDuration(300);
        set.setInterpolator(null);
        set.start();
    }

    private void startExitAnimator() {
        ObjectAnimator animatorX = ObjectAnimator.ofFloat(mRlContainer, "X", mMaxX, mMinX);
        ObjectAnimator animatorY = ObjectAnimator.ofFloat(mRlContainer, "Y", mMaxY, mMinY);
        ValueAnimator animatorAlpha = ValueAnimator.ofInt(Color.BLACK, Color.WHITE);
        animatorAlpha.setEvaluator(new ArgbEvaluator());
        animatorAlpha.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int alpha = (int) animation.getAnimatedValue();
                mViewBackground.setBackgroundColor(alpha);
                mViewLeft.setBackgroundColor(alpha);
                mViewTop.setBackgroundColor(alpha);
                mViewRight.setBackgroundColor(alpha);
                mViewBottom.setBackgroundColor(alpha);
            }
        });
        ValueAnimator animatorWidth = ValueAnimator.ofInt(mMaxWidth, mMinWidth);
        animatorWidth.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int width = (int) animation.getAnimatedValue();
                mRlContainer.getLayoutParams().width = width;
//                Log.d("width", width + "");
                mRlContainer.requestLayout();
            }
        });
        ValueAnimator animatorHeight = ValueAnimator.ofInt(mMaxHeight, mMinHeight);
        animatorHeight.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int height = (int) animation.getAnimatedValue();
//                Log.d("height", height + "");
                mRlContainer.getLayoutParams().height = height;
                mRlContainer.requestLayout();
            }
        });
        ValueAnimator animatorShield = null;
        if(mHigher) {
            animatorShield = ValueAnimator.ofInt(0, (mMinHeight - mJumpBean.height)/2);
        }else {
            animatorShield = ValueAnimator.ofInt(0, (mMinWidth - mJumpBean.width)/2);
        }
        animatorShield.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int distance = (int) animation.getAnimatedValue();
                //如果图片比ImageView相对更高，那么缩放上下的view
                if(mHigher) {
                    mViewTop.getLayoutParams().height = distance;
                    mViewBottom.getLayoutParams().height = distance;
                }else {
                    mViewLeft.getLayoutParams().width = distance;
                    mViewRight.getLayoutParams().width = distance;
                }
                mRlContainer.requestLayout();
            }
        });

        AnimatorSet set = new AnimatorSet();
        set.play(animatorX).with(animatorY).with(animatorWidth).with(animatorAlpha).with(animatorShield).with(animatorHeight);
        set.setDuration(300);
        set.setInterpolator(null);
        set.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                finish();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        set.start();
    }

    private void initData() {
        mJumpBean = (JumpBean) getIntent().getSerializableExtra("data");
        mImages = mJumpBean.mImages;
    }

    private void initView() {
        mRlContainer = (RelativeLayout) findViewById(R.id.rl_container);
        mViewPager = (ViewPager) findViewById(R.id.view_pager);
        mIndicator = (CircleIndicator) findViewById(R.id.indicator);
        mViewLeft = findViewById(R.id.view_left);
        mViewTop = findViewById(R.id.view_top);
        mViewRight = findViewById(R.id.view_right);
        mViewBottom = findViewById(R.id.view_bottom);
        mViewBackground = findViewById(R.id.view_background);

        MyPagerAdapter adapter = new MyPagerAdapter();
        mViewPager.setAdapter(adapter);
        mIndicator.setViewPager(mViewPager);
    }

    private void initScale() {
        ViewTreeObserver vto = mRlContainer.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mRlContainer.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                mMaxWidth = mRlContainer.getMeasuredWidth();
                mMaxHeight = mRlContainer.getMeasuredHeight();
                mMaxX = mRlContainer.getX();
                mMaxY = mRlContainer.getY();

                Glide.with(ImagePreviewActivity.this)
                        .load(R.drawable.biting4)
                        .asBitmap()
                        .into(new SimpleTarget<Bitmap>(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL) {

                            @Override
                            public void onResourceReady(Bitmap bitmap, GlideAnimation glideAnimation) {
                                // Do something with bitmap here.
                                int height = bitmap.getHeight();
                                int width = bitmap.getWidth();

                                //初始化最小的尺寸及对应起点坐标
                                initMinSize(height, width);

                                startEnterAnimator();
                            }

                        });
            }
        });
    }

    /**
     * 初始化最小的尺寸及对应起点坐标
     * @param height
     * @param width
     */
    private void initMinSize(int height, int width) {
        if ((height * 1f / width) > (mJumpBean.height * 1f / mJumpBean.width)) {
            mHigher = true;
            mMinHeight = height * mJumpBean.width / width;
            mMinWidth = mMinHeight * mJumpBean.width / mJumpBean.height;
        } else {
            mMinWidth = width * mJumpBean.height / height;
            mMinHeight = mMinWidth * mJumpBean.height / mJumpBean.width;
            mHigher = false;
        }
        mMinX = mJumpBean.x - (mMinWidth - mJumpBean.width) / 2;
        mMinY = mJumpBean.y - (mMinHeight - mJumpBean.height) / 2;
    }

    private class MyPagerAdapter extends PagerAdapter {
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

            imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
            Glide.with(ImagePreviewActivity.this)
                    .load(R.drawable.biting4)
                    .into(imageView);
            container.addView(imageView);
            return imageView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, 0);
    }

    @Override
    public void onBackPressed() {
        startExitAnimator();
    }
}
