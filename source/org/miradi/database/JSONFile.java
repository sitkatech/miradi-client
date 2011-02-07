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
package org.miradi.database;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;

import org.json.JSONObject;
import org.martus.util.UnicodeReader;
import org.martus.util.UnicodeWriter;
import org.miradi.utils.EnhancedJsonObject;

public class JSONFile
{
	static public EnhancedJsonObject read(File file) throws IOException, ParseException
	{
		UnicodeReader reader = new UnicodeReader(file);
		try
		{
			return read(reader);
		}
		finally
		{
			reader.close();
		}
	}
	
	static public EnhancedJsonObject read(UnicodeReader reader) throws IOException, ParseException
	{
		String json = reader.readAll();
		return new EnhancedJsonObject(json);
	}
	
	static public void write(File file, JSONObject object) throws IOException
	{
		UnicodeWriter writer = new UnicodeWriter(file);
		try
		{
			write(writer, object);
		}
		finally
		{
			writer.close();
		}
	}
	
	static public void write(UnicodeWriter writer, JSONObject object) throws IOException
	{
		writer.write(object.toString());
	}
}