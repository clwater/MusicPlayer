package wdd.musicplayer.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;
import android.widget.Toast;

import com.githang.statusbar.StatusBarCompat;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import wdd.musicplayer.R;
import wdd.musicplayer.model.FileModel;
import wdd.musicplayer.ui.adapter.ChooseFileAdapter;
import wdd.musicplayer.utils.FileUtils;

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
        Intent intent = getIntent();
        path = intent.getStringExtra("path");

        File file = new File(path);
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


}
