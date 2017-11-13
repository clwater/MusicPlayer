package wdd.musicplayer.ui.fragment;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import wdd.musicplayer.R;
import wdd.musicplayer.model.Music;
import wdd.musicplayer.utils.MediaUtils;

/**
 * Created by gengzhibo on 2017/11/6.
 */

public class FileFragment extends Fragment {
    List<Music> dBMusicList = new ArrayList<Music>();

    @BindView(R.id.textview_file_alltag)
    TextView textview_file_alltag;

    @BindView(R.id.textview_file_localtag)
    TextView textview_file_localtag;


    private boolean isAllTag = true;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragemnt_file, container, false);
        ButterKnife.bind(this, view);

        init();

        return view;

    }

    private void init() {
        dBMusicList = MediaUtils.DBInfo(getActivity());
    }

    @OnClick(R.id.textview_file_alltag)
    public void OnClick_text_file_alltag(){
        isAllTag = false;
        changeTag();
    }

    @OnClick(R.id.textview_file_localtag)
    public void OnClick_textview_file_localtag(){
        isAllTag = true;
        changeTag();
    }

    private void changeTag() {
        if (isAllTag){
            textview_file_alltag.setBackgroundResource(R.drawable.selector_file_all);
            textview_file_localtag.setBackgroundResource(R.drawable.selector_file_local_slect);
        }else {
            textview_file_alltag.setBackgroundResource(R.drawable.selector_file_all_slect);
            textview_file_localtag.setBackgroundResource(R.drawable.selector_file_local);
        }
    }
}
