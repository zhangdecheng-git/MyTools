package tools.zhang.com.mytools.checkbox;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;

import tools.zhang.com.mytools.R;
import tools.zhang.com.mytools.utils.ToastUtil;

/**
 * Created by zhangdecheng on 2016/12/8.
 */
public class CheckBoxActivity extends Activity implements View.OnClickListener {
    private CheckBox mCheckBox;
    private LinearLayout mCheckLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkbox);
        mCheckBox = (CheckBox) findViewById(R.id.checkbox);
        mCheckLayout = (LinearLayout) findViewById(R.id.checkbox_layout);
        mCheckLayout.setOnClickListener(this);
        mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    ToastUtil.show(CheckBoxActivity.this, "checked",1);
                } else {
                    ToastUtil.show(CheckBoxActivity.this, "unchecked", 1);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        int viewId= v.getId();
        switch (viewId) {
            case R.id.checkbox_layout:
                if (mCheckBox.isChecked()) {
                    mCheckBox.setChecked(false);
                } else {
                    mCheckBox.setChecked(true);
                }
                break;
        }
    }
}
