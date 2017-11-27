package wdd.musicplayer.ui.fragment;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import wdd.musicplayer.R;
import wdd.musicplayer.db.DataBaseManager;
import wdd.musicplayer.eventbus.EB_PlayerList;
import wdd.musicplayer.eventbus.EB_PlayerMusic;
import wdd.musicplayer.eventbus.EB_UpdataList;
import wdd.musicplayer.model.ListItemModel;
import wdd.musicplayer.model.Music;
import wdd.musicplayer.model.PlayModel;
import wdd.musicplayer.ui.adapter.AllFileAdapter;
import wdd.musicplayer.ui.weiget.ShadowImageView;
import wdd.musicplayer.utils.MediaUtils;
import wdd.musicplayer.utils.TimeUtils;

/**
 * 播放页面
 */

public class PlayerFragment extends Fragment {

    //切换模式按钮的绑定
    @BindView(R.id.button_player_mode)
    ImageView button_player_mode;

    //播放状态按钮的绑定
    @BindView(R.id.button_player_toggle)
    ImageView button_player_toggle;

    //歌曲图片
    @BindView(R.id.shadowImageView_player_image)
    ShadowImageView shadowImageView_player_image;

    //当前歌曲是否被收藏
    @BindView(R.id.button_player_favorite)
    ImageView button_player_favorite;

    //歌曲的名称及作者
    @BindView(R.id.textview_player_name)
    TextView textview_player_name;
    @BindView(R.id.textview_player_artist)
    TextView textview_player_artist;

    //当前播放时间及结束时间及进度条
    @BindView(R.id.textview_player_nowtime)
    TextView textview_player_nowtime;
    @BindView(R.id.textview_player_endtime)
    TextView textview_player_endtime;
    @BindView(R.id.seekbar_player_seekbar)
    SeekBar seekbar_player_seekbar;

    Music music = new Music();

    //播放模式
    PlayModel playModel = new PlayModel();


    MediaPlayer mediaPlayer = new MediaPlayer();
    private ListItemModel listItemModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(PlayerFragment.this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragemnt_player, container, false);
        ButterKnife.bind(this, view);


        //初始化
        init();


        return view;

    }

    private void init() {

        //更新播放状态的页面信息
        initPlayIcon(music.isPlaying);
        initPlayModeIcon();
        initPlayFavorite();
    }


    /**
     * @param e
     * 通过EventBus接收播放音乐的请求
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void EVENT_EB_PlayerMusic(EB_PlayerMusic e){
        if(e.tag.equals(AllFileAdapter.Tag)) {
            music = e.music;
            playingMusic(music);
        }
    }

    //更新页面歌曲信息
    private void playingMusic(Music music) {
        Bitmap bitmap = MediaUtils.parseAlbum(music);
        if (bitmap == null) {
            shadowImageView_player_image.setImageResource(R.drawable.default_record_album);
        } else {
            shadowImageView_player_image.setImageBitmap(MediaUtils.getCroppedBitmap(bitmap));
        }

        textview_player_name.setText(music.name);
        textview_player_artist.setText(music.artist);

        this.music.isPlaying = false;
        shadowImageView_player_image.cancelRotateAnimation();
        updatePlayIcon(this.music.isPlaying);
        changePlayFavorite();

        textview_player_endtime.setText(TimeUtils.tranTime(music.longTime));

        playerMusic(music);

    }

    private void playerMusic(Music music) {
        File file = new File(music.path);
        try{
            mediaPlayer.setDataSource(file.getAbsolutePath());
            mediaPlayer.prepare();
            mediaPlayer.start();
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }


    private void initPlayIcon(boolean isPlaying) {
        changePlayIcon(isPlaying);
    }

    private void updatePlayIcon(boolean isPlaying) {
        changePlayIcon(isPlaying);
        music.isPlaying = !music.isPlaying;
    }

    private void changePlayIcon(boolean isPlaying) {
        if (!isPlaying){
            //更新图片是否旋转
            shadowImageView_player_image.resumeRotateAnimation();
        }else {
            shadowImageView_player_image.pauseRotateAnimation();
        }
        //更改播放按钮状态
        button_player_toggle.setImageResource(!isPlaying ? R.drawable.ic_pause : R.drawable.ic_play);
    }


    private void initPlayModeIcon() {
        changePlayModeIcon();
    }

    private void updatePlayModeIcon() {
        playModel.next();
        changePlayModeIcon();
    }

    //更换播放模式图标
    private void changePlayModeIcon() {
        playModel.next();
        switch (playModel.index){
            case PlayModel.SINGLE:
                button_player_mode.setImageResource(R.drawable.ic_play_mode_single);
                break;
            case PlayModel.LOOP:
                button_player_mode.setImageResource(R.drawable.ic_play_mode_loop);
                break;
            case PlayModel.LIST:
                button_player_mode.setImageResource(R.drawable.ic_play_mode_list);
                break;
            case PlayModel.SHUFFLE:
                button_player_mode.setImageResource(R.drawable.ic_play_mode_shuffle);
                break;
            default:break;
        }
    }


    private void initPlayFavorite() {
        changePlayFavorite();
    }

    private void updatePlayFavorite() {
        if (music.favorite){
            listItemModel = DataBaseManager.getInstance(getContext()).queryMusinInFavorite(music.path).get(0);
            DataBaseManager.getInstance(getContext()).delete(listItemModel);
        }else {
            listItemModel = new ListItemModel(music.name , TimeUtils.tranTime(music.longTime) , music.artist , "收藏" , music.path);
            DataBaseManager.getInstance(getContext()).insert(listItemModel);
        }

        changePlayFavorite();
    }

    //更改当前歌曲是否被收藏
    private void changePlayFavorite() {
        music.favorite = DataBaseManager.getInstance(getContext()).isQueryMusinInFavorite(music.path);
        button_player_favorite.setImageResource(music.favorite ? R.drawable.ic_favorite_yes : R.drawable.ic_favorite_no);
    }



    @OnClick(R.id.button_player_mode)
    public void onClick_button_player_mode(){
        updatePlayModeIcon();
//        Log.d("wdd" , "button_player_mode");
    }


    @OnClick(R.id.button_player_last)
    public void onClick_button_player_last(){

    }

    @OnClick(R.id.button_player_toggle)
    public void onClick_button_player_toggle(){
        updatePlayIcon(music.isPlaying);
    }

    @OnClick(R.id.button_player_next)
    public void onClick_button_player_next(){

    }

    @OnClick(R.id.button_player_favorite)
    public void onClick_button_player_favorite(){
        updatePlayFavorite();
        EventBus.getDefault().post(new EB_UpdataList(EB_UpdataList.UPDATAPLAYERLIST));
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void EventBus_EB_PlayerList(EB_PlayerList e){
        if (e.viewPart.equals(EB_PlayerList.FAVORITE)){
            changePlayFavorite();
        }
    }

    @Override
    public void onDestroyView() {
        EventBus.getDefault().unregister(getActivity());
        super.onDestroyView();
    }
}
