package ae.gov.sdg.paperless.platform.util;

import static java.lang.String.valueOf;
import static java.util.Base64.getEncoder;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isEmpty;

import java.text.DecimalFormat;

import org.apache.commons.lang3.StringUtils;
import org.springframework.lang.NonNull;

import ae.gov.sdg.paperless.platform.common.model.components.types.LangType;


public final class BusinessUtil {
    private BusinessUtil() {
    }

    public static String maskMobileNumber(final String mobileNumber) {
        if (isBlank(mobileNumber) || mobileNumber.length() < 10) {
            return EMPTY;
        } else {
            return mobileNumber.replace(valueOf(mobileNumber.subSequence(5, 10)), "*****");
        }
    }

    public static String formatEID(final String eid) {
        if (isBlank(eid) || eid.length() != 15) {
            return EMPTY;
        } else {
            final StringBuilder result = new StringBuilder(eid);
            return result.insert(3, "-")
                    .insert(8, "-")
                    .insert(16, "-")
                    .toString();
        }
    }
    
    /***
     * This method is used to convert Null to "" string value
     * @param str
     * @return "" is str value is null or str
     */
    public static String encodeBase64String(final String str) {
        return !isEmpty(str) ? getEncoder().encodeToString(str.getBytes()) : "";
    }

    /**
     * Set the default language to english if the language is null.
     *
     * @param lang
     * @return
     */
    public static LangType fetchLangType(LangType lang) {
		return lang!=null? lang : LangType.en;
	}
    
    /**
	 * Get AcceptLanguage based on LangType.
	 * 
	 * @param lang
	 * @return acceptLanguage as String
	 */
    public static String fetchAcceptLanguage(LangType lang) {
		String acceptLanguage = "en-US";
		if (LangType.ar.equals(lang)) {
			acceptLanguage = "ar-AE";
		}
		return acceptLanguage;
	}

    /***
	 * This method is used to convert Null to "" string value
	 * @param str
	 * @return "" is str value is null or str
	 */
    public static String validateString(String str) {
        return !isEmpty(str) ? str : "";
    }

    /***
     * This method is used to format mobile number to start with 971 if it starts with 0
     * @param mobileNumber
     * @return "" is str value is null or formatted mobileNumber
     */
    public static String formatMobileNumber(String mobileNumber) {
    	if (StringUtils.isEmpty(mobileNumber)) {
            return "";
        } else {
            final StringBuilder result = new StringBuilder(mobileNumber);
            if (result.length() == 10 && result.indexOf("0") == 0) {
                return result.replace(0, 1, "971").toString();
            } else if (result.length() == 13 && result.indexOf("+971") == 0) {
                return result.substring(1, 13);
            } else if (result.length() == 14 && result.indexOf("00971") == 0) {
                return result.replace(0, 2, "").toString();
            }
        }
        return mobileNumber;
    }

    /**
     * Format decimal value based on <i>#.00</i> pattern and comma separated grouping
     *
     * @param amount double value to be formatted
     * @return formatted decimal value in string e.g 12,345,678.98
     */
    public static String formatAmount(double amount) {
        return formatAmount(amount, true);
    }

    /**
     * Format decimal value based on <i>#.00</i> pattern
     *
     * @param amount      double value to be formatted
     * @param useGrouping whether comma to be used as a separator
     * @return formatted decimal value in string e.g 10,324,452.45
     */
    public static String formatAmount(double amount, boolean useGrouping) {
        return formatAmount("#0.00", amount, useGrouping);
    }

    /**
     * Format decimal value based on a given pattern
     *
     * @param pattern     pattern with which double number will be formatted
     * @param amount      double value to be formatted
     * @param useGrouping whether comma to be used as a separator
     * @return formatted decimal value in string e.g 10,324,452.45
     */
    public static String formatAmount(@NonNull final String pattern, double amount, boolean useGrouping) {
        DecimalFormat decimalFormat = new DecimalFormat(pattern);
        decimalFormat.setGroupingUsed(useGrouping); //comma separated values
        decimalFormat.setGroupingSize(3); //after every 3 digits comma appended e.g 10,324,452.45
        return decimalFormat.format(amount);
    }

    /***
     * This method is used to format mobile number to at-least 9 digit length and prefix with "0"s
     * @param mobileNumber
     * @return "" if mobileNumber is null or formatted mobileNumber
     */
    public static String formatMobileNumberToMinimumLength(final String mobileNumber) {
        if (StringUtils.isEmpty(mobileNumber)) {
            return "";
        } else {
            final StringBuilder result = new StringBuilder(mobileNumber);
            if (result.length() == 12) {
                return result.substring(3, 12);
            } else if (result.length() < 9) {
                return StringUtils.leftPad(result.toString(), 9, '0');
            }
        }
        return mobileNumber;
    }

    /***
     * This method is used to truncate input string to the defined max length
     * @param inputStr
     * @return Truncated inputStr value
     */
    public static String truncateStringToDefinedMaxLength(String inputStr, int maxLength) {
        if (StringUtils.isEmpty(inputStr)) {
            return "";
        } else if (inputStr.length() > maxLength) {
            return inputStr.substring(0, maxLength);
        }
        return inputStr;
    }

}