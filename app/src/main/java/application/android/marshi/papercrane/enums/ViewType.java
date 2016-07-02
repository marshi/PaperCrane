package application.android.marshi.papercrane.enums;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Map;

/**
 * @author marshi on 2016/06/13.
 */
@AllArgsConstructor
@Getter
public enum ViewType {
    Normal(0),
    ReadMore(1);

    private int value;

    private static final Map<Integer, ViewType> MAP;

    static {
        MAP = Stream.of(values()).collect(Collectors.toMap(ViewType::getValue, v -> v));
    }

    public static ViewType from(int value) {
        return MAP.get(value);
    }

}
