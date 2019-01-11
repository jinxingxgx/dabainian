package com.xgx.dabainian;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.ess.filepicker.SelectOptions;
import com.ess.filepicker.activity.FileTypeListFragment;
import com.ess.filepicker.loader.EssMimeTypeCollection;
import com.ess.filepicker.model.EssFile;
import com.ess.filepicker.util.DialogUtil;
import com.ess.filepicker.util.FileUtils;
import com.hss01248.dialog.StyledDialog;
import com.wuhenzhizao.titlebar.widget.CommonTitleBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SelectFileActivity extends AppCompatActivity implements EssMimeTypeCollection.EssMimeTypeCallbacks {

    @BindView(R.id.titlebar)
    CommonTitleBar titlebar;
    @BindView(R.id.list)
    RecyclerView list;
    private FileListAdapter mAdapter;

    private static final String ARG_FileType = "ARG_FileType";
    private static final String ARG_IsSingle = "mIsSingle";
    private static final String ARG_MaxCount = "mMaxCount";
    private static final String ARG_SortType = "mSortType";
    private static final String ARG_Loader_Id = "ARG_Loader_Id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_file);
        ButterKnife.bind(this);
        List<FileBean> files = new ArrayList<>();
        files = Utils.queryFiles(this, "大拜年");
        LinearLayoutManager manager = new LinearLayoutManager(this);
        list.setLayoutManager(manager);
        mAdapter = new FileListAdapter(files);
        View notDataView = getLayoutInflater().inflate(R.layout.empty_layout, (ViewGroup) list.getParent(), false);
        mAdapter.setEmptyView(notDataView);

        list.setAdapter(mAdapter);
        list.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent();
                intent.putExtra("path", mAdapter.getItem(position).getPath());
                setResult(1001, intent);
                finish();
            }
        });
        titlebar.getLeftTextView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        StyledDialog.buildLoading();
        StyledDialog.dismissLoading(this);
    }

    @Override
    public void onFileLoad(List<EssFile> essFileList) {

    }

    @Override
    public void onFileReset() {

    }
}
