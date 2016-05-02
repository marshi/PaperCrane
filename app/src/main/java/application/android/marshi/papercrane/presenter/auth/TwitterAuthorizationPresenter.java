package application.android.marshi.papercrane.presenter.auth;

import application.android.marshi.papercrane.domain.usecase.auth.AuthorizationUseCase;
import application.android.marshi.papercrane.presenter.Presenter;

import javax.inject.Inject;

/**
 * @author marshi on 2016/04/16.
 */
public class TwitterAuthorizationPresenter extends Presenter {

	@Inject
	AuthorizationUseCase authorizationUseCase;

	@Inject
	public TwitterAuthorizationPresenter() {}

	public void oAuthLogin() {
		authorizationUseCase.start(null);
	}

}
