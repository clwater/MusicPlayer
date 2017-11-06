package wdd.musicplayer.ui.activity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import wdd.musicplayer.R;
import wdd.musicplayer.ui.adapter.FragmentAdapter;
import wdd.musicplayer.ui.fragment.FileFragment;
import wdd.musicplayer.ui.fragment.PlayerFragment;
import wdd.musicplayer.ui.fragment.PlayerListFragment;
import wdd.musicplayer.ui.fragment.SettingFragment;

/**
 * 应用主页面
 */
public class MainActivity extends AppCompatActivity {

    //定义四个Fragment相关变量
    FileFragment fileFragment;
    PlayerFragment playerFragment;
    PlayerListFragment playerListFragment;
    SettingFragment settingFragment;

    //定义主页面菜单Textview
    @BindView(R.id.textview_main_playerlist)
    TextView textview_main_playerlist;
    @BindView(R.id.textview_main_player)
    TextView textview_main_player;
    @BindView(R.id.textview_main_file)
    TextView textview_main_file;
    @BindView(R.id.textview_main_setting)
    TextView textview_main_setting;
    //标题内容
    @BindView(R.id.textview_main_title)
    TextView textview_main_title;


    @BindView(R.id.viewpager_main)
    ViewPager viewpager_main;

    //当前展示的页面
    int index;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //ButterKnife注册
        ButterKnife.bind(this);

        //执行初始化操作
        init();


    }

    private void init() {
        initFragment();
    }

    private void initFragment() {
        List<Fragment> list = new ArrayList<Fragment>();
        list.add(new PlayerListFragment());
        list.add(new PlayerFragment());
        list.add(new FileFragment());
        list.add(new SettingFragment());

        viewpager_main.setAdapter(new FragmentAdapter(getSupportFragmentManager(), list));

    }


    //封装一下更改页面的方法 自动更换标题内容
    void changeFragment(){
        viewpager_main.setCurrentItem(index);
        switch (index){
            case 0:
                textview_main_title.setText("播放列表");
                break;
            case 1:
                textview_main_title.setText("音乐");
                break;
            case 2:
                textview_main_title.setText("本地音乐");
                break;
            case 3:
                textview_main_title.setText("设置");
                break;
            default:
                break;
        }
    }

    @OnClick(R.id.textview_main_playerlist)
    public void onClick_textview_main_playerlist(){
        index = 0;
        changeFragment();
    }

    @OnClick(R.id.textview_main_player)
    public void onClick_textview_main_player(){
        index = 1;
        changeFragment();
    }

    @OnClick(R.id.textview_main_file)
    public void onClick_textview_main_file(){
        index = 2;
        changeFragment();
    }

    @OnClick(R.id.textview_main_setting)
    public void onClick_textview_main_setting(){
        index = 3;
        changeFragment();
    }



}
