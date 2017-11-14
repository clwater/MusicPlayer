package wdd.musicplayer.ui.fragment;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
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

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import wdd.musicplayer.R;
import wdd.musicplayer.eventbus.EB_PlayerMusic;
import wdd.musicplayer.model.Music;
import wdd.musicplayer.model.PlayMode;
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
    PlayMode playMode = new PlayMode();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragemnt_player, container, false);
        ButterKnife.bind(this, view);
        EventBus.getDefault().register(PlayerFragment.this);


        //初始化
        init(music);


        return view;

    }

    private void init(Music music) {

        //更新播放状态的页面信息
        updatePlayIcon(music.isPlaying);
        updatePlayModeIcon();
        updatePlayFavorite();


    }


    /**
     * @param e
     * 通过EventBus接收播放音乐的请求
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void EVENT_EB_PlayerMusic(EB_PlayerMusic e){
        if(e.tag.equals(AllFileAdapter.Tag)) {
            playingMusic(e.music);
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

        textview_player_endtime.setText(TimeUtils.tranTime(music.longTime));

    }


    private void updatePlayIcon(boolean isPlaying) {
        if (!isPlaying){
            //更新图片是否旋转
            shadowImageView_player_image.resumeRotateAnimation();
        }else {
            shadowImageView_player_image.pauseRotateAnimation();
        }

        //更改播放按钮状态
        button_player_toggle.setImageResource(!isPlaying ? R.drawable.ic_pause : R.drawable.ic_play);
        music.isPlaying = !music.isPlaying;
    }

    //更换播放模式图标
    private void updatePlayModeIcon() {
        playMode.next();
        switch (playMode.index){
            case PlayMode.SINGLE:
                button_player_mode.setImageResource(R.drawable.ic_play_mode_single);
                break;
            case PlayMode.LOOP:
                button_player_mode.setImageResource(R.drawable.ic_play_mode_loop);
                break;
            case PlayMode.LIST:
                button_player_mode.setImageResource(R.drawable.ic_play_mode_list);
                break;
            case PlayMode.SHUFFLE:
                button_player_mode.setImageResource(R.drawable.ic_play_mode_shuffle);
                break;
            default:break;
        }
    }

    //更改当前歌曲是否被收藏
    private void updatePlayFavorite() {
        button_player_favorite.setImageResource(music.favorite ? R.drawable.ic_favorite_no : R.drawable.ic_favorite_yes);
        music.favorite = !music.favorite;
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
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(getActivity());
    }
}
