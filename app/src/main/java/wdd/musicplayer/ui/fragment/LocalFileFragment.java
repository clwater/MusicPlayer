package wdd.musicplayer.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import wdd.musicplayer.R;
import wdd.musicplayer.db.DataBaseManager;
import wdd.musicplayer.eventbus.EB_UpdataList;
import wdd.musicplayer.model.FileModel;
import wdd.musicplayer.model.Music;
import wdd.musicplayer.ui.activity.ChooseLoaclFileActivity;
import wdd.musicplayer.ui.adapter.LoaclFileAdapter;
import wdd.musicplayer.utils.FileUtils;
import wdd.musicplayer.utils.SharedPreferencesUtils;

/**
 * 通过文件夹获取列表页面
 */

public class LocalFileFragment extends Fragment {

    private List<FileModel> fileList = new ArrayList<>();
    private LoaclFileAdapter localFileAdapter;

    @BindView(R.id.recycler_fileall_list)
    RecyclerView recycler_fileall_list;
    @BindView(R.id.textview_filelocal_bottom)
    TextView textview_filelocal_bottom;

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(getContext());
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(LocalFileFragment.this);

    }

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
//        initDB();
        //初始化列表信息
        initList();
    }


    private void initList() {
        //获取数据库中本地目录存储信息
        fileList = DataBaseManager.getInstance(getContext()).queryAll(FileModel.class);
        recycler_fileall_list.setLayoutManager(new LinearLayoutManager(getContext()));
        localFileAdapter = new LoaclFileAdapter(getContext() , fileList);
        recycler_fileall_list.setAdapter(localFileAdapter);

        textview_filelocal_bottom.setText(String.format("总共有 " + fileList.size() + " 个文件夹"));
        //        for (int i = 0 ; i < fileList.size() ;i++) {
//            final FileModel fileModel = fileList.get(i);
//            FileUtils.findMusicInFile(getContext(), fileModel.path, new FileUtils.FindMusicInFileBack() {
//                @Override
//                public void onCompleted(List<Music> musicList) {
//                    Log.d("wdd", fileModel.path + "have " + musicList.size() + "songs");
//                }
//            });
//        }
    }

    private void initDB() {
        if (SharedPreferencesUtils.getFirst(getContext())){


            String MusicPath = Environment.getExternalStorageDirectory() + "/Music/";
            final File file = new File(MusicPath);
            FileUtils.findMusicInFile(getContext() , file.getPath() , new FileUtils.FindMusicInFileBack() {
                @Override
                public void onCompleted(List<Music> musicList) {
                    FileModel fileModel = new FileModel(file.getName() , file.getPath() , musicList.size());
                    DataBaseManager.getInstance(getContext()).insert(fileModel);
                    SharedPreferencesUtils.setFirst(getContext());
                }
            });

        }
    }

    @OnClick(R.id.textview_filelocal_choose)
    public void OnClick_textview_filelocal_choose(){
        Intent intent = new Intent(getContext() , ChooseLoaclFileActivity.class);
        intent.putExtra("path" , Environment.getExternalStorageDirectory().getPath());
//        intent.putExtra("path" , Environment.getExternalStorageDirectory());
        startActivity(intent);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnEvent_EB_updataLoaclFileList(EB_UpdataList e){
        if (e.list.equals(EB_UpdataList.UPDATALOACLFILELIST)) {
            fileList = DataBaseManager.getInstance(getContext()).queryAll(FileModel.class);
            localFileAdapter = new LoaclFileAdapter(getContext(), fileList);
            recycler_fileall_list.setAdapter(localFileAdapter);
            textview_filelocal_bottom.setText(String.format("总共有 " + fileList.size() + " 个文件夹"));
        }

    }


}
