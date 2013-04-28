/**
 * 
 */
package com.shaikapsar.vmware.api.extensions.listener.util;

import java.util.Calendar;

/**
 * @author Shaik Apsar | shaik.apsar@live.com
 * 
 */
public class ApplicationUtil {

	public static boolean isEmpty(Object instance) {
		return instance == null ? true : false;
	}

	public static boolean isNotEmpty(Object instance) {
		return !isEmpty(instance);
	}

	public static boolean isEmpty(String[] value) {
		return value == null || value.length == 0;
	}

	public static String rightNow() {
		return (formatCalendar(Calendar.getInstance()));
	}

	public static String formatCalendar(Calendar gc) {
		if (gc == null)
			return "";
		return (gc.get(Calendar.YEAR) + "-"
				+ toTwoDigit(gc.get(Calendar.MONTH) + 1) + " "
				+ toTwoDigit(gc.get(Calendar.DATE)) + "-"
				+ toTwoDigit(gc.get(Calendar.HOUR_OF_DAY)) + ":"
				+ toTwoDigit(gc.get(Calendar.MINUTE)) + ":" + toTwoDigit(gc
					.get(Calendar.SECOND)));
	}

	public static String toTwoDigit(int aNumber) {
		if (aNumber < 10) {
			return ("0" + aNumber);
		} else {
			return (aNumber + "");
		}
	}

	public static String[] stringArray(String value) {
		return value.split(",");
	}

	public static String componentPrefix(String components, String reg) {
		String component[] = components.split(reg);
		return component[0];
	}

	public static String componentSuffix(String components, String reg) {
		String component[] = components.split(reg);
		return component[component.length - 1];
	}

	public static String getIdFromHref(String href) {
		String[] tokens = href.split("/");
		return tokens[tokens.length - 1];
	}

	public static String getIdFromUrnPattern(String urn) {
		String[] tokens = urn.split(":");
		return tokens[tokens.length - 1];
	}

	public static String getPathVariable(String uri, String pathVariable) {
		String pathVariableValue = null;
		String[] tokens = uri.split("/");
		for (int i = 0; i < tokens.length; i++) {
			if (tokens[i].equalsIgnoreCase(pathVariable)) {
				if (i < tokens.length - 1) {
					pathVariableValue = tokens[i + 1];
					break;
				}
			}
		}
		return pathVariableValue;
	}
	
	public static String[] toArray(String value, String regex){
		return ApplicationUtils.isEmpty(value) ? null : ApplicationUtils.isEmpty(regex) ? null : value.split(regex);
	}
}
