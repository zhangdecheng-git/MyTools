package tools.zhang.com.mytools.baseadapter.recycleview.normal;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import tools.zhang.com.mytools.R;

/**
 * Created by zhangdecheng on 2016/11/11.
 */
public class NormalViewHolder extends RecyclerView.ViewHolder {
    public TextView mTextView;

    NormalViewHolder(View itemView) {
        super(itemView);
        mTextView = (TextView) itemView.findViewById(R.id.recycler_view_item);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("NormalTextViewHolder", "onClick--> position = " + getPosition());
                Toast.makeText(v.getContext(), "onClick--> position = " + getPosition(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}