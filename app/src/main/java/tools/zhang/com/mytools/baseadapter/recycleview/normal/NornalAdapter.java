package tools.zhang.com.mytools.baseadapter.recycleview.normal;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import tools.zhang.com.mytools.R;

/**
 * Created by zhangdecheng on 2016/11/11.
 */
public class NornalAdapter extends RecyclerView.Adapter<NormalViewHolder> {

    private final LayoutInflater mLayoutInflater;
    private final Context mContext;
    private String[] mTitles;

    public NornalAdapter(Context context) {
        mTitles = context.getResources().getStringArray(R.array.titles);
        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public NormalViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new NormalViewHolder(mLayoutInflater.inflate(R.layout.activity_recyclerview_item, parent, false));
    }

    @Override
    public void onBindViewHolder(NormalViewHolder holder, int position) {
        holder.mTextView.setText(mTitles[position]);
    }

    @Override
    public int getItemCount() {
        return mTitles == null ? 0 : mTitles.length;
    }
}