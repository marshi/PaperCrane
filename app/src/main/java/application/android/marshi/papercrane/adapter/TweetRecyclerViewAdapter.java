package application.android.marshi.papercrane.adapter;

/**
 * @author marshi on 2016/07/09.
 */

import android.content.Intent;
import android.graphics.Rect;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import application.android.marshi.papercrane.BindingHolder;
import application.android.marshi.papercrane.LoadCellBindingHolder;
import application.android.marshi.papercrane.R;
import application.android.marshi.papercrane.activity.TweetDetailActivity;
import application.android.marshi.papercrane.databinding.ReadMoreTweetItemBinding;
import application.android.marshi.papercrane.databinding.TweetItemBinding;
import application.android.marshi.papercrane.domain.model.TweetItem;
import application.android.marshi.papercrane.enums.TweetPage;
import application.android.marshi.papercrane.enums.ViewType;
import application.android.marshi.papercrane.service.auth.AccessTokenService;
import application.android.marshi.papercrane.service.twitter.TimelineService;
import com.trello.rxlifecycle.components.support.RxFragment;
import lombok.Getter;

import javax.inject.Inject;
import java.util.LinkedList;
import java.util.List;

/**
 * tweetデータ保持用Adapter
 */
public class TweetRecyclerViewAdapter extends RecyclerView.Adapter<BindingHolder<TweetItemBinding>> {

	private RxFragment rxFragment;

	@Inject
	AccessTokenService accessTokenService;

	@Inject
	TimelineService timelineService;

	@Getter
	private final LinkedList<TweetItem> mValues = new LinkedList<>();

	public TweetRecyclerViewAdapter(RxFragment rxFragment) {
		this.rxFragment = rxFragment;
	}

	@Override
	public BindingHolder<TweetItemBinding> onCreateViewHolder(ViewGroup parent, int viewType) {
		switch(ViewType.from(viewType)) {
			case ReadMore:
				return new LoadCellBindingHolder(rxFragment.getActivity(), parent, R.layout.read_more_tweet_item);
			case Normal:
			default:
				return new BindingHolder<>(rxFragment.getActivity(), parent, R.layout.tweet_item);
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
			binding.content.setOnClickListener(v -> {
				TextView textView = (TextView)v;
				if (textView.getSelectionStart() == -1 && textView.getSelectionEnd() == -1) {
					FragmentActivity activity = rxFragment.getActivity();
					Intent intent = new Intent(activity, TweetDetailActivity.class);
					activity.startActivity(intent, ActivityOptionsCompat.makeSceneTransitionAnimation(activity).toBundle());
				}
			});
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
						tweetItems -> add(tweetItems, position)
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

	/**
	 * RecyclerViewの見た目を調整する.
	 * 各要素の余白やボーダーなど.
	 */
	public class TweetRecyclerViewItemDecoration extends RecyclerView.ItemDecoration {

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
	public class InfinityScrollListener extends RecyclerView.OnScrollListener {

		private RxFragment rxFragment;
		private int previousTotalItemCount = 0;
		private LinearLayoutManager linearLayoutManager;
		private TweetPage tweetPage;

		public InfinityScrollListener(RxFragment fragment, LinearLayoutManager linearLayoutManager, TweetPage tweetPage) {
			this.rxFragment = fragment;
			this.linearLayoutManager = linearLayoutManager;
			this.tweetPage = tweetPage;
		}

		@Override
		public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
			super.onScrolled(recyclerView, dx, dy);
			int totalItemCount = linearLayoutManager.getItemCount();
			int lastVisibleItemPosition = linearLayoutManager.findLastVisibleItemPosition();
			boolean isScrollEnd = lastVisibleItemPosition + 1 == totalItemCount;
			if (previousTotalItemCount != totalItemCount && isScrollEnd) {
				previousTotalItemCount = totalItemCount;
				onLoadMore(getMValues().get(getMValues().size() - 1));
			}
		}

		private void onLoadMore(TweetItem lastTweetItem) {
			timelineService.loadTweetItems(
				rxFragment,
				accessTokenService.getAccessToken(),
				lastTweetItem.getId(),
				tweetPage,
				tweetItems -> addLast(tweetItems)
			);
		}
	}

}
