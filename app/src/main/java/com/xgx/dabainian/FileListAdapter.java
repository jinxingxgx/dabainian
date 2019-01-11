package com.xgx.dabainian;

import android.annotation.SuppressLint;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.MenuItem;

import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lucasurbas.listitemview.ListItemView;

import java.io.File;
import java.util.List;

/**
 * Created by xgx on 2018/12/13 for MusicPlayDemo
 */
public class FileListAdapter extends BaseQuickAdapter<FileBean, BaseViewHolder> {
    public FileListAdapter(@Nullable List<FileBean> data) {
        super(R.layout.adapter_file_item, data);
    }

    @SuppressLint("RestrictedApi")
    @Override
    protected void convert(final BaseViewHolder helper, final FileBean item) {
        ListItemView listItemView = helper.getView(R.id.list_item_view);
        listItemView.setTitle(item.getName());
        listItemView.getAvatarView().setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.file));
        listItemView.setSubtitle("" + FileUtils.getFileSize(item.getPath())
                + "\n" + item.getPath()
                + "\n" + TimeUtils.millis2String(FileUtils.getFileLastModified(item.getPath())));
    }
}
