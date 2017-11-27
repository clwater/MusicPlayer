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
        listModels = DataBaseManager.getInstance(getContext()).queryAll(ListModel.class);
        initList();
    }

    private void initList() {
        recycler_playerlist.setLayoutManager(new LinearLayoutManager(getContext()));
        PlayerListAdapter playerListAdapter = new PlayerListAdapter(getContext() , listModels);
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
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext() , R.style.MusicPlayer_Dialog);
        builder.setTitle("创建播放列表");
        LinearLayout linearLayout = (LinearLayout) getLayoutInflater().inflate(R.layout.dialog_add_playerlist, null);
        builder.setView(linearLayout);
        builder.setCancelable(true);

        final EditText edittext_playerlist  =linearLayout.findViewById(R.id.edittext_playerlist);

        builder.setPositiveButton("创建", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String name = edittext_playerlist.getText().toString();
                createNewList(name);
            }
        });
        builder.setNegativeButton("取消", null);

        AlertDialog dialog = builder.create();
        dialog.show();
        return dialog;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void EB_EB_RenamePlayerListName(EB_RenamePlayerListName e){
        AlertDialog dialog = updataNewListDialog(e.listModel);
        changeDialogSize(dialog);
    }

    private AlertDialog updataNewListDialog(final ListModel listModel) {
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
                listModel.name = name;
                updateNewList(listModel);
            }
        });
        builder.setNegativeButton("取消", null);

        AlertDialog dialog = builder.create();
        dialog.show();
        return dialog;
    }

    private void updateNewList(ListModel listModel) {
        DataBaseManager.getInstance(getContext()).update(listModel);
        updataList();
    }

    private void createNewList(String name) {
        boolean check = checkName(name);
        if (!check) {
            ListModel listModel = new ListModel(name, 0);
            DataBaseManager.getInstance(getContext()).insert(listModel);
            updataList();
        }else {
            Toast.makeText(getContext() , "该名称已存在" , Toast.LENGTH_SHORT).show();
        }
    }

    private void updataList() {
        listModels = DataBaseManager.getInstance(getContext()).queryAll(ListModel.class);
        PlayerListAdapter playerListAdapter = new PlayerListAdapter(getContext() , listModels);
        recycler_playerlist.setAdapter(playerListAdapter);
    }

    private boolean checkName(String name) {
        for (int i = 0 ; i < listModels.size() ; i++){
            ListModel listModel = listModels.get(i);
            if (listModel.name.equals(name)){
                return true;
            }
        }
        return false;
    }

    private void changeDialogSize(AlertDialog dialog) {
        Window window = dialog.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.gravity = Gravity.CENTER;


        WindowManager wm = getActivity().getWindowManager();
        int width = wm.getDefaultDisplay().getWidth();
        width = width /4 * 3;

        lp.width = width;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setAttributes(lp);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void EB_EB_UpdataPlayerList(EB_UpdataList e){
        if (e.list.equals(EB_UpdataList.UPDATAPLAYERLIST)) {
            updataList();
        }
    }

}
