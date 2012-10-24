package org.imaginationforpeople.android.projectview;

import java.util.List;

import org.imaginationforpeople.android.R;
import org.imaginationforpeople.android.model.Picture;

import com.viewpagerindicator.UnderlinePageIndicator;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.MenuItem;

public class FullImageProjectViewActivity extends FragmentActivity {
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
		setContentView(R.layout.projectview_gallery_full);
		
		if(Build.VERSION.SDK_INT >= 11)
			getActionBar().setDisplayHomeAsUpEnabled(true);
		
		Bundle extras = getIntent().getExtras();
		List<Picture> pictures = extras.getParcelableArrayList("pictures");
		int current = extras.getInt("current");
		
		FullImageProjectViewAdapter adapter = new FullImageProjectViewAdapter(getSupportFragmentManager(), pictures);
		
		ViewPager pager = (ViewPager) findViewById(R.id.pager);
		pager.setAdapter(adapter);
		
		UnderlinePageIndicator indicator = (UnderlinePageIndicator) findViewById(R.id.indicator);
		indicator.setViewPager(pager);
		indicator.setCurrentItem(current);
	}
}
