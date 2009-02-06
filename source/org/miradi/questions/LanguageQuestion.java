/* 
Copyright 2005-2009, Foundations of Success, Bethesda, Maryland 
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
package org.miradi.questions;

import java.io.InputStream;
import java.net.URL;
import java.util.Vector;

import org.martus.util.UnicodeReader;
import org.miradi.main.EAM;
import org.miradi.main.ResourcesHandler;

public class LanguageQuestion extends ChoiceQuestion
{
	public LanguageQuestion()
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
		Vector<ChoiceItem> loadedChoices = new Vector();
		URL url = ResourcesHandler.getEnglishResourceURL("/resources/Languages.dat");

		InputStream inputStream = url.openStream();
		UnicodeReader reader = new UnicodeReader(inputStream);
		try
		{
			while(true)
			{
				String line = reader.readLine();
				if(line == null)
					break;
				
				// NOTE: File was downloaded directly from ISO, and has these fields,
				// separated by vertical bars (|):
				// 3-letter code | other code | 2-letter code | name (in English) | name (in French)
				String[] parts = line.split("\\|");
				String code = parts[2];
				String name = parts[3];
				if(code.length() != 0)
					loadedChoices.add(new ChoiceItem(code, name));
			}
		}
		finally
		{
			reader.close();
		}
		
		choices = loadedChoices.toArray(new ChoiceItem[0]);
	}

	private ChoiceItem[] choices;
}
