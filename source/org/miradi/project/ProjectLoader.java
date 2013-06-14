/* 
Copyright 2005-2011, Foundations of Success, Bethesda, Maryland 
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

package org.miradi.project;

import java.io.File;
import java.io.InputStream;

import org.martus.util.UnicodeReader;
import org.martus.util.UnicodeStringReader;
import org.miradi.objecthelpers.ORef;
import org.miradi.project.threatrating.ThreatRatingBundle;

public class ProjectLoader extends AbstractProjectLoader
{
	private ProjectLoader(final UnicodeReader readerToUse, Project projectToUse) throws Exception
	{
		super(readerToUse, projectToUse);
	}
	
	public static void loadProject(File projectFile, Project projectToLoad) throws Exception
	{
		String contents = UnicodeReader.getFileContents(projectFile);
		loadProject(new UnicodeStringReader(contents), projectToLoad);
	}

	public static void loadProject(InputStream inputStream, Project project) throws Exception
	{
		UnicodeReader reader = new UnicodeReader(inputStream);
		try
		{
			loadProject(reader, project);
		}
		finally
		{
			reader.close();
		}
	}

	public static void loadProject(final UnicodeReader reader, Project project) throws Exception
	{
		final ProjectLoader projectLoader = new ProjectLoader(reader, project);
		projectLoader.load();
	}
	
	public static long loadLastModifiedTime(final UnicodeStringReader reader) throws Exception
	{
		final ProjectLoader loader = new ProjectLoader(reader, null);
		return loader.getLastModified();
	}

	@Override
	protected void updateObjectWithData(ORef ref, String tag, String value)	throws Exception
	{
		if (getProject().doesBaseObjectContainField(ref, tag))
			getProject().setObjectData(ref, tag, value);
	}

	@Override
	protected void createObject(ORef ref) throws Exception
	{
		getProject().createObject(ref);
	}
	
	@Override
	protected void prepareToLoad() throws Exception
	{
		getProject().clear();
	}
	
	@Override
	protected void setLastModifiedTime(long lastModified)
	{
		getProject().setLastModified(lastModified);
	}

	@Override
	protected void saveSimpleThreatRatingBundle(ThreatRatingBundle bundle) throws Exception
	{
		getProject().getSimpleThreatRatingFramework().saveBundle(bundle);
	}
}
