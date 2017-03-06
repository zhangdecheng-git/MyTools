package tools.zhang.com.mytools.recyclerView;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

import tools.zhang.com.mytools.R;

/**
 * Created by zhangdecheng on 2016/11/10.
 *
 * Android 优雅的为RecyclerView添加HeaderView和FooterView
 * http://blog.csdn.net/lmj623565791/article/details/51854533
 *
 *
 *  Android 自定义RecyclerView 实现真正的Gallery效果
 * http://blog.csdn.net/lmj623565791/article/details/38173061
 */
public class RecyclerViewActivity extends Activity {
    RecyclerView recyclerView ;
    LinearLayoutManager layoutManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recyclerview);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        NormalRecyclerViewAdapter adapter = new NormalRecyclerViewAdapter(this);
        recyclerView.setAdapter(adapter);

        layoutManager = new LinearLayoutManager(this);
//        layoutManager = new GridLayoutManager(this, 2);
//        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
//        recyclerView.setLayoutManager(layoutManager);


//        recyclerView.setLayoutManager(layoutManager);//这里用线性显示 类似于listview
//        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));//这里用线性宫格显示 类似于grid view
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, OrientationHelper.VERTICAL));//这里用线性宫格显示 类似于瀑布流
    }
}
