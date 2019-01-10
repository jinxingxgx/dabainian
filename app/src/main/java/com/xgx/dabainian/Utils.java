package com.xgx.dabainian;

import android.content.Context;

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

/**
 * Created by xgx on 2018/12/13 for MusicPlayDemo
 */
public class Utils {
    private static final boolean isDemo = false;

    public static boolean checkIsDemo() {
        if (isDemo) {
            return true;
        }
        if (TimeUtils.getNowDate().getTime() / 1000 - 1547370231 < 0) {
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
