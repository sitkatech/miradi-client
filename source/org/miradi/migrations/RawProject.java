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

package org.miradi.migrations;

import java.util.Collection;
import java.util.HashMap;
import java.util.Set;
import java.util.Vector;

import org.miradi.ids.BaseId;
import org.miradi.ids.IdAssigner;
import org.miradi.objecthelpers.ORef;
import org.miradi.project.threatrating.SimpleThreatRatingFramework;
import org.miradi.project.threatrating.ThreatRatingBundle;

public class RawProject
{
	public RawProject()
	{
		typeToRawPoolMap = new HashMap<Integer, RawPool>();
		threatRatingBundles = new HashMap<String, ThreatRatingBundle>();
		idAssigner = new IdAssigner();
	}
	
	public boolean containsAnyObjectsOfType(int type)
	{
	  return typeToRawPoolMap.containsKey(type);
	}
	
	public void putTypeToNewPoolEntry(int type, RawPool rawPoolToAdd)
	{
		typeToRawPoolMap.put(type, rawPoolToAdd);
	}
	
	public RawPool getRawPoolForType(int type)	
	{		
		return typeToRawPoolMap.get(type);
	}
	
	public RawObject findObject(ORef ref)
	{
		return getRawPoolForType(ref.getObjectType()).get(ref);
	}
	
	public void deleteRawObject(ORef ref)
	{
		RawPool pool = getRawPoolForType(ref.getObjectType());
		pool.remove(ref);
	}

	public void setExceptionLog(final String exceptionLogToUse)
	{
		exceptionLog = exceptionLogToUse;
	}
	
	public String getExceptionLog()
	{
		return exceptionLog;
	}
	
	public void setQuarantineValue(String quarantineDataToUse)
	{
		quarantineData = quarantineDataToUse;
	}
	
	public String getQuarantineData()
	{
		return quarantineData;
	}
	
	public Collection<ThreatRatingBundle> getSimpleThreatRatingBundles()
	{
		return threatRatingBundles.values();
	}

	public String getProjectMetadataId()
	{
		return projectMetadataId;
	}

	public int getHighestAssignedId()
	{
		return idAssigner.getHighestAssignedId();
	}

	public long getLastModifiedTime()
	{
		return lastModifiedTime;
	}
	
	public void addThreatRatingBundle(ThreatRatingBundle newBundle)
	{
		String key = SimpleThreatRatingFramework.getBundleKey(newBundle.getThreatId(), newBundle.getTargetId());
		threatRatingBundles.put(key, newBundle);
	}
	
	public void setProjectMetadataId(String projectMetadataIdToUse)
	{
		projectMetadataId = projectMetadataIdToUse;
	}
	
	public void setHighestAssignedId(String highestAssignedIdToUse)
	{
		idAssigner.idTaken(new BaseId(highestAssignedIdToUse));
	}
	
	public void setLastModifiedTime(long lastModifiedTimeToUse)
	{
		lastModifiedTime = lastModifiedTimeToUse;
	}
	
	public BaseId getNextHighestId()
	{
		return idAssigner.takeNextId();
	}
	
	public RawObject createNewRawObject(ORef ref)
	{
		RawObject newRawObject = new RawObject();
		getRawPoolForType(ref.getObjectType()).put(ref, newRawObject);
		
		return newRawObject;
	}
	
	public VersionRange getCurrentVersionRange()
	{
		return currentVersionRange;
	}
	
	public void setCurrentVersionRange(VersionRange versionRange)
	{
		currentVersionRange = versionRange;
	}
	
	public void visitAllObjectsInPool(int objectType, Vector<RawObjectVisitor> visitors) throws Exception
	{
		if (!containsAnyObjectsOfType(objectType))
			return;
		
		RawPool rawPool = getRawPoolForType(objectType);
		Set<ORef> refs = rawPool.keySet();
		for(ORef ref : refs)
		{
			RawObject rawObject = rawPool.get(ref);
			visitAllVisitors(visitors, rawObject);
		}
	}

	private void visitAllVisitors(Vector<RawObjectVisitor> visitors, RawObject rawObject) throws Exception
	{
		for(RawObjectVisitor visitor : visitors)
		{
			visitor.visit(rawObject);	
		}
	}

	private HashMap<Integer, RawPool> typeToRawPoolMap;
	private String exceptionLog;
	private String quarantineData;
	private HashMap<String, ThreatRatingBundle> threatRatingBundles;
	private String projectMetadataId;
	private long lastModifiedTime;
	private IdAssigner idAssigner;
	private VersionRange currentVersionRange;
}
