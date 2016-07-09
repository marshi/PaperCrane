package application.android.marshi.papercrane.service;

import application.android.marshi.papercrane.domain.usecase.toast.ToastUseCase;

import javax.inject.Inject;

/**
 * @author marshi on 2016/07/09.
 */
public class ToastService {

	@Inject
	public ToastService() {}

	@Inject
	ToastUseCase toastUseCase;

	public void showToast(String text, int duration) {
		toastUseCase.start(new ToastUseCase.ToastRequest(text, duration));
	}

}
