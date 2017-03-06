package tools.zhang.com.mytools.propertyAnimation;

import android.util.Log;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;

public class RapperButton {
	private static final String TAG = "RapperButton";
	private final Button mButton;
	public RapperButton(Button button) {
		mButton = button;
	}

	public void setWidth(int width) {
		LayoutParams layoutParams = mButton.getLayoutParams();
		layoutParams.width = width;
		mButton.requestLayout();
	}

	public int getWidth() {
		Log.e(TAG, "getWidth: with:" + mButton.getLayoutParams().width );
		return mButton.getWidth();
	}

	public void setHeight(int height) {
		LayoutParams layoutParams = mButton.getLayoutParams();
		layoutParams.height = height;
		mButton.requestLayout();
	}

	public int getHeight() {
		return mButton.getLayoutParams().height;
	}
}
