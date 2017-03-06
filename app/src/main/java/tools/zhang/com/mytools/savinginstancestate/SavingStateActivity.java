package tools.zhang.com.mytools.savinginstancestate;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import tools.zhang.com.mytools.R;

/**
 // 首次加载
 onCreate: savedInstanceState, name:null

 // 横竖屏切换
 onSaveInstanceState  outState, name:阿里郎
 onCreate: savedInstanceState, name:阿里郎
 onRestoreInstanceState  savedInstanceState,name:阿里郎
 */
public class SavingStateActivity extends Activity {
    private static final String TAG = "MyNumberPicker-Activity";
    TextView actiTextView;
    EditText firstnameEditText;
    private MyNumberPicker myNumberPicker;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saving_state);
        actiTextView = (TextView) findViewById(R.id.activity_state);
        firstnameEditText = (EditText) findViewById(R.id.first_name);
        myNumberPicker = (MyNumberPicker) findViewById(R.id.mynumberpicker);
        myNumberPicker.setMinValue(0);
        myNumberPicker.setMaxValue(10);
        Log.e(TAG, "SavingStateActivity onCreate: savedInstanceState, name:" + (savedInstanceState != null ? savedInstanceState.getString("name") : null));
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        String name = firstnameEditText.getText().toString();
        outState.putString("name", name);
        Log.e(TAG, "SavingStateActivity onSaveInstanceState  outState, name:" + name );
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        String name = savedInstanceState.getString("name");
        Log.e(TAG, "SavingStateActivity onRestoreInstanceState  savedInstanceState,name:" + name);
//        actiTextView.setText(name);
        super.onRestoreInstanceState(savedInstanceState);
    }
}
