package com.xgx.dabainian;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.CacheDiskUtils;
import com.blankj.utilcode.util.ImageUtils;
import com.bumptech.glide.Glide;
import com.litesuits.common.utils.BitmapUtil;


/**
 * Created by xgx on 2019/1/11 for dabainian
 */
public class SplashScreen {

    Activity mActivity;
    LayoutInflater mInflater;
    ImageView logo_iv;
    TextView header_tv;
    TextView footer_tv;
    TextView before_logo_tv;
    TextView after_logo_tv;
    String header_text = null;
    String footer_text = null;
    String before_logo_text = null;
    String after_logo_text = null;
    RelativeLayout splash_wrapper_rl;
    Bundle bundle = null;
    private View mView;
    private int splashBackgroundColor = 0;
    private int splashBackgroundResource = 0;
    private int mLogo = 0;
    private Class<?> TargetActivity = null;
    private int SPLASH_TIME_OUT = 2000; //The time before launch target Activity - by default 2 seconds

    public RelativeLayout getSplash_wrapper_rl() {
        return splash_wrapper_rl;
    }

    public void setSplash_wrapper_rl(RelativeLayout splash_wrapper_rl) {
        this.splash_wrapper_rl = splash_wrapper_rl;
    }

    public SplashScreen(Activity activity) {
        this.mActivity = activity;
        this.mInflater = LayoutInflater.from(activity);
        this.mView = mInflater.inflate(gr.net.maroulis.library.R.layout.splash, null);
        this.splash_wrapper_rl = (RelativeLayout) mView.findViewById(gr.net.maroulis.library.R.id.splash_wrapper_rl);

    }

    public SplashScreen withFullScreen() {
        mActivity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        return this;
    }

    public SplashScreen withTargetActivity(Class<?> tAct) {
        this.TargetActivity = tAct;
        return this;
    }

    public SplashScreen withSplashTimeOut(int timout) {
        this.SPLASH_TIME_OUT = timout;
        return this;
    }

    public SplashScreen withBundleExtras(Bundle bundle) {
        this.bundle = bundle;
        return this;
    }

    public SplashScreen withBackgroundColor(int color) {
        this.splashBackgroundColor = color;
        splash_wrapper_rl.setBackgroundColor(splashBackgroundColor);
        return this;
    }

    public SplashScreen withBackgroundResource(int resource) {
        this.splashBackgroundResource = resource;
        splash_wrapper_rl.setBackgroundResource(splashBackgroundResource);
        return this;
    }

    public SplashScreen withBackgroundDrawable(String url) {
        this.url = url;
        splash_wrapper_rl.setBackground(CacheDiskUtils.getInstance().getDrawable("welcome", ContextCompat.getDrawable(mActivity, R.drawable.welcome)));
        /**异步加载*/
        new FetchDataTask().execute(url);

        return this;
    }

    private String url;

    /**
     * 异步加载
     */
    @SuppressLint("StaticFieldLeak")
    public class FetchDataTask extends AsyncTask<String, Void, Bitmap> {

        //执行前调用
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        //执行后台任务
        @Override
        protected Bitmap doInBackground(String... strings) {
            Bitmap bitmap = null;
            try {
                bitmap = Utils.getBitmap(url);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return bitmap;
        }

        //任务完成时调用
        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            CacheDiskUtils.getInstance().put("welcome", ImageUtils.bitmap2Drawable(bitmap));

        }
    }

    public SplashScreen withLogo(int logo) {
        this.mLogo = logo;
        logo_iv = (ImageView) mView.findViewById(gr.net.maroulis.library.R.id.logo);
        logo_iv.setImageResource(mLogo);
        return this;
    }

    public SplashScreen withHeaderText(String text) {
        this.header_text = text;
        header_tv = (TextView) mView.findViewById(gr.net.maroulis.library.R.id.header_tv);
        header_tv.setText(text);
        return this;
    }

    public SplashScreen withFooterText(String text) {
        this.footer_text = text;
        footer_tv = (TextView) mView.findViewById(gr.net.maroulis.library.R.id.footer_tv);
        footer_tv.setText(text);
        return this;
    }

    public SplashScreen withBeforeLogoText(String text) {
        this.before_logo_text = text;
        before_logo_tv = (TextView) mView.findViewById(gr.net.maroulis.library.R.id.before_logo_tv);
        before_logo_tv.setText(text);
        return this;
    }

    public SplashScreen withAfterLogoText(String text) {
        this.after_logo_text = text;
        after_logo_tv = (TextView) mView.findViewById(gr.net.maroulis.library.R.id.after_logo_tv);
        after_logo_tv.setText(text);
        return this;
    }

    public ImageView getLogo() {
        return logo_iv;
    }

    public TextView getBeforeLogoTextView() {
        return before_logo_tv;
    }

    public TextView getAfterLogoTextView() {
        return after_logo_tv;
    }

    public TextView getHeaderTextView() {
        return header_tv;
    }

    public TextView getFooterTextView() {
        return footer_tv;
    }

    public View create() {
        setUpHandler();

        return mView;
    }


    private void setUpHandler() {
        if (TargetActivity != null) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent i = new Intent(mActivity, TargetActivity);
                    if (bundle != null) {
                        i.putExtras(bundle);
                    }
                    mActivity.startActivity(i);
                    // close splash
                    mActivity.finish();
                }
            }, SPLASH_TIME_OUT);
        }
    }


}
