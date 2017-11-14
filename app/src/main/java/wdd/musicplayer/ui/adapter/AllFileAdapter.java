package wdd.musicplayer.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import wdd.musicplayer.R;
import wdd.musicplayer.model.Music;

/**
 * Created by gengzhibo on 2017/11/14.
 */

public class AllFileAdapter extends RecyclerView.Adapter<AllFileAdapter.AllFileAdapterHolder> {
    private final LayoutInflater mLayoutInflater;
    private final Context mContext;
    private List<Music> list = new ArrayList<>();

    public AllFileAdapter(Context context , List<Music> list) {
//        mTitles = context.getResources().getStringArray(R.array.titles);
        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
        this.list = list;
    }

    @Override
    public AllFileAdapterHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new AllFileAdapterHolder(mLayoutInflater.inflate(R.layout.item_file_all, parent, false));
    }

    @Override
    public void onBindViewHolder(AllFileAdapterHolder holder, int position) {
        holder.mTextView.setText(list.get(position).name);
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    public static class AllFileAdapterHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.text_view_name)
        TextView mTextView;

        AllFileAdapterHolder(View view) {
            super(view);
            ButterKnife.bind(this , view);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("AllFileAdapterHolder", "onClick--> position = " + getPosition());
                }
            });
        }
    }
}