package application.android.marshi.papercrane.service.auth;

import android.net.Uri;
import android.support.annotation.Nullable;
import application.android.marshi.papercrane.domain.usecase.auth.AccessTokenManageUseCase;
import rx.schedulers.Schedulers;
import twitter4j.auth.AccessToken;

import javax.inject.Inject;

/**
 * @author marshi on 2016/04/17.
 */
public class AccessTokenService {

	@Inject
	AccessTokenManageUseCase accessTokenManageUseCase;

	@Inject
	public AccessTokenService() {}

	public void saveAccessToken(Uri uri) {
		accessTokenManageUseCase.start(uri)
			.subscribeOn(Schedulers.io())
			.subscribe();
	}

	@Nullable
	public AccessToken getAccessToken() {
		return accessTokenManageUseCase.getAccessToken();
	}

}
