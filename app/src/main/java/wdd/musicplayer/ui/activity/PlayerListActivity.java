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
    TextView textview_toolbar_title;
    @BindView(R.id.recycler_playerlist)
    RecyclerView recycler_playerlist;
    @BindView(R.id.textview_playerlist_bottom)
    TextView textview_playerlist_bottom;

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
        Intent intent = getIntent();
        listModel = (ListModel) intent.getSerializableExtra("ListModel");
        textview_toolbar_title.setText(listModel.name);

        recycler_playerlist.setLayoutManager(new LinearLayoutManager(this));
        updataList();
    }

    public void updataList(){
        List<ListItemModel> listItemModels = DataBaseManager.getInstance(this).queryByWhere(ListItemModel.class,
                "parent" , new String[]{listModel.name});
        PlayerListShowAdapter playerListShowAdapter = new PlayerListShowAdapter(this , listItemModels , listModel);
        recycler_playerlist.setAdapter(playerListShowAdapter);
        textview_playerlist_bottom.setText(String.format("总用有 %s 首歌" , listItemModels.size()));
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void EB_EB_Updatlist(EB_UpdataList e){
        if (e.list.equals(EB_UpdataList.UPDATAPLAYERLISTSHOW)){
            updataList();
        }
    }

}
