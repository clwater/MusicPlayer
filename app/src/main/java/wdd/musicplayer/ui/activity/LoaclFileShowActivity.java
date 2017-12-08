package wdd.musicplayer.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.githang.statusbar.StatusBarCompat;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import wdd.musicplayer.R;
import wdd.musicplayer.model.Music;
import wdd.musicplayer.ui.adapter.FileShowAdapter;
import wdd.musicplayer.utils.FileUtils;
import wdd.musicplayer.utils.MediaUtils;

/**
 * 已经添加的文件夹展示
 */

public class LoaclFileShowActivity extends AppCompatActivity {

    @BindView(R.id.textview_toolbar_title)
    TextView textview_toolbar_title;        //页面顶部标题信息
    @BindView(R.id.recycler_loaclfle)
    RecyclerView recycler_loaclfle;         //页面中列表信息

    private String path;
    private File file;

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
        //通过intent获取path信息
        path = getIntent().getStringExtra("path");
        //通过path构建file信息
        file = new File(path);

        //设置系统通知栏颜色
        StatusBarCompat.setStatusBarColor(this , Color.parseColor("#3C5F78"));
        //设置标题信息
        textview_toolbar_title.setText(file.getName());
        //设置recyclerview
        recycler_loaclfle.setLayoutManager(new LinearLayoutManager(this));
        //构建FileShowAdapter
        FileShowAdapter fileShowAdapter = new FileShowAdapter(this , getMusicList(file) ,LoaclFileShowActivity.this);
        //设置构建FileShowAdapter
        recycler_loaclfle.setAdapter(fileShowAdapter);

    }

    /**
     * @param file
     * @return
     * 获取当前文件夹下音频文件信息
     */
    private List<Music> getMusicList(File file) {
        List<Music> musicList = new ArrayList<>();
        //通过数据库查询所有音频信息
        List<Music> allList = MediaUtils.DBInfo(this);
        //获取当前文件下所有文件
        File[] files = file.listFiles();
        //遍历所有文件
        for (File checkFile : files){
            //监测当前文件是否为音频文件
            if (FileUtils.checkIsMusic(checkFile.getName())){
                //检测当前文件是否在系统数据库中 如果在则添加到Music列表中
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
