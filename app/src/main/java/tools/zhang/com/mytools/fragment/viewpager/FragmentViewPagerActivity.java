package tools.zhang.com.mytools.fragment.viewpager;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.util.Log;

import tools.zhang.com.mytools.R;

public class FragmentViewPagerActivity extends FragmentActivity {

	private static final String TAG = "zhangdechengFragmentViewPagerActivity";

	private Context mContext;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = this;
		setContentView(R.layout.fragment_viewpager_activity);
		ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
		PagerTabStrip tabStrip = (PagerTabStrip) findViewById(R.id.tabstrip);
		tabStrip.setTabIndicatorColor(getResources().getColor(R.color.gray));
		tabStrip.setDrawFullUnderline(false);
		tabStrip.setBackgroundColor(getResources().getColor(R.color.new_ui_blue));
		tabStrip.setTextSpacing(50);
		AppGroupAdapter adapter = new AppGroupAdapter(mContext, getSupportFragmentManager());
		viewPager.setAdapter(adapter);
		Log.d(TAG, "onCreate");
	}

	@Override
	protected void onStart() {
		Log.d(TAG, "onStart");
		super.onStart();
	}

	@Override
	protected void onResume() {
		Log.d(TAG, "onResume");
		super.onResume();
	}

	@Override
	protected void onPause() {
		Log.d(TAG, "onPause");
		super.onPause();
	}

	@Override
	protected void onStop() {
		Log.d(TAG, "onStop");
		super.onStop();
	}
	@Override
	protected void onDestroy() {
		Log.d(TAG, "onDestroy");
		super.onDestroy();
	}

	public class AppGroupAdapter extends FragmentStatePagerAdapter {

        private final String[] titleResIds = new String[]{"发现1","发现2","发现3","发现4","发现5","发现6","发现7"};

        private final Context mContext;
        private Fragment primary;

        public AppGroupAdapter(Context context, FragmentManager manager) {
            super(manager);
            mContext = context;
        }

        // Called to inform the adapter of which item is currently considered to be the "primary", that is the one show to the user as the current page.
//        @Override
//        public void setPrimaryItem(ViewGroup container, int position, Object object) {
//            Fragment fragment = (Fragment) object;
//            if (fragment != primary) {
//                if (primary != null) {
//                    primary.setMenuVisibility(false);
//                    primary.setUserVisibleHint(false);
//                }
//                if (fragment != null) {
//                    fragment.setMenuVisibility(true);
//                    fragment.setUserVisibleHint(getUserVisibleHint());
//                }
//                primary = fragment;
//            }
//        }

        public void setUserVisibleHint(boolean isVisible) {
            if (primary != null) {
                primary.setUserVisibleHint(isVisible);
            }
        }

        @Override
        public int getCount() {
            return titleResIds.length;
//        	return 3;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new MyFragment1();
                case 1:
                	return new MyFragment2();
                case 2:
                	return new MyFragment3();
                case 3:
                	return new MyFragment4();
                case 4:
                	return new MyFragment5();
                case 5:
                	return new MyFragment6();
                case 6:
                	return new MyFragment7();
                default:
                    return null;
            }
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return titleResIds[0];
                case 1:
                    return titleResIds[1];
                case 2:
                    return titleResIds[2];
                case 3:
                    return titleResIds[3];
                case 4:
                    return titleResIds[4];
                case 5:
                    return titleResIds[5];
                case 6:
                    return titleResIds[6];
                default:
                    return null;
            }
        }
    }
}
