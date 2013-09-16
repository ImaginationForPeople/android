package org.imaginationforpeople.android2.activity;

import java.util.ArrayList;

import org.imaginationforpeople.android2.R;
import org.imaginationforpeople.android2.adapter.DrawerAdapter;
import org.imaginationforpeople.android2.fragment.FavoritesFragment;
import org.imaginationforpeople.android2.fragment.GroupListFragment;
import org.imaginationforpeople.android2.fragment.LoadingFragment;
import org.imaginationforpeople.android2.fragment.ProjectListFragment;
import org.imaginationforpeople.android2.helper.DataHelper;
import org.imaginationforpeople.android2.helper.LanguageHelper;
import org.imaginationforpeople.android2.model.Group;
import org.imaginationforpeople.android2.model.I4pProjectTranslation;
import org.imaginationforpeople.android2.shake.ShakeAnimation;
import org.imaginationforpeople.android2.shake.ShakeAnimation.AnimationListener;
import org.imaginationforpeople.android2.shake.ShakeEventListener;

import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ListView;
import android.widget.SpinnerAdapter;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.OnNavigationListener;
import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.widget.SearchView;

public class HomepageActivity extends SherlockFragmentActivity implements OnClickListener,
OnCancelListener, ShakeEventListener.ShakeListener, AnimationListener,
OnCheckedChangeListener, LoadingFragment.OnContentLoadedListener,
OnNavigationListener, OnItemClickListener {
	public final static String STATE_KEY = "selected";

	private final ArrayList<ArrayList<I4pProjectTranslation>> projects = new ArrayList<ArrayList<I4pProjectTranslation>>(DataHelper.CONTENT_NUMBER);
	private ArrayList<Group> groups = new ArrayList<Group>();
	private AlertDialog languagesDialog;
	private SharedPreferences preferences;
	private AlertDialog contentDialog;
	private ShakeEventListener shaker;
	private ShakeAnimation animation;
	private SearchView searchView;
	private DrawerLayout drawer;
	private ListView drawerContent;
	private ActionBarDrawerToggle drawerToggle;
	private int activeDrawerItem = DrawerAdapter.DRAWER_PROJECTS_ITEM;
	private boolean isStopping = false;

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getSupportMenuInflater();
		switch(activeDrawerItem) {
		case DrawerAdapter.DRAWER_PROJECTS_ITEM:
			inflater.inflate(R.menu.projectslist, menu);

			SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
			searchView = (SearchView) menu.findItem(R.id.homepage_search).getActionView();
			searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
			searchView.setIconifiedByDefault(true);
			break;
		}

		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()) {
		case android.R.id.home:
			if(drawer.isDrawerOpen(drawerContent))
				drawer.closeDrawer(drawerContent);
			else
				drawer.openDrawer(drawerContent);
			break;
		case R.id.homepage_content:
			AlertDialog.Builder contentBuilder = new AlertDialog.Builder(this);
			contentBuilder.setTitle(R.string.homepage_spinner_content_prompt);
			contentBuilder.setSingleChoiceItems(R.array.homepage_spinner_dropdown, getSupportActionBar().getSelectedNavigationIndex(), this);
			contentDialog = contentBuilder.create();
			contentDialog.show();
			break;
		case R.id.homepage_search:
			onSearchRequested();
			break;
		case R.id.homepage_lang:
			languagesDialog.show();
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.homepage);
		// -- Initializing application
		preferences = getPreferences(Context.MODE_PRIVATE);
		LanguageHelper.setSharedPreferences(preferences);

		// -- Initializing ribbon menu
		drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
		drawerContent = (ListView) findViewById(R.id.left_drawer);

		drawer.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
		drawerContent.setAdapter(new DrawerAdapter(this));
		drawerContent.setOnItemClickListener(this);

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);

		drawerToggle = new ActionBarDrawerToggle(this, drawer, R.drawable.ic_drawer, R.string.drawer_open, R.string.drawer_close);
		drawer.setDrawerListener(drawerToggle);

		drawerContent.setItemChecked(0, true);

		// -- Initializing projects list
		if(savedInstanceState != null) {
			for(int i = 0; i < DataHelper.CONTENT_NUMBER; i++) {
				ArrayList<I4pProjectTranslation> project = savedInstanceState.getParcelableArrayList("projects_" + String.valueOf(i));
				projects.add(i, project);
			}
			ArrayList<Group> groups = savedInstanceState.getParcelableArrayList("groups");
			this.groups = groups;
			selectDrawerItem(savedInstanceState.getInt("drawer_item"));
		} else {
			for(int i = 0; i < DataHelper.CONTENT_NUMBER; i++) {
				projects.add(i, new ArrayList<I4pProjectTranslation>());
			}
			selectDrawerItem(DrawerAdapter.DRAWER_PROJECTS_ITEM);
		}

		if(drawerContent.getSelectedItemPosition() == DrawerAdapter.DRAWER_PROJECTS_ITEM) {
			initActionBarSpinner();
			if(savedInstanceState != null && savedInstanceState.containsKey(STATE_KEY))
				getSupportActionBar().setSelectedNavigationItem(savedInstanceState.getInt(STATE_KEY));
		}

		// -- Initializing language chooser UI
		int selectedLanguage = LanguageHelper.getPreferredLanguageInt();

		AlertDialog.Builder languagesBuilder = new AlertDialog.Builder(this);
		languagesBuilder.setTitle(R.string.homepage_spinner_language_prompt);
		languagesBuilder.setSingleChoiceItems(R.array.homepage_spinner_languages, selectedLanguage, this);
		languagesDialog = languagesBuilder.create();

		// -- Initializing shaker
		shaker = new ShakeEventListener(this);
		if(Build.VERSION.SDK_INT >= 12)
			animation = new ShakeAnimation(findViewById(R.id.homepage_shake), this);
		else
			animation = new ShakeAnimation(this, findViewById(R.id.homepage_shake), this);
	}

	@Override
	protected void onRestart() {
		super.onRestart();
		if(searchView != null) {
			// Calling twice: first empty text field, second iconify the view
			searchView.setIconified(true);
			searchView.setIconified(true);
		}
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		isStopping = true;
		for(int i = 0; i < DataHelper.CONTENT_NUMBER; i++)
			outState.putParcelableArrayList("projects_" + String.valueOf(i), projects.get(i));
		outState.putParcelableArrayList("groups", groups);
		outState.putInt(STATE_KEY, getSupportActionBar().getSelectedNavigationIndex());
		outState.putInt("drawer_item", activeDrawerItem);
		super.onSaveInstanceState(outState);
	}

	@Override
	protected void onPause() {
		shaker.unregisterListener();
		super.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();
		shaker.registerListener();
	}

	@Override
	protected void onStop() {
		super.onStop();
		if(languagesDialog.isShowing())
			languagesDialog.cancel();
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		drawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		drawerToggle.onConfigurationChanged(newConfig);
	}

	@Override
	public void onClick(DialogInterface dialog, int which) {
		if(which == DialogInterface.BUTTON_NEGATIVE) {
			finish();
		} else if(which != LanguageHelper.getPreferredLanguageInt()) {
			Editor editor = preferences.edit();
			editor.putInt("language", which);
			editor.commit();
			resetProjects();
		}
		dialog.dismiss();
	}

	@Override
	public void onCancel(DialogInterface arg0) {
		finish();
	}

	public void resetProjects() {
		for(ArrayList<I4pProjectTranslation> array : projects)
			array.clear();
		onNavigationItemSelected(0, getSupportActionBar().getSelectedNavigationIndex());
	}

	@Override
	public void onShake() {
		Intent intent = new Intent(this, ProjectViewActivity.class);
		intent.putExtra("project_id", DataHelper.PROJECT_RANDOM);
		startActivity(intent);
	}

	@Override
	public void onLittleShake() {
		if(preferences.getBoolean("shake_hint", true))
			animation.showAnimation();
	}

	@Override
	public void onClick() {
		animation.hideAnimation();

		View shakeLayout = getLayoutInflater().inflate(R.layout.shake_dialog, null);
		CheckBox shakeCheckbox = (CheckBox) shakeLayout.findViewById(R.id.shake_checkbox);
		shakeCheckbox.setOnCheckedChangeListener(this);

		AlertDialog.Builder shakeAlert = new AlertDialog.Builder(this);
		shakeAlert.setIcon(android.R.drawable.ic_dialog_info);
		shakeAlert.setTitle(R.string.shake_dialog_title);
		shakeAlert.setView(shakeLayout);
		shakeAlert.setPositiveButton(android.R.string.ok, null);
		shakeAlert.create().show();
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		Editor editor = preferences.edit();
		editor.putBoolean("shake_hint", !isChecked);
		editor.commit();
	}

	@Override
	public void onContentLoaded(int contentType, Bundle bundle) {
		if(isStopping)
			return;
		SherlockFragment fragment = null;
		switch(contentType) {
		case LoadingFragment.LOAD_BESTOF_PROJECTS:
		case LoadingFragment.LOAD_LATEST_PROJECTS:
		case LoadingFragment.LOAD_MYCOUNTRY_PROJECTS:
			ArrayList<I4pProjectTranslation> projectsList = bundle.getParcelableArrayList(ProjectListFragment.PROJECTS_KEY);
			projects.set(contentType, projectsList);
			fragment = new ProjectListFragment();
			fragment.setArguments(bundle);
			break;
		case LoadingFragment.LOAD_GROUPS:
			groups = bundle.getParcelableArrayList(GroupListFragment.GROUPS_KEY);
			fragment = new GroupListFragment();
			fragment.setArguments(bundle);
		}
		FragmentManager fm = getSupportFragmentManager();
		if(isStopping)
			return;
		fm.beginTransaction().replace(R.id.homepage_content, fragment).commit();
	}

	@Override
	public boolean onNavigationItemSelected(int itemPosition, long itemId) {
		int id = (int) itemId;
		setTitle("");

		Bundle data = new Bundle();
		data.putParcelableArrayList(ProjectListFragment.PROJECTS_KEY, projects.get(id));
		SherlockFragment fragment;
		if(projects.get(id).size() > 0)
			fragment = new ProjectListFragment();
		else {
			fragment = new LoadingFragment();
			data.putInt(LoadingFragment.CONTENT_TO_LOAD, id);
			switch(id) {
			case LoadingFragment.LOAD_MYCOUNTRY_PROJECTS:
				data.putInt(LoadingFragment.TEXT_RESID, R.string.loading_location);
				break;
			case LoadingFragment.LOAD_BESTOF_PROJECTS:
			case LoadingFragment.LOAD_LATEST_PROJECTS:
				data.putInt(LoadingFragment.TEXT_RESID, R.string.loading_projects);
				break;
			}
		}
		fragment.setArguments(data);
		FragmentManager fm = getSupportFragmentManager();
		fm.beginTransaction().replace(R.id.homepage_content, fragment).commit();
		return true;
	}

	private void initActionBarSpinner() {
		SpinnerAdapter spinner = ArrayAdapter.createFromResource(this, R.array.homepage_spinner_dropdown, com.actionbarsherlock.R.layout.sherlock_spinner_dropdown_item);

		getSupportActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
		getSupportActionBar().setListNavigationCallbacks(spinner, this);
	}

	private void selectDrawerItem(int position) {
		activeDrawerItem = position;
		SherlockFragment fragment;
		FragmentManager fm = getSupportFragmentManager();
		switch(position) {
		case DrawerAdapter.DRAWER_PROJECTS_ITEM:
			initActionBarSpinner();
			onNavigationItemSelected(0, getSupportActionBar().getSelectedNavigationIndex());

			supportInvalidateOptionsMenu();
			break;
		case DrawerAdapter.DRAWER_GROUPS_ITEM:
			setTitle(R.string.app_groups);
			getSupportActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
			supportInvalidateOptionsMenu();
			Bundle data = new Bundle();
			if(groups.size() > 0) {
				data.putParcelableArrayList(GroupListFragment.GROUPS_KEY, groups);
				fragment = new GroupListFragment();
				fragment.setArguments(data);
				fm.beginTransaction().replace(R.id.homepage_content, fragment).commit();
			} else {
				data.putInt(LoadingFragment.CONTENT_TO_LOAD, LoadingFragment.LOAD_GROUPS);
				data.putInt(LoadingFragment.TEXT_RESID, R.string.loading_groups);
				fragment = new LoadingFragment();
				fragment.setArguments(data);
				fm.beginTransaction().replace(R.id.homepage_content, fragment).commit();
			}
			break;
		case DrawerAdapter.DRAWER_FAVORITES_ITEM:
			getSupportActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
			fragment = new FavoritesFragment();
			fm.beginTransaction().replace(R.id.homepage_content, fragment).commit();
			break;
		}
		drawerContent.setItemChecked(position, true);
		drawer.closeDrawer(drawerContent);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		selectDrawerItem(position);
	}
}
