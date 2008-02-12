/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */


import java.util.ListResourceBundle;

import org.miradi.utils.TestTranslations;

public class EAM_test_LOCALE extends ListResourceBundle
{
	protected Object[][] getContents()
	{
		return contents;
	}
	
	private static Object[][] contents =
	{
		{TestTranslations.ENGLISH_STRING, TestTranslations.FAKE_TRANSLATION},
	};
}
