package wdd.musicplayer.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import wdd.musicplayer.R;
import wdd.musicplayer.model.Music;
import wdd.musicplayer.ui.adapter.AllFileAdapter;
import wdd.musicplayer.utils.MediaUtils;

/**
 * 通过系统数据库获取的列表信息
 */

public class AllFileFragment extends Fragment {
    //数据库中获取音频信息
    List<Music> DBMusicList = new ArrayList<Music>();

    //获取列表对象
    @BindView(R.id.recycler_fileall_list)
    RecyclerView recycler_fileall_list;
    //获取下部提示信息
    @BindView(R.id.textview_fileall_bottom)
    TextView textview_fileall_bottom;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragemnt_file_all, container, false);
        ButterKnife.bind(this, view);

        //初始化相关信息
        init();

        return view;

    }

    private void init() {
        initList();
    }

    //初始化列表
    private void initList() {
        //获取系统数据库中信息列表
        DBMusicList = MediaUtils.DBInfo(getActivity());

        //设置列表样式
        recycler_fileall_list.setLayoutManager(new LinearLayoutManager(getContext()));
        //创建adapter并设置为列表数据源
        recycler_fileall_list.setAdapter(new AllFileAdapter(getContext() , DBMusicList));

        textview_fileall_bottom.setText("总共有 " + DBMusicList.size() + " 首歌");
    }

}
