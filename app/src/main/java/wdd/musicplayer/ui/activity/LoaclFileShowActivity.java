package wdd.musicplayer.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.githang.statusbar.StatusBarCompat;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import wdd.musicplayer.R;
import wdd.musicplayer.db.DataBaseManager;
import wdd.musicplayer.eventbus.EB_updataLoaclFileList;
import wdd.musicplayer.model.FileModel;
import wdd.musicplayer.model.Music;
import wdd.musicplayer.ui.adapter.ChooseFileAdapter;
import wdd.musicplayer.ui.adapter.FileShowAdapter;
import wdd.musicplayer.utils.FileUtils;
import wdd.musicplayer.utils.MediaUtils;

/**
 * 已经添加的文件夹展示
 */

public class LoaclFileShowActivity extends AppCompatActivity {

    @BindView(R.id.textview_toolbar_title)
    TextView textview_toolbar_title;
    @BindView(R.id.recycler_loaclfle)
    RecyclerView recycler_loaclfle;

    private String path;
    private File file;
    private Context context;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loaclfileshow);
        //ButterKnife注册
        ButterKnife.bind(this);


        //执行初始化操作
        init();
    }

    private void init() {
        context = this;
        Intent intent = getIntent();
        path = intent.getStringExtra("path");

        file = new File(path);

        //设置系统通知栏颜色
        StatusBarCompat.setStatusBarColor(this , Color.parseColor("#3C5F78"));

        textview_toolbar_title.setText(file.getName());

        recycler_loaclfle.setLayoutManager(new LinearLayoutManager(this));
        FileShowAdapter fileShowAdapter = new FileShowAdapter(this , getMusicList(file));
        recycler_loaclfle.setAdapter(fileShowAdapter);

    }

    private List<Music> getMusicList(File file) {
        List<Music> musicList = new ArrayList<>();
        List<Music> allList = MediaUtils.DBInfo(this);
        File[] files = file.listFiles();
        for (File checkFile : files){
            if (FileUtils.checkIsMusic(checkFile.getName())){
                for (int i =0 ; i < allList.size() ; i++) {
                    Music baseMusic = allList.get(i);
                    if (checkFile.getPath().equals(baseMusic.path)){
                        musicList.add(baseMusic);
                    }
                }
            }
        }
        return musicList;
    }


}
