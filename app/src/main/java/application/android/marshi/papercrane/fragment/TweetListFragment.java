package application.android.marshi.papercrane.fragment;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import application.android.marshi.papercrane.BindingHolder;
import application.android.marshi.papercrane.R;
import application.android.marshi.papercrane.databinding.FragmentTweetBinding;
import application.android.marshi.papercrane.databinding.FragmentTweetListBinding;
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
					});
			timelinePresenter.getTweetItems(accessToken);
		}
		return recyclerView;
	}

	private class TweetRecyclerViewAdapter extends RecyclerView.Adapter<BindingHolder<FragmentTweetBinding>> {

		private final List<TweetItem> mValues;

		public TweetRecyclerViewAdapter(List<TweetItem> items) {
			mValues = items;
		}

		@Override
		public BindingHolder<FragmentTweetBinding> onCreateViewHolder(ViewGroup parent, int viewType) {
			return new BindingHolder<>(getActivity(), parent, R.layout.fragment_tweet);
		}

		@Override
		public void onBindViewHolder(BindingHolder<FragmentTweetBinding> holder, int position) {
			TweetItem tweetItem = mValues.get(position);
			holder.binding.setTweet(tweetItem);
		}

		@Override
		public int getItemCount() {
			return mValues.size();
		}

	}
}
