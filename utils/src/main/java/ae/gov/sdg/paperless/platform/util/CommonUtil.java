package ae.gov.sdg.paperless.platform.util;

import static java.lang.Math.atan2;
import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static java.lang.Math.sqrt;
import static java.lang.Math.toRadians;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.resolve;

import java.util.List;

import org.apache.commons.collections4.MapUtils;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.util.UriComponentsBuilder;

import ae.gov.sdg.paperless.platform.common.PlatformConstants;
import ae.gov.sdg.paperless.platform.common.model.IDirectoryType;
import ae.gov.sdg.paperless.platform.common.model.IJourneyType;

public final class CommonUtil {
	private CommonUtil() {}

	public static float calculateDistance(final double lat1, final double lng1, final double lat2, final double lng2) {
		final double earthRadius = 6371; // kilometers
		final double dLat = toRadians(lat2 - lat1);
		final double dLng = toRadians(lng2 - lng1);
		final double a = sin(dLat / 2) * sin(dLat / 2) + cos(toRadians(lat1))
				* cos(toRadians(lat2)) * sin(dLng / 2) * sin(dLng / 2);
		final double c = 2 * atan2(sqrt(a), sqrt(1 - a));
		return (float) (earthRadius * c);
	}
	
	@SuppressWarnings("unchecked")
	public static <T extends Throwable> void sneakyThrow(final Throwable t) throws T {
      throw (T) t;
    }
	
	/**
     * @param url    non-null url to which request parameters to be appended
     * @param params Nullable request parameters map
     * @return updated url
     */
    public static String appendRequestParameters(final @NonNull String url, @Nullable final MultiValueMap<String, String> params) {
        if (MapUtils.isNotEmpty(params)) {
            return UriComponentsBuilder.fromUriString(url).queryParams(params).build().toString();
        }
        return url;
    }

    public static boolean is404Status(Exception ex) {
        return isStatusEquals(ex, NOT_FOUND);
    }

    public static boolean isStatusEquals(Exception ex, HttpStatus httpStatus) {
        return (ex.getCause() instanceof HttpStatusCodeException && resolve(((HttpStatusCodeException) ex.getCause()).getRawStatusCode()) == httpStatus);
    }

    /**
	 * Gets the string.
	 *
	 * @param listToConvert the list to convert
	 * @return the string
	 */
	public static String convertListToString(List<String> listToConvert) {
		StringBuilder stringBuilder = new StringBuilder();
		for (String item : listToConvert) {
			stringBuilder.append(item);
			stringBuilder.append(PlatformConstants.COMMA);
		}
		String value = stringBuilder.toString();
		if (value.endsWith(PlatformConstants.COMMA)) {
			value = value.substring(0, value.length() - (PlatformConstants.COMMA).length());
		}
		return value;
	}
	
	/**
	 * Journey or directory name.
	 *
	 * @param <T> the generic type
	 * @param journeyname the journeyname
	 * @return the string
	 */
	public static <T> String journeyOrDirectoryName(final T journeyname) {
		String name = "";
		if(journeyname instanceof IJourneyType) {
			name = ((IJourneyType)journeyname).name();
		} else if(journeyname instanceof IDirectoryType) {
			name = ((IDirectoryType)journeyname).name();
		}
		return name;
	}

}