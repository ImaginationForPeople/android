package org.imaginationforpeople.android.activity;

import java.util.ArrayList;

import org.imaginationforpeople.android.R;
import org.imaginationforpeople.android.model.I4pProjectTranslation;
import org.imaginationforpeople.android.sqlite.FavoriteSqlite;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class FavoritesActivity extends ListActivity implements OnClickListener {
	private FavoriteSqlite db;
	private ArrayAdapter<I4pProjectTranslation> adapter;
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.favorites, menu);
		return super.onCreateOptionsMenu(menu);
	}
	
	@TargetApi(11)
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()) {
		case android.R.id.home:
			finish();
			break;
		case R.id.favorites_remove_all:
			AlertDialog alert = new AlertDialog.Builder(this).create();
			alert.setTitle(R.string.favorites_dialog_remove_all_title);
			if(Build.VERSION.SDK_INT >= 11)
				alert.setIconAttribute(android.R.attr.alertDialogIcon);
			else
				alert.setIcon(android.R.drawable.ic_dialog_alert);
			alert.setMessage(getResources().getString(R.string.cant_be_undone));
			alert.setButton(AlertDialog.BUTTON_NEGATIVE, getResources().getString(android.R.string.no), this);
			alert.setButton(AlertDialog.BUTTON_POSITIVE, getResources().getString(android.R.string.yes), this);
			alert.show();
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
		if(!db.hasFavorites())
			noMoreFavorites();
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
	
	public void onClick(DialogInterface dialog, int which) {
		switch(which) {
		case AlertDialog.BUTTON_POSITIVE:
			db.removeAllFavorites();
			noMoreFavorites();
			break;
		}
	}
	
	private void noMoreFavorites() {
		Toast t = Toast.makeText(this, R.string.favorites_no_anymore, Toast.LENGTH_SHORT);
		t.show();
		finish();
	}
}
