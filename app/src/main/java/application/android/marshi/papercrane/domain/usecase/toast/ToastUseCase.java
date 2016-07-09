package application.android.marshi.papercrane.domain.usecase.toast;

import android.content.Context;
import android.widget.Toast;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.inject.Inject;

/**
 * @author marshi on 2016/07/09.
 */
public class ToastUseCase {

	private Context context;

	@Inject
	public ToastUseCase(Context context) {
		this.context = context;
	}

	public void start(ToastRequest param) {
		if (param.getDuration() == Toast.LENGTH_SHORT) {
			Toast.makeText(context, param.getText(), Toast.LENGTH_SHORT).show();
		} else {
			Toast.makeText(context, param.getText(), Toast.LENGTH_LONG).show();
		}
	}

	@AllArgsConstructor
	@Data
	public static class ToastRequest {
		private String text;

		/**
		 * duration of Toast
		 * {@link Toast#LENGTH_LONG}, {@link Toast#LENGTH_SHORT}
		 */
		private int duration;
	}

}
