package org.conservationmeasures.eam.database;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;

import org.conservationmeasures.eam.utils.EnhancedJsonObject;
import org.json.JSONObject;
import org.martus.util.DirectoryLock;
import org.martus.util.DirectoryLock.AlreadyLockedException;

public class FileBasedProjectServer extends ProjectServer
{
	public FileBasedProjectServer() throws IOException
	{
		super();
		lock = new DirectoryLock();
	}

	public void create(File directory) throws Exception
	{
		if(!isEmpty(directory))
			throw new RuntimeException("Can't create project in non-empty directory");
		
		openNonDatabaseStore(directory);
		writeVersion();
	}

	public void open(File directory) throws IOException, AlreadyLockedException
	{
		if(!isExistingProject(directory))
			throw new IOException("Can't open non-project, non-empty directory");

		openNonDatabaseStore(directory);
	}
	
	public void close() throws IOException
	{
		topDirectory = null;
		name = null;
		lock.close();
	}

	public boolean isOpen()
	{
		return lock.isLocked();
	}

	public boolean doesFileExist(File infoFile)
	{
		return infoFile.exists();
	}


	
	
	void writeJsonFile(File file, JSONObject json) throws IOException
	{
		file.getParentFile().mkdirs();
		JSONFile.write(file, json);
	}
	
	EnhancedJsonObject readJsonFile(File file) throws IOException, ParseException
	{
		return JSONFile.read(file);
	}
	
	boolean deleteJsonFile(File objectFile)
	{
		return objectFile.delete();
	}
	

	
	private boolean isEmpty(File directory)
	{
		String[] files = directory.list();
		if(files == null)
			return true;
		return (files.length == 0);
	}
	
	protected void openNonDatabaseStore(File directory) throws IOException, AlreadyLockedException
	{
		directory.mkdirs();
		lock.lock(directory);
		setTopDirectory(directory);
		createJsonDirectories();
		name = topDirectory.getName();
	}

	private void createJsonDirectories()
	{
		getJsonDirectory().mkdirs();
	}

	DirectoryLock lock;
}
