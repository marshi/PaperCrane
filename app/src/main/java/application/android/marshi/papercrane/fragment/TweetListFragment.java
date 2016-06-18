package application.android.marshi.papercrane.fragment;

import android.databinding.DataBindingUtil;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import application.android.marshi.papercrane.BindingHolder;
import application.android.marshi.papercrane.LoadCellBindingHolder;
import application.android.marshi.papercrane.R;
import application.android.marshi.papercrane.databinding.FragmentTweetListBinding;
import application.android.marshi.papercrane.databinding.ReadMoreTweetItemBinding;
import application.android.marshi.papercrane.databinding.TweetItemBinding;
import application.android.marshi.papercrane.di.App;
import application.android.marshi.papercrane.domain.model.TweetItem;
import application.android.marshi.papercrane.enums.TweetPage;
import application.android.marshi.papercrane.enums.ViewType;
import application.android.marshi.papercrane.repository.LastTweetAccessTimeRepository;
import application.android.marshi.papercrane.service.auth.AccessTokenService;
import application.android.marshi.papercrane.service.twitter.TimelineService;
import com.annimon.stream.Stream;
import com.trello.rxlifecycle.components.support.RxFragment;
import org.threeten.bp.LocalDateTime;
import org.threeten.bp.temporal.ChronoUnit;
import twitter4j.auth.AccessToken;

import javax.inject.Inject;
import java.text.MessageFormat;
import java.util.LinkedList;
import java.util.List;

/**
 * A fragment representing a list of Items.
 */
public class TweetListFragment extends RxFragment {

	private FragmentTweetListBinding fragmentTweetListBinding;

	private TweetRecyclerViewAdapter tweetRecyclerViewAdapter;

	@Inject
	AccessTokenService accessTokenService;

	@Inject
	TimelineService timelineService;

	@Inject
	LastTweetAccessTimeRepository lastTweetAccessTimeRepository;

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
		configureTweetRecyclerView();
		Bundle args = getArguments();
		if (args != null) {
			int code = args.getInt(TweetPage.BUNDLE_KEY);
			this.tweetPage = TweetPage.from(code);
		}
		AccessToken accessToken = accessTokenService.getAccessToken();
		//TODO 処理が多くなってきたので整理する
		//swipe to refresh で最新ツイートを取得.
		fragmentTweetListBinding.swipeRefreshLayout.setOnRefreshListener(() -> {
			SwipeRefreshLayout swipeRefreshLayout = fragmentTweetListBinding.swipeRefreshLayout;
			swipeRefreshLayout.setColorSchemeResources(R.color.theme500);
			LocalDateTime now = LocalDateTime.now();
			LocalDateTime lastAccessedTime = lastTweetAccessTimeRepository.get();
			int waitSeconds = 5;
			if (lastAccessedTime != null &&  lastAccessedTime.isAfter(now.minus(waitSeconds, ChronoUnit.SECONDS))) {
				swipeRefreshLayout.setRefreshing(false);
			} else {
				LinkedList<TweetItem> mValues = tweetRecyclerViewAdapter.mValues;
				timelineService.loadLatestTweetItems(
					this,
					accessToken,
					//取得済の最新ツイートと新たに取得したツイートが一致することを調べるためにsinceIdは最新から2番目のツイートを利用する
					2 <= mValues.size() ? mValues.get(1).getId() : 1 == mValues.size() ? mValues.getFirst().getId() : null,
					!mValues.isEmpty() ? mValues.getFirst().getId() : null,
					tweetPage,
					tweetItems -> {
						if (tweetItems.isEmpty()) {
							Toast.makeText(this.getActivity(), "取得ツイートなし", Toast.LENGTH_SHORT).show();
						} else {
							Toast.makeText(this.getActivity(), MessageFormat.format("{0}件取得",
								Stream.of(tweetItems)
									.filter(t -> t.getViewType() == ViewType.Normal)
									.count()),
								Toast.LENGTH_SHORT).show();
							tweetRecyclerViewAdapter.addFirst(tweetItems);
						}
						swipeRefreshLayout.setRefreshing(false);
					},
					() -> swipeRefreshLayout.setRefreshing(false)
				);
			}
		});
	}

	@Override
	public void onResume() {
		super.onResume();
		if (tweetRecyclerViewAdapter.mValues.isEmpty()) {
			timelineService.loadStoredTweets(
				this,
				tweetPage,
				tweetItemList -> tweetRecyclerViewAdapter.addLast(tweetItemList)
			);
		}
	}

	private void configureTweetRecyclerView() {
		RecyclerView recyclerView = fragmentTweetListBinding.tweetRecyclerView;
		recyclerView.setItemViewCacheSize(1000);
		LinearLayoutManager layoutManager = new LinearLayoutManager(this.getActivity());
		recyclerView.setLayoutManager(layoutManager);
		recyclerView.addItemDecoration(new TweetRecyclerViewItemDecoration());
		recyclerView.addOnScrollListener(new InfinityScrollListener(this, layoutManager));
		tweetRecyclerViewAdapter = new TweetRecyclerViewAdapter(this, new LinkedList<>());
		recyclerView.setAdapter(tweetRecyclerViewAdapter);
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt(TweetPage.BUNDLE_KEY, this.tweetPage.getCode());
	}

	/**
	 * tweetデータ保持用Adapter
	 */
	private class TweetRecyclerViewAdapter extends RecyclerView.Adapter<BindingHolder<TweetItemBinding>> {

		private RxFragment rxFragment;

		private final LinkedList<TweetItem> mValues;

		public TweetRecyclerViewAdapter(RxFragment rxFragment, LinkedList<TweetItem> items) {
			this.rxFragment = rxFragment;
			mValues = items;
		}

		@Override
		public BindingHolder<TweetItemBinding> onCreateViewHolder(ViewGroup parent, int viewType) {
			switch(ViewType.from(viewType)) {
				case ReadMore:
					return new LoadCellBindingHolder(getActivity(), parent, R.layout.read_more_tweet_item);
				case Normal:
				default:
					return new BindingHolder<>(getActivity(), parent, R.layout.tweet_item);
			}
		}

		@Override
		public int getItemViewType(int position) {
			return mValues.get(position).getViewType().getValue();
		}

		@Override
		public void onBindViewHolder(BindingHolder holder, int position) {
			if (holder.binding instanceof TweetItemBinding) {
				TweetItemBinding binding = (TweetItemBinding)holder.binding;
				TweetItem tweetItem = mValues.get(position);
				binding.setTweet(tweetItem);
				return;
			}
			if (holder.binding instanceof ReadMoreTweetItemBinding) {
				ReadMoreTweetItemBinding binding = (ReadMoreTweetItemBinding) holder.binding;
				TweetItem tweetItem = mValues.get(position);
				TweetItem newerTweetItem = mValues.get(position - 1);
				//取得済の最新ツイートと新たに取得したツイートが一致することを調べるためにsinceIdは最新から2番目のツイートを利用する
				TweetItem olderTweetItem = 2 <= mValues.size() ? mValues.get(position + 2) : mValues.get(position + 1);
				binding.setOnClickReadMoreTweetItem(v -> {
					if (olderTweetItem != null) {
						timelineService.loadTweetItems(
							rxFragment,
							accessTokenService.getAccessToken(),
							newerTweetItem.getId(),
							olderTweetItem.getId(),
							!mValues.isEmpty() ? mValues.getFirst().getId() : null,
							TweetPage.HomeTimeline,
							tweetItems -> tweetRecyclerViewAdapter.add(tweetItems, position)
						);
					}
					remove(tweetItem);
				});
			}
		}

		@Override
		public int getItemCount() {
			return mValues.size();
		}

		synchronized public void addLast(List<TweetItem> tweetList) {
			for (TweetItem tweet: tweetList) {
				this.mValues.addLast(tweet);
				notifyItemInserted(mValues.size() - 1);
			}
		}

		synchronized public void addFirst(List<TweetItem> tweetList) {
			for (int i = tweetList.size() -  1; 0 <= i; i--) {
				addFirst(tweetList.get(i));
			}
		}

		synchronized public void addFirst(TweetItem tweetItem) {
			this.mValues.addFirst(tweetItem);
			notifyItemInserted(0);
		}

		synchronized public void add(List<TweetItem> tweetList, int position) {
			for (int i = tweetList.size() -  1; 0 <= i; i--) {
				add(tweetList.get(i), position);
			}
		}

		synchronized public void add(TweetItem tweetItem, int position) {
			this.mValues.add(position, tweetItem);
			notifyItemInserted(position);
		}

		synchronized public void remove(TweetItem tweetItem) {
			int position = mValues.indexOf(tweetItem);
			this.mValues.remove(tweetItem);
			timelineService.deleteStoredReadMore(tweetItem.getId());
			notifyItemRemoved(position);
		}

	}

	/**
	 * RecyclerViewの見た目を調整する.
	 * 各要素の余白やボーダーなど.
	 */
	private class TweetRecyclerViewItemDecoration extends RecyclerView.ItemDecoration {
		@Override
		public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
			super.getItemOffsets(outRect, view, parent, state);
			outRect.top = 20;
			outRect.bottom = 20;
		}
	}

	/**
	 * 無限スクロールを行うためのリスナー.
	 * 取得済みのツイートのうち最古のツイートを表示したらさらに古いツイートを取得してRecyclerViewに追加する.
	 */
	private class InfinityScrollListener extends RecyclerView.OnScrollListener {

		private RxFragment rxFragment;
		private int previousTotalItemCount = 0;
		private LinearLayoutManager linearLayoutManager;

		public InfinityScrollListener(RxFragment fragment, LinearLayoutManager linearLayoutManager) {
			this.rxFragment = fragment;
			this.linearLayoutManager = linearLayoutManager;
		}

		@Override
		public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
			super.onScrolled(recyclerView, dx, dy);
			int totalItemCount = linearLayoutManager.getItemCount();
			int lastVisibleItemPosition = linearLayoutManager.findLastVisibleItemPosition();
			boolean isScrollEnd = lastVisibleItemPosition + 1 == totalItemCount;
			if (previousTotalItemCount != totalItemCount && isScrollEnd) {
				previousTotalItemCount = totalItemCount;
				onLoadMore(tweetRecyclerViewAdapter.mValues.get(tweetRecyclerViewAdapter.mValues.size() - 1));
			}
		}

		private void onLoadMore(TweetItem lastTweetItem) {
			timelineService.loadTweetItems(
				rxFragment,
				accessTokenService.getAccessToken(),
				lastTweetItem.getId(),
				tweetPage,
				tweetItems -> tweetRecyclerViewAdapter.addLast(tweetItems)
			);
		}
	}


}
