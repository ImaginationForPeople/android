package org.imaginationforpeople.android2.groupview;

import org.imaginationforpeople.android2.R;
import org.imaginationforpeople.android2.helper.DataHelper;
import org.imaginationforpeople.android2.model.Group;

import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;

public class InfoGroupViewFragment extends SherlockFragment {
	private Group group;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		group = getArguments().getParcelable(DataHelper.GROUP_VIEW_KEY);
		ScrollView layout = (ScrollView) inflater.inflate(R.layout.groupview_info, null);

		LinearLayout overlay = (LinearLayout) layout.findViewById(R.id.groupview_description_overlay);
		overlay.getBackground().setAlpha(127);

		if(group.getImageUrl() != null) {
			ImageView image = (ImageView) layout.findViewById(R.id.groupview_description_image);
			image.setImageBitmap(group.getImageBitmap());
		}

		TextView title = (TextView) layout.findViewById(R.id.groupview_description_title);
		title.setText(group.getName());

		TextView baseline = (TextView) layout.findViewById(R.id.groupview_description_baseline);
		baseline.setText(group.getDescription());

		if(group.getTags().isEmpty()) {
			LinearLayout themesContainer = (LinearLayout) layout.findViewById(R.id.groupview_description_themes_container);
			themesContainer.setVisibility(View.GONE);
		} else {
			StringBuilder themesBuilder = new StringBuilder();
			themesBuilder.append(group.getTags().get(0));

			for(int i = 1; i < group.getTags().size(); i++) {
				themesBuilder.append(", ");
				themesBuilder.append(group.getTags().get(i));
			}
			TextView themesText = (TextView) layout.findViewById(R.id.groupview_description_themes_text);
			themesText.setText(themesBuilder);
		}

		if(group.getArticle() == null || "".equals(group.getArticle())) {
			LinearLayout aboutContainer = (LinearLayout) layout.findViewById(R.id.groupview_description_about_container);
			aboutContainer.setVisibility(View.GONE);
		} else {
			TextView about = (TextView) layout.findViewById(R.id.groupview_description_about_text);
			about.setText(Html.fromHtml(group.getArticle().trim()));
			about.setMovementMethod(LinkMovementMethod.getInstance());
		}

		return layout;
	}
}
