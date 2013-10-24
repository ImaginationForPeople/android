package org.imaginationforpeople.android2.activity;

import org.imaginationforpeople.android2.R;
import org.imaginationforpeople.android2.handler.CreateProjectHandler;
import org.imaginationforpeople.android2.model.I4pProject;
import org.imaginationforpeople.android2.model.I4pProjectTranslation;
import org.imaginationforpeople.android2.thread.CreateProjectThread;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;

public class CreateProjectActivity extends SherlockActivity implements OnClickListener {
	private static CreateProjectThread thread;
	private I4pProjectTranslation projectTranslation;
	private boolean blockBack = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		setContentView(R.layout.createproject);

		if(thread != null && thread.isAlive()) {
			projectTranslation = savedInstanceState.getParcelable("project");
			CreateProjectHandler handler = new CreateProjectHandler(this, projectTranslation);
			thread.setHandler(handler);
			blockBack = true;

			final RelativeLayout loadingLayout = (RelativeLayout) findViewById(R.id.createproject_view_loading);
			final ScrollView contentLayout = (ScrollView) findViewById(R.id.createproject_view_form);
			loadingLayout.setVisibility(View.VISIBLE);
			contentLayout.setVisibility(View.GONE);
		} else {
			final EditText title = (EditText) findViewById(R.id.createproject_title);
			title.addTextChangedListener(new TextWatcher() {
				@Override
				public void onTextChanged(CharSequence s, int start, int before, int count) {
					if(!"".equals(s.toString()))
						title.setError(null);
				}

				@Override
				public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
				@Override
				public void afterTextChanged(Editable s) {}
			});
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		if(thread == null || !thread.isAlive())
			getSupportMenuInflater().inflate(R.menu.createproject, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()) {
		case android.R.id.home:
			doBackFunction();
			break;
		case R.id.createproject_menu_send:
			EditText title = (EditText) findViewById(R.id.createproject_title);
			if("".equals(title.getText().toString().trim())) {
				title.setError(getResources().getString(R.string.createproject_title_empty));
				title.requestFocus();
			} else {
				supportInvalidateOptionsMenu();
				blockBack = true;

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

				projectTranslation = new I4pProjectTranslation();
				I4pProject project = new I4pProject();

				projectTranslation.setTitle(title.getText().toString());

				EditText baseline = (EditText) findViewById(R.id.createproject_baseline);
				projectTranslation.setBaseline(baseline.getText().toString());

				Spinner status = (Spinner) findViewById(R.id.createproject_status);
				String[] statusKeys = getResources().getStringArray(R.array.createproject_statuses_keys);
				project.setStatus(statusKeys[status.getSelectedItemPosition()]);

				EditText aboutSection = (EditText) findViewById(R.id.createproject_about_section);
				projectTranslation.setAboutSection(aboutSection.getText().toString());

				EditText themes = (EditText) findViewById(R.id.createproject_themes);
				projectTranslation.setThemes(themes.getText().toString());

				projectTranslation.setProject(project);

				CreateProjectHandler handler = new CreateProjectHandler(this, projectTranslation);
				thread = new CreateProjectThread(handler, projectTranslation);
				thread.start();
			}
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		outState.putParcelable("project", projectTranslation);
		super.onSaveInstanceState(outState);
	}

	@Override
	public void onBackPressed() {
		doBackFunction();
	}

	private void doBackFunction() {
		if(blockBack) {
			AlertDialog dialog = new AlertDialog.Builder(this).create();
			dialog.setTitle(R.string.createproject_cancel_title);
			dialog.setMessage(getResources().getString(R.string.createproject_cancel_message));
			dialog.setButton(DialogInterface.BUTTON_NEGATIVE, getResources().getString(android.R.string.cancel), this);
			dialog.setButton(DialogInterface.BUTTON_POSITIVE, getResources().getString(android.R.string.ok), this);
			dialog.show();
		} else {
			if(thread != null && thread.isAlive())
				thread.requestStop();
			finish();
		}
	}

	@Override
	public void onClick(DialogInterface dialog, int which) {
		switch(which) {
		case DialogInterface.BUTTON_POSITIVE:
			if(thread != null)
				thread.requestStop();
			finish();
			break;
		}
	}
}
