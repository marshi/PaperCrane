package application.android.marshi.papercrane.domain.spec;

import android.widget.TextView;

/**
 * @author marshi on 2016/07/19.
 */
public class Spec {

	public static boolean notTapWebLink(TextView textView){
		return textView.getSelectionStart() == -1 && textView.getSelectionEnd() == -1;
	}

}
