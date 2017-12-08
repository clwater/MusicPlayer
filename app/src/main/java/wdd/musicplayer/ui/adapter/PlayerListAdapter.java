package wdd.musicplayer.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import wdd.musicplayer.R;
import wdd.musicplayer.db.DataBaseManager;
import wdd.musicplayer.eventbus.EB_PlayerList;
import wdd.musicplayer.eventbus.EB_PlayerMusic;
import wdd.musicplayer.eventbus.EB_RenamePlayerListName;
import wdd.musicplayer.eventbus.EB_UpdataList;
import wdd.musicplayer.model.ListModel;
import wdd.musicplayer.ui.activity.PlayerListActivity;

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
        //设置info信息
        String info = String.format("%s 首歌" , String.valueOf(listModel.number) );
        holder.textview_playerlist_info.setText(info);


        //设置播放列表的图标
        if (listModel.name.equals("收藏")) {
            holder.textview_playerlist.setBackgroundResource(R.drawable.ic_favorite_yes);
        }else {
            String name = listModel.name;
            name = name.substring(0 , 1);
            holder.textview_playerlist.setText(name);
            holder.textview_playerlist.setBackgroundColor(Color.parseColor("#3C5F78"));

        }

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
        @BindView(R.id.textview_playerlist)
        TextView textview_playerlist;

        /**
         * @param view
         */
        AllFileAdapterHolder(final View view) {
            super(view);
            ButterKnife.bind(this , view);

            //item点击响应事件
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //构建intent并传入listmodel数据 跳转至PlayerListActivity页面
                    Intent intent = new Intent(context , PlayerListActivity.class);
                    intent.putExtra("ListModel" , list.get(getAdapterPosition()));
                    context.startActivity(intent);
                }
            });
            //弹出菜单栏的点击事件
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
        //判断当前播放列表是否为"收藏"列表
        //如果是首个(收藏列表)即将重命名和隐藏菜单隐蔽
        if(postion == 0){
            popupMenu.getMenu().findItem(R.id.playerlist_rename).setVisible(false);
            popupMenu.getMenu().findItem(R.id.playerlist_del).setVisible(false);
        }
        //弹出的菜单栏中菜单内容点击响应
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                //获取当前listmodel
                ListModel listModel = list.get(postion);
                //根据id判断当前点击的菜单项是哪个
                switch (item.getItemId()){
                    //播放本歌单
                    case R.id.playerlist_player:
                        //通过EventBus发送播放本播放列表的请求
                        EventBus.getDefault().post(new EB_PlayerMusic(EB_PlayerMusic.LIST , listModel.name));
                        break;
                    //重命名本歌单
                    case R.id.playerlist_rename:
                        //通过EventBus发送重命名本播放列表的请求
                        EventBus.getDefault().post(new EB_RenamePlayerListName(listModel));
                        break;
                    //删除本歌单
                    case R.id.playerlist_del:
                        //从数据库中删除本歌单
                        DataBaseManager.getInstance(context).delete(listModel);
                        //通过EventBus发送更新播放列表的请求
                        EventBus.getDefault().post(new EB_UpdataList(EB_UpdataList.UPDATAPLAYERLIST));
                        break;

                }
                return false;
            }
        });
    }

}