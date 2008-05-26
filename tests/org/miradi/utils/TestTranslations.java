/* 
Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
(on behalf of the Conservation Measures Partnership, "CMP") and 
Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 

This file is part of Miradi

Miradi is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License version 3, 
as published by the Free Software Foundation.

Miradi is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Miradi.  If not, see <http://www.gnu.org/licenses/>. 
*/
package org.miradi.utils;

import java.io.IOException;
import java.util.Locale;

import org.miradi.main.EAM;
import org.miradi.main.EAMTestCase;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.objects.Indicator;

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
	
	public void testBadLocale() throws Exception
	{
		Locale badLocale = new Locale("xx", "YY");
		try
		{
			EAM.setTranslationLocale(badLocale);
			fail("Should have thrown setting bad locale");
		}
		catch(IOException ignoreExpected)
		{
		}
	}

	public void testOtherLocale() throws Exception
	{
		Locale testingLocale = new Locale("test");
		EAM.setTranslationLocale(testingLocale);
		assertEquals(testingLocale, Translation.getTranslationLocale());
		
		EAM.setLogToString();
		String sampleText = "should indicate non-translated";
		assertEquals("<" + sampleText + ">", EAM.text(sampleText));
		
		assertEquals(FAKE_TRANSLATION, EAM.text(ENGLISH_STRING));
	}
	
	public void testFieldLabel() throws Exception
	{
		Translation.initialize();
		String badTag = "whoops";
		assertEquals("Didn't leave unknown tag alone?", badTag, EAM.fieldLabel(1, badTag));
		
		assertEquals("Didn't convert Indicator Label?", "Name", EAM.fieldLabel(ObjectType.INDICATOR, Indicator.TAG_LABEL));
	}
	
	public static String ENGLISH_STRING = "To be translated";
	public static String FAKE_TRANSLATION = "Aha! It worked!";
}
