package wdd.musicplayer.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
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

    @BindView(R.id.recycler_fileall_list)
    RecyclerView recycler_fileall_list;

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

        recycler_fileall_list.setLayoutManager(new LinearLayoutManager(getContext()));
        recycler_fileall_list.setAdapter(new AllFileAdapter(getContext() , DBMusicList));
    }
}
