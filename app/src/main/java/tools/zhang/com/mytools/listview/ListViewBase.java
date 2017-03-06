package tools.zhang.com.mytools.listview;

/**
 * Created by zhangdecheng on 2017/3/4.
 */
public class ListViewBase {
    /**
     listView.pointToPosition()
     listView.getItemAtPosition()


    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        //firstVisibleItem：当前能看见的第一个列表项ID（从0开始, 包含header）
        //visibleItemCount：当前能看见的列表项个数（小半个也算）
        //totalItemCount：列表项共数(包括header和footer)

        //判断是否滚到最后一行
        if (firstVisibleItem + visibleItemCount == totalItemCount && totalItemCount > 0) {
            mIsLastRow = true;
        }

        // 判断是否第一行
        if (firstVisibleItem == 0) {
            View firstView = view.getChildAt(firstVisibleItem);
            Log.d(TAG, "firstVisibleItem:" + firstVisibleItem + ",firstView:" + firstView);
            if (firstView != null) {
                int top = firstView.getTop();  // 往上滚动，滑出list边界，值：-5
                int scrollY = firstView.getScrollY();// 一直为：0
                float y = firstView.getY();
                float translateY = firstView.getTranslationY();
                Log.d(TAG, "top:" + top + ",scrollY:" + scrollY + ",y:" + y + ",translateY:" + translateY);
                // top:-90,scrollY:0,y:-90.0,translateY:0.0
            }
        }
    }

    如果ListView中的单个Item的view中存在checkbox，button等view，会导致ListView.setOnItemClickListener无效，事件会被子View捕获到，ListView无法捕获处理该事件.
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        // position:包含header排序  ； id:不包含header排序
        Log.d(TAG, "position:" + position + ",id:" + id); // 点击header：position:0,id:-1

        String name = (String) parent.getItemAtPosition(position);
        String nam2 = (String) mListView.getItemAtPosition(position);  // parent  和  mListView  ，一样
    }
    */
}
