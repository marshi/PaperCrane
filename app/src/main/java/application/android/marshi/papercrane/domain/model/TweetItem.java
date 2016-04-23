package application.android.marshi.papercrane.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;


@AllArgsConstructor
@Data
public class TweetItem {

    public final Long id;
    public final String content;

    @Override
    public String toString() {
        return content;
    }
}

