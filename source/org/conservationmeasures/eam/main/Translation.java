/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.main;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;


public class Translation
{
	public static Locale getTranslationLocale()
	{
		return currentTranslationLocale;
	}

	public static void setTranslationLocale(Locale locale)
	{
		currentTranslationLocale = locale;
		currentResourceBundle = getResourceBundle(locale);
		Locale actualLocaleUsed = currentResourceBundle.getLocale();
		if(!locale.equals(actualLocaleUsed))
		{
			EAM.logWarning("Requested " + locale + " but fell back to: " + actualLocaleUsed);
		}
	}

	private static ResourceBundle getResourceBundle(Locale locale)
	{
		return ResourceBundle.getBundle("EAM", locale);
	}

	public static String text(String key)
	{
		if(currentTranslationLocale.equals(Locale.US))
			return extractPartToDisplay(key);
	
		try
		{
			ResourceBundle resources = getResourceBundle(currentTranslationLocale);
			return resources.getString(key);
		}
		catch(MissingResourceException e)
		{
			EAM.logWarning("Unknown translation key: " + key);
			return "<" + extractPartToDisplay(key) + ">";
		}
	}

	public static String extractPartToDisplay(String result)
	{
		int lastBar = result.lastIndexOf('|');
		if(lastBar >= 0)
			result = result.substring(lastBar + 1);
	
		return result;
	}

	private static Locale currentTranslationLocale = new Locale("en", "US");
	private static ResourceBundle currentResourceBundle;
}
