package wdd.musicplayer.ui.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RadioButton;
import android.widget.TextView;

import com.githang.statusbar.StatusBarCompat;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
import wdd.musicplayer.R;
import wdd.musicplayer.db.DataBaseManager;
import wdd.musicplayer.model.ListModel;
import wdd.musicplayer.ui.adapter.FragmentAdapter;
import wdd.musicplayer.ui.fragment.FileFragment;
import wdd.musicplayer.ui.fragment.PlayerFragment;
import wdd.musicplayer.ui.fragment.PlayerListFragment;
import wdd.musicplayer.ui.fragment.SettingFragment;
import wdd.musicplayer.utils.SharedPreferencesUtils;

/**
 * 应用主页面
 */
public class MainActivity extends AppCompatActivity {

    //定义主页面菜单Textview
    @BindViews({R.id.radiobutton_main_playerlist, R.id.radiobutton_main_player, R.id.radiobutton_main_file, R.id.radiobutton_main_setting})
    List<RadioButton> radioButtons;

    //标题内容
    @BindView(R.id.textview_main_title)
    TextView textview_main_title;

    //ViewPager初始化
    @BindView(R.id.viewpager_main)
    ViewPager viewpager_main;

    //当前展示的页面
    //默认为1(音乐页面)
    int index = 1;

    String[] titles = {"播放列表" , "音乐" , "本地音乐" , "设置"};


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
        //初始化ViewPager信息 将Fragment页面添加其中
        initFragment();

        //设置系统通知栏颜色
        StatusBarCompat.setStatusBarColor(this , Color.parseColor("#3C5F78"));

        initPlayerList();
    }

    private void initPlayerList() {
        if (SharedPreferencesUtils.getFirst(this)){
            ListModel listModel = new ListModel("收藏" , 0);
            DataBaseManager.getInstance(this).insert(listModel);
            SharedPreferencesUtils.setFirst(this);
        }
    }

    private void initFragment() {
        //初始化Fragment列表
        List<Fragment> list = new ArrayList<Fragment>();
        list.add(new PlayerListFragment());
        list.add(new PlayerFragment());
        list.add(new FileFragment());
        list.add(new SettingFragment());

        //设置ViewPager的适配器
        viewpager_main.setAdapter(new FragmentAdapter(getSupportFragmentManager(), list));
        //设置ViewPager的滑动监听
        viewpager_main.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            //监听通过滑动切换页面后的页面
            @Override
            public void onPageSelected(int position) {
                index = position;
                changeFragment();
            }

            @Override
            public void onPageScrollStateChanged(int state) {}
        });
        changeFragment();
    }


    //封装一下更改页面的方法 自动更换标题内容
    void changeFragment(){
        //手动更改ViewPager的页面选择
        viewpager_main.setCurrentItem(index);
        //设置标题栏中标签的展示情况
        radioButtons.get(index).setChecked(true);
        //根据当前页面情况来设置标题信息
        textview_main_title.setText(titles[index]);
    }


    //首页菜单栏四个标题的点击事件
    @OnClick(R.id.radiobutton_main_playerlist)
    public void onClick_radiobutton_main_playerlist(){
        index = 0;
        changeFragment();
    }

    @OnClick(R.id.radiobutton_main_player)
    public void onClick_radiobutton_main_player(){
        index = 1;
        changeFragment();
    }

    @OnClick(R.id.radiobutton_main_file)
    public void onClick_radiobutton_main_file(){
        index = 2;
        changeFragment();
    }

    @OnClick(R.id.radiobutton_main_setting)
    public void onClick_radiobutton_main_setting(){
        index = 3;
        changeFragment();
    }


}
