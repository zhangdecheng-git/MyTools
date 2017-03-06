package tools.zhang.com.mytools.parcelable;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import java.util.List;

import tools.zhang.com.mytools.R;


public class ParcelableSecondActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_parcelable_second_page);
		TextView textView = (TextView) findViewById(R.id.txt);
		StringBuilder content = new StringBuilder();
		Intent intent = getIntent();
		if (intent != null) {
			Person person = intent.getParcelableExtra("person");
			String name = person.name;
			int age = person.age;
			boolean isMan = person.isMan;
			List<Person> family = person.family;
			List<String> address = person.address;
			String[] extra = person.extra;
			Person[] groups = person.groups;
			BirthDay birthDay = person.birthDay;

			content.append("name:").append(name)
			.append(" , age:").append(age)
			.append(" , isMan:").append(isMan)
			.append(" , family.size:").append(family != null ? family.size() : -1)
			.append(" , family.first.name:").append(family != null ? family.get(0).name : -1)

			.append(" , address.size:").append(address != null ? address.size() : -1)
			.append(" , address.first").append(address != null ? address.get(0) : null)
			.append(" , extra.size:").append(extra != null ? extra.length : -1)
			.append(" , extra.first:").append(extra != null ? extra[0] : null)
			.append(" , groups.size:").append(groups != null ? groups.length: -1)
			.append(" , groups.first:").append(groups != null ? groups[0].name : null)
			.append(" , birthday.year:").append(birthDay != null ? birthDay.year : -1);
			;
		}

		textView.setText(content.toString());

		int i = 0;
		i++;
	}

}
