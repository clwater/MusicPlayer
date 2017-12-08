package wdd.musicplayer.ui.fragment;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import wdd.musicplayer.R;
import wdd.musicplayer.model.Music;
import wdd.musicplayer.utils.MediaUtils;

/**
 * 本地音乐页面
 */

public class FileFragment extends Fragment {

    //获取所有歌曲的tag
    @BindView(R.id.textview_file_alltag)
    TextView textview_file_alltag;
    //获取本地文件夹 的tag
    @BindView(R.id.textview_file_localtag)
    TextView textview_file_localtag;

    //判断当前是所有歌曲还是文件夹标签
    private boolean isAllTag = true;

    private AllFileFragment allFileFragment;
    private LocalFileFragment localFileFragment;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragemnt_file, container, false);
        ButterKnife.bind(this, view);

        //初始化相关信息
        init();

        return view;

    }

    private void init() {
        initFragment();
        //更改对应标签的样式
        changeTag();
    }

    //初始化下部列表列表Fragment信息
    private void initFragment() {
        //获取Fragment管理器
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        //初始化allFileFragment及localFileFragment
        allFileFragment = new AllFileFragment();
        localFileFragment = new LocalFileFragment();
        //将这两个fragment添加到本页面中
        transaction.add(R.id.fragment_file_all , allFileFragment , allFileFragment.getTag());
        transaction.add(R.id.fragment_file_local , localFileFragment , localFileFragment.getTag());
        //默认展示所有歌曲的页面 隐藏本地文件夹
        transaction.hide(localFileFragment);

        transaction.commit();


    }

    //点击所有歌曲的标签
    @OnClick(R.id.textview_file_alltag)
    public void OnClick_text_file_alltag(){
        isAllTag = true;
        changeTag();
    }

    //点击文件夹的标签
    @OnClick(R.id.textview_file_localtag)
    public void OnClick_textview_file_localtag(){
        isAllTag = false;
        changeTag();
    }

    //更改对应标签的样式
    private void changeTag() {
        //获取Fragment管理
        FragmentTransaction transaction = getFragmentManager().beginTransaction();

        //根据isAllTag设置页面信息
        if (!isAllTag){
            //设置顶部tag样式
            textview_file_alltag.setBackgroundResource(R.drawable.selector_file_all);
            textview_file_alltag.setTextColor(getResources().getColor(R.color.baseTextColor));
            textview_file_localtag.setBackgroundResource(R.drawable.selector_file_local_slect);
            textview_file_localtag.setTextColor(getResources().getColor(R.color.tagTextClolor));
            //设置下部列表页面
            transaction.show(localFileFragment);
            transaction.hide(allFileFragment);
        }else {
            textview_file_alltag.setBackgroundResource(R.drawable.selector_file_all_slect);
            textview_file_alltag.setTextColor(getResources().getColor(R.color.tagTextClolor));
            textview_file_localtag.setBackgroundResource(R.drawable.selector_file_local);
            textview_file_localtag.setTextColor(getResources().getColor(R.color.baseTextColor));
            transaction.show(allFileFragment);
            transaction.hide(localFileFragment);
        }
        //提交更改
        transaction.commit();
    }
}
