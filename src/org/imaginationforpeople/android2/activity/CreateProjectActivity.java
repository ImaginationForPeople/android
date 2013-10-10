package org.imaginationforpeople.android2.activity;

import org.imaginationforpeople.android2.R;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

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

	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
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
			} else {
				InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);

				final RelativeLayout loadingLayout = (RelativeLayout) findViewById(R.id.createproject_view_loading);
				final ScrollView contentLayout = (ScrollView) findViewById(R.id.createproject_view_form);

				if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1) {
					loadingLayout.setVisibility(View.VISIBLE);
					loadingLayout.setAlpha(0);
					loadingLayout.animate().setDuration(1000).alpha(1);

					contentLayout.animate().setDuration(1000)
					.alpha(0)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							contentLayout.setVisibility(View.GONE);
						}
					});
				} else {
					loadingLayout.setVisibility(View.VISIBLE);
					contentLayout.setVisibility(View.GONE);
				}
			}
			break;
		}
		return super.onOptionsItemSelected(item);
	}
}
