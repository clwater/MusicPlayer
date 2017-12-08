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
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import wdd.musicplayer.R;
import wdd.musicplayer.db.DataBaseManager;
import wdd.musicplayer.eventbus.EB_UpdataList;
import wdd.musicplayer.model.FileModel;
import wdd.musicplayer.model.Music;
import wdd.musicplayer.ui.adapter.ChooseFileAdapter;
import wdd.musicplayer.utils.FileUtils;

/**
 * 本地文件夹选择的页面
 */

public class ChooseLoaclFileActivity extends AppCompatActivity {

    //页面顶部的文本
    @BindView(R.id.textview_toolbar_title)
    TextView textview_toolbar_title;
    //页面中的RecyclerView 用于展示文件的信息
    @BindView(R.id.recycler_choose)
    RecyclerView recycler_choose;

    private String path;    //路径
    private File file;      //文件
    private Context context;    //上下午

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
        //设置context
        context = this;
        //通过intent获取到path的信息
        Intent intent = getIntent();
        path = intent.getStringExtra("path");

        //构建文件
        file = new File(path);
        //判断当前时候为文件夹
        if (!file.exists()){
            Toast.makeText(this , path + " 文件夹不存在" , Toast.LENGTH_SHORT).show();
        }

        //设置系统通知栏颜色
        StatusBarCompat.setStatusBarColor(this , Color.parseColor("#3C5F78"));

        //设置当前路径为顶部标题内容
        if (path.equals(Environment.getExternalStorageDirectory().getPath())) {
            textview_toolbar_title.setText("外部储存空间");
        }else {
            textview_toolbar_title.setText(file.getName());
        }

        //设置recyclerview的布局
        recycler_choose.setLayoutManager(new LinearLayoutManager(this));
        //设置recyclerview的的Adapter
        //改Adapter通过文件夹的信息进行构建
        recycler_choose.setAdapter(new ChooseFileAdapter(this , FileUtils.getFilderInfo(path)));

    }

    /**
     * 页面中的确定按钮响应时间
     */
    @OnClick(R.id.imagebutton_choose_select)
    public void OnCkick_imagebutton_choose_select(){
        //通过数据库查询当前文件夹是否被添加
        List<FileModel> checkFile = DataBaseManager.getInstance(context).queryByWhere(FileModel.class ,
                "path" , new String[]{file.getPath()});

        if (checkFile.size() > 0){
            Toast.makeText(this , "当前文件夹已经被添加,请选择其他文件夹重试" , Toast.LENGTH_SHORT).show();
        }else {
            //如果当前文件夹没有被添加
            //返回主页面
            startActivity(new Intent(this , MainActivity.class));
            Log.d("wdd" , "file.getPath(): " + file.getPath());
            //获取某个路径下的音频文件
            FileUtils.findMusicInFile(context , file.getPath() , new FileUtils.FindMusicInFileBack() {
                /**
                 * @param musicList
                 * 等待查询完成 接收回调
                 */
                @Override
                public void onCompleted(List<Music> musicList) {
                    //构建FileModel
                    FileModel fileModel = new FileModel(file.getName() , file.getPath() , musicList.size());
                    //将此FileModel添加到数据库中
                    DataBaseManager.getInstance(context).insert(fileModel);
                    //通知页面更新文件夹信息
                    EventBus.getDefault().post(new EB_UpdataList(EB_UpdataList.UPDATALOACLFILELIST));
                }
            });
        }
    }
}
