package ae.gov.sdg.paperless.platform.util;

import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.commons.lang3.StringUtils;

public final class StringUtil {
    private StringUtil() {
    }

    public static String toSortedString(final Map<?, ?> map) {
        final StringBuilder sb = new StringBuilder();

        final SortedSet<?> keys = new TreeSet<>(map.keySet());

        for (final Object key : keys) {
            final Object value = map.get(key);
            sb.append(key).append(": ");
            sb.append(value).append('\n');
        }

        return sb.toString();
    }
    
    public static boolean isBlank(final String input) {
        return !isNotBlank(input);
    }

    public static boolean isNotBlank(final String input) {
        return StringUtils.isNotBlank(input) && !"null".equals(input) && !"{null}".equals(input) && !"(null)".equals(input);
    }

}
