package wdd.musicplayer.ui.fragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindInt;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import wdd.musicplayer.R;
import wdd.musicplayer.db.DataBaseManager;
import wdd.musicplayer.eventbus.EB_RenamePlayerListName;
import wdd.musicplayer.eventbus.EB_UpdataList;
import wdd.musicplayer.model.ListItemModel;
import wdd.musicplayer.model.ListModel;
import wdd.musicplayer.ui.adapter.PlayerListAdapter;

/**
 * 播放列表页面
 */

public class PlayerListFragment extends Fragment {

    @BindView(R.id.recycler_playerlist)
    RecyclerView recycler_playerlist;


    List<ListModel> listModels = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragemnt_playlist, container, false);
        ButterKnife.bind(this, view);
        //初始化相关内容
        init();

        return view;

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(PlayerListFragment.this);

    }

    //初始化相关
    private void init() {
        //从数据库获取listmodel对象
        listModels = DataBaseManager.getInstance(getContext()).queryAll(ListModel.class);
        initList();
    }

    private void initList() {
        //设置recycler_playerlist页面布局
        recycler_playerlist.setLayoutManager(new LinearLayoutManager(getContext()));
        //通过listModels构建PlayerListAdapter
        PlayerListAdapter playerListAdapter = new PlayerListAdapter(getContext() , listModels);
        //设置adapter为PlayerListAdapter
        recycler_playerlist.setAdapter(playerListAdapter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(PlayerListFragment.this);
    }

    @OnClick(R.id.textvie_playerlist_choose)
    public void OnClick_textvie_playerlist_choose() {
        AlertDialog dialog = createNewListDialog();
        //更改dialog大小
        changeDialogSize(dialog);

    }

    //创建一个新增播放列表的dialog
    private AlertDialog createNewListDialog() {
        //创建dialog对话框内容
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext() , R.style.MusicPlayer_Dialog);
        //设置dialog标题
        builder.setTitle("创建播放列表");
        //创建页面布局
        LinearLayout linearLayout = (LinearLayout) getLayoutInflater().inflate(R.layout.dialog_add_playerlist, null);
        //设置dialog布局
        builder.setView(linearLayout);
        //设置dialog可以在点击非dailog区域取消
        builder.setCancelable(true);
        //获取布局中输入框对象
        final EditText edittext_playerlist  =linearLayout.findViewById(R.id.edittext_playerlist);
        //设置dialog确认按钮文本内容及响应事件
        builder.setPositiveButton("创建", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //获取输入框中的文本内容
                String name = edittext_playerlist.getText().toString();
                //如果内容为空,提示用户输入正确内容
                if (name.isEmpty()){
                    Toast.makeText(getActivity() , "请输入列表名称" , Toast.LENGTH_SHORT).show();
                }else {
                    //如果内容不为空 创建该列表
                    createNewList(name);
                }
            }
        });
        //设置dialog取消按钮
        builder.setNegativeButton("取消", null);
        //创建dialog对象
        AlertDialog dialog = builder.create();
        //展示dialog
        dialog.show();
        return dialog;
    }

    /**
     * @param e
     * 通过EventBuds响应弹出重命名的功能
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void EB_EB_RenamePlayerListName(EB_RenamePlayerListName e){
        //构建重命名dialog
        AlertDialog dialog = updataNewListDialog(e.listModel);
        changeDialogSize(dialog);
    }

    /**
     * @param listModel
     * @return
     * 构建重命名dialog
     */
    private AlertDialog updataNewListDialog(final ListModel listModel) {
        //这里和createNewListDialog(line:161)基本一致
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext() , R.style.MusicPlayer_Dialog);
        builder.setTitle("编辑播放列表");
        LinearLayout linearLayout = (LinearLayout) getLayoutInflater().inflate(R.layout.dialog_add_playerlist, null);
        builder.setView(linearLayout);
        builder.setCancelable(true);

        final EditText edittext_playerlist  =linearLayout.findViewById(R.id.edittext_playerlist);
        edittext_playerlist.setText(listModel.name);
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String name = edittext_playerlist.getText().toString();

                if (name.isEmpty()){
                    Toast.makeText(getActivity() , "请输入列表名称" , Toast.LENGTH_SHORT).show();
                }else {
                    //如果内容不为空 更新该列表名称
                    listModel.name = name;
                    updateNewList(listModel);
                }
            }
        });
        builder.setNegativeButton("取消", null);

        AlertDialog dialog = builder.create();
        dialog.show();
        return dialog;
    }

    /**
     * @param listModel
     * 更新列表名称
     */
    private void updateNewList(ListModel listModel) {
        //更新数据中该列表的名称
        DataBaseManager.getInstance(getContext()).update(listModel);
        updataList();
    }

    /**
     * @param name
     * 创建该列表
     */
    private void createNewList(String name) {
        //检测当前名称是否被使用
        boolean check = checkName(name);
        if (!check) {
            //如果没有被使用
            //创建listModel对象
            ListModel listModel = new ListModel(name, 0);
            //将listModel对象插入到数据库中
            DataBaseManager.getInstance(getContext()).insert(listModel);
            //更新列表页面
            updataList();
        }else {
            Toast.makeText(getContext() , "该名称已存在" , Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 更新列表页面
     */
    private void updataList() {
        //从数据库中传获取最新的listModels对象内容
        listModels = DataBaseManager.getInstance(getContext()).queryAll(ListModel.class);
        //构建PlayerListAdapter并设置为recycler_playerlist的adapter以更新页面内容
        PlayerListAdapter playerListAdapter = new PlayerListAdapter(getContext() , listModels);
        recycler_playerlist.setAdapter(playerListAdapter);
    }

    /**
     * @param name
     * @return
     * 检测当前列表名称是否被使用(通过查询数据库完成)
     */
    private boolean checkName(String name) {
        for (int i = 0 ; i < listModels.size() ; i++){
            ListModel listModel = listModels.get(i);
            if (listModel.name.equals(name)){
                return true;
            }
        }
        return false;
    }

    /**
     * @param dialog
     */
    private void changeDialogSize(AlertDialog dialog) {
        //获取当前窗口对象
        Window window = dialog.getWindow();
        //获取当前窗口的属性
        WindowManager.LayoutParams lp = window.getAttributes();
        //设置属性为居中
        lp.gravity = Gravity.CENTER;
        //获取当前窗口的宽度
        WindowManager wm = getActivity().getWindowManager();
        //调整将要设置的dialog的窗口的宽度的数据
        int width = wm.getDefaultDisplay().getWidth();
        width = width /4 * 3;
        //更改窗口的宽度为width
        lp.width = width;
        //更改窗口的高度为自适应
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        //设置dialog的属性
        dialog.getWindow().setAttributes(lp);
    }


    /**
     * @param e
     * 通过EventBus接收更新当前页面的请求
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void EB_EB_UpdataPlayerList(EB_UpdataList e){
        if (e.list.equals(EB_UpdataList.UPDATAPLAYERLIST)) {
            //更新listModel表的信息内容
            updataListInfo();
            //更新列表页面
            updataList();
        }
    }

    /**
     * 更新listModel表的信息内容
     * (因为listModel表之简单存储了该表名称及包含的音频数量,没有相信的音频内容信息
     * 当表中音频信息更改后 需要调用此方法来更新listModel表内容)
     */
    private void updataListInfo() {
        //从数据库中查询listModels对象情况
        listModels = DataBaseManager.getInstance(getContext()).queryAll(ListModel.class);
        for (int i = 0 ; i < listModels.size() ; i++ ){
            //遍历该listModels对象 查询在该
            ListModel listModel = listModels.get(i);
            //通过ListItemModel数据库中parent列构建ListItemModel列表对象
            List<ListItemModel> listItemModels = DataBaseManager.getInstance(getContext()).queryByWhere(ListItemModel.class,
                    "parent" , new String[]{listModel.name});
            //获取该条件下ListItemModel的数量
            listModel.number = listItemModels.size();
            //更新该listModels的数据内容
            DataBaseManager.getInstance(getContext()).update(listModel);
        }
    }

}
