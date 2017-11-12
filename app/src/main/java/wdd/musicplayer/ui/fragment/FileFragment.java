package wdd.musicplayer.ui.fragment;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import wdd.musicplayer.R;
import wdd.musicplayer.Utils.MediaUtils;
import wdd.musicplayer.model.Music;

/**
 * Created by gengzhibo on 2017/11/6.
 */

public class FileFragment extends Fragment {
    List<Music> DBMusicList = new ArrayList<Music>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragemnt_file, container, false);
        ButterKnife.bind(this, view);

        init();

        return view;

    }

    private void init() {
        DBMusicList = MediaUtils.DBInfo(getActivity());
    }
}
