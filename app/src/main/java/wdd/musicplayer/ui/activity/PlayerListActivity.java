package wdd.musicplayer.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import wdd.musicplayer.R;
import wdd.musicplayer.db.DataBaseManager;
import wdd.musicplayer.model.ListItemModel;

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

    private List<ListItemModel> listItemModels = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playerlist);

        //ButterKnife注册
        ButterKnife.bind(this);
        //执行初始化操作
        init();
    }

    private void init() {
        Intent intent = getIntent();
        String title = intent.getStringExtra("name");
        textview_toolbar_title.setText(title);

        listItemModels = DataBaseManager.getInstance(this).queryByWhere(ListItemModel.class,
                            "parent" , new String[]{title});
        textview_playerlist_bottom.setText(String.format("总用有 %s 首歌" , listItemModels.size()));
    }

}
