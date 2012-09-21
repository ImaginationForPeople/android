package org.imaginationforpeople.android.activity;

import java.util.List;

import org.imaginationforpeople.android.R;
import org.imaginationforpeople.android.handler.ProjectViewHandler;
import org.imaginationforpeople.android.helper.DisplayHelper;
import org.imaginationforpeople.android.helper.UriHelper;
import org.imaginationforpeople.android.model.I4pProjectTranslation;
import org.imaginationforpeople.android.model.Question;
import org.imaginationforpeople.android.thread.ProjectViewThread;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ShareActionProvider;
import android.widget.TextView;

public class ProjectViewActivity extends Activity {
	private boolean displayMenu = false;
	private Intent shareIntent;
	private I4pProjectTranslation project;
	
	@TargetApi(14)
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		if(displayMenu && menu.size() == 0) {
			// Inflating the menu
			MenuInflater inflater = getMenuInflater();
			inflater.inflate(R.menu.projectview, menu);
			
			// Creating share intent
			shareIntent = new Intent(Intent.ACTION_SEND);
			shareIntent.putExtra(Intent.EXTRA_TEXT, UriHelper.getProjectUrl(project));
			shareIntent.putExtra(Intent.EXTRA_SUBJECT, project.getTitle());
			shareIntent.setType("text/plain");
			
			// Configuring share button (Android 4.0+)
			if(Build.VERSION.SDK_INT >= 14) {
				MenuItem share = menu.findItem(R.id.projectview_share);
				ShareActionProvider sap = (ShareActionProvider) share.getActionProvider();
				sap.setShareIntent(shareIntent);
			}
		}
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()) {
		case android.R.id.home:
			if(getIntent().getData() != null) {
				Intent intent = new Intent(this, HomepageActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(intent);
			} else 
				finish();
			break;
		case R.id.projectview_share:
			startActivity(shareIntent);
			break;
		}
		return super.onOptionsItemSelected(item);
	}
	
	@TargetApi(11)
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.loading);
		
		if(Build.VERSION.SDK_INT >= 11)
			getActionBar().setDisplayHomeAsUpEnabled(true);
		
		project = (I4pProjectTranslation) getLastNonConfigurationInstance();
		if(project != null)
			displayProject();
		else {
			String projectLang;
			String projectSlug;
			
			Uri data = getIntent().getData();
			if(data != null) {
				List<String> path = data.getPathSegments();
				projectLang = path.get(0);
				projectSlug = path.get(2);
			} else {
				Bundle extras = getIntent().getExtras();
				
				if(extras.containsKey("project_title"))
					setTitle(extras.getString("project_title"));
				
				projectLang = extras.getString("project_lang");
				projectSlug = extras.getString("project_slug");
			}
			
			ProjectViewHandler handler = new ProjectViewHandler(this);
			ProjectViewThread thread = new ProjectViewThread(handler, projectLang, projectSlug);
			
			thread.start();
		}
	}
	
	@Override
	public Object onRetainNonConfigurationInstance() {
		return project;
	}
	
	public void setProject(I4pProjectTranslation p) {
		project = p;
	}
	
	@TargetApi(11)
	public void displayProject() {
		setContentView(R.layout.projectview_description);
		displayMenu = true;
		if(Build.VERSION.SDK_INT >= 11)
			invalidateOptionsMenu(); // Rebuild the menu
		
		setTitle(project.getTitle());
		
		TextView baseline = (TextView) findViewById(R.id.projectview_description_baseline_text);
		baseline.setText(project.getBaseline());
		
		if(project.getAboutSection() != null) {
			TextView about = (TextView) findViewById(R.id.projectview_description_about_text);
			about.setText(project.getAboutSection());
		} else {
			LinearLayout about = (LinearLayout) findViewById(R.id.projectview_description_about_container);
			about.setVisibility(View.GONE);
		}
		
		ProgressBar status = (ProgressBar) findViewById(R.id.projectview_description_status_progress);
		if("IDEA".equals(project.getProject().getStatus())) {
			status.setProgress(1);
		} else if("BEGIN".equals(project.getProject().getStatus())) {
			status.setProgress(2);
		} else if("WIP".equals(project.getProject().getStatus())) {
			status.setProgress(3);
		} else if("END".equals(project.getProject().getStatus())) {
			status.setProgress(4);
		}
		
		if(project.getProject().getWebsite() != null) {
			TextView website = (TextView) findViewById(R.id.projectview_description_website_text);
			website.setText(project.getProject().getWebsite());
		} else {
			LinearLayout website = (LinearLayout) findViewById(R.id.projectview_description_website_container);
			website.setVisibility(View.GONE);
		}
		
		if(project.getCalltoSection() != null) {
			TextView callto = (TextView) findViewById(R.id.projectview_description_callto_text);
			callto.setText(project.getCalltoSection());
		} else {
			LinearLayout callto = (LinearLayout) findViewById(R.id.projectview_description_callto_container);
			callto.setVisibility(View.GONE);
		}
		
		LinearLayout questions = (LinearLayout) findViewById(R.id.projectview_description_questions_container);
		TextView questionView, answerView;
		for(Question question : project.getProject().getQuestions()) {
			if(question.getAnswer() != null) {
				questionView = new TextView(this);
				questionView.setText(question.getQuestion());
				
				answerView = new TextView(this);
				answerView.setText(question.getAnswer());
				answerView.setPadding(DisplayHelper.dpToPx(10), 0, 0, 0);
				
				questions.addView(questionView);
				questions.addView(answerView);
			}
		}
	}
}
