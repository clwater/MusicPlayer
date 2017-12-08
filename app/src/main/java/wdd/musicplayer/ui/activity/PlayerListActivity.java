package wdd.musicplayer.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import wdd.musicplayer.R;
import wdd.musicplayer.db.DataBaseManager;
import wdd.musicplayer.eventbus.EB_UpdataList;
import wdd.musicplayer.model.ListItemModel;
import wdd.musicplayer.model.ListModel;
import wdd.musicplayer.ui.adapter.PlayerListAdapter;
import wdd.musicplayer.ui.adapter.PlayerListShowAdapter;

/**
 * 播放列表中详情
 */

public class PlayerListActivity extends AppCompatActivity{

    @BindView(R.id.textview_toolbar_title)
    TextView textview_toolbar_title;        //页面顶部标题信息
    @BindView(R.id.recycler_playerlist)
    RecyclerView recycler_playerlist;       //页面中的list
    @BindView(R.id.textview_playerlist_bottom)
    TextView textview_playerlist_bottom;    //页面底部的文本信息

    private ListModel listModel;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playerlist);
        //ButterKnife注册
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        //执行初始化操作
        init();
    }

    private void init() {
        //通过intent获取ListModel信息
        listModel = (ListModel) getIntent().getSerializableExtra("ListModel");
        //设置页面顶面标题
        textview_toolbar_title.setText(listModel.name);
        //设置list页面布局
        recycler_playerlist.setLayoutManager(new LinearLayoutManager(this));
        //更新列表信息
        updataList();
    }

    public void updataList(){
        //从数据库中获取ListItemModel的信息
        List<ListItemModel> listItemModels = DataBaseManager.getInstance(this).queryByWhere(ListItemModel.class,
                "parent" , new String[]{listModel.name});
        //构建PlayerListShowAdapter
        PlayerListShowAdapter playerListShowAdapter = new PlayerListShowAdapter(this , listItemModels , listModel);
        //设置recycler_playerlist的adapter为playerListShowAdapter
        recycler_playerlist.setAdapter(playerListShowAdapter);
        //设置页面底部的文本信息
        textview_playerlist_bottom.setText(String.format("总用有 %s 首歌" , listItemModels.size()));
    }

    /**
     * @param e
     * 通过EventBus接收更新页面的请求
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void EB_EB_Updatlist(EB_UpdataList e){
        if (e.list.equals(EB_UpdataList.UPDATAPLAYERLISTSHOW)){
            updataList();
        }
    }

}
