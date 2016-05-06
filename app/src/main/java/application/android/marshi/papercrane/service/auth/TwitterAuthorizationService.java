package application.android.marshi.papercrane.service.auth;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import application.android.marshi.papercrane.domain.usecase.auth.AuthorizationUseCase;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import javax.inject.Inject;

/**
 * @author marshi on 2016/04/16.
 */
public class TwitterAuthorizationService {

	@Inject
	AuthorizationUseCase authorizationUseCase;

	@Inject
	public TwitterAuthorizationService() {}

	public void oAuthLogin(Activity activity) {
		authorizationUseCase.start(null)
				.observeOn(AndroidSchedulers.mainThread())
				.subscribeOn(Schedulers.computation())
				.subscribe(authUrl -> activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(authUrl))));
	}

}
