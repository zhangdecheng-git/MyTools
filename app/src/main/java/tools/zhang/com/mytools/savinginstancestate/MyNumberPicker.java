package tools.zhang.com.mytools.savinginstancestate;

import android.content.Context;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.NumberPicker;

public class MyNumberPicker extends NumberPicker {
	private static final String TAG = "MyNumberPicker";

	public MyNumberPicker(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
	public MyNumberPicker(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}
	public MyNumberPicker(Context context) {
		this(context, null);
	}
	
	@Override
	protected Parcelable onSaveInstanceState() {
		Log.e(TAG, "MyNumberPicker  onSaveInstanceState");
		return super.onSaveInstanceState();
	}

	@Override
	protected void onRestoreInstanceState(Parcelable state) {
		Log.e(TAG, "MyNumberPicker  onRestoreInstanceState  state:" );
		super.onRestoreInstanceState(state);
	}
}
