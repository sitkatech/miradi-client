/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.database;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;

import org.conservationmeasures.eam.utils.EnhancedJsonObject;
import org.json.JSONObject;
import org.martus.util.UnicodeReader;
import org.martus.util.UnicodeWriter;

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