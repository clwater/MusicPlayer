package wdd.musicplayer.ui.adapter;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import wdd.musicplayer.R;
import wdd.musicplayer.db.DataBaseManager;
import wdd.musicplayer.model.ListModel;
import wdd.musicplayer.model.Music;
import wdd.musicplayer.utils.TimeUtils;

/**
 * 选择文件夹展示adapter
 */

public class FileShowAdapter extends RecyclerView.Adapter<FileShowAdapter.AllFileAdapterHolder> {
    private final LayoutInflater layoutInflater;
    private final Context context;
    private List<Music> list = new ArrayList<>();
    private Activity activity;
    private  static AlertDialog dialog;

    //构造函数 设置相关变量
    public FileShowAdapter(Context context , List<Music> list , Activity activity) {
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
        this.list = list;
        this.activity = activity;
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
                    Log.d("wdd", "onClick--> position = " + getAdapterPosition());

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
                if (item.getItemId() == R.id.flodershow_insert){
                    showChooseListDiaog(music);
                }

                return false;
            }
        });
    }

    private void showChooseListDiaog(Music music) {
        AlertDialog dialog = createChooseListDialog(music);
        changeDialogSize(dialog);

    }

    private AlertDialog createChooseListDialog(Music music) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context , R.style.MusicPlayer_Dialog);
        builder.setTitle("选择播放列表");
        LinearLayout linearLayout = (LinearLayout) layoutInflater.inflate(R.layout.dialog_chooselist, null);
        builder.setView(linearLayout);
        builder.setCancelable(true);

        final RecyclerView recyclerview_dialog_chooselist  = linearLayout.findViewById(R.id.recyclerview_dialog_chooselist);

        recyclerview_dialog_chooselist.setLayoutManager(new LinearLayoutManager(context));
        List<ListModel> listModel = DataBaseManager.getInstance(context).queryAll(ListModel.class);
        DialogPlayerListAdapter dialogPlayerListAdapter = new DialogPlayerListAdapter(context , listModel , music);
        recyclerview_dialog_chooselist.setAdapter(dialogPlayerListAdapter);

        builder.setNegativeButton("取消", null);

        dialog = builder.create();
        dialog.show();
        return dialog;
    }

    private void changeDialogSize(AlertDialog dialog) {
        Window window = dialog.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.gravity = Gravity.CENTER;


        WindowManager wm = activity.getWindowManager();
        int width = wm.getDefaultDisplay().getWidth();
        width = width / 5 * 3;

        lp.width = width;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setAttributes(lp);
    }


    public static  void closeDialog(){
        dialog.dismiss();
    }
}