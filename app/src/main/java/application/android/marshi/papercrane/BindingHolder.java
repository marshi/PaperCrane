package application.android.marshi.papercrane;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

/**
 * @author marshi on 2016/04/04.
 */
public class BindingHolder<T extends ViewDataBinding> extends RecyclerView.ViewHolder {

	public final T binding;

	public BindingHolder(@NonNull Context context, @NonNull ViewGroup parent, @LayoutRes int layoutResId) {
		super(LayoutInflater.from(context).inflate(layoutResId, parent, false));
		binding = DataBindingUtil.bind(itemView);
	}

}