/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.main;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import javax.swing.JFrame;

public class EAM
{
	public static void main(String[] args)
	{
		setTranslationLocale(Locale.getDefault());
		
		JFrame mainWindow = new MainWindow();
		mainWindow.setVisible(true);
	}

	public static void setTranslationLocale(Locale locale)
	{
		currentResourceBundle = ResourceBundle.getBundle("eam", locale);
		if(!locale.equals(currentResourceBundle.getLocale()))
		{
			logWarning("Requested " + locale + " but fell back to: " + currentResourceBundle.getLocale());
		}
	}

	public static String text(String key)
	{
		try
		{
			String result = currentResourceBundle.getString(key);
			if(result.equals(key))
				result = extractPartToDisplay(result);
			
			return result;
		}
		catch(MissingResourceException e)
		{
			//e.printStackTrace();
			logWarning("Unknown translation key: " + key);
			return "<" + key + ">";
		}
	}

	private static String extractPartToDisplay(String result)
	{
		int lastBar = result.lastIndexOf('|');
		if(lastBar >= 0)
			result = result.substring(lastBar + 1);

		return result;
	}
	
	public static void logWarning(String text)
	{
		System.out.println("WARNING: " + text);
	}
	
	private static ResourceBundle currentResourceBundle;
}
