/* 
Copyright 2005-2013, Foundations of Success, Bethesda, Maryland 
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

import java.util.Collection;
import java.util.Vector;

import org.martus.util.UnicodeStringWriter;
import org.miradi.migrations.RawObject;
import org.miradi.migrations.RawPool;
import org.miradi.migrations.RawProject;
import org.miradi.migrations.VersionRange;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.project.threatrating.ThreatRatingBundle;

public class RawProjectSaver extends AbstractMiradiProjectSaver
{
	public static void saveProject(final RawProject projectToUse, final UnicodeStringWriter writerToUse, VersionRange versionRangeToUse) throws Exception
	{
		saveProject(projectToUse, writerToUse, versionRangeToUse.getLowVersion(), versionRangeToUse.getHighVersion());
	}
	
	public static void saveProject(final RawProject projectToUse, final UnicodeStringWriter writerToUse, int versionLow, int versionHigh) throws Exception
	{
		final RawProjectSaver projectSaver = new RawProjectSaver(projectToUse, writerToUse);
		projectSaver.setVersionLow(versionLow);
		projectSaver.setVersionHigh(versionHigh);
		projectSaver.saveProject();
	}
	
	private RawProjectSaver(RawProject rawProjectToUse, UnicodeStringWriter writerToUse)
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
		for(String tag : fieldTags)
		{
			String data = rawObject.get(tag);
			if (data.length() > 0)
			{
				writeUpdateObjectLine(ref, tag, data);
			}
		}
	}

	@Override
	protected int getHighVersion()
	{
		return versionHigh;
	}

	@Override
	protected int getLowVersion()
	{
		return versionLow;
	}
	
	private void setVersionHigh(int versionHighToUse)
	{
		versionHigh = versionHighToUse;
	}

	private void setVersionLow(int versionLowToUse)
	{
		versionLow = versionLowToUse;
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
	private int versionLow;
	private int versionHigh;
}
