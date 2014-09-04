package org.miradi.questions;

import java.io.InputStream;
import java.net.URL;
import java.util.Arrays;
import java.util.Vector;

import org.martus.util.UnicodeReader;
import org.miradi.main.EAM;
import org.miradi.main.ResourcesHandler;
import org.miradi.utils.IgnoreCaseStringComparator;
import org.miradi.utils.XmlUtilities2;
/* 
Copyright 2005-2014, Foundations of Success, Bethesda, Maryland
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

abstract public class AbstractLanguagesQuestion extends MultiSelectDynamicChoiceQuestion
{
	public AbstractLanguagesQuestion()
	{
		try
		{
			loadChoices();
		}
		catch(Exception e)
		{
			EAM.logException(e);
		}
	}
	
	@Override
	public ChoiceItem[] getChoices()
	{
		return choices;
	}
	
	private void loadChoices() throws Exception
	{
		Vector<ChoiceItem> loadedChoices = new Vector<ChoiceItem>();
		URL url = ResourcesHandler.getEnglishResourceURL("/resources/Languages.dat");
		loadedChoices.add(new ChoiceItem("", EAM.text("Unspecified")));
		
		InputStream inputStream = url.openStream();
		UnicodeReader reader = new UnicodeReader(inputStream);
		try
		{
			while(true)
			{
				String line = reader.readLine();
				if(line == null)
					break;
				
				if(line.indexOf('#') == 0)
					continue;
				
				// NOTE: File is ISO-639-3, with the header row commented out
				// Fields (delimited by tabs) are:
				// Id	Part2B	Part2T	Part1	Scope	Language_Type	Ref_Name	Comment
				line = XmlUtilities2.getXmlEncoded(line);
				String[] parts = line.split("\t");
				String threeLetterCode = parts[0];
				String oldThreeLetterCode = parts[1];
				String twoLetterCode = parts[3];
				String name = parts[6];
				
				String code = threeLetterCode;
				if(twoLetterCode.length() > 0)
					code = twoLetterCode;

				if(shouldIncludeLanguage(threeLetterCode, oldThreeLetterCode, twoLetterCode))
				{
					ChoiceItem languageChoice = new ChoiceItem(code, name);
					loadedChoices.add(languageChoice);
				}
			}
		}
		finally
		{
			reader.close();
		}
		
		choices = loadedChoices.toArray(new ChoiceItem[0]);
		Arrays.sort(choices, new IgnoreCaseStringComparator());
	}

	abstract protected boolean shouldIncludeLanguage(String threeLetterCode, String oldThreeLetterCode, String twoLetterCode);

	public String lookupLanguageCode(String languageCode)
	{
		return getValue(languageCode);
	}

	private ChoiceItem[] choices;

}
