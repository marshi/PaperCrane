package application.android.marshi.papercrane.service;

import android.content.Context;
import android.widget.Toast;

import javax.inject.Inject;

/**
 * @author marshi on 2016/07/09.
 */
public class ToastService {

	private Context context;

	@Inject
	public ToastService(Context context) {
		this.context = context;
	}

	public void showToast(String text, int duration) {
		if (duration == Toast.LENGTH_SHORT) {
			Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
		} else {
			Toast.makeText(context, text, Toast.LENGTH_LONG).show();
		}
	}

}
