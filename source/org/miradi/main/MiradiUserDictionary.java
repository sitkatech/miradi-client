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

import java.util.HashSet;
import java.util.Iterator;
import java.util.Locale;

import org.miradi.utils.Translation;

import com.inet.jortho.FileUserDictionary;

public class MiradiUserDictionary extends FileUserDictionary
{
	public MiradiUserDictionary()
	{
		super(EAM.getHomeDirectory().getAbsolutePath());
	}

	@Override
	public Iterator<String> getWords(Locale locale)
	{
		Locale currentLocale = new Locale(Translation.getCurrentLanguageCode());
		Iterator<String> userWords = super.getWords(currentLocale);
		return new UserDictionaryWordsWithMiradiAlwaysIncluded(userWords);
	}
	
	static class UserDictionaryWordsWithMiradiAlwaysIncluded implements Iterator<String>
	{
		public UserDictionaryWordsWithMiradiAlwaysIncluded(Iterator<String> originalIterator)
		{
			original = originalIterator;
			if(original == null)
				original = new HashSet<String>().iterator();
			HashSet<String> extraWords = new HashSet<String>();
			extraWords.add("Miradi");
			extraWords.add("Benetech");
			extras = extraWords.iterator();
		}
		
		public boolean hasNext()
		{
			boolean result = original.hasNext();
			if(result)
				return result;
			
			return extras.hasNext();
		}

		public String next()
		{
			if(original.hasNext())
				return original.next();

			return extras.next();
		}

		public void remove()
		{
			original.remove();
		}
		
		private Iterator<String> original;
		private Iterator<String> extras;
	}
}
