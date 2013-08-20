package org.imaginationforpeople.android2.activity;

import java.util.List;

import org.imaginationforpeople.android2.R;
import org.imaginationforpeople.android2.adapter.ProjectViewAdapter;
import org.imaginationforpeople.android2.handler.ProjectViewHandler;
import org.imaginationforpeople.android2.helper.DataHelper;
import org.imaginationforpeople.android2.helper.UriHelper;
import org.imaginationforpeople.android2.model.I4pProjectTranslation;
import org.imaginationforpeople.android2.sqlite.FavoriteSqlite;
import org.imaginationforpeople.android2.thread.ProjectViewThread;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.viewpagerindicator.PageIndicator;
import com.viewpagerindicator.TitlePageIndicator;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.widget.Toast;

public class ProjectViewActivity extends SherlockFragmentActivity {
	private boolean displayMenu = false;
	private Intent shareIntent;
	private FavoriteSqlite db;
	private ProjectViewThread thread;
	private I4pProjectTranslation project;
	
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		if(displayMenu) {
			// Inflate menu only if it hasn't been done before
			if(menu.size() == 0) {
				// Inflating the menu
				MenuInflater inflater = getSupportMenuInflater();
				inflater.inflate(R.menu.projectview, menu);
				
				// Creating share intent
				Intent prepareShareIntent = new Intent(Intent.ACTION_SEND);
				prepareShareIntent.putExtra(Intent.EXTRA_TEXT, UriHelper.getProjectUrl(project));
				prepareShareIntent.putExtra(Intent.EXTRA_SUBJECT, project.getTitle());
				prepareShareIntent.setType("text/plain");
				shareIntent = Intent.createChooser(prepareShareIntent, getResources().getText(R.string.projectview_menu_share_dialog));
			}
			
			// Defining favorite state
			MenuItem favoriteItem = menu.getItem(0);
			if(db.isFavorite(project))
				favoriteItem.setTitle(R.string.projectview_menu_favorites_remove);
			else
				favoriteItem.setTitle(R.string.projectview_menu_favorites_add);
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
		case R.id.projectview_favorite:
			Toast t;
			if(db.isFavorite(project)) {
				db.removeFavorite(project);
				t = Toast.makeText(this, getResources().getString(R.string.projectview_toast_favorites_remove, project.getTitle()), Toast.LENGTH_SHORT);
			} else {
				db.addFavorite(project);
				t = Toast.makeText(this, getResources().getString(R.string.projectview_toast_favorites_add, project.getTitle()), Toast.LENGTH_SHORT);
			}
			t.show();
			break;
		case R.id.projectview_share:
			startActivity(shareIntent);
			break;
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		db = new FavoriteSqlite(this);
		
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		
		if(savedInstanceState != null && savedInstanceState.containsKey(DataHelper.PROJECT_VIEW_KEY)) {
			project = savedInstanceState.getParcelable(DataHelper.PROJECT_VIEW_KEY);
			displayProject();
		} else {
			setContentView(R.layout.loading);
			ProjectViewHandler handler = new ProjectViewHandler(this);
			
			String projectLang = null;
			String projectSlug = null;
			
			Uri data = getIntent().getData();
			if(data != null) {
				List<String> path = data.getPathSegments();
				projectLang = path.get(0);
				projectSlug = path.get(2);
			} else {
				Bundle extras = getIntent().getExtras();
				
				if(extras.containsKey("project_title"))
					setTitle(extras.getString("project_title"));
				
				if(extras.containsKey("project_id")) { // Mostly used if we want a random project
					thread = new ProjectViewThread(handler, extras.getInt("project_id"));
				} else {
					projectLang = extras.getString("project_lang");
					projectSlug = extras.getString("project_slug");
				}
			}
			
			if(thread == null)
				thread = new ProjectViewThread(handler, projectLang, projectSlug);
			
			thread.start();
		}
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		if(thread == null || !thread.isAlive())
			outState.putParcelable(DataHelper.PROJECT_VIEW_KEY, project);
		super.onSaveInstanceState(outState);
	}
	
	@Override
	protected void onStop() {
		if(thread != null)
			thread.requestStop();
		super.onStop();
	}
	
	public void setProject(I4pProjectTranslation p) {
		project = p;
	}
	
	public void displayProject() {
		setContentView(R.layout.view_root);
		displayMenu = true;
		supportInvalidateOptionsMenu(); // Rebuild the menu
		
		setTitle(project.getTitle());
		
		ProjectViewAdapter adapter = new ProjectViewAdapter(getSupportFragmentManager(), project, getResources());
		
		ViewPager pager = (ViewPager)findViewById(R.id.pager);
        pager.setAdapter(adapter);

        PageIndicator indicator = (TitlePageIndicator)findViewById(R.id.indicator);
        indicator.setViewPager(pager);
	}
}
