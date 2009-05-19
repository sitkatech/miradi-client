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
import java.util.Vector;

import org.martus.util.DirectoryUtils;
import org.martus.util.UnicodeReader;
import org.miradi.main.EAMTestCase;
import org.miradi.project.ObjectManager;
import org.miradi.project.Project;
import org.miradi.project.ProjectForTesting;
import org.miradi.utils.EnhancedJsonObject;
import org.miradi.utils.Utility;

abstract public class AbstractMigration extends EAMTestCase
{
	public AbstractMigration(String name)
	{
		super(name);
	}

	public void setUp() throws Exception
	{
		super.setUp();
		tempDirectory = createTempDirectory();
		project = new ProjectForTesting(getName());
	}

	public void tearDown() throws Exception
	{
		DirectoryUtils.deleteEntireDirectoryTree(tempDirectory);
		project.close();
		project = null;
		super.tearDown();
	}

	protected File createObjectsDir(File parentDir, String dirName)
	{
		File objectsDir = new File(parentDir, dirName);
		objectsDir.mkdirs();
		return objectsDir;
	}

	protected int[] createObjectFiles(File jsonDir, final int objectType, String[] jsonStrings) throws Exception
	{
		File objectsDir = DataUpgrader.createObjectsDir(jsonDir, objectType);
		Vector<Integer> objectIds = new Vector();
		
		for (int index = 0; index < jsonStrings.length; ++index)
		{
			EnhancedJsonObject json = new EnhancedJsonObject(jsonStrings[index]);
			int id = json.getId("Id").asInt();
			objectIds.add(id);
			createObjectFile(jsonStrings[index], id, objectsDir);
		}
		
		int[] ids = Utility.convertToIntArray(objectIds);
		File manifestFile = createManifestFile(objectsDir, ids);
		assertTrue(manifestFile.exists());
		
		return ids;
	}

	protected void createObjectFile(String jsonAsString, int id, File dir) throws Exception
	{
		File objectFile = new File(dir, Integer.toString(id));
		createFile(objectFile, jsonAsString);
		assertTrue(objectFile.exists());
	}

	protected void createObjectFile(File objectDir, String fileName, String objectString) throws Exception
	{
		File objectFile = new File(objectDir, fileName);
		createFile(objectFile, objectString);
	}

	protected String readFile(File file) throws IOException
	{
		UnicodeReader reader = new UnicodeReader(file);
		String contents = reader.readAll();
		reader.close();
		return contents;
	}

	protected File createManifestFile(File parent, int[] ids) throws Exception
	{
		return DataUpgrader.createManifestFile(parent, ids);
	}

	protected String buildManifestContents(int[] ids)
	{
		return DataUpgrader.buildManifestContents(ids);
	}

	protected void createFile(File file, String contents) throws Exception
	{
		DataUpgrader.createFile(file, contents);
	}

	protected File createJsonDir()
	{
		File jsonDir = new File(tempDirectory, "json");
		jsonDir.mkdirs();
		
		return jsonDir;
	}

	private Project getProject()
	{
		return project;
	}

	protected ObjectManager getObjectManager()
	{
		return getProject().getObjectManager();
	}

	protected File tempDirectory;
	ProjectForTesting project;
}
