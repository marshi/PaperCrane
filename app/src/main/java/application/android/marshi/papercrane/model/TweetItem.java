package application.android.marshi.papercrane.model;

import lombok.AllArgsConstructor;
import lombok.Data;


@AllArgsConstructor
@Data
public class TweetItem {

    public final String id;
    public final String content;
    public final String details;

    @Override
    public String toString() {
        return content;
    }
}

