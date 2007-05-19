package org.conservationmeasures.eam.database;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.HashMap;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.objects.ValueOption;
import org.conservationmeasures.eam.utils.EnhancedJsonObject;
import org.json.JSONObject;
import org.martus.util.DirectoryLock;

public class HybridFileBasedProjectServer extends FileBasedProjectServer
{
	public HybridFileBasedProjectServer() throws IOException
	{
		super();
		objects = new HashMap();
	}

	static EnhancedJsonObject vo = new ValueOption(new BaseId(1)).toJson();
	void writeJsonFile(File file, JSONObject json) throws IOException
	{
		objects.put(file.getAbsoluteFile(), json);
		file.getParentFile().mkdirs();
		//NOTE: the write here is to make the manefest work
		if (json instanceof EnhancedJsonObject)
			JSONFile.write(file, vo);
		else
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
		objects.remove(objectFile);
		return objectFile.delete();
	}
	
	DirectoryLock lock;
	HashMap objects;
}
