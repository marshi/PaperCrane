package application.android.marshi.papercrane.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import application.android.marshi.papercrane.di.App;
import application.android.marshi.papercrane.service.auth.AccessTokenService;

import javax.inject.Inject;

public class MainActivity extends AppCompatActivity {

	@Inject
	AccessTokenService accessTokenService;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		((App)getApplication()).getApplicationComponent().inject(this);
		boolean login = login();
		if (login) {
			Intent intent = new Intent(getApplicationContext(), HomeTimelineActivity.class);
			startActivity(intent);
		} else {
			Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
			startActivity(intent);
		}
	}

	private boolean login() {
		return accessTokenService.getAccessToken() != null;
	}



}
