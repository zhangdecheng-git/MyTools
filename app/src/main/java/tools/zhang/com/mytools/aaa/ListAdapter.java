package tools.zhang.com.mytools.aaa;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import tools.zhang.com.mytools.R;

public class ListAdapter extends BaseAdapter {

	public List<MainData> mDataList;
	private final Context mContext;
	private final LayoutInflater mInflater;
	public ListAdapter(Context context) {
		mContext  = context;
		mInflater = LayoutInflater.from(mContext);
	}

	public void setData(List<MainData> dataList) {
		mDataList = dataList;
	}

	@Override
	public int getCount() {
		return mDataList != null ? mDataList.size() : 0;
	}

	@Override
	public MainData getItem(int arg0) {
		return mDataList != null ? mDataList.get(arg0) : null;
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int position, View view, ViewGroup arg2) {
		ViewHolder viewHolder;

		if (view == null) {
			view = mInflater.inflate(R.layout.activity_main_list_item, null, false);
			TextView nameView = (TextView) view.findViewById(R.id.name);
			viewHolder = new ViewHolder();
			viewHolder.mNameView = nameView;
			view.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) view.getTag();
		}
		viewHolder.mNameView.setText(getItem(position).mName);
		return view;
	}

	public class ViewHolder {
		public TextView mNameView;

	}
}
