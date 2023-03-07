package vn.topwines.core.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Collection;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CollectionUtils {
    public static <T> Boolean isEmpty(Collection<T> collection) {
        return collection == null || collection.isEmpty();
    }

    public static <T> Boolean isNotEmpty(Collection<T> collection) {
        return !isEmpty(collection);
    }
}
