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
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

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
    Music _music = new Music();

    List<Music> musicPlayerList = new ArrayList<>();

    //播放模式
    PlayModel playModel = new PlayModel();


    MediaPlayer mediaPlayer = new MediaPlayer();
    private ListItemModel listItemModel;

    Timer timer;
    boolean isChanging = false;

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
        _music.path = "";

        //更新播放状态的页面信息
        initPlayIcon(music.isPlaying);
        initPlayModeIcon();
        initPlayFavorite();
    }




    private void initMusicPlayerList(String parent) {
        musicPlayerList.clear();
        List<ListItemModel> listItemModels = DataBaseManager.getInstance(getContext()).queryByWhere(ListItemModel.class,
                "parent" , new String[]{parent});
        for (ListItemModel listItemModel : listItemModels){
            Music music = new Music();
            music.name = listItemModel.name;
            music.artist = listItemModel.artist;
            music.path = listItemModel.path;
            music.longTime = listItemModel.time;
            musicPlayerList.add(music);
        }
        if (musicPlayerList.size() > 0) {
            music = musicPlayerList.get(0);
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
        updatePlayIcon();
        changePlayFavorite();

        textview_player_endtime.setText(TimeUtils.tranTime(music.longTime));
        Log.d("wdd" , "music.longTime:  " + music.longTime);
        seekbar_player_seekbar.setMax(music.longTime);
        seekbar_player_seekbar.setOnSeekBarChangeListener(new SeekbarListener());
        prepareMusic(music);

    }


    class SeekbarListener implements SeekBar.OnSeekBarChangeListener {
        public void onProgressChanged(SeekBar seekBar, int progress,
                                      boolean fromUser) {
        }

        public void onStartTrackingTouch(SeekBar seekBar) {
            isChanging=true;
        }

        public void onStopTrackingTouch(SeekBar seekBar) {
            mediaPlayer.seekTo(seekBar.getProgress());
            isChanging=false;
        }

    }


    private void prepareMusic(Music music) {
        File file = new File(music.path);
        mediaPlayer.reset();
        try{
            mediaPlayer.setDataSource(file.getAbsolutePath());
            mediaPlayer.prepare();
            playerMusic();
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }

    private void playerMusic() {
        if (!music.path.equals(_music.path)){

            timer = new Timer();

            TimerTask timerTask = new TimerTask() {
                @Override
                public void run() {
                    if(isChanging==true) {
                        return;
                    }
                    seekbar_player_seekbar.setProgress(mediaPlayer.getCurrentPosition());
                    EventBus.getDefault().post(new EB_PlayerList(EB_PlayerList.NOWTIME  , TimeUtils.tranTime(mediaPlayer.getCurrentPosition())));
                }
            };
            timer.schedule(timerTask, 0, 1000);

        }
        _music = music;

        mediaPlayer.start();
    }

    private void stopMusic() {
        mediaPlayer.pause();
    }

    private void initPlayIcon(boolean isPlaying) {
        changePlayIcon(isPlaying);
    }

    private void updatePlayIcon() {
        changePlayIcon(music.isPlaying);
        music.isPlaying = !music.isPlaying;

        if (music.isPlaying){
            playerMusic();
        }else {
            stopMusic();
        }
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
            listItemModel = new ListItemModel(music.name , music.longTime , music.artist , "收藏" , music.path);
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
        updatePlayIcon();
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
        }else if (e.viewPart.equals(EB_PlayerList.NOWTIME)){
            textview_player_nowtime.setText(e.time);
        }
    }

    /**
     * @param e
     * 通过EventBus接收播放音乐的请求
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void EVENT_EB_PlayerMusic(EB_PlayerMusic e){
        if(e.tag.equals(EB_PlayerMusic.ALLFILE)) {
            music = e.music;
            playingMusic(music);
        }else if (e.tag.equals(EB_PlayerMusic.LIST)){
            initMusicPlayerList(e.parent);
        }
    }

    @Override
    public void onDestroyView() {
        EventBus.getDefault().unregister(getActivity());
        super.onDestroyView();
    }
}
