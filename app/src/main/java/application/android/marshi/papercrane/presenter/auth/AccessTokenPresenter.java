package application.android.marshi.papercrane.presenter.auth;

import android.net.Uri;
import application.android.marshi.papercrane.domain.usecase.auth.AccessTokenManageUseCase;

import javax.inject.Inject;

/**
 * @author marshi on 2016/04/17.
 */
public class AccessTokenPresenter {

	@Inject
	AccessTokenManageUseCase accessTokenManageUseCase;

	@Inject
	public AccessTokenPresenter() {}

	public void saveAccessToken(Uri uri) {
		accessTokenManageUseCase.start(uri);
	}

}
