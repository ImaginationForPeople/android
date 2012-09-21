package org.imaginationforpeople.android.activity;

import java.util.ArrayList;

import org.imaginationforpeople.android.model.I4pProjectTranslation;
import org.imaginationforpeople.android.sqlite.FavoriteSqlite;

import android.annotation.TargetApi;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class FavoritesActivity extends ListActivity {
	private FavoriteSqlite db;
	private ArrayAdapter<I4pProjectTranslation> adapter;
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()) {
		case android.R.id.home:
			finish();
			break;
		}
		return super.onOptionsItemSelected(item);
	}
	
	@TargetApi(11)
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		db = new FavoriteSqlite(this);
		
		adapter = new ArrayAdapter<I4pProjectTranslation>(this, android.R.layout.simple_list_item_1, new ArrayList<I4pProjectTranslation>());
		setListAdapter(adapter);
		
		if(Build.VERSION.SDK_INT >= 11)
			getActionBar().setDisplayHomeAsUpEnabled(true);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		adapter.clear();
		for(I4pProjectTranslation project : db.getFavorites())
			adapter.add(project);
	}
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		
		I4pProjectTranslation project = adapter.getItem(position);
		Intent intent = new Intent(this, ProjectViewActivity.class);
		intent.putExtra("project_lang", project.getLanguageCode());
		intent.putExtra("project_slug", project.getSlug());
		intent.putExtra("project_title", project.getTitle());
		startActivity(intent);
	}
}
