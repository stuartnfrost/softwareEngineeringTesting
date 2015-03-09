package com.example.stuart.testingremotedatasource;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

/**
 * Created by Stuart on 06/03/2015.
 */
public class ActivityItem extends Activity {

	// XML node keys
	static final String KEY_TITLE = "title";
	static final String KEY_TIME = "time";
	static final String KEY_LOCATION = "location";
	static final String KEY_DESCRIPTION = "description";
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.single_list_item);

		// getting intent data
		Intent in = getIntent();

		// Get XML values from previous intent
		String title = in.getStringExtra(KEY_TITLE);
		String time = in.getStringExtra(KEY_TIME);
		String location = in.getStringExtra(KEY_LOCATION);
		String description = in.getStringExtra(KEY_DESCRIPTION);

		// Displaying all values on the screen
		TextView lblTitle = (TextView) findViewById(R.id.name_label);
		TextView lblTime = (TextView) findViewById(R.id.time_label);
		TextView lblLocation = (TextView) findViewById(R.id.location_label);
		TextView lblDescription = (TextView) findViewById(R.id.description_label);

		lblTitle.setText(title);
		lblTime.setText(time);
		lblLocation.setText(location);
		lblDescription.setText(description);
	}



}
