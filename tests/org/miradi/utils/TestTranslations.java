/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.miradi.utils;

import java.util.Locale;
import java.util.MissingResourceException;

import org.miradi.main.EAM;
import org.miradi.main.EAMTestCase;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.objects.Indicator;
import org.miradi.utils.Translation;

public class TestTranslations extends EAMTestCase
{
	public TestTranslations(String name)
	{
		super(name);
	}

	public void testExtractPartToDisplay()
	{
		assertEquals("whole thing", Translation.extractPartToDisplay("whole thing"));
		assertEquals("part", Translation.extractPartToDisplay("only|return|last|part"));
	}
	
	public void testDefaultLocale()
	{
		Locale defaultTranslationLocale = Translation.getTranslationLocale();
		assertEquals("en", defaultTranslationLocale.getLanguage());
		assertEquals("US", defaultTranslationLocale.getCountry());
		
		String sampleText = "should return unchanged";
		assertEquals(sampleText, EAM.text(sampleText));
		
		String sampleWithBar = "should strip all but|this";
		assertEquals("this", EAM.text(sampleWithBar));
	}
	
	public void testBadLocale()
	{
		Locale badLocale = new Locale("xx", "YY");
		try
		{
			EAM.setTranslationLocale(badLocale);
			fail("Should have thrown setting bad locale");
		}
		catch(MissingResourceException ignoreExpected)
		{
		}
	}

	public void testOtherLocale()
	{
		Locale testingLocale = new Locale("test", "LOCALE");
		EAM.setTranslationLocale(testingLocale);
		assertEquals(testingLocale, Translation.getTranslationLocale());
		
		EAM.setLogToString();
		String sampleText = "should indicate non-translated";
		assertEquals("<" + sampleText + ">", EAM.text(sampleText));
		
		assertEquals(FAKE_TRANSLATION, EAM.text(ENGLISH_STRING));
	}
	
	public void testFieldLabel() throws Exception
	{
		Translation.loadFieldLabels();
		String badTag = "whoops";
		assertEquals("Didn't leave unknown tag alone?", badTag, EAM.fieldLabel(1, badTag));
		
		assertEquals("Didn't convert Indicator Label?", "Name", EAM.fieldLabel(ObjectType.INDICATOR, Indicator.TAG_LABEL));
	}
	
	public static String ENGLISH_STRING = "To be translated";
	public static String FAKE_TRANSLATION = "Aha! It worked!";
}
