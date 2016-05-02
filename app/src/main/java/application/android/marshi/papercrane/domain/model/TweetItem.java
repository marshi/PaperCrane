package application.android.marshi.papercrane.domain.model;

import android.databinding.BindingAdapter;
import android.widget.ImageView;
import com.bumptech.glide.Glide;

public class TweetItem {

    private final Long id;
    private final String userName;
    private final String content;
    private final String profileImageUrl;

    public TweetItem(Long id, String content, String userName, String profileImageUrl) {
        this.id = id;
        this.content = content;
        this.userName = userName;
        this.profileImageUrl = profileImageUrl;
    }

    public Long getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public String getUserName() {
        return userName;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    @BindingAdapter("profileImage")
    public static void setImage(ImageView view,String oldUrl, String newUrl) {
        if (!newUrl.equals(oldUrl)) {
            Glide.with(view.getContext()).load(newUrl).into(view);
        }
    }

}

