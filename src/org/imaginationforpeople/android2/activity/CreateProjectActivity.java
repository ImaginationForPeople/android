package org.imaginationforpeople.android2.activity;

import org.imaginationforpeople.android2.R;

import android.os.Bundle;
import android.widget.EditText;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;

public class CreateProjectActivity extends SherlockActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		setContentView(R.layout.createproject);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getSupportMenuInflater().inflate(R.menu.createproject, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()) {
		case android.R.id.home:
			finish();
			break;
		case R.id.createproject_menu_send:
			EditText title = (EditText) findViewById(R.id.createproject_title);
			if("".equals(title.getText().toString().trim())) {
				title.setError(getResources().getString(R.string.createproject_title_empty));
				title.requestFocus();
			}
			break;
		}
		return super.onOptionsItemSelected(item);
	}
}
