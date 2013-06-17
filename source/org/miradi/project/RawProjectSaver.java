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
import org.miradi.mpfMigrations.RawObject;
import org.miradi.mpfMigrations.RawPool;
import org.miradi.mpfMigrations.RawProject;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objecthelpers.ORefSet;
import org.miradi.project.threatrating.ThreatRatingBundle;

public class RawProjectSaver extends AbstractMiradiProjectSaver
{
	public static void saveProject(final RawProject projectToUse, final UnicodeStringWriter writerToUse) throws Exception
	{
		final RawProjectSaver projectSaver = new RawProjectSaver(projectToUse, writerToUse);
		projectSaver.saveProject();
	}
	
	private RawProjectSaver(RawProject rawProjectToUse, UnicodeStringWriter writerToUse)
	{
		super(writerToUse);
		
		rawProject = rawProjectToUse;
	}

	@Override
	protected void writePoolForType(int type) throws Exception
	{
		RawPool rawPool = getRawProject().getRawPoolForType(type);
		if (rawPool == null)
			return;
		
		ORefList refList = new ORefSet(rawPool.keySet()).toRefList();
		refList.sort();
		writeObjects(rawPool, refList);
	}
	
	private void writeObjects(RawPool rawPool, ORefList sortedObjectRefs) throws Exception
	{
		for (int index = 0; index < sortedObjectRefs.size(); ++index)
		{
			final ORef ref = sortedObjectRefs.get(index);
			writeNewObjectEntry(ref);
			writeObjectUpdateEntries(rawPool, ref);
		}
	}

	private void writeObjectUpdateEntries(RawPool rawPool, ORef ref) throws Exception
	{
		RawObject rawObject = rawPool.get(ref);
		Vector<String> fieldTags = new Vector<String>(rawObject.keySet());
		for(String tag : fieldTags)
		{
			String data = rawObject.get(tag);
			if (data.length() > 0)
			{
				writeRefTagValue(UPDATE_OBJECT_CODE, ref, tag, data);
			}
		}
	}

	private void writeNewObjectEntry(ORef ref) throws Exception
	{
		writeValue(CREATE_OBJECT_CODE, createSimpleRefString(ref));
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
