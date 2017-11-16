package wdd.musicplayer.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
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
import wdd.musicplayer.eventbus.EB_PlayerMusic;
import wdd.musicplayer.model.FileModel;
import wdd.musicplayer.model.Music;
import wdd.musicplayer.utils.TimeUtils;

/**
 * 数据库歌曲adap
 */

public class LoaclFileAdapter extends RecyclerView.Adapter<LoaclFileAdapter.AllFileAdapterHolder> {
    private final LayoutInflater layoutInflater;
    private final Context context;
    private static List<FileModel> list = new ArrayList<>();
    public static String Tag = "allfile";

    //构造函数 设置相关变量
    public LoaclFileAdapter(Context context , List<FileModel> list) {
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
        this.list = list;
    }

    //初始化设置资源文件等信息
    @Override
    public AllFileAdapterHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new AllFileAdapterHolder(layoutInflater.inflate(R.layout.item_file_local, parent, false));
    }

    //设置文本内容
    @Override
    public void onBindViewHolder(AllFileAdapterHolder holder, int position) {
        //获取当前对象
        FileModel fileModel = list.get(position);
        //设置歌曲名
        holder.textview_fileitem_name.setText(fileModel.name);
        String info = String.format("%s 首歌 | %s" , String.valueOf(fileModel.number) , fileModel.path);
        holder.textview_fileitem_info.setText(info);


    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    public static class AllFileAdapterHolder extends RecyclerView.ViewHolder {

        //初始化item相关组件
        @BindView(R.id.textview_fileitem_name)
        TextView textview_fileitem_name;
        @BindView(R.id.textview_fileitem_info)
        TextView textview_fileitem_info;
        @BindView(R.id.imagebutton_fileitem_action)
        ImageView imagebutton_fileitem_action;

        AllFileAdapterHolder(View view) {
            super(view);
            ButterKnife.bind(this , view);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("wdd", "onClick--> position = " + getPosition());
//
//                    //通过EventBus发送播放音频的请求
//                    EB_PlayerMusic eb_playerMusic = new EB_PlayerMusic();
//                    eb_playerMusic.music = list.get(getPosition());
//                    eb_playerMusic.tag = Tag;
//                    EventBus.getDefault().post(eb_playerMusic);
                }
            });
        }
    }
}