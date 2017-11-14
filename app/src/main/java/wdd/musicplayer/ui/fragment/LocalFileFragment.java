package wdd.musicplayer.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import wdd.musicplayer.R;

/**
 * 通过文件夹获取列表页面
 */

public class LocalFileFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragemnt_file_local, container, false);
        ButterKnife.bind(this, view);

        //初始化相关信息
//        init();

        return view;

    }
}
