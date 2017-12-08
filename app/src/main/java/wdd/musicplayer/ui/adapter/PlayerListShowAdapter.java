package wdd.musicplayer.ui.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import wdd.musicplayer.R;
import wdd.musicplayer.db.DataBaseManager;
import wdd.musicplayer.eventbus.EB_PlayerList;
import wdd.musicplayer.eventbus.EB_UpdataList;
import wdd.musicplayer.model.ListItemModel;
import wdd.musicplayer.model.ListModel;
import wdd.musicplayer.model.Music;
import wdd.musicplayer.utils.TimeUtils;

/**
 * 播放列表详情展示adapter
 */

public class PlayerListShowAdapter extends RecyclerView.Adapter<PlayerListShowAdapter.AllFileAdapterHolder> {
    private final LayoutInflater layoutInflater;
    private final Context context;
    private List<ListItemModel> list = new ArrayList<>();
    private ListModel listModel;

    //构造函数 设置相关变量
    public PlayerListShowAdapter(Context context , List<ListItemModel> list , ListModel listModel) {
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
        this.list = list;
        this.listModel = listModel;
    }

    //初始化设置资源文件等信息
    @Override
    public AllFileAdapterHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new AllFileAdapterHolder(layoutInflater.inflate(R.layout.item_playerlisthow, parent, false));
    }

    //设置文本内容
    @Override
    public void onBindViewHolder(AllFileAdapterHolder holder, int position) {
        //获取当前对象
        ListItemModel listItemModel = list.get(position);
        //设置音频的名称
        holder.textview_playerlistshow_name.setText(listItemModel.name);
        //设置音频的info文本信息
        String info = String.format("%s | %s" , TimeUtils.tranTime(listItemModel.time) , listItemModel.artist);
        holder.textview_playerlistshow_info.setText(info);
        //设置当前的序号
        holder.textview_playerlistshow_index.setText(String.valueOf(position + 1));
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    public class AllFileAdapterHolder extends RecyclerView.ViewHolder {

        //初始化item相关组件
        @BindView(R.id.textview_playerlistshow_name)
        TextView textview_playerlistshow_name;
        @BindView(R.id.textview_playerlistshow_info)
        TextView textview_playerlistshow_info;
        @BindView(R.id.imagebutton_playerlistshow_action)
        ImageView imagebutton_playerlistshow_action;
        @BindView(R.id.textview_playerlistshow_index)
        TextView textview_playerlistshow_index;

        AllFileAdapterHolder(View view) {
            super(view);
            ButterKnife.bind(this , view);
            //添加item菜单被点击的响应
            imagebutton_playerlistshow_action.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showPopupMenu(imagebutton_playerlistshow_action , getAdapterPosition());
                }
            });
        }
    }

    /**
     * @param view
     * @param postion
     * 展示弹出菜单栏的内容
     */
    private  void showPopupMenu(final View view , final int postion) {
        // 这里的view代表popupMenu需要依附的view
        PopupMenu popupMenu = new PopupMenu(context, view);
        // 获取布局文件
        popupMenu.getMenuInflater().inflate(R.menu.playerlistshow_menu, popupMenu.getMenu());
        popupMenu.show();

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                //根据id判断是否为删除改音频的按钮
                if (item.getItemId() == R.id.playerlistshow_del){
                    deleteItemFromPlayerlist(postion);
                    EventBus.getDefault().post(new EB_PlayerList(EB_PlayerList.FAVORITE));
                }

                return false;
            }
        });
    }

    /**
     * @param postion
     * 删除此音频
     */
    private void deleteItemFromPlayerlist(int postion) {
        //获取当前对象
        ListItemModel listItemModel = list.get(postion);
        //从数据库中删除此条内容
        DataBaseManager.getInstance(context).delete(listItemModel);
        //更新此音频所在的播放列表的信息
        listModel.number = listModel.number - 1;
        DataBaseManager.getInstance(context).update(listModel);
        //通过EventBus更新播放列表的视图内容
        EventBus.getDefault().post(new EB_UpdataList(EB_UpdataList.UPDATAPLAYERLISTSHOW));
        EventBus.getDefault().post(new EB_UpdataList(EB_UpdataList.UPDATAPLAYERLIST));
    }

}