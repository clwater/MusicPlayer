package wdd.musicplayer.ui.fragment;

import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import butterknife.ButterKnife;
import wdd.musicplayer.R;
import wdd.musicplayer.db.DataBaseManager;
import wdd.musicplayer.model.FileModel;
import wdd.musicplayer.model.Music;
import wdd.musicplayer.utils.SharedPreferencesUtils;

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
        init();

        return view;

    }

    private void init() {
        //初始化数据库相关
        initDB();
    }

    private void initDB() {
        if (SharedPreferencesUtils.getFirst(getContext())){
            String MusicPath = Environment.getExternalStorageDirectory() + "/Music/";
            FileModel fileModel = new FileModel("Music" , MusicPath);
            DataBaseManager.getInstance(getContext()).insert(fileModel);
            SharedPreferencesUtils.setFirst(getContext());
        }

        List<FileModel> list = DataBaseManager.getInstance(getContext()).queryAll(FileModel.class);
        Log.d("wdd" , list.get(0).path);
    }
}
