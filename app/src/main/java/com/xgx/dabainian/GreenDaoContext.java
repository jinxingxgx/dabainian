package com.xgx.dabainian;

import android.content.Context;
import android.content.ContextWrapper;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.PathUtils;

import java.io.File;

/**
 * Created by xgx on 2019/1/10 for dabainian
 */
public class GreenDaoContext extends ContextWrapper {
    public GreenDaoContext(Context base) {
        super(base);
    }

    private static final String SDCARD_ROOT = Environment.getExternalStorageDirectory().toString();

    @Override
    public File getDatabasePath(String name) {
        File dir = new File(SDCARD_ROOT + File.separator + "dabainian" + File.separator);
        if (!dir.exists()) {
            dir.mkdir();
        }
        String path = dir + File.separator + name;
        LogUtils.e(path);
        return new File(path);
    }

    @Override
    public SQLiteDatabase openOrCreateDatabase(String name, int mode, SQLiteDatabase.CursorFactory factory) {
        return super.openOrCreateDatabase(name, mode, factory);
    }

    @Override
    public SQLiteDatabase openOrCreateDatabase(String name, int mode, SQLiteDatabase.CursorFactory factory, DatabaseErrorHandler errorHandler) {
        return super.openOrCreateDatabase(name, mode, factory, errorHandler);
    }
}
