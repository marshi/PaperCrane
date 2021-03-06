package application.android.marshi.papercrane.fragment;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import application.android.marshi.papercrane.R;
import application.android.marshi.papercrane.databinding.FragmentTweetEditorBinding;
import application.android.marshi.papercrane.di.App;
import application.android.marshi.papercrane.enums.ExtraKeys;
import application.android.marshi.papercrane.presenter.TweetPostPresenter;

import javax.inject.Inject;

/**
 *
 */
public class TweetEditorFragment extends Fragment {

	private FragmentTweetEditorBinding binding;

	@Inject
	TweetPostPresenter tweetPostPresenter;

	public TweetEditorFragment() {
		// Required empty public constructor
	}

	/**
	 * Use this factory method to create a new instance of
	 * this fragment using the provided parameters.
	 *
	 * @return A new instance of fragment TweetEditorFragment.
	 */
	public static TweetEditorFragment newInstance() {
		TweetEditorFragment fragment = new TweetEditorFragment();
		Bundle args = new Bundle();
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		App.getApplicationComponent().inject(this);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		binding = DataBindingUtil.inflate(inflater, R.layout.fragment_tweet_editor, null, false);
		binding.tweetButton.setOnClickListener(v -> {
			SpannableStringBuilder text = (SpannableStringBuilder) binding.tweetEditor.getText();
			tweetPostPresenter.post(this, text.toString());
		});

		Intent intent = getActivity().getIntent();
		//リプライの場合はUSER_IDがextraに送られてくる.
		String userId = intent.getStringExtra(ExtraKeys.USER_ID);
		if (!TextUtils.isEmpty(userId)) {
			String text = userId + " ";
			binding.tweetEditor.setText(text);
			binding.tweetEditor.setSelection(text.length());
		}
		//リツイートの場合はUSER_CONTENTがextraに送られてくる.
		String tweetContent = intent.getStringExtra(ExtraKeys.TWEET_CONTENT);
		if (!TextUtils.isEmpty(tweetContent)) {
			binding.tweetEditor.setText("RT " + tweetContent);
		}

		return binding.tweetEditorLayout;
	}



}
