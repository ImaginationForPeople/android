package org.imaginationforpeople.android2.activity;

import java.util.ArrayList;

import org.imaginationforpeople.android2.R;
import org.imaginationforpeople.android2.handler.SearchHandler;
import org.imaginationforpeople.android2.model.I4pProjectTranslation;
import org.imaginationforpeople.android2.thread.SearchThread;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.widget.SearchView;

public class SearchActivity extends SherlockActivity implements OnItemClickListener {
	private ArrayList<I4pProjectTranslation> projects;
	private String search;

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getSupportMenuInflater();
		inflater.inflate(R.menu.search, menu);

		SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
		SearchView searchView = (SearchView) menu.findItem(R.id.search_search).getActionView();
		searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
		searchView.setIconifiedByDefault(true);
		searchView.setQuery(search, false);

		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()) {
		case android.R.id.home:
			finish();
			break;
		case R.id.search_search:
			onSearchRequested();
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onSearchRequested() {
		startSearch(search, false, null, false);
		return false;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if(Intent.ACTION_VIEW.equals(getIntent().getAction())) {
			openProject();
			finish();
		} else {
			if(savedInstanceState != null)
				search = savedInstanceState.getString("search");
			else
				search = getIntent().getStringExtra(SearchManager.QUERY);
			setTitle(getResources().getString(R.string.search_searching, search));

			getSupportActionBar().setDisplayHomeAsUpEnabled(true);

			if(savedInstanceState != null)
				projects = savedInstanceState.getParcelableArrayList("results");
			if(projects == null || projects.size() == 0)
				handleSearch();
			else
				displayResults();
		}
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
		if(Intent.ACTION_VIEW.equals(intent.getAction()))
			openProject();
		else {
			search = intent.getStringExtra(SearchManager.QUERY);
			setTitle(getResources().getString(R.string.search_searching, search));
			supportInvalidateOptionsMenu();
			handleSearch();
		}
	}

	private void openProject() {
		Intent intent = new Intent(this, ProjectViewActivity.class);
		String[] data = getIntent().getDataString().split("/", 2);
		intent.putExtra("project_lang", data[0]);
		intent.putExtra("project_slug", data[1]);
		intent.putExtra("project_title", getIntent().getStringExtra(SearchManager.EXTRA_DATA_KEY));
		startActivity(intent);
	}

	private void handleSearch() {
		setContentView(R.layout.loading);

		projects = new ArrayList<I4pProjectTranslation>();
		SearchHandler handler = new SearchHandler(this);
		SearchThread thread = new SearchThread(search, projects, handler);
		thread.start();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putString("search", search);
		outState.putParcelableArrayList("results", projects);
	}

	public void displayResults() {
		setContentView(R.layout.search);

		ArrayAdapter<I4pProjectTranslation> adapter = new ArrayAdapter<I4pProjectTranslation>(this, android.R.layout.simple_list_item_1, projects);

		ListView list = (ListView) findViewById(android.R.id.list);
		list.setAdapter(adapter);
		list.setOnItemClickListener(this);
	}

	@Override
	public void onItemClick(AdapterView<?> l, View v, int position, long id) {
		I4pProjectTranslation project = projects.get(position);

		Intent intent = new Intent(this, ProjectViewActivity.class);
		intent.putExtra("project_lang", project.getLanguageCode());
		intent.putExtra("project_slug", project.getSlug());
		intent.putExtra("project_title", project.getTitle());
		startActivity(intent);
	}
}
