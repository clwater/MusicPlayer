package wdd.musicplayer.ui.fragment;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import wdd.musicplayer.R;

/**
 * Created by gengzhibo on 2017/12/8.
 */

//这个页面....  不知道写什么设置  没什么可设置的  就单纯的留个页面 有需要在添就好
public class SettingFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragemnt_setting, container, false);
        ButterKnife.bind(this, view);
        // TODO Use fields...
        return view;

    }
}
