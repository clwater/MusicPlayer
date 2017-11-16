package wdd.musicplayer.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import wdd.musicplayer.R;
import wdd.musicplayer.model.FileModel;
import wdd.musicplayer.ui.activity.ChooseLoaclFileActivity;

/**
 * 选择文件夹adapter
 */

public class ChooseFileAdapter extends RecyclerView.Adapter<ChooseFileAdapter.AllFileAdapterHolder> {
    private final LayoutInflater layoutInflater;
    private final Context context;
    private List<FileModel> list = new ArrayList<>();

    //构造函数 设置相关变量
    public ChooseFileAdapter(Context context , List<FileModel> list) {
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
        this.list = list;
    }

    //初始化设置资源文件等信息
    @Override
    public AllFileAdapterHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new AllFileAdapterHolder(layoutInflater.inflate(R.layout.item_choosefile, parent, false));
    }

    //设置文本内容
    @Override
    public void onBindViewHolder(AllFileAdapterHolder holder, int position) {
        //获取当前对象
        FileModel fileModel = list.get(position);


        holder.textview_choose_name.setText(fileModel.name);
        holder.textview_choose_time.setText(fileModel.createTime);
        String info = null;
        switch (fileModel.type){
            case 0:
                holder.imageview_choose_folder.setBackgroundResource(R.drawable.ic_folder);
                info = fileModel.size;
                break;
            case 1:
                holder.imageview_choose_folder.setBackgroundResource(R.drawable.ic_file_music);
                info = fileModel.size;
                break;
            case 2:
                holder.imageview_choose_folder.setBackgroundResource(R.drawable.ic_file);
                info = fileModel.size;
                break;
            default: break;
        }

        holder.textview_choose_info.setText(info);


    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    public class AllFileAdapterHolder extends RecyclerView.ViewHolder {

        //初始化item相关组件
        @BindView(R.id.textview_choose_name)
        TextView textview_choose_name;
        @BindView(R.id.textview_choose_info)
        TextView textview_choose_info;
        @BindView(R.id.textview_choose_time)
        TextView textview_choose_time;
        @BindView(R.id.imageview_choose_folder)
        ImageView imageview_choose_folder;

        AllFileAdapterHolder(View view) {
            super(view);
            ButterKnife.bind(this , view);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("wdd", "onClick--> position = " + getPosition());
                    FileModel fileModel = list.get(getAdapterPosition());
                    if (fileModel.type == 0) {
                        Intent intent = new Intent(context, ChooseLoaclFileActivity.class);
                        intent.putExtra("path",fileModel.path);
                        context.startActivity(intent);
                    }
                }
            });
        }
    }
}