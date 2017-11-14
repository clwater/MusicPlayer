package wdd.musicplayer.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import wdd.musicplayer.R;
import wdd.musicplayer.eventbus.EB_PlayerMusic;
import wdd.musicplayer.model.Music;
import wdd.musicplayer.utils.TimeUtils;

/**
 * 所有歌曲列表的Adapter
 */

public class AllFileAdapter extends RecyclerView.Adapter<AllFileAdapter.AllFileAdapterHolder> {
    private final LayoutInflater layoutInflater;
    private final Context context;
    private static List<Music> list = new ArrayList<>();
    public static String Tag = "allfile";

    //构造函数 设置相关变量
    public AllFileAdapter(Context context , List<Music> list) {
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
        this.list = list;
    }

    //初始化设置资源文件等信息
    @Override
    public AllFileAdapterHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new AllFileAdapterHolder(layoutInflater.inflate(R.layout.item_file_all, parent, false));
    }

    //设置文本内容
    @Override
    public void onBindViewHolder(AllFileAdapterHolder holder, int position) {
        //获取当前对象
        Music music = list.get(position);
        //设置歌曲名
        holder.textview_fileitem_name.setText(music.name);
        //获取作者名
        holder.textview_fileitem_artist.setText(music.artist);



        //设置时间
        holder.textview_fileitem_time.setText(TimeUtils.tranTime(music.longTime));
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    public static class AllFileAdapterHolder extends RecyclerView.ViewHolder {

        //初始化item相关组件
        @BindView(R.id.textview_fileitem_name)
        TextView textview_fileitem_name;
        @BindView(R.id.textview_fileitem_artist)
        TextView textview_fileitem_artist;
        @BindView(R.id.textview_fileitem_time)
        TextView textview_fileitem_time;

        AllFileAdapterHolder(View view) {
            super(view);
            ButterKnife.bind(this , view);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("wdd", "onClick--> position = " + getPosition());

                    //通过EventBus发送播放音频的请求
                    EB_PlayerMusic eb_playerMusic = new EB_PlayerMusic();
                    eb_playerMusic.music = list.get(getPosition());
                    eb_playerMusic.tag = Tag;
                    EventBus.getDefault().post(eb_playerMusic);
                }
            });
        }
    }
}