package org.imaginationforpeople.android2.fragment;

import java.util.ArrayList;

import org.imaginationforpeople.android2.R;
import org.imaginationforpeople.android2.activity.ProjectViewActivity;
import org.imaginationforpeople.android2.model.I4pProjectTranslation;
import org.imaginationforpeople.android2.sqlite.FavoriteSqlite;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class FavoritesFragment extends Fragment implements OnItemClickListener, OnClickListener {
	private FavoriteSqlite db;
	private ArrayAdapter<I4pProjectTranslation> adapter;
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.favorites, menu);
	}
	
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()) {
		case R.id.favorites_remove_all:
			AlertDialog alert = new AlertDialog.Builder(getActivity()).create();
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
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
		getActivity().setTitle(R.string.app_favorites);
		db = new FavoriteSqlite(getActivity());
		adapter = new ArrayAdapter<I4pProjectTranslation>(getActivity(), android.R.layout.simple_list_item_1, new ArrayList<I4pProjectTranslation>());
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.favorites, container, false);
		ListView list = (ListView) view.findViewById(R.id.favorites_list);
		list.setAdapter(adapter);
		list.setOnItemClickListener(this);
		return view;
	}
	
	@Override
	public void onResume() {
		super.onResume();
		
		if(db.getFavorites().size() == 0) {
			noFavorites();
		} else {
			adapter.clear();
			for(I4pProjectTranslation project : db.getFavorites())
				adapter.add(project);
		}
	}
	
	@Override
	public void onItemClick(AdapterView<?> l, View v, int position, long id) {
		I4pProjectTranslation project = adapter.getItem(position);
		Intent intent = new Intent(getActivity(), ProjectViewActivity.class);
		intent.putExtra("project_lang", project.getLanguageCode());
		intent.putExtra("project_slug", project.getSlug());
		intent.putExtra("project_title", project.getTitle());
		startActivity(intent);
	}
	
	@Override
	public void onClick(DialogInterface dialog, int which) {
		switch(which) {
		case AlertDialog.BUTTON_POSITIVE:
			db.removeAllFavorites();
			TextView text = (TextView) getView().findViewById(R.id.favorites_empty_text);
			text.setText(R.string.favorites_no_anymore);
			noFavorites();
			break;
		}
	}
	
	private void noFavorites() {
		ListView list = (ListView) getView().findViewById(R.id.favorites_list);
		list.setVisibility(View.GONE);
		RelativeLayout emptyContainer = (RelativeLayout) getView().findViewById(R.id.favorites_empty_container);
		emptyContainer.setVisibility(View.VISIBLE);
	}
}
