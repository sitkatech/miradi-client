/* 
Copyright 2005-2015, Foundations of Success, Bethesda, Maryland
on behalf of the Conservation Measures Partnership ("CMP").
Material developed between 2005-2013 is jointly copyright by Beneficent Technology, Inc. ("The Benetech Initiative"), Palo Alto, California.

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
import java.io.File;

import org.martus.util.TestCaseEnhanced;
import org.miradi.main.Miradi;

public class TestLanguagePackFileFilter extends TestCaseEnhanced
{
	public TestLanguagePackFileFilter(String name)
	{
		super(name);
	}

	public void testFileFilter()
	{
		LanguagePackFileFilter filter = new LanguagePackFileFilter();
		
		File directory = new File(".");
		assertTrue(filter.accept(directory));
		assertFalse(filter.accept(directory.getParentFile(), directory.getName()));
		
		File notLanguagePack = new File(".", "MiradiContent-1.0-xx.jar");
		assertFalse(filter.accept(notLanguagePack));
		assertFalse(filter.accept(notLanguagePack.getParentFile(), notLanguagePack.getName()));

		File languagePackShortCode = new File(".", Miradi.LANGUAGE_PACK_PREFIX + "xx.jar");
		assertTrue(filter.accept(languagePackShortCode));
		assertTrue(filter.accept(languagePackShortCode.getParentFile(), languagePackShortCode.getName()));

		File languagePackLongCode = new File(".", Miradi.LANGUAGE_PACK_PREFIX + "yyy.jar");
		assertTrue(filter.accept(languagePackLongCode));
		assertTrue(filter.accept(languagePackLongCode.getParentFile(), languagePackLongCode.getName()));

		File languagePackTooShortCode = new File(".", Miradi.LANGUAGE_PACK_PREFIX + "z.jar");
		assertFalse(filter.accept(languagePackTooShortCode));
		assertFalse(filter.accept(languagePackTooShortCode.getParentFile(), languagePackTooShortCode.getName()));

		File languagePackTooLongCode = new File(".", Miradi.LANGUAGE_PACK_PREFIX + "zzzz.jar");
		assertFalse(filter.accept(languagePackTooLongCode));
		assertFalse(filter.accept(languagePackTooLongCode.getParentFile(), languagePackTooLongCode.getName()));

	}
}
