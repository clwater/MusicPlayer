package wdd.musicplayer.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import wdd.musicplayer.eventbus.EB_updataLoaclFileList;
import wdd.musicplayer.model.FileModel;
import wdd.musicplayer.model.Music;
import wdd.musicplayer.ui.activity.ChooseLoaclFileActivity;
import wdd.musicplayer.utils.FileUtils;
import wdd.musicplayer.utils.TimeUtils;

/**
 * 选择文件夹adapter
 */

public class FileShowAdapter extends RecyclerView.Adapter<FileShowAdapter.AllFileAdapterHolder> {
    private final LayoutInflater layoutInflater;
    private final Context context;
    private List<Music> list = new ArrayList<>();

    //构造函数 设置相关变量
    public FileShowAdapter(Context context , List<Music> list) {
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
        this.list = list;
    }

    //初始化设置资源文件等信息
    @Override
    public AllFileAdapterHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new AllFileAdapterHolder(layoutInflater.inflate(R.layout.item_fileshow, parent, false));
    }

    //设置文本内容
    @Override
    public void onBindViewHolder(AllFileAdapterHolder holder, int position) {
        //获取当前对象
        Music music = list.get(position);
        holder.textview_fileshow_name.setText(music.filename);
        String info = String.format("%s | %s" , music.artist , TimeUtils.tranTime(music.longTime));
        holder.textview_fileshow_info.setText(info);
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    public class AllFileAdapterHolder extends RecyclerView.ViewHolder {

        //初始化item相关组件
        @BindView(R.id.textview_fileshow_name)
        TextView textview_fileshow_name;
        @BindView(R.id.textview_fileshow_info)
        TextView textview_fileshow_info;
        @BindView(R.id.imagebutton_fileshow_action)
        ImageView imagebutton_fileshow_action;

        AllFileAdapterHolder(View view) {
            super(view);
            ButterKnife.bind(this , view);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("wdd", "onClick--> position = " + getPosition());

                }
            });

            imagebutton_fileshow_action.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showPopupMenu(imagebutton_fileshow_action , getAdapterPosition());
                }
            });
        }
    }

    private  void showPopupMenu(final View view , final int postion) {
        // 这里的view代表popupMenu需要依附的view
        PopupMenu popupMenu = new PopupMenu(context, view);
        // 获取布局文件
        popupMenu.getMenuInflater().inflate(R.menu.flodershow_menu, popupMenu.getMenu());
        popupMenu.show();

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Music music = list.get(postion);
                if (view.getId() == R.id.flodershow_insert){

                }

                return false;
            }
        });
    }
}