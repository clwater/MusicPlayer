package wdd.musicplayer.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import wdd.musicplayer.R;

/**
 * Created by gengzhibo on 2017/11/6.
 */

public class PlayerListFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragemnt_playlist, container, false);
        ButterKnife.bind(this, view);
        // TODO Use fields...
        return view;

    }
}
