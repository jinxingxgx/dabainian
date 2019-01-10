package com.xgx.dabainian;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.allen.library.SuperButton;
import com.allen.library.SuperTextView;
import com.blankj.utilcode.util.ConvertUtils;
import com.blankj.utilcode.util.EncodeUtils;
import com.blankj.utilcode.util.EncryptUtils;
import com.blankj.utilcode.util.ImageUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.hss01248.dialog.StyledDialog;
import com.hss01248.dialog.interfaces.MyDialogListener;
import com.wuhenzhizao.titlebar.widget.CommonTitleBar;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class UploadActivity extends AppCompatActivity {

    @BindView(R.id.show_pic_view)
    ImageView showPicView;
    @BindView(R.id.pwd_stv)
    SuperTextView pwdStv;
    @BindView(R.id.save_btn)
    SuperButton saveBtn;
    String path;
    @BindView(R.id.titlebar)
    CommonTitleBar titlebar;
    private String pwd = "";
    private byte[] base64Pic;
    private byte[] b;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);
        ButterKnife.bind(this);
        path = getIntent().getStringExtra("path");

        b = ConvertUtils.bitmap2Bytes(ImageUtils.compressBySampleSize(ImageUtils.getBitmap(new File(path)), 2), Bitmap.CompressFormat.JPEG);
        base64Pic = EncodeUtils.base64Decode(EncodeUtils.base64Encode2String(b));
        Glide.with(UploadActivity.this).load(base64Pic).into(showPicView);
        titlebar.getLeftTextView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @OnClick({R.id.pwd_stv, R.id.save_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.pwd_stv:
                StyledDialog.buildMdInput("请输入密码", "", "填写管理员密码",
                        "确定", "取消", new MyDialogListener() {

                            @Override
                            public void onFirst() {
                                pwdStv.setRightString(pwd);
                            }

                            /**
                             * 提供给Input的回调
                             * @param input1
                             * @param input2
                             */
                            public void onGetInput(CharSequence input1, CharSequence input2) {
                                pwd = input2.toString();
                            }

                            @Override
                            public void onSecond() {
                                pwdStv.setSwitchIsChecked(false);
                            }
                        }).setInput2HideAsPassword(true).show();
                break;
            case R.id.save_btn:
                if (StringUtils.isEmpty(pwd)) {
                    ToastUtils.showShort("请先设置密码");
                    return;
                }
                if (MyApplication.getDaoInstant().getPicInfoDao().count() > 7) {
                    //已经显示8张图片
                    ToastUtils.showShort("只能上传8张图片");
                    return;
                }
                PicInfo info = new PicInfo();
                info.setId(TimeUtils.getNowMills());
                info.setPic(EncodeUtils.base64Encode2String(base64Pic));
                info.setPwd(EncryptUtils.encryptMD5ToString(pwd));
                MyApplication.getDaoInstant().getPicInfoDao().insertInTx(info);
                ToastUtils.showShort("保存成功");
                finish();
                break;
        }
    }
}
