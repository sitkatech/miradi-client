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

package org.miradi.migrations;

import java.io.File;
import java.io.IOException;

import org.martus.util.UnicodeReader;
import org.martus.util.UnicodeStringReader;
import org.miradi.objecthelpers.ORef;
import org.miradi.project.AbstractProjectLoader;
import org.miradi.project.threatrating.ThreatRatingBundle;

public class RawProjectLoader extends AbstractProjectLoader
{
	public static RawProject loadProject(File projectFile) throws Exception
	{
		String projectToMigrateAsString = UnicodeReader.getFileContents(projectFile);
		
		return loadProject(projectToMigrateAsString);
	}
	
	public static RawProject loadProject(String mpfAsString) throws Exception, IOException
	{
		VersionRange versionRange = loadVersionRange(new UnicodeStringReader(mpfAsString));
		final RawProjectLoader projectLoader = new RawProjectLoader(new UnicodeStringReader(mpfAsString));
		projectLoader.load();
		
		final RawProject rawProject = projectLoader.getRawProject();
		rawProject.setCurrentVersionRange(versionRange);
		return rawProject;
	}
	
	public static VersionRange loadVersionRange(final UnicodeReader reader) throws Exception
	{
		RawProjectLoader projectLoader = new RawProjectLoader(reader);
		String fileHeaderLine = projectLoader.getStringReader().readLine();
		
		return projectLoader.loadVersionRange(fileHeaderLine);
	}
	
	private RawProjectLoader(UnicodeReader readerToUse) throws Exception
	{
		super(readerToUse);
	}
	
	@Override
	protected void prepareToLoad() throws Exception
	{
		rawProject = new RawProject();
	}
	
	@Override
	protected void createObject(ORef ref) throws Exception
	{
		RawPool pool = getRawPool(ref);
		pool.put(ref, new RawObject(ref));
	}

	@Override
	protected void updateObjectWithData(ORef ref, String tag, String value)	throws Exception
	{
		RawPool pool = getRawPool(ref);
		RawObject rawObject = pool.get(ref);
		rawObject.put(tag, value);
	}
	
	private RawPool getRawPool(ORef ref)
	{
		if (!rawProject.containsAnyObjectsOfType(ref.getObjectType()))
			getRawProject().putTypeToNewPoolEntry(ref.getObjectType(), new RawPool());
		
		return getRawProject().getRawPoolForType(ref.getObjectType());
	}
	
	private RawProject getRawProject()
	{
		return rawProject;
	}

	@Override
	protected void setLastModifiedTime(long lastModified)
	{
		getRawProject().setLastModifiedTime(lastModified);
	}

	@Override
	protected void saveSimpleThreatRatingBundle(ThreatRatingBundle bundle) throws Exception
	{
		getRawProject().addThreatRatingBundle(bundle);
	}

	@Override
	protected void updateHighestId(String value)
	{
		getRawProject().setHighestAssignedId(value);
	}

	@Override
	protected void updateProjectMetadataId(String value)
	{
		getRawProject().setProjectMetadataId(value);
	}

	@Override
	protected void updateExceptionLog(String value) throws Exception
	{
		getRawProject().setExceptionLog(value);
	}

	@Override
	protected void updateQuarantineFile(String value) throws Exception
	{
		getRawProject().setQuarantineValue(value);
	}
	
	@Override
	protected void validateHeaderLine(String fileHeaderLine) throws Exception
	{
	}
	
	private RawProject rawProject;
}
