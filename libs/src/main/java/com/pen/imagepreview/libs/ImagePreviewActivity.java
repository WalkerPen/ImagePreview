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
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.pen.imagepreview.libs.circleindicator.CircleIndicator;

import java.io.Serializable;
import java.util.ArrayList;

public class ImagePreviewActivity extends AppCompatActivity{


    private ViewPager mViewPager;
    private CircleIndicator mIndicator;
    private int mIndex;
    private ExitAlphaJumpBean mExitAlphaJumpBean;
    private ExitScaleJumpBean mExitScaleJumpBean;
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
    private boolean mExitScale;//true表示退出时缩放效果，false表示退出时透明度渐变
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
        if(mExitScale) {
            if(mHigher) {
                animatorShield = ValueAnimator.ofInt((mMinHeight - mExitScaleJumpBean.images.get(mIndex).height)/2, 0);
            }else {
                animatorShield = ValueAnimator.ofInt((mMinWidth - mExitScaleJumpBean.images.get(mIndex).width)/2, 0);
            }
        }else {
            if(mHigher) {
                animatorShield = ValueAnimator.ofInt((mMinHeight - mExitAlphaJumpBean.height)/2, 0);
            }else {
                animatorShield = ValueAnimator.ofInt((mMinWidth - mExitAlphaJumpBean.width)/2, 0);
            }
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
        if(mExitScale) {
            if(mHigher) {
                animatorShield = ValueAnimator.ofInt(0, (mMinHeight - mExitScaleJumpBean.images.get(mIndex).height)/2);
            }else {
                animatorShield = ValueAnimator.ofInt(0, (mMinWidth - mExitScaleJumpBean.images.get(mIndex).width)/2);
            }
        }else {
            if(mHigher) {
                animatorShield = ValueAnimator.ofInt(0, (mMinHeight - mExitAlphaJumpBean.height)/2);
            }else {
                animatorShield = ValueAnimator.ofInt(0, (mMinWidth - mExitAlphaJumpBean.width)/2);
            }
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
        Serializable serializable = getIntent().getSerializableExtra("data");
        if(serializable instanceof ExitAlphaJumpBean) {
            mExitAlphaJumpBean = (ExitAlphaJumpBean) serializable;
            mExitScale = false;
            mIndex = mExitAlphaJumpBean.index;
        }else if(serializable instanceof ExitScaleJumpBean) {
            mExitScaleJumpBean = (ExitScaleJumpBean) serializable;
            mIndex = mExitScaleJumpBean.index;
            mExitScale = true;
        }
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
        mViewPager.setCurrentItem(mIndex);
        mViewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                mIndex = position;
                upDataScale();
            }
        });
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

                Object iamge = null;
                if(mExitScale) {
                    iamge = mExitScaleJumpBean.images.get(mIndex).image;
                }else {
                    iamge = mExitAlphaJumpBean.images.get(mIndex);
                }
                Glide.with(ImagePreviewActivity.this)
                        .load(iamge)
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

    private void upDataScale() {
        Object iamge = null;
        if(mExitScale) {
            iamge = mExitScaleJumpBean.images.get(mIndex).image;
        }else {
            iamge = mExitAlphaJumpBean.images.get(mIndex);
        }
        Glide.with(ImagePreviewActivity.this)
                .load(iamge)
                .asBitmap()
                .into(new SimpleTarget<Bitmap>(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL) {

                    @Override
                    public void onResourceReady(Bitmap bitmap, GlideAnimation glideAnimation) {
                        // Do something with bitmap here.
                        int height = bitmap.getHeight();
                        int width = bitmap.getWidth();

                        //初始化最小的尺寸及对应起点坐标
                        initMinSize(height, width);
                    }

                });
    }

    /**
     * 初始化最小的尺寸及对应起点坐标
     * @param height
     * @param width
     */
    private void initMinSize(int height, int width) {
        if(mExitScale) {
            if ((height * 1f / width) > (mExitScaleJumpBean.images.get(mIndex).height * 1f / mExitScaleJumpBean.images.get(mIndex).width)) {
                mHigher = true;
                mMinHeight = height * mExitScaleJumpBean.images.get(mIndex).width / width;
                mMinWidth = mMinHeight * mExitScaleJumpBean.images.get(mIndex).width / mExitScaleJumpBean.images.get(mIndex).height;
            } else {
                mMinWidth = width * mExitScaleJumpBean.images.get(mIndex).height / height;
                mMinHeight = mMinWidth * mExitScaleJumpBean.images.get(mIndex).height / mExitScaleJumpBean.images.get(mIndex).width;
                mHigher = false;
            }
            mMinX = mExitScaleJumpBean.images.get(mIndex).x - (mMinWidth - mExitScaleJumpBean.images.get(mIndex).width) / 2;
            mMinY = mExitScaleJumpBean.images.get(mIndex).y - (mMinHeight - mExitScaleJumpBean.images.get(mIndex).height) / 2;
        }else {
            if ((height * 1f / width) > (mExitAlphaJumpBean.height * 1f / mExitAlphaJumpBean.width)) {
                mHigher = true;
                mMinHeight = height * mExitAlphaJumpBean.width / width;
                mMinWidth = mMinHeight * mExitAlphaJumpBean.width / mExitAlphaJumpBean.height;
            } else {
                mMinWidth = width * mExitAlphaJumpBean.height / height;
                mMinHeight = mMinWidth * mExitAlphaJumpBean.height / mExitAlphaJumpBean.width;
                mHigher = false;
            }
            mMinX = mExitAlphaJumpBean.x - (mMinWidth - mExitAlphaJumpBean.width) / 2;
            mMinY = mExitAlphaJumpBean.y - (mMinHeight - mExitAlphaJumpBean.height) / 2;
        }
    }

    private class MyPagerAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            return mExitScale ? mExitScaleJumpBean.images.size() : mExitAlphaJumpBean.images.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView imageView = new ImageView(container.getContext());

            imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
            if(mExitScale) {
                Glide.with(ImagePreviewActivity.this)
                        .load(mExitScaleJumpBean.images.get(position).image)
                        .into(imageView);
            }else {
                Glide.with(ImagePreviewActivity.this)
                        .load(mExitAlphaJumpBean.images.get(position))
                        .into(imageView);
            }
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
