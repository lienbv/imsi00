package com.vibee.utils;

import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.LocaleUtils;
import org.apache.commons.lang3.StringUtils;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

@Log4j2
public final class MessageUtils {
	public static final String BUNDLE_MESSAGE_NAME = "messages";
	private static HashMap<String, ResourceBundle> resourceBundles = new HashMap<String, ResourceBundle>();

	private static ResourceBundle getInstance(Locale locale) {
		ResourceBundle resourceBundle = resourceBundles.get(locale.getLanguage());
		if (resourceBundle == null) {
			resourceBundle = ResourceBundle.getBundle(BUNDLE_MESSAGE_NAME, locale);
			resourceBundles.put(locale.getLanguage(), resourceBundle);
		}
		return resourceBundle;
	}

	public static String get(final String language, final String key, final Object... arguments) {
		if (key == null || key.isEmpty()) {
			return StringUtils.EMPTY;
		}

		String keyTrim = key.trim();

		String lang = StringUtils.lowerCase(language);
		if (StringUtils.isBlank(lang)) {
			lang = "vi";
		}
		Locale locale = LocaleUtils.toLocale(lang);
		ResourceBundle resourceBundle = getInstance(locale);
		try {
			String value = resourceBundle.getString(keyTrim).trim();
			return arguments == null || arguments.length == 0 ? value : MessageFormat.format(value, arguments);
		} catch (MissingResourceException mre) {
			log.error("Message key not found: " + keyTrim);
			return '!' + keyTrim + '!';
		}
	}
}
