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
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.githang.statusbar.StatusBarCompat;
import com.litesuits.orm.db.utils.FieldUtil;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindInt;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import wdd.musicplayer.R;
import wdd.musicplayer.db.DataBaseManager;
import wdd.musicplayer.eventbus.EB_updataLoaclFileList;
import wdd.musicplayer.model.FileModel;
import wdd.musicplayer.model.Music;
import wdd.musicplayer.ui.adapter.ChooseFileAdapter;
import wdd.musicplayer.utils.FileUtils;

import static wdd.musicplayer.utils.FileUtils.floderChild;
import static wdd.musicplayer.utils.FileUtils.getFilderInfo;

/**
 * Created by gengzhibo on 2017/11/16.
 */

public class ChooseLoaclFileActivity extends AppCompatActivity {

    @BindView(R.id.textview_toolbar_title)
    TextView textview_toolbar_title;
    @BindView(R.id.recycler_choose)
    RecyclerView recycler_choose;

    private String path;
    private File file;
    private Context context;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chooseloaclfile);
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
        if (!file.exists()){
            Toast.makeText(this , path + " 文件夹不存在" , Toast.LENGTH_SHORT).show();
        }

        //设置系统通知栏颜色
        StatusBarCompat.setStatusBarColor(this , Color.parseColor("#3C5F78"));

        if (path.equals(Environment.getExternalStorageDirectory().getPath())) {
            textview_toolbar_title.setText("外部储存空间");
        }else {
            textview_toolbar_title.setText(file.getName());
        }

        recycler_choose.setLayoutManager(new LinearLayoutManager(this));
        recycler_choose.setAdapter(new ChooseFileAdapter(this , FileUtils.getFilderInfo(path)));

    }

    @OnClick(R.id.imagebutton_choose_select)
    public void OnCkick_imagebutton_choose_select(){


        List<FileModel> checkFile = DataBaseManager.getInstance(context).queryByWhere(FileModel.class ,
                "path" , new String[]{file.getPath()});

        if (checkFile.size() > 0){
            Toast.makeText(this , "当前文件夹已经被添加,请选择其他文件夹重试" , Toast.LENGTH_SHORT).show();
        }else {
            startActivity(new Intent(this , MainActivity.class));
            Log.d("wdd" , "file.getPath(): " + file.getPath());
            FileUtils.findMusicInFile(context , file.getPath() , new FileUtils.FindMusicInFileBack() {
                @Override
                public void onCompleted(List<Music> musicList) {
                    FileModel fileModel = new FileModel(file.getName() , file.getPath() , musicList.size());
                        DataBaseManager.getInstance(context).insert(fileModel);
                        EventBus.getDefault().post(new EB_updataLoaclFileList());
                }
            });
        }





    }


}
