package tools.zhang.com.mytools.baseadapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import tools.zhang.com.mytools.R;
import tools.zhang.com.mytools.baseadapter.recycleview.common.CommonAdapterActivity;
import tools.zhang.com.mytools.baseadapter.recycleview.normal.NormalAdapterActivity;

/**
 * Created by zhangdecheng on 2016/11/11.
 */
public class BaseAdapterActivity extends Activity implements View.OnClickListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_baseadapter);
        findViewById(R.id.normal_list).setOnClickListener(this);
        findViewById(R.id.common_list).setOnClickListener(this);
        findViewById(R.id.normal_recycleview).setOnClickListener(this);
        findViewById(R.id.common_recycleview).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int viewId = v.getId();
        Intent intent = null;
        switch (viewId) {
            case R.id.normal_list:
                break;
            case R.id.common_list:
                break;
            case R.id.normal_recycleview:
                intent = new Intent(this, NormalAdapterActivity.class);
                startActivity(intent);
                break;
            case R.id.common_recycleview:
                intent = new Intent(this, CommonAdapterActivity.class);
                startActivity(intent);
                break;
        }
    }
}
