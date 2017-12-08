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
        //设置文件名称
        holder.textview_fileshow_name.setText(music.filename);
        //设置info信息并格式化结构
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
            //item尾部菜单按钮点击响应事件
            imagebutton_fileshow_action.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showPopupMenu(imagebutton_fileshow_action , getAdapterPosition());
                }
            });
        }
    }

    /**
     * @param view
     * @param postion
     * 展示弹出菜单栏
     */
    private  void showPopupMenu(final View view , final int postion) {
        // 这里的view代表popupMenu需要依附的view
        PopupMenu popupMenu = new PopupMenu(context, view);
        // 获取布局文件
        popupMenu.getMenuInflater().inflate(R.menu.flodershow_menu, popupMenu.getMenu());
        popupMenu.show();

        //菜单栏中内容的点击事件
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                //获取当前音频对象
                Music music = list.get(postion);
                //判断当前点击的按钮是否为添加到播放列表
                if (item.getItemId() == R.id.flodershow_insert){
                    //展示选择播放列表的dialog
                    showChooseListDiaog(music);
                }

                return false;
            }
        });
    }

    /**
     * @param music
     * 展示选择播放列表的dialog
     */
    private void showChooseListDiaog(Music music) {
        //构建dialog
        AlertDialog dialog = createChooseListDialog(music);
        //动态更改dialog的大小
        changeDialogSize(dialog);

    }

    /**
     * @param music
     * @return
     * 构造AlertDialog对象
     */
    private AlertDialog createChooseListDialog(Music music) {
        //创建dialog构建对话
        final AlertDialog.Builder builder = new AlertDialog.Builder(context , R.style.MusicPlayer_Dialog);
        //设置标题
        builder.setTitle("选择播放列表");
        //构建布局文件
        LinearLayout linearLayout = (LinearLayout) layoutInflater.inflate(R.layout.dialog_chooselist, null);
        //设置布局文件
        builder.setView(linearLayout);
        //设置点击非dialog区域的当前dialog自动关闭
        builder.setCancelable(true);

        //获取布局文件中的RecyclerView对象
        final RecyclerView recyclerview_dialog_chooselist  = linearLayout.findViewById(R.id.recyclerview_dialog_chooselist);
        //设置RecyclerView布局形式
        recyclerview_dialog_chooselist.setLayoutManager(new LinearLayoutManager(context));
        //通过系统数据库获取listModel对象
        List<ListModel> listModel = DataBaseManager.getInstance(context).queryAll(ListModel.class);
        //创建DialogPlayerListAdapter
        DialogPlayerListAdapter dialogPlayerListAdapter = new DialogPlayerListAdapter(context , listModel , music);
        //设置recyclerview_dialog_chooselist的adapter为DialogPlayerListAdapter
        recyclerview_dialog_chooselist.setAdapter(dialogPlayerListAdapter);
        //设置取消按钮的内容
        builder.setNegativeButton("取消", null);
        //根据内容构建dialog
        dialog = builder.create();
        //展示dialog
        dialog.show();
        return dialog;
    }

    /**
     * @param dialog
     * 动态更改dialog的大小
     */
    private void changeDialogSize(AlertDialog dialog) {
        //获取当前窗口对象
        Window window = dialog.getWindow();
        //获取当前窗口的属性
        WindowManager.LayoutParams lp = window.getAttributes();
        //设置属性为居中
        lp.gravity = Gravity.CENTER;

        //获取当前窗口的宽度
        WindowManager wm = activity.getWindowManager();
        int width = wm.getDefaultDisplay().getWidth();
        //调整将要设置的dialog的窗口的宽度的数据
        width = width / 5 * 3;
        //更改窗口的宽度为width
        lp.width = width;
        //更改窗口的高度为自适应
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        //设置dialog的属性
        dialog.getWindow().setAttributes(lp);
    }


    public static  void closeDialog(){
        dialog.dismiss();
    }
}