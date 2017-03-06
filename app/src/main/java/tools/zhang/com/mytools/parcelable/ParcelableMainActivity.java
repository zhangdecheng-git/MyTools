package tools.zhang.com.mytools.parcelable;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

import java.util.ArrayList;

import tools.zhang.com.mytools.R;


public class ParcelableMainActivity extends Activity {
	private static final String TAG = "ParcelableMainActivity";

	private Handler mHandler = new Handler();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_parcelable_main_page);

		findViewById(R.id.btn).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				startSecondActivity();
			}
		});
	}

	private void startSecondActivity() {
		Intent intent = new Intent(ParcelableMainActivity.this, ParcelableSecondActivity.class);
		final Person person = new Person();
		person.name = "张德成";
		person.age = 11;
		person.isMan = true;
		person.address = new ArrayList<String>();
		person.address.add("北京");
		person.address.add("上海");
		person.family = new ArrayList<Person>();
		person.family.add(new Person("张老板", 60));
		person.family.add(new Person("王老板", 61));
		person.extra = new String[5];
		person.extra[0] = "extra0";
		person.extra[1] = "extra1";
		person.extra[2] = "extra2";

		person.groups = new Person[2];
		person.groups[0] = new Person("group0", 12);
		person.groups[1] = new Person("group1", 15);

		person.birthDay = new BirthDay(1982,5);

		intent.putExtra("person", person);
//		startActivity(intent);

		Log.e(TAG, "startSecondActivity: name:"+person.name );
		new Thread(new Runnable() {
			@Override
			public void run() {
				Log.e(TAG, "run: name:"+person.name );
				person.name="Gggggggg";
			}
		}).start();
		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				Log.e(TAG, "startSecondActivity: after name:" + person.name );
			}
		}, 1000);

	}
}
