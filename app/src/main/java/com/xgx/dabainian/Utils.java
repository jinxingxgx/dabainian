package com.xgx.dabainian;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import com.blankj.utilcode.util.EncodeUtils;
import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.TimeUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by xgx on 2018/12/13 for MusicPlayDemo
 */
public class Utils {

    private static final boolean isDemo = false;

    /**
     * 通过传入的图片地址，获取图片
     */
    public static Bitmap getBitmap(String url) {
        Bitmap bitmap = null;
        try {
            HttpURLConnection connection = (HttpURLConnection) (new URL(url)).openConnection();
            InputStream is = connection.getInputStream();
            bitmap = BitmapFactory.decodeStream(is);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    public static List<FileBean> getKeyFiles(Context context, String keyword) {
        List<FileBean> list = new ArrayList<>();
        File[] files = new File(Environment.getExternalStorageState()).listFiles();
        for (int i = 0; i < files.length; i++) {
            if (FileUtils.getFileName(files[i]).equals(keyword)) {
                FileBean bean = new FileBean();
                bean.setName(FileUtils.getFileName(files[i]));
                bean.setPath(files[i].getPath());
                list.add(bean);
            }
        }
        return list;
    }


    public static List<FileBean> queryFiles(Context context, String keyword) {
        String[] projection = new String[]{MediaStore.Files.FileColumns._ID,
                MediaStore.Files.FileColumns.DATA,
                MediaStore.Files.FileColumns.SIZE
        };

        Cursor cursor = context.getContentResolver().query(
                Uri.parse("content://media/external/file"),
                projection,
                MediaStore.Files.FileColumns.DATA + " like ?",
                new String[]{"%json"},
                MediaStore.Files.FileColumns.DATE_MODIFIED + " DESC");

        List<FileBean> list = new ArrayList<>();
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                int dataindex = cursor.getColumnIndex(MediaStore.Files.FileColumns.DATA);
                do {
                    String path = cursor.getString(dataindex);
                    int dot = path.lastIndexOf("/");
                    String name = path.substring(dot + 1);
                    if (name.lastIndexOf(".") > 0)
                        name = name.substring(0, name.lastIndexOf("."));
                    if (!name.startsWith(".") && name.equals(keyword)) {
                        FileBean books = new FileBean();
                        books.setName(name);
                        books.setPath(path);
                        list.add(books);
                    }
                } while (cursor.moveToNext());
            }
        }
        cursor.close();
        return list;
    }

    //base64字符串转化成图片
    public static boolean GenerateImage(Context context, String imgStr, long id) {//对字节数组字符串进行Base64解码并生成图片
        if (imgStr == null) //图像数据为空
            return false;
        try {
            //Base64解码
            byte[] b = EncodeUtils.base64Decode(imgStr);
            for (int i = 0; i < b.length; ++i) {
                if (b[i] < 0) {//调整异常数据
                    b[i] += 256;
                }
            }
//生成jpeg图片
            File dir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "大拜年" + File.separator);
            if (!dir.exists()) {
                dir.mkdir();
            }
            String imgFilePath = dir.getPath() + File.separator + "pic_" + id + ".jpg";
            if (!new File(imgFilePath).exists()) {

                OutputStream out = new FileOutputStream(imgFilePath);
                out.write(b);
                out.flush();
                out.close();
                MediaStore.Images.Media.insertImage(context.getContentResolver(), imgFilePath, "pic_" + id, "大拜年图片");
                context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(new File(imgFilePath))));
            }

            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean checkIsDemo() {
        if (isDemo) {
            return true;
        }
        if (TimeUtils.getNowDate().getTime() / 1000 - 1547542727 < 0) {
            return false;
        } else {
            return true;
        }
    }

    public static String getJson(String fileName, Context context) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            InputStream input = new FileInputStream(new File(fileName));
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(input));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return stringBuilder.toString();
    }

    //字符串、json 写入文件
    public static void writeStringToFile(String json, String filePath) {
        File txt = new File(filePath);
        if (!txt.exists()) {
            try {
                txt.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        byte[] bytes = json.getBytes(); //新加的
        int b = json.length(); //改
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(txt);
            fos.write(bytes);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
