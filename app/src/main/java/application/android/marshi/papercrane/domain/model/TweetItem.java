package application.android.marshi.papercrane.domain.model;

import android.databinding.BindingAdapter;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.format.DateFormat;
import android.widget.ImageView;
import android.widget.TextView;
import application.android.marshi.papercrane.enums.ViewType;
import com.annimon.stream.function.Predicate;
import com.bumptech.glide.Glide;
import lombok.NonNull;
import twitter4j.Status;

import java.util.Date;

public class TweetItem implements Parcelable {

    private Long id;
    private String userId;
    private String userName;
    private String content;
    private String profileImageUrl;
    private boolean fav;
    private Date tweetAt;

    @NonNull
    private ViewType viewType;

    public TweetItem(Long id, String userId, String content, String userName, String profileImageUrl, boolean fav, Date tweetAt, ViewType viewType) {
        this.id = id;
        this.userId = userId;
        this.content = content;
        this.userName = userName;
        this.profileImageUrl = profileImageUrl;
        this.tweetAt = tweetAt;
        this.fav = fav;
        this.viewType = viewType;
    }

    public TweetItem(Status status) {
        this(
          status.getId(),
          "@" + status.getUser().getScreenName(),
          status.getText(),
          status.getUser().getName(),
          status.getUser().getProfileImageURL(),
          status.isFavorited(),
          status.getCreatedAt(),
          ViewType.Normal
        );
    }

    /**
     *
     * @param id
     * {@link application.android.marshi.papercrane.database.dto.ReadMore}のid
     *
     * @return
     */
    public static TweetItem createReadMore(Long id) {
        return new TweetItem(id, null, null, null, null, false, null, ViewType.ReadMore);
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

    public boolean isFav() { return fav; }

    public void setFav(boolean fav) {
        this.fav = fav;
    }

    public Date getTweetAt() {
        return tweetAt;
    }

    public ViewType getViewType() {
        return viewType;
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
            view.setText(DateFormat.format("MM/dd kk:mm", newDate));
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

    public static Predicate<TweetItem> viewTypePredicate(ViewType viewType) {
        return value -> value.getViewType() == viewType;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.id);
        dest.writeString(this.userId);
        dest.writeString(this.userName);
        dest.writeString(this.content);
        dest.writeString(this.profileImageUrl);
        dest.writeByte(this.fav ? (byte) 1 : (byte) 0);
        dest.writeLong(this.tweetAt != null ? this.tweetAt.getTime() : -1);
        dest.writeInt(this.viewType == null ? -1 : this.viewType.ordinal());
    }

    protected TweetItem(Parcel in) {
        this.id = (Long) in.readValue(Long.class.getClassLoader());
        this.userId = in.readString();
        this.userName = in.readString();
        this.content = in.readString();
        this.profileImageUrl = in.readString();
        this.fav = in.readByte() != 0;
        long tmpTweetAt = in.readLong();
        this.tweetAt = tmpTweetAt == -1 ? null : new Date(tmpTweetAt);
        int tmpViewType = in.readInt();
        this.viewType = tmpViewType == -1 ? null : ViewType.values()[tmpViewType];
    }

    public static final Creator<TweetItem> CREATOR = new Creator<TweetItem>() {
        @Override
        public TweetItem createFromParcel(Parcel source) {
            return new TweetItem(source);
        }

        @Override
        public TweetItem[] newArray(int size) {
            return new TweetItem[size];
        }
    };
}

