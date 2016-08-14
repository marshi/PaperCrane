package application.android.marshi.papercrane.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import application.android.marshi.papercrane.R;

public class TweetEditorActivity extends AppCompatActivity {

	public static void startActivity(Context context, Intent intent) {
		intent.setClass(context, TweetEditorActivity.class);
		context.startActivity(intent);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tweet_editor);
	}

}
