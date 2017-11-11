package wdd.musicplayer.ui.fragment;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import wdd.musicplayer.R;
import wdd.musicplayer.model.Music;
import wdd.musicplayer.model.PlayMode;
import wdd.musicplayer.ui.weiget.ShadowImageView;

/**
 * Created by gengzhibo on 2017/11/6.
 */

public class PlayerFragment extends Fragment {

    //切换模式按钮的绑定
    @BindView(R.id.button_player_mode)
    ImageView button_player_mode;

    //播放状态按钮的绑定
    @BindView(R.id.button_player_toggle)
    ImageView button_player_toggle;

    @BindView(R.id.shadowImageView_player_image)
    ShadowImageView shadowImageView_player_image;

    @BindView(R.id.button_player_favorite)
    ImageView button_player_favorite;

    Music music = new Music();

    //播放模式
    PlayMode playMode = new PlayMode();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragemnt_player, container, false);
        ButterKnife.bind(this, view);


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
}
