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
package org.miradi.utils;

import java.net.URL;
import java.util.HashMap;
import java.util.Vector;

import org.martus.util.UnicodeReader;
import org.miradi.main.ResourcesHandler;

public class ConproMiradiHabitatCodeMap
{
	public HashMap<String, String> loadMap() throws Exception
	{
		HashMap habitatCodeMap = new HashMap();
		URL fileToImport = ResourcesHandler.getEnglishResourceURL(TNC_MIRADI_HABITAT_CODE_MAP_FILE_NAME);
		UnicodeReader reader = new UnicodeReader(fileToImport.openStream());
		try 
		{
			Vector fileVector = new DelimitedFileLoader().getDelimitedContents(reader);
			for (int i = 0; i < fileVector.size(); ++i)
			{
				Vector vector = (Vector) fileVector.get(i);
				Object miradiHabitatCode = vector.get(0);
				Object conProHabitatCode = vector.get(1);
				habitatCodeMap.put(miradiHabitatCode, conProHabitatCode);
			}
		}
		finally
		{
			reader.close();
		}
		
		return habitatCodeMap;
	}
	
	public static final String TNC_MIRADI_HABITAT_CODE_MAP_FILE_NAME = "/org/miradi/xml/conpro/TncMiradiHabitatAssociationCodeMap.txt";
}
