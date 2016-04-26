package application.android.marshi.papercrane.fragment;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import application.android.marshi.papercrane.BindingHolder;
import application.android.marshi.papercrane.R;
import application.android.marshi.papercrane.databinding.FragmentTweetListBinding;
import application.android.marshi.papercrane.databinding.TweetItemBinding;
import application.android.marshi.papercrane.di.App;
import application.android.marshi.papercrane.domain.model.TweetItem;
import application.android.marshi.papercrane.eventbus.Event;
import application.android.marshi.papercrane.eventbus.EventBusBroker;
import application.android.marshi.papercrane.presenter.auth.AccessTokenPresenter;
import application.android.marshi.papercrane.presenter.twitter.TimelinePresenter;
import rx.android.schedulers.AndroidSchedulers;
import twitter4j.auth.AccessToken;

import javax.inject.Inject;
import java.util.List;

/**
 * A fragment representing a list of Items.
 */
public class TweetListFragment extends Fragment {

	// TODO: Customize parameter argument names
	private static final String ARG_COLUMN_COUNT = "column-count";

	private FragmentTweetListBinding fragmentTweetListBinding;

	private TweetItemBinding tweetItemBinding;

	@Inject
	AccessTokenPresenter accessTokenPresenter;

	@Inject
	TimelinePresenter timelinePresenter;

	// TODO: Customize parameter initialization
	@SuppressWarnings("unused")
	public static TweetListFragment newInstance(int columnCount) {
		TweetListFragment fragment = new TweetListFragment();
		Bundle args = new Bundle();
		args.putInt(ARG_COLUMN_COUNT, columnCount);
		fragment.setArguments(args);
		return fragment;
	}

	/**
	 * Mandatory empty constructor for the fragment manager to instantiate the
	 * fragment (e.g. upon screen orientation changes).
	 */
	public TweetListFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		((App) (getActivity().getApplication())).getApplicationComponent().inject(this);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		fragmentTweetListBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_tweet_list, container, false);
		return fragmentTweetListBinding.fragmentTweetList;
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		RecyclerView recyclerView = fragmentTweetListBinding.fragmentTweetList;
		// Set the adapter
		if (recyclerView != null) {
			AccessToken accessToken = accessTokenPresenter.getAccessToken();
			EventBusBroker.tweetListEventBus.get(Event.GetTweetList)
					.observeOn(AndroidSchedulers.mainThread())
					.subscribe(tweetItems -> {
						Context context = recyclerView.getContext();
						recyclerView.setLayoutManager(new LinearLayoutManager(context));
						recyclerView.setAdapter(new TweetRecyclerViewAdapter(tweetItems));
						recyclerView.addItemDecoration(new TweetRecyclerViewItemDecoration());
					});
			timelinePresenter.getTweetItems(accessToken);
		}
	}

	private class TweetRecyclerViewAdapter extends RecyclerView.Adapter<BindingHolder<TweetItemBinding>> {

		private final List<TweetItem> mValues;

		public TweetRecyclerViewAdapter(List<TweetItem> items) {
			mValues = items;
		}

		@Override
		public BindingHolder<TweetItemBinding> onCreateViewHolder(ViewGroup parent, int viewType) {
			return new BindingHolder<>(getActivity(), parent, R.layout.tweet_item);
		}

		@Override
		public void onBindViewHolder(BindingHolder<TweetItemBinding> holder, int position) {
			TweetItem tweetItem = mValues.get(position);
			holder.binding.setTweet(tweetItem);
		}

		@Override
		public int getItemCount() {
			return mValues.size();
		}

	}

	private class TweetRecyclerViewItemDecoration extends RecyclerView.ItemDecoration {

		@Override
		public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
			super.getItemOffsets(outRect, view, parent, state);
			outRect.top = 20;
			outRect.bottom = 20;
		}
	}
}
