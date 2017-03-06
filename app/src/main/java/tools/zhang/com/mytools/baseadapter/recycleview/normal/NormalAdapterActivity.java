package tools.zhang.com.mytools.baseadapter.recycleview.normal;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

import tools.zhang.com.mytools.R;
import tools.zhang.com.mytools.recyclerView.NormalRecyclerViewAdapter;

/**
 * Created by zhangdecheng on 2016/11/11.
 */
public class NormalAdapterActivity extends Activity {
    private RecyclerView recyclerView ;
    private LinearLayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recyclerview_normal);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        NormalRecyclerViewAdapter adapter = new NormalRecyclerViewAdapter(this);
        recyclerView.setAdapter(adapter);

//        layoutManager = new LinearLayoutManager(this);
//        layoutManager = new GridLayoutManager(this, 2);
//        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
//        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, OrientationHelper.VERTICAL));//这里用线性宫格显示 类似于瀑布流
    }
}
