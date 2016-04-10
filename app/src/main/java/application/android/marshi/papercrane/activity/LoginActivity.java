package application.android.marshi.papercrane.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import application.android.marshi.papercrane.R;
import application.android.marshi.papercrane.databinding.ActivityLoginBinding;
import application.android.marshi.papercrane.di.App;
import application.android.marshi.papercrane.eventbus.Event;
import application.android.marshi.papercrane.eventbus.EventBusBroker;
import application.android.marshi.papercrane.presenter.auth.TwitterAuthorizationPresenter;
import twitter4j.auth.AccessToken;

import javax.inject.Inject;


/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {

	private ActivityLoginBinding activityLoginBinding;

	@Inject
	TwitterAuthorizationPresenter authorizationPresenter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		((App)getApplication()).getApplicationComponent().inject(this);
		AccessToken accessToken = authorizationPresenter.getAccessToken();
		if (accessToken != null) {
			startActivity(new Intent(getApplicationContext(), MainTimelineActivity.class));
		}
		activityLoginBinding =  DataBindingUtil.setContentView(this, R.layout.activity_login);
		Button mEmailSignInButton = activityLoginBinding.emailSignInButton;
		EventBusBroker.stringEventBus.get(Event.LoginAuthorization).subscribe(
				authUrl -> startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(authUrl)))
		);
		mEmailSignInButton.setOnClickListener(view -> {
			progress();
			authorizationPresenter.oAuthLogin();
		});
	}

	@Override
	public void onStart() {
		super.onStart();
	}

	@Override
	public void onStop() {
		super.onStop();
	}

	private void progress() {
		View progressView = activityLoginBinding.loginProgress;
		View mLoginFormView = activityLoginBinding.loginForm;
		int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

		mLoginFormView.setVisibility(View.GONE);
		mLoginFormView.animate().setDuration(shortAnimTime).alpha(0)
				.setListener(new AnimatorListenerAdapter() {
					@Override
					public void onAnimationEnd(Animator animation) {
						mLoginFormView.setVisibility(View.GONE);
					}
				});

		progressView.setVisibility(View.VISIBLE);
		progressView.animate().setDuration(shortAnimTime).alpha(1)
				.setListener(new AnimatorListenerAdapter() {
					@Override
					public void onAnimationEnd(Animator animation) {
						progressView.setVisibility(View.VISIBLE);
					}
				});
	}

}

