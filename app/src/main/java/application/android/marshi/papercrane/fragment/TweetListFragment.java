package application.android.marshi.papercrane.fragment;

import android.app.Activity;
import android.content.Context;
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
import application.android.marshi.papercrane.domain.timeline.TimelineListUseCase;
import application.android.marshi.papercrane.model.TweetItem;

import java.util.List;

/**
 * A fragment representing a list of Items.
 * <p>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class TweetListFragment extends Fragment {

	// TODO: Customize parameter argument names
	private static final String ARG_COLUMN_COUNT = "column-count";

	private OnListFragmentInteractionListener mListener;

	private TimelineListUseCase tiemTimelineListUseCase = new TimelineListUseCase();

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
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_tweet_list, container, false);

		// Set the adapter
		if (view instanceof RecyclerView) {
			Context context = view.getContext();
			RecyclerView recyclerView = (RecyclerView) view;
			recyclerView.setLayoutManager(new LinearLayoutManager(context));
			recyclerView.setAdapter(new MyTweetRecyclerViewAdapter(null, mListener));
		}
		return view;
	}


	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		if (activity instanceof OnListFragmentInteractionListener) {
			mListener = (OnListFragmentInteractionListener) activity;
		} else {
			throw new RuntimeException(activity.toString()
					+ " must implement OnListFragmentInteractionListener");
		}
	}

	@Override
	public void onDetach() {
		super.onDetach();
		mListener = null;
	}

	/**
	 * This interface must be implemented by activities that contain this
	 * fragment to allow an interaction in this fragment to be communicated
	 * to the activity and potentially other fragments contained in that
	 * activity.
	 * <p>
	 * See the Android Training lesson <a href=
	 * "http://developer.android.com/training/basics/fragments/communicating.html"
	 * >Communicating with Other Fragments</a> for more information.
	 */
	public interface OnListFragmentInteractionListener {
		// TODO: Update argument type and name
		void onListFragmentInteraction(TweetItem item);
	}

	private class MyTweetRecyclerViewAdapter extends RecyclerView.Adapter<BindingHolder<FragmentTweetBinding>> {

		private final List<TweetItem> mValues;

		public MyTweetRecyclerViewAdapter(List<TweetItem> items, TweetListFragment.OnListFragmentInteractionListener listener) {
			mValues = items;
			mListener = listener;
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
