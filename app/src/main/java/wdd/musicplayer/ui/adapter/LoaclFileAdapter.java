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
import wdd.musicplayer.eventbus.EB_UpdataList;
import wdd.musicplayer.model.FileModel;
import wdd.musicplayer.model.Music;
import wdd.musicplayer.ui.activity.LoaclFileShowActivity;
import wdd.musicplayer.utils.FileUtils;

/**
 * 系统数据库歌曲adapter
 */

public class LoaclFileAdapter extends RecyclerView.Adapter<LoaclFileAdapter.AllFileAdapterHolder> {
    private final LayoutInflater layoutInflater;
    private final Context context;
    private  List<FileModel> list = new ArrayList<>();
    public  String Tag = "allfile";

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
        //获取info并设置
        String info = String.format("%s 首歌 | %s" , String.valueOf(fileModel.number) , fileModel.path);
        holder.textview_fileitem_info.setText(info);


    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    public  class AllFileAdapterHolder extends RecyclerView.ViewHolder {

        //初始化item相关组件
        @BindView(R.id.textview_fileitem_name)
        TextView textview_fileitem_name;
        @BindView(R.id.textview_fileitem_info)
        TextView textview_fileitem_info;
        @BindView(R.id.imagebutton_fileitem_action)
        ImageView imagebutton_fileitem_action;

        AllFileAdapterHolder(final View view) {
            super(view);
            ButterKnife.bind(this , view);
            //item点击事件
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //构建intent对象 添加path内容
                    //进入LoaclFileShowActivity页面
                    Intent intent = new Intent(context , LoaclFileShowActivity.class);
                    intent.putExtra("path" , list.get(getAdapterPosition()).path);
                    context.startActivity(intent);
                }
            });
            //设置菜单栏点击事件响应内容
            imagebutton_fileitem_action.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showPopupMenu(imagebutton_fileitem_action , getAdapterPosition());
                }
            });
        }

    }


    private  void showPopupMenu(View view , final int postion) {
        // 这里的view代表popupMenu需要依附的view
        PopupMenu popupMenu = new PopupMenu(context, view);
        // 获取布局文件
        popupMenu.getMenuInflater().inflate(R.menu.floder_menu, popupMenu.getMenu());
        popupMenu.show();
        //弹出菜单栏点击事件
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                //获取当前fileModel对象
                FileModel fileModel = list.get(postion);
                //获取当前点击的菜单栏id
                switch (item.getItemId()){
                    //刷新文件夹
                    case R.id.floder_refresh:
                        final FileModel checkFile = DataBaseManager.getInstance(context).query(fileModel.id , FileModel.class);
                        FileUtils.findMusicInFile(context , checkFile.path , new FileUtils.FindMusicInFileBack() {
                            @Override
                            public void onCompleted(List<Music> musicList) {
                                checkFile.number = musicList.size();
                                DataBaseManager.getInstance(context).update(checkFile);
                                EventBus.getDefault().post(new EB_UpdataList(EB_UpdataList.UPDATALOACLFILELIST));
                            }
                        });
                        break;
                        //删除文件夹
                    case R.id.floder_del:
                        //从数据库中删除该条内容
                        DataBaseManager.getInstance(context).delete(fileModel);
                        //通过EventBus通知页面更新文件夹页面内容
                        EventBus.getDefault().post(new EB_UpdataList(EB_UpdataList.UPDATALOACLFILELIST));
                        break;
                }
                return false;
            }
        });
    }

}