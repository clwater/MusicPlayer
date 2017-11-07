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

/**
 * Created by gengzhibo on 2017/11/6.
 */

public class PlayerFragment extends Fragment {

    @BindView(R.id.button_player_mode)
    ImageView button_player_mode;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragemnt_player, container, false);
        ButterKnife.bind(this, view);
        // TODO Use fields...
        return view;

    }

    @OnClick(R.id.button_player_mode)
    public void onClick_button_player_mode(){
//        Log.d("wdd" , "button_player_mode");
    }

    @OnClick(R.id.button_player_last)
    public void onClick_button_player_last(){

    }

    @OnClick(R.id.button_player_toggle)
    public void onClick_button_player_toggle(){

    }

    @OnClick(R.id.button_player_next)
    public void onClick_button_player_next(){

    }

    @OnClick(R.id.button_player_favoritee)
    public void onClick_button_player_favoritee(){

    }
}
