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
import java.util.HashMap;

import org.json.JSONObject;
import org.martus.util.DirectoryLock;
import org.miradi.utils.EnhancedJsonObject;

public class HybridFileBasedProjectServer extends FileBasedProjectServer
{
	public HybridFileBasedProjectServer() throws IOException
	{
		super();
		objects = new HashMap();
	}

	void writeJsonFile(File file, JSONObject json) throws IOException
	{
		objects.put(file.getAbsoluteFile(), json);
		file.getParentFile().mkdirs();
		if (!(json instanceof EnhancedJsonObject))
			JSONFile.write(file, json);
	}
	
	EnhancedJsonObject readJsonFile(File file) throws IOException, ParseException
	{
		JSONObject object = (JSONObject)objects.get(file.getAbsoluteFile()); 
		if (object instanceof EnhancedJsonObject)
			return (EnhancedJsonObject) objects.get(file.getAbsoluteFile());
		return JSONFile.read(file);
	}
	
	boolean deleteJsonFile(File objectFile)
	{
		if (objects.containsKey(objectFile))
		{
			objects.remove(objectFile);
			return true;
		}
		return objectFile.delete();
	}
	
	
	 public boolean doesFileExist(File infoFile)
	 {
		 if (objects.containsKey(infoFile))
			 return true;
		 return super.doesFileExist(infoFile);
	 }
	 
	 
	DirectoryLock lock;
	HashMap objects;
}
