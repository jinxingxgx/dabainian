package com.xgx.dabainian;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONReader;
import com.allen.library.SuperTextView;
import com.blankj.utilcode.util.ConvertUtils;
import com.blankj.utilcode.util.EncodeUtils;
import com.blankj.utilcode.util.EncryptUtils;
import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.ImageUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.hss01248.dialog.StyledDialog;
import com.hss01248.dialog.interfaces.MyDialogListener;
import com.wuhenzhizao.titlebar.widget.CommonTitleBar;

import org.greenrobot.greendao.query.QueryBuilder;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bingoogolapple.photopicker.activity.BGAPhotoPickerActivity;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.picLayout)
    LinearLayout picLayout;
    @BindView(R.id.settingLayout)
    LinearLayout settingLayout;
    @BindView(R.id.navigation)
    BottomNavigationView navigation;
    @BindView(R.id.container)
    ConstraintLayout container;
    @BindView(R.id.pwd_stv)
    SuperTextView pwdStv;
    @BindView(R.id.uppic_stv)
    SuperTextView uppicStv;
    @BindView(R.id.titlebar)
    CommonTitleBar titlebar;
    @BindView(R.id.show_pic_view)
    ImageView showPicView;
    @BindView(R.id.export_stv)
    SuperTextView exportStv;
    @BindView(R.id.updb_stv)
    SuperTextView updbStv;
    @BindView(R.id.json_path_tv)
    TextView jsonPathTv;
    private Handler mhandler;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    picLayout.setVisibility(View.VISIBLE);
                    settingLayout.setVisibility(View.GONE);
                    if (MyApplication.getDaoInstant().getPicInfoDao().count() > 0) {
                        titlebar.getRightTextView().setText("删除");
                        titlebar.getRightTextView().setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (isAdmin) {
                                    deletePic();
                                } else {
                                    //如果是普通模式，需要判断 当前几张照片
                                    if (MyApplication.getDaoInstant().getPicInfoDao().count() == 8) {
                                        deletePic();
                                    } else {
                                        ToastUtils.showShort("必须满8张图片，才可以删除");
                                    }
                                }
                            }
                        });
                    }

                    return true;
                case R.id.navigation_dashboard:
                    picLayout.setVisibility(View.GONE);
                    settingLayout.setVisibility(View.VISIBLE);
                    titlebar.getRightTextView().

                            setText("");
                    titlebar.getRightTextView().

                            setOnClickListener(null);
                    return true;
            }
            return false;
        }
    };
    private TimerTask task;
    private String adminPwd = "";
    private boolean isAdmin;

    @Override
    protected void onResume() {
        super.onResume();
        uppicStv.setRightString("共上传" + MyApplication.getDaoInstant().getPicInfoDao().count()
                + "张图片");
        //获取第一张图片
        PicInfoDao dao = MyApplication.getDaoInstant().getPicInfoDao();
        QueryBuilder<PicInfo> builder = dao.queryBuilder();
        builder.limit(1);
        List<PicInfo> picInfos = builder.list();
        if (picInfos != null && picInfos.size() > 0) {
            try {
                byte[] base64Pic = EncodeUtils.base64Decode(picInfos.get(0).getPic());
                Glide.with(MainActivity.this).load(base64Pic).into(showPicView);
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            Glide.with(MainActivity.this).load(ContextCompat.getDrawable(this, R.drawable.upload)).into(showPicView);

        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        picLayout.setVisibility(View.VISIBLE);
        mhandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                delayTimer.cancel();
                count = 0;
                super.handleMessage(msg);
            }
        };
        boolean isDebug = MyApplication.getInstance().getSP().getBoolean("isAppDebug", false);
        isAdmin = MyApplication.getInstance().getSP().getBoolean("isAdmin", false);
        if (isDebug) {
            pwdStv.setVisibility(View.VISIBLE);
        } else {
            pwdStv.setVisibility(View.GONE);

        }
        if (isAdmin) {
            pwdStv.setSwitchIsChecked(true);
            exportStv.setVisibility(View.VISIBLE);
        } else {
            pwdStv.setSwitchIsChecked(false);
            exportStv.setVisibility(View.GONE);

        }
        if (MyApplication.getDaoInstant().getPicInfoDao().count() > 0) {
            titlebar.getRightTextView().setText("删除");
            titlebar.getRightTextView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isAdmin) {
                        deletePic();
                    } else {
                        //如果是普通模式，需要判断 当前几张照片
                        if (MyApplication.getDaoInstant().getPicInfoDao().count() == 8) {
                            deletePic();
                        } else {
                            ToastUtils.showShort("必须满8张图片，才可以删除");
                        }
                    }
                }
            });
        }
        pwdStv.setSwitchCheckedChangeListener(new SuperTextView.OnSwitchCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    //如果要开启，则进入管理员模式，可上传文件
                    StyledDialog.buildMdInput("请输入密码", "", "填写管理员密码",
                            "确定", "取消", new MyDialogListener() {
                                @Override
                                public void onFirst() {
                                    ToastUtils.showShort(adminPwd);
                                    if (EncryptUtils.encryptMD5ToString(adminPwd).equals(EncryptUtils.encryptMD5ToString("admin111111"))) {
                                        ToastUtils.showShort("密码输入成功，开启管理员模式");
                                        isAdmin = true;
                                        exportStv.setVisibility(View.VISIBLE);

                                        MyApplication.getInstance().getSP().edit().putBoolean("isAdmin", true).apply();
                                    } else {
                                        exportStv.setVisibility(View.GONE);
                                        ToastUtils.showShort("密码校验失败");
                                        pwdStv.setSwitchIsChecked(false);
                                    }
                                }

                                /**
                                 * 提供给Input的回调
                                 *
                                 * @param input1
                                 * @param input2
                                 */
                                public void onGetInput(CharSequence input1, CharSequence input2) {
                                    adminPwd = input2.toString();
                                }

                                @Override
                                public void onSecond() {
                                    pwdStv.setSwitchIsChecked(false);
                                }
                            }).setInput2HideAsPassword(true).show();
                }
            }
        });
    }

    private static final int PRC_PHOTO_PICKER = 1;

    private static final int RC_CHOOSE_PHOTO = 1;
    private static final int RC_PHOTO_PREVIEW = 2;

    @AfterPermissionGranted(PRC_PHOTO_PICKER)
    private void choicePhotoWrapper() {
        String[] perms = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA};
        if (EasyPermissions.hasPermissions(this, perms)) {
            // 拍照后照片的存放目录，改成你自己拍照后要存放照片的目录。如果不传递该参数的话就没有拍照功能
            Intent photoPickerIntent = new BGAPhotoPickerActivity.IntentBuilder(this)
                    .maxChooseCount(1) // 图片选择张数的最大值
                    .selectedPhotos(null) // 当前已选中的图片路径集合
                    .pauseOnScroll(false) // 滚动列表时是否暂停加载图片

                    .build();
            startActivityForResult(photoPickerIntent, RC_CHOOSE_PHOTO);
        } else {
            EasyPermissions.requestPermissions(this, "图片选择需要以下权限:\n\n1.访问设备上的照片\n\n2.拍照", PRC_PHOTO_PICKER, perms);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == RC_CHOOSE_PHOTO) {
            final List<String> selectedPhotos = BGAPhotoPickerActivity.getSelectedPhotos(data);
            if (selectedPhotos != null && selectedPhotos.size() > 0) {

                startActivity(new Intent(MainActivity.this, UploadActivity.class).putExtra("path", selectedPhotos.get(0)));
            }
        }
        if (requestCode == 1112 && null != data) {
            Uri uri = data.getData();
            try {
                String filepath = MyFileEntity.getPath(this, uri);
                List<PicInfo> picInfos = JSON.parseArray(Utils.getJson(filepath, this), PicInfo.class);
                MyApplication.getDaoInstant().getPicInfoDao().deleteAll();
                MyApplication.getDaoInstant().getPicInfoDao().insertInTx(picInfos);

                ToastUtils.showShort("数据导入成功");
                onResume();
            } catch (Exception e) {
                ToastUtils.showShort("数据格式错误");
            }

        }
    }

    private long firstTime = 0;
    private Timer delayTimer;
    private long interval = 500;
    private int count = 0;
    private static final String SDCARD_ROOT = Environment.getExternalStorageDirectory().toString();
    //查询

    @OnClick({R.id.updb_stv, R.id.uppic_stv, R.id.titlebar, R.id.export_stv})
    public void onViewClicked(View view) {
        if (Utils.checkIsDemo()) {
            ToastUtils.showShort("你已超出试用期");
            return;
        }
        switch (view.getId()) {
            case R.id.export_stv:
                long size = MyApplication.getDaoInstant().getPicInfoDao().count();
                List<PicInfo> infos = new ArrayList<>();
                for (int i = 0; i < size; i++) {
                    PicInfoDao dao = MyApplication.getDaoInstant().getPicInfoDao();
                    QueryBuilder<PicInfo> builder = dao.queryBuilder();
                    builder.limit(1);
                    builder.offset(i);
                    List<PicInfo> picInfos = builder.list();
                    infos.addAll(picInfos);
                }
                File dir = new File(SDCARD_ROOT + File.separator + "dabainian" + File.separator);
                if (!dir.exists()) {
                    dir.mkdir();
                }
                String path = dir.toString() + File.separator + "dabainian.json";
                Utils.writeStringToFile(JSON.toJSONString(infos), dir.toString() + File.separator + "dabainian.json");
                jsonPathTv.setText(path);
                ToastUtils.showShort("导出成功");
                break;
            case R.id.updb_stv:
                String[] perms = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA};
                if (EasyPermissions.hasPermissions(this, perms)) {
                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                    intent.setType("*/*");
                    intent.addCategory(Intent.CATEGORY_OPENABLE);
                    try {
                        startActivityForResult(Intent.createChooser(intent, "请选择一个要上传的文件"), 1112);
                    } catch (ActivityNotFoundException ex) {
                        Toast.makeText(MainActivity.this, "请安装文件管理器", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    EasyPermissions.requestPermissions(this, "文件选择需要以下权限:\n\n1.访问设备上的文件夹\n\n2.写文件", PRC_PHOTO_PICKER, perms);

                }


                break;
            case R.id.uppic_stv:
                if (MyApplication.getDaoInstant().getPicInfoDao().count() > 7) {
                    //已经显示8张图片
                    ToastUtils.showShort("只能上传8张图片");
                    return;
                }
                if (isAdmin) {
                    choicePhotoWrapper();
                } else {
                    //如果是普通用户 ，只能默认显示第一张
                    //必须要删除第一张才能添加
                    if (MyApplication.getDaoInstant().getPicInfoDao().count() == 7) {
                        choicePhotoWrapper();

                    } else {
                        ToastUtils.showLong("图片必须满8张，请联系管理员，上传识别数据文件");
                    }
                }
                break;
            case R.id.titlebar:
                long secondTime = System.currentTimeMillis();
                // 判断每次点击的事件间隔是否符合连击的有效范围
                // 不符合时，有可能是连击的开始，否则就仅仅是单击
                if (secondTime - firstTime <= interval) {
                    ++count;
                    boolean isDebug = MyApplication.getInstance().getSP().getBoolean("isAppDebug", false);
                    String s = "";
                    if (isDebug) {
                        s = "管理员";
                    } else {
                        s = "普通";
                    }
                    if (count == 3) {

                        ToastUtils.showShort("连续点击10次开启" + s + "模式");
                    }
                    if (count == 7) {
                        ToastUtils.showShort("连续点击3次开启" + s + "模式");

                    } else if (count == 8) {
                        ToastUtils.showShort("连续点击2次开启" + s + "模式");
                    } else if (count == 9) {
                        ToastUtils.showShort("连续点击1次开启" + s + "模式");
                    } else if (count == 10) {
                        MyApplication.getInstance().getSP().edit().putBoolean("isAppDebug", !isDebug).apply();
                        if (!isDebug) {
                            pwdStv.setVisibility(View.VISIBLE);
                            ToastUtils.showShort("你已开启了" + s + "模式");

                        } else {
                            pwdStv.setVisibility(View.GONE);
                            ToastUtils.showShort("你已开启了" + s + "模式");
                        }
                    } else if (count > 10) {
                        ToastUtils.showShort("手酸不酸？不要再点击啦~");

                    }
                } else {
                    count = 1;
                }
                // 延迟，用于判断用户的点击操作是否结束
                delay();
                firstTime = secondTime;
                break;
        }
    }

    // 延迟时间是连击的时间间隔有效范围
    private void delay() {
        if (task != null) {
            task.cancel();
        }

        task = new TimerTask() {
            @Override
            public void run() {
                Message message = new Message();
                // message.what = 0;
                mhandler.sendMessage(message);
            }
        };
        delayTimer = new Timer();
        delayTimer.schedule(task, interval);
    }

    private void deletePic() {
        StyledDialog.buildMdInput("请输入密码", "", "填写密码删除",
                "确定", "取消", new MyDialogListener() {
                    private String pwd = "";

                    @Override
                    public void onFirst() {
                        //删除第一张
                        PicInfoDao dao = MyApplication.getDaoInstant().getPicInfoDao();
                        QueryBuilder<PicInfo> builder = dao.queryBuilder();
                        builder.limit(1);
                        List<PicInfo> picInfos = builder.list();
                        if (picInfos != null && picInfos.size() > 0) {
                            if (EncryptUtils.encryptMD5ToString(pwd).equals(picInfos.get(0).getPwd())) {
                                MyApplication.getDaoInstant().getPicInfoDao().deleteByKey(picInfos.get(0).getId());
                                onResume();
                                ToastUtils.showShort("删除成功");
                            } else {
                                ToastUtils.showShort("密码错误");

                            }
                        }
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
                    }
                }).setInput2HideAsPassword(true).show();
    }

}