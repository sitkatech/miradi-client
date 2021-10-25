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

import java.util.Collection;
import java.util.Collections;
import java.util.Vector;

import org.martus.util.UnicodeStringWriter;
import org.martus.util.UnicodeWriter;
import org.miradi.migrations.RawObject;
import org.miradi.migrations.RawPool;
import org.miradi.migrations.RawProject;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.project.threatrating.ThreatRatingBundle;

public class RawProjectSaver extends AbstractMiradiProjectSaver
{
	public static String saveProject(final RawProject projectToUse) throws Exception
	{
		UnicodeWriter stringWriter = UnicodeStringWriter.create();
		saveProject(projectToUse, stringWriter);
		
		return stringWriter.toString();
	}

	public static void saveProject(final RawProject projectToUse, final UnicodeWriter writerToUse) throws Exception
	{
		final RawProjectSaver projectSaver = new RawProjectSaver(projectToUse, writerToUse);
		projectSaver.saveProject();
	}
	
	private RawProjectSaver(RawProject rawProjectToUse, UnicodeWriter writerToUse)
	{
		super(writerToUse);
		
		rawProject = rawProjectToUse;
	}

	@Override
	protected ORefList getSortedRefsForType(int type) throws Exception
	{
		RawPool rawPool = getRawProject().getRawPoolForType(type);
		if (rawPool == null)
			return new ORefList();
		
		return rawPool.getSortedReflist();
	}

	@Override
	protected void writeObjectUpdateEntries(ORef ref) throws Exception
	{
		//FIXME medium - writeObjectUpdateEntries should be made generic and pulled up, to avoid 
		//writing in leaf classes and duplication between leaf classes. 
		RawObject rawObject = getRawProject().findObject(ref);
		Vector<String> fieldTags = new Vector<String>(rawObject.keySet());
		Collections.sort(fieldTags);
		for(String tag : fieldTags)
		{
			String data = rawObject.get(tag);
			if (data.length() > 0)
			{
				MiradiProjectFileUtilities.writeUpdateObjectLine(getWriter(), ref, tag, data);
			}
		}
	}

	@Override
	protected int getHighVersion()
	{
		return getRawProject().getCurrentVersionRange().getHighVersion();
	}

	@Override
	protected int getLowVersion()
	{
		return getRawProject().getCurrentVersionRange().getLowVersion();
	}
	
	@Override
	protected String getExceptionLog() throws Exception
	{
		return getRawProject().getExceptionLog();
	}

	@Override
	protected String getQuarantineData() throws Exception
	{
		return getRawProject().getQuarantineData();
	}

	@Override
	protected Collection<ThreatRatingBundle> getSimpleThreatRatingBundles()
	{
		return getRawProject().getSimpleThreatRatingBundles();
	}

	@Override
	protected String getProjectMetadataId()
	{
		return getRawProject().getProjectMetadataId();
	}

	@Override
	protected int getHighestAssignedId()
	{
		return getRawProject().getHighestAssignedId();
	}

	@Override
	protected long getLastModifiedTime()
	{
		return getRawProject().getLastModifiedTime();
	}

	private RawProject getRawProject()
	{
		return rawProject;
	}
	
	private RawProject rawProject;
}
