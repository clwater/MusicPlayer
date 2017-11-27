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
        //设置歌曲名
        holder.textview_dialog_playerlist_name.setText(listModel.name);
        String info = String.format("%s 首歌" , String.valueOf(listModel.number) );
        holder.textview_dialog_playerlist_info.setText(info);

        if (listModel.name.equals("收藏")) {
            holder.textview_dialog_playerlist.setBackgroundResource(R.drawable.ic_favorite_yes);
        }else {
            String name = listModel.name;
            name = name.substring(0 , 1);
            holder.textview_dialog_playerlist.setText(name);
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
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    updataList(getAdapterPosition());
                }
            });

        }

    }

    private void updataList(int postion) {

        ListModel listModel = list.get(postion);
        boolean check = checkMusinInList(listModel , music);
        if (!check){
//            listModel.number = listModel.number + 1;
//            DataBaseManager.getInstance(context).update(listModel);
            ListItemModel listItemModel = new ListItemModel(music.name , TimeUtils.tranTime(music.longTime)  , music.artist , listModel.name , music.path );
            DataBaseManager.getInstance(context).insert(listItemModel);
            EventBus.getDefault().post(new EB_UpdataList(EB_UpdataList.UPDATAPLAYERLIST));
            FileShowAdapter.closeDialog();
        }else {
            Toast.makeText(context , "当前歌曲已经在本列表中", Toast.LENGTH_SHORT).show();
        }
    }

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