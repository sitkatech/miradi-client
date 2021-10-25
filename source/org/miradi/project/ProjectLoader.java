/* 
Copyright 2005-2021, Foundations of Success, Bethesda, Maryland
on behalf of the Conservation Measures Partnership ("CMP").
Material developed between 2005-2013 is jointly copyright by Beneficent Technology, Inc. ("The Benetech Initiative"), Palo Alto, California.

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
import org.miradi.ids.BaseId;
import org.miradi.objecthelpers.ORef;
import org.miradi.objectpools.BaseObjectPool;
import org.miradi.project.threatrating.ThreatRatingBundle;
import org.miradi.schemas.BaseObjectSchema;

public class ProjectLoader extends AbstractProjectLoader
{
	private ProjectLoader(final UnicodeReader readerToUse, Project projectToUse) throws Exception
	{
		super(readerToUse);
		
		project = projectToUse;
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
	protected void prepareToLoad() throws Exception
	{
		getProject().clear();
	}
	
	@Override
	protected void createObject(ORef ref) throws Exception
	{
		getProject().createObject(ref);
	}

	@Override
	protected void updateObjectWithData(ORef ref, String tag, String value)	throws Exception
	{
		final BaseObjectPool pool = getProject().getPool(ref.getObjectType());
		if (pool == null)
			return;
		
		BaseObjectSchema schema = pool.createBaseObjectSchema(getProject());
		if (schema.containsField(tag))
			getProject().setObjectData(ref, tag, value);
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

	@Override
	protected void updateHighestId(String value)
	{
		getProject().getProjectInfo().getNormalIdAssigner().idTaken(new BaseId(value));
	}

	@Override
	protected void updateProjectMetadataId(String value)
	{
		getProject().getProjectInfo().setMetadataId(new BaseId(value));
	}

	@Override
	protected void updateExceptionLog(String value) throws Exception
	{
		getProject().appendToExceptionLog(value);
	}	

	@Override
	protected void updateQuarantineFile(String value) throws Exception
	{
		getProject().appendToQuarantineFile(value);
	}
	
	private Project getProject()
	{
		return project;
	}
	
	private Project project;
}
