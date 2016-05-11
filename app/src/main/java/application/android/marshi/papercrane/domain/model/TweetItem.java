package application.android.marshi.papercrane.domain.model;

import android.databinding.BindingAdapter;
import android.text.format.DateFormat;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;

import java.util.Date;

public class TweetItem {

    private Long id;
    private String userId;
    private String userName;
    private String content;
    private String profileImageUrl;
    private Date tweetAt;

    public TweetItem(Long id, String userId, String content, String userName, String profileImageUrl, Date tweetAt) {
        this.id = id;
        this.userId = userId;
        this.content = content;
        this.userName = userName;
        this.profileImageUrl = profileImageUrl;
        this.tweetAt = tweetAt;
    }

    public Long getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUserName() {
        return userName;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public String getUserId() {
        return userId;
    }

    public Date getTweetAt() {
        return tweetAt;
    }

    @BindingAdapter("profileImage")
    public static void setImage(ImageView view,String oldUrl, String newUrl) {
        if (!newUrl.equals(oldUrl)) {
            Glide.with(view.getContext()).load(newUrl).into(view);
        }
    }

    @BindingAdapter("tweetTime")
    public static void setTweetTime(TextView view, Date oldDate, Date newDate) {
        Date now = new Date();
        long millis = now.getTime() - newDate.getTime();
        int sec = (int)(millis / 1000);
        if (0 < sec / 24 / 60 / 60) {
            view.setText(DateFormat.format("MM/dd HH:mm", new Date(millis)));
            return;
        }
        if (0 < sec / 60 / 60) {
            view.setText(sec / 60 / 60 + "時間前");
            return;
        }
        if (0 < sec / 60) {
            view.setText(sec / 60 + "分前");
            return;
        }
        view.setText(sec + "秒前");
    }

}

