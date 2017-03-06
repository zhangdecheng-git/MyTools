package tools.zhang.com.mytools.fragment.onefragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;

import tools.zhang.com.mytools.R;

/**
 * Created by zhangdecheng on 2016/11/29.
 */
public class OneFragmentActivity extends FragmentActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one_fragment);
    }
}
