package application.android.marshi.papercrane.activity;

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
		App.getApplicationComponent().inject(this);
		boolean login = login();
		if (login) {
			HomeTimelineActivity.startActivity(this);
		} else {
			LoginActivity.startActivity(this);
		}
	}

	private boolean login() {
		return accessTokenService.getAccessToken() != null;
	}



}
