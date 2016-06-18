package application.android.marshi.papercrane;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.ViewGroup;
import application.android.marshi.papercrane.databinding.TweetItemBinding;

/**
 * @author marshi on 2016/06/13.
 */
public class LoadCellBindingHolder extends BindingHolder<TweetItemBinding> {

	public LoadCellBindingHolder(@NonNull Context context, @NonNull ViewGroup parent, @LayoutRes int layoutResId) {
		super(context, parent, layoutResId);
	}

}
