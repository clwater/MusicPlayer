package wdd.musicplayer.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import wdd.musicplayer.R;
import wdd.musicplayer.db.DataBaseManager;
import wdd.musicplayer.eventbus.EB_RenamePlayerListName;
import wdd.musicplayer.eventbus.EB_UpdataPlayerList;
import wdd.musicplayer.eventbus.EB_updataLoaclFileList;
import wdd.musicplayer.model.FileModel;
import wdd.musicplayer.model.ListModel;
import wdd.musicplayer.model.Music;
import wdd.musicplayer.ui.activity.LoaclFileShowActivity;
import wdd.musicplayer.utils.FileUtils;

/**
 * 播放列表adapter
 */

public class PlayerListAdapter extends RecyclerView.Adapter<PlayerListAdapter.AllFileAdapterHolder> {
    private final LayoutInflater layoutInflater;
    private final Context context;
    private  List<ListModel> list = new ArrayList<>();

    //构造函数 设置相关变量
    public PlayerListAdapter(Context context , List<ListModel> list) {
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
        this.list = list;
    }

    //初始化设置资源文件等信息
    @Override
    public AllFileAdapterHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new AllFileAdapterHolder(layoutInflater.inflate(R.layout.item_playerlist, parent, false));
    }

    //设置文本内容
    @Override
    public void onBindViewHolder(AllFileAdapterHolder holder, int position) {
        //获取当前对象
        ListModel listModel = list.get(position);
        //设置歌曲名
        holder.textview_playerlist_name.setText(listModel.name);
        String info = String.format("%s 首歌" , String.valueOf(listModel.number) );
        holder.textview_playerlist_info.setText(info);

    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    public  class AllFileAdapterHolder extends RecyclerView.ViewHolder {

        //初始化item相关组件
        @BindView(R.id.textview_playerlist_name)
        TextView textview_playerlist_name;
        @BindView(R.id.textview_playerlist_info)
        TextView textview_playerlist_info;
        @BindView(R.id.imagebutton_playerlist_action)
        ImageView imagebutton_playerlist_action;

        AllFileAdapterHolder(final View view) {
            super(view);
            ButterKnife.bind(this , view);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });

            imagebutton_playerlist_action.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showPopupMenu(imagebutton_playerlist_action , getAdapterPosition());

                }
            });
        }

    }


    private  void showPopupMenu(View view , final int postion) {
        // 这里的view代表popupMenu需要依附的view
        PopupMenu popupMenu = new PopupMenu(context, view);
        // 获取布局文件
        popupMenu.getMenuInflater().inflate(R.menu.playerlist_menu, popupMenu.getMenu());
        popupMenu.show();

        if(postion == 0){
            popupMenu.getMenu().findItem(R.id.playerlist_rename).setVisible(false);
            popupMenu.getMenu().findItem(R.id.playerlist_del).setVisible(false);
        }

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                ListModel listModel = list.get(postion);

                switch (item.getItemId()){
                    case R.id.playerlist_player:
                        break;
                    case R.id.playerlist_rename:
                        EventBus.getDefault().post(new EB_RenamePlayerListName(listModel));
                        break;
                    case R.id.playerlist_del:
                        DataBaseManager.getInstance(context).delete(listModel);
                        EventBus.getDefault().post(new EB_UpdataPlayerList());
                        break;

                }
                return false;
            }
        });
    }

}