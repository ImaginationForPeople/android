package org.imaginationforpeople.android.activity;

import org.imaginationforpeople.android.R;
import org.imaginationforpeople.android.handler.ProjectViewHandler;
import org.imaginationforpeople.android.helper.DisplayHelper;
import org.imaginationforpeople.android.model.I4pProjectTranslation;
import org.imaginationforpeople.android.model.Question;
import org.imaginationforpeople.android.thread.ProjectViewThread;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

public class ProjectViewActivity extends Activity {
	private I4pProjectTranslation project;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.loading);
		
		project = (I4pProjectTranslation) getLastNonConfigurationInstance();
		if(project != null)
			displayProject();
		else {
			Bundle extras = getIntent().getExtras();
			
			if(extras.containsKey("project_title"))
				setTitle(extras.getString("project_title"));
			else
				setTitle("");
			
			ProjectViewHandler handler = new ProjectViewHandler(this);
			ProjectViewThread thread = new ProjectViewThread(handler, extras.getInt("project_id"));
			
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
	
	public void displayProject() {
		setContentView(R.layout.projectview_description);
		
		if("".equals(getTitle()))
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
