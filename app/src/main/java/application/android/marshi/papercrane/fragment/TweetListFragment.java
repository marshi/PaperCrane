package application.android.marshi.papercrane.fragment;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import application.android.marshi.papercrane.R;
import application.android.marshi.papercrane.adapter.TweetRecyclerViewAdapter;
import application.android.marshi.papercrane.databinding.FragmentTweetListBinding;
import application.android.marshi.papercrane.di.App;
import application.android.marshi.papercrane.domain.model.TweetItem;
import application.android.marshi.papercrane.enums.TweetPage;
import application.android.marshi.papercrane.enums.ViewType;
import application.android.marshi.papercrane.presenter.TweetListPresenter;
import application.android.marshi.papercrane.service.ToastService;
import application.android.marshi.papercrane.view.TimelineSwipeRefreshView;
import com.annimon.stream.Stream;
import com.trello.rxlifecycle.components.support.RxFragment;

import javax.inject.Inject;
import java.text.MessageFormat;
import java.util.List;

/**
 * A fragment representing a list of Items.
 */
public class TweetListFragment extends RxFragment implements TimelineSwipeRefreshView {

	private FragmentTweetListBinding fragmentTweetListBinding;

	private TweetRecyclerViewAdapter tweetRecyclerViewAdapter;

	private SwipeRefreshLayout swipeRefreshLayout;

	@Inject
	TweetListPresenter tweetListPresenter;

	@Inject
	ToastService toastService;

	private TweetPage tweetPage;

	public static TweetListFragment newInstance(TweetPage tweetPage) {
		TweetListFragment fragment = new TweetListFragment();
		Bundle args = new Bundle();
		args.putInt(TweetPage.BUNDLE_KEY, tweetPage.getCode());
		fragment.setArguments(args);
		return fragment;
	}

	/**
	 * Mandatory empty constructor for the fragment manager to instantiate the
	 * fragment (e.g. upon screen orientation changes).
	 */
	public TweetListFragment() {}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		App.getApplicationComponent().inject(this);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		fragmentTweetListBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_tweet_list, null, false);
		return fragmentTweetListBinding.swipeRefreshLayout;
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		configureTweetRecyclerView(fragmentTweetListBinding.tweetRecyclerView);
		Bundle args = getArguments();
		if (args != null) {
			int code = args.getInt(TweetPage.BUNDLE_KEY);
			this.tweetPage = TweetPage.from(code);
		}
		//swipe to refresh で最新ツイートを取得.
		fragmentTweetListBinding.swipeRefreshLayout.setOnRefreshListener(() -> {
			this.swipeRefreshLayout = fragmentTweetListBinding.swipeRefreshLayout;
			this.swipeRefreshLayout.setColorSchemeResources(R.color.theme500);
			tweetListPresenter.fetchLatestTimeline(this, this, tweetRecyclerViewAdapter, tweetPage);
		});
	}

	@Override
	public void onResume() {
		super.onResume();
		tweetListPresenter.loadStoredTweetList(this, tweetRecyclerViewAdapter, tweetPage);
	}

	private void configureTweetRecyclerView(@NonNull RecyclerView recyclerView) {
		tweetRecyclerViewAdapter = new TweetRecyclerViewAdapter(this);
		LinearLayoutManager layoutManager = new LinearLayoutManager(this.getActivity());
		recyclerView.setItemViewCacheSize(1000);
		recyclerView.setLayoutManager(layoutManager);
		recyclerView.addItemDecoration(tweetRecyclerViewAdapter.new TweetRecyclerViewItemDecoration());
		recyclerView.addOnScrollListener(tweetRecyclerViewAdapter.new InfinityScrollListener(this, layoutManager, tweetPage));
		recyclerView.setAdapter(tweetRecyclerViewAdapter);
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt(
			TweetPage.BUNDLE_KEY,
			this.tweetPage != null ? this.tweetPage.getCode() : TweetPage.HomeTimeline.getCode()
		);
	}

	@Override
	public void updateTimeline(List<TweetItem> tweetItems) {
		if (tweetItems.isEmpty()) {
			toastService.showToast("取得ツイートなし", Toast.LENGTH_SHORT);
		} else {
			long count = Stream.of(tweetItems).filter(t -> t.getViewType() == ViewType.Normal).count();
			toastService.showToast(MessageFormat.format("{0}件取得", count), Toast.LENGTH_SHORT);
		}
		tweetRecyclerViewAdapter.addFirst(tweetItems);
		swipeRefreshLayout.setRefreshing(false);
	}

	@Override
	public void failToUpdateTimeline() {
		swipeRefreshLayout.setRefreshing(false);
	}
}
