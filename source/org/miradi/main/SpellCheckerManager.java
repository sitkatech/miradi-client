/* 
Copyright 2005-2010, Foundations of Success, Bethesda, Maryland 
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

package org.miradi.main;

import java.net.MalformedURLException;
import java.net.URL;

import org.miradi.utils.Translation;

import com.inet.jortho.SpellChecker;

public class SpellCheckerManager
{
	public static void initializeSpellChecker() throws MalformedURLException
	{
		String english = Translation.DEFAULT_LANGUAGE_CODE;
		URL dictionaryFolderURL = ResourcesHandler.getEnglishResourceURL("");
		URL dictionaryURL = new URL(dictionaryFolderURL, getDictionaryName(english));
		if(dictionaryURL != null)
		{
			System.out.println("Initializing English spell checker");
			SpellChecker.registerDictionaries(dictionaryFolderURL, english, english);
			SpellChecker.setUserDictionaryProvider(new MiradiUserDictionary());
		}
		else
		{
			EAM.logWarning("English dictionary not found");
		}
		// TODO: Probably remove this code, assuming we choose a different mechanism
		// for loading non-English dictionaries
//				String dictionaryName = getDictionaryName(languageCode);
//				ZipFile languagePackZip = new ZipFile(new File(jarFile.toURI()));
//				ZipEntry dictionaryEntry = languagePackZip.getEntry(dictionaryName);
//				if(dictionaryEntry != null)
//					initializeSpellChecker(new URL("jar:" + jarFile.toURI().toURL() + "!/"), languageCode);
	}

	private static String getDictionaryName(String languageCode)
	{
		String dictionaryName = "dictionary_" + languageCode + ".ortho";
		return dictionaryName;
	}
	
}
