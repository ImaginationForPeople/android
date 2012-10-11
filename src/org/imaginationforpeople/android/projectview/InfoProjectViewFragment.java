package org.imaginationforpeople.android.projectview;

import java.util.List;

import org.imaginationforpeople.android.R;
import org.imaginationforpeople.android.model.Objective;
import org.imaginationforpeople.android.model.Question;
import org.imaginationforpeople.android.model.User;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class InfoProjectViewFragment extends BaseProjectViewFragment implements OnClickListener {
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		ScrollView layout = (ScrollView) inflater.inflate(R.layout.projectview_info, null);
		
		LinearLayout overlay = (LinearLayout) layout.findViewById(R.id.projectview_description_overlay);
		overlay.getBackground().setAlpha(127);
		
		if(getProject().getProject().getPictures().size() > 0) {
			ImageView image = (ImageView) layout.findViewById(R.id.projectview_description_image);
			image.setImageBitmap(getProject().getProject().getPictures().get(0).getImageBitmap());
		}
		
		ImageView bestof = (ImageView) layout.findViewById(R.id.projectview_description_bestof);
		if(!getProject().getProject().getBestOf())
			bestof.setVisibility(View.GONE);
		
		TextView title = (TextView) layout.findViewById(R.id.projectview_description_title);
		title.setText(getProject().getTitle());
		
		TextView baseline = (TextView) layout.findViewById(R.id.projectview_description_baseline);
		baseline.setText(getProject().getBaseline());
		
		ImageView status = (ImageView) layout.findViewById(R.id.projectview_description_status);
		if("IDEA".equals(getProject().getProject().getStatus())) {
			status.setImageResource(R.drawable.project_status_idea);
			status.setContentDescription(getResources().getString(R.string.projectview_description_status_idea));
		} else if("BEGIN".equals(getProject().getProject().getStatus())) {
			status.setImageResource(R.drawable.project_status_begin);
			status.setContentDescription(getResources().getString(R.string.projectview_description_status_begin));
		} else if("WIP".equals(getProject().getProject().getStatus())) {
			status.setImageResource(R.drawable.project_status_wip);
			status.setContentDescription(getResources().getString(R.string.projectview_description_status_wip));
		} else if("END".equals(getProject().getProject().getStatus())) {
			status.setImageResource(R.drawable.project_status_end);
			status.setContentDescription(getResources().getString(R.string.projectview_description_status_end));
		}
		
		if(getProject().getProject().getLocation() != null) {
			if(!"".equals(getProject().getProject().getLocation().getCountry())) {
				int flag = getResources().getIdentifier("flag_"+getProject().getProject().getLocation().getCountry().toLowerCase(), "drawable", "org.imaginationforpeople.android");
				if(flag != 0) {
					ImageView flagView = (ImageView) layout.findViewById(R.id.projectview_description_flag);
					flagView.setImageResource(flag);
				}
			}
		}
		
		TextView website = (TextView) layout.findViewById(R.id.projectview_description_website);
		if("".equals(getProject().getProject().getWebsite()))
			website.setVisibility(View.GONE);
		else
			website.setOnClickListener(this);
		
		if(getProject().getAboutSection() == null || "".equals(getProject().getAboutSection())) {
			LinearLayout aboutContainer = (LinearLayout) layout.findViewById(R.id.projectview_description_about_container);
			aboutContainer.setVisibility(View.GONE);
		} else {
			TextView aboutText = (TextView) layout.findViewById(R.id.projectview_description_about_text);
			aboutText.setText(getProject().getAboutSection().trim());
		}
		
		if("".equals(getProject().getThemes())) {
			LinearLayout themesContainer = (LinearLayout) layout.findViewById(R.id.projectview_description_themes_container);
			themesContainer.setVisibility(View.GONE);
		} else {
			TextView themesText = (TextView) layout.findViewById(R.id.projectview_description_themes_text);
			themesText.setText(getProject().getThemes());
		}
		
		if(getProject().getProject().getObjectives().size() == 0) {
			LinearLayout objectivesContainer = (LinearLayout) layout.findViewById(R.id.projectview_description_objectives_container);
			objectivesContainer.setVisibility(View.GONE);
		} else {
			TextView objectivesText = (TextView) layout.findViewById(R.id.projectview_description_objectives_text);
			List<Objective> objectivesObject = getProject().getProject().getObjectives(); 
			String objectives = objectivesObject.get(0).getName();
			for(int i = 1; i < objectivesObject.size(); i++) {
				objectives += ", " + objectivesObject.get(i).getName();
			}
			objectivesText.setText(objectives);
		}
			
		LinearLayout questions = (LinearLayout) layout.findViewById(R.id.projectview_description_questions_container);
		for(Question question : getProject().getProject().getQuestions()) {
			if(question.getAnswer() != null) {
				LinearLayout questionLayout = (LinearLayout) inflater.inflate(R.layout.projectview_question, null);
				
				TextView questionView = (TextView) questionLayout.findViewById(R.id.projectview_question_question);
				TextView answerView = (TextView) questionLayout.findViewById(R.id.projectview_question_answer);
				
				questionView.setText(Build.VERSION.SDK_INT < 14 ? question.getQuestion().toUpperCase() : question.getQuestion());
				answerView.setText(question.getAnswer().trim());
				
				questions.addView(questionLayout);
			}
		}
		
		if(getProject().getProject().getMembers().size() == 0) {
			LinearLayout membersContainer = (LinearLayout) layout.findViewById(R.id.projectview_description_members_container);
			membersContainer.setVisibility(View.GONE);
		} else {
			LinearLayout members = (LinearLayout) layout.findViewById(R.id.projectview_description_members_text);
			for(User member : getProject().getProject().getMembers()) {
				TextView memberName = (TextView) inflater.inflate(android.R.layout.simple_list_item_1, null);
				
				memberName.setText(member.getFullname());
				memberName.setCompoundDrawablesWithIntrinsicBounds(null, null, member.getAvatarDrawable(), null);
				
				members.addView(memberName);
			}
		}
		
		return layout;
	}
	
	public void onClick(View arg0) {
		Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(getProject().getProject().getWebsite()));
		startActivity(intent);
	}
}
