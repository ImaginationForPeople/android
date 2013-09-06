package org.imaginationforpeople.android2.activity;

import java.util.List;

import org.imaginationforpeople.android2.R;
import org.imaginationforpeople.android2.adapter.GroupViewAdapter;
import org.imaginationforpeople.android2.handler.GroupViewHandler;
import org.imaginationforpeople.android2.helper.DataHelper;
import org.imaginationforpeople.android2.helper.UriHelper;
import org.imaginationforpeople.android2.model.Group;
import org.imaginationforpeople.android2.thread.GroupViewThread;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.viewpagerindicator.PageIndicator;
import com.viewpagerindicator.TitlePageIndicator;

import android.annotation.TargetApi;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.ViewPager;

public class GroupViewActivity extends SherlockFragmentActivity {
	private boolean displayMenu = false;
	private Intent shareIntent;
	private GroupViewThread thread;
	private Group group;
	
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		if(displayMenu) {
			// Inflate menu only if it hasn't been done before
			if(menu.size() == 0) {
				// Inflating the menu
				MenuInflater inflater = getSupportMenuInflater();
				inflater.inflate(R.menu.groupview, menu);
				
				// Creating share intent
				Intent prepareShareIntent = new Intent(Intent.ACTION_SEND);
				prepareShareIntent.putExtra(Intent.EXTRA_TEXT, UriHelper.getGroupUrl(group));
				prepareShareIntent.putExtra(Intent.EXTRA_SUBJECT, group.getName());
				prepareShareIntent.setType("text/plain");
				shareIntent = Intent.createChooser(prepareShareIntent, getResources().getText(R.string.groupview_menu_share_dialog));
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
		case R.id.groupview_share:
			startActivity(shareIntent);
			break;
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		
		if(savedInstanceState != null && savedInstanceState.containsKey(DataHelper.GROUP_VIEW_KEY)) {
			group = savedInstanceState.getParcelable(DataHelper.GROUP_VIEW_KEY);
			displayGroup();
		} else {
			setContentView(R.layout.loading);
			GroupViewHandler handler = new GroupViewHandler(this);
			
			String groupSlug = null;
			
			Uri data = getIntent().getData();
			if(data != null) {
				List<String> path = data.getPathSegments();
				groupSlug = path.get(2);
			} else {
				Bundle extras = getIntent().getExtras();
				
				if(extras.containsKey("group_title"))
					setTitle(extras.getString("group_title"));
				
				groupSlug = extras.getString("group_slug");
			}
			
			if(thread == null)
				thread = new GroupViewThread(handler, groupSlug);
			
			thread.start();
		}
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		if(thread == null || !thread.isAlive())
			outState.putParcelable(DataHelper.GROUP_VIEW_KEY, group);
		super.onSaveInstanceState(outState);
	}
	
	@Override
	protected void onStop() {
		if(thread != null)
			thread.requestStop();
		super.onStop();
	}
	
	public void setGroup(Group g) {
		group = g;
	}
	
	@TargetApi(11)
	public void displayGroup() {
		setContentView(R.layout.view_root);
		displayMenu = true;
		supportInvalidateOptionsMenu(); // Rebuild the menu
		
		setTitle(group.getName());
		
		GroupViewAdapter adapter = new GroupViewAdapter(getSupportFragmentManager(), group, getResources());
		
		ViewPager pager = (ViewPager)findViewById(R.id.pager);
        pager.setAdapter(adapter);

        PageIndicator indicator = (TitlePageIndicator)findViewById(R.id.indicator);
        indicator.setViewPager(pager);
	}
}
