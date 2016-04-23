package application.android.marshi.papercrane.presenter.auth;

import android.support.annotation.Nullable;
import application.android.marshi.papercrane.domain.usecase.auth.AccessTokenManageUseCase;
import application.android.marshi.papercrane.domain.usecase.auth.AuthorizationUseCase;
import application.android.marshi.papercrane.presenter.Presenter;
import twitter4j.auth.AccessToken;

import javax.inject.Inject;

/**
 * @author marshi on 2016/04/16.
 */
public class TwitterAuthorizationPresenter extends Presenter {

	@Inject
	AuthorizationUseCase authorizationUseCase;

	@Inject
	AccessTokenManageUseCase accessTokenManageUseCase;

	@Inject
	public TwitterAuthorizationPresenter() {}

	public void oAuthLogin() {
		authorizationUseCase.start(null);
	}

	@Nullable
	public AccessToken getAccessToken() {
		return accessTokenManageUseCase.getAccessToken();
	}

}
