package wdd.musicplayer.ui.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import wdd.musicplayer.R;
import wdd.musicplayer.db.DataBaseManager;
import wdd.musicplayer.eventbus.EB_UpdataList;
import wdd.musicplayer.model.ListItemModel;
import wdd.musicplayer.model.ListModel;
import wdd.musicplayer.model.Music;
import wdd.musicplayer.utils.TimeUtils;

/**
 * Dialog播放列表adapter
 */

public class DialogPlayerListAdapter extends RecyclerView.Adapter<DialogPlayerListAdapter.AllFileAdapterHolder> {
    private final LayoutInflater layoutInflater;
    private final Context context;
    private List<ListModel> list = new ArrayList<>();
    private Music music;

    //构造函数 设置相关变量
    public DialogPlayerListAdapter(Context context , List<ListModel> list , Music music) {
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
        this.list = list;
        this.music = music;
    }

    //初始化设置资源文件等信息
    @Override
    public AllFileAdapterHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new AllFileAdapterHolder(layoutInflater.inflate(R.layout.item_dialog_playerlist, parent, false));
    }

    //设置文本内容
    @Override
    public void onBindViewHolder(AllFileAdapterHolder holder, int position) {
        //获取当前对象
        ListModel listModel = list.get(position);
        //设置播放列表名称
        holder.textview_dialog_playerlist_name.setText(listModel.name);
        //设置播放列表信息
        String info = String.format("%s 首歌" , String.valueOf(listModel.number) );
        holder.textview_dialog_playerlist_info.setText(info);

        //设置播放列表的图标
        //如果是收藏列表则使用默认的图像
        //如果不是则根据收藏列表的名称进行设置
        if (listModel.name.equals("收藏")) {
            holder.textview_dialog_playerlist.setBackgroundResource(R.drawable.ic_favorite_yes);
        }else {
            //获取播放列表名称
            String name = listModel.name;
            //获取播放列表名称的第一个字
            name = name.substring(0 , 1);
            //设置图标中的文本
            holder.textview_dialog_playerlist.setText(name);
            //设置图标的颜色
            holder.textview_dialog_playerlist.setBackgroundColor(Color.parseColor("#3C5F78"));
        }

    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    public  class AllFileAdapterHolder extends RecyclerView.ViewHolder {

        //初始化item相关组件
        @BindView(R.id.textview_dialog_playerlist_name)
        TextView textview_dialog_playerlist_name;
        @BindView(R.id.textview_dialog_playerlist_info)
        TextView textview_dialog_playerlist_info;
        @BindView(R.id.textview_dialog_playerlist)
        TextView textview_dialog_playerlist;

        AllFileAdapterHolder(final View view) {
            super(view);
            ButterKnife.bind(this , view);
            //列表item的点击事件
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    updataList(getAdapterPosition());
                }
            });

        }

    }

    /**
     * @param postion
     * 更新列表内容
     */
    private void updataList(int postion) {
        //获取ListModel对象
        ListModel listModel = list.get(postion);
        //判断当前音频文件是否在该listModel中
        boolean check = checkMusinInList(listModel , music);
        //如果当前音频对象不在改列表中
        if (!check){
            //创建新的listItemModel对象 并插入到数据库中
            ListItemModel listItemModel = new ListItemModel(music.name , music.longTime  , music.artist , listModel.name , music.path );
            DataBaseManager.getInstance(context).insert(listItemModel);
            //通知页面更新列表视图的内容
            EventBus.getDefault().post(new EB_UpdataList(EB_UpdataList.UPDATAPLAYERLIST));
            //关闭当前dialog对话框
            FileShowAdapter.closeDialog();
        }else {
            Toast.makeText(context , "当前歌曲已经在本列表中", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * @param listModel
     * @param music
     * @return
     * 判断当前音频文件是否在该listModel中
     */
    private boolean checkMusinInList(ListModel listModel, Music music) {
        List<ListItemModel> listItemModels = DataBaseManager.getInstance(context).queryByWhere(ListItemModel.class,
                "path" , new String[]{music.path});
        for (int i = 0 ; i < listItemModels.size() ; i++){
            ListItemModel listItemModel = listItemModels.get(i);
            if (listItemModel.parent.equals(listModel.name)){
                return true;
            }
        }
        return false;
    }

}