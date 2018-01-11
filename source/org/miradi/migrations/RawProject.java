/* 
Copyright 2005-2018, Foundations of Success, Bethesda, Maryland
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

import java.util.*;

import org.miradi.ids.BaseId;
import org.miradi.ids.IdAssigner;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.project.ProjectInterface;
import org.miradi.project.threatrating.SimpleThreatRatingFramework;
import org.miradi.project.threatrating.ThreatRatingBundle;

public class RawProject implements ProjectInterface
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
	
	public void ensurePoolExists(int objectType)
	{
		if (!containsAnyObjectsOfType(objectType))
			putTypeToNewPoolEntry(objectType, new RawPool());
	}
	
	public ORefList getAllRefsForType(int objectType)
	{
		return getRawPoolForType(objectType).getSortedReflist();
	}
	
	public RawPool getRawPoolForType(int type)	
	{		
		return typeToRawPoolMap.get(type);
	}
	
	public RawPool getRawPoolForType(ORef ref)	
	{		
		return getRawPoolForType(ref.getObjectType());
	}
	
	public RawObject findObject(ORef ref)
	{
		return getRawPoolForType(ref).findObject(ref);
	}
	
	public void deleteRawObject(ORef ref)
	{
		getRawPoolForType(ref).deleteObject(ref);
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
	
	public String getData(ORef ref, String tag)
	{
		return findObject(ref).getData(tag);
	}
	
	public void setObjectData(ORef ref, String tag, String data) throws Exception
	{
		findObject(ref).setData(tag, data);
	}

	public ORef createObject(int objectType)
	{
		return createObject(new ORef(objectType, getNextHighestId()));
	}
	
	public boolean containsObject(ORef ref)
	{
		return findObject(ref) != null;
	}

	public ORef createObject(ORef ref)
	{
		createObjectAndReturnObject(ref);

		return ref;
	}

	public RawObject createObjectAndReturnObject(ORef ref)
	{
		return getRawPoolForType(ref).createObject(ref);
	}
	
	public VersionRange getCurrentVersionRange()
	{
		return currentVersionRange;
	}
	
	public void setCurrentVersionRange(VersionRange versionRange)
	{
		currentVersionRange = versionRange;
	}
	
	public void deletePoolWithData(int objectType) throws Exception
	{
		RawPool pool = getRawPoolForType(objectType);
		if (pool == null)
			return;
		
		pool.clear();
		deleteEmptyPool(objectType);
	}
	
	public void deleteEmptyPool(int objectType) throws Exception
	{
		if (getRawPoolForType(objectType) == null)
			return;
		
		if (getRawPoolForType(objectType).size() != 0)
			throw new Exception("Cannot delete an pool with data");
		
		typeToRawPoolMap.remove(objectType);
	}
	
	public void visitAllObjectsInPool(RawObjectVisitor visitor) throws Exception
	{
		int objectType = visitor.getTypeToVisit();
		if (!containsAnyObjectsOfType(objectType))
			return;

		RawPool rawPool = getRawPoolForType(objectType);
		ORefList refList = rawPool.getSortedReflist();
		for (ORef ref : refList)
		{
			RawObject rawObject = rawPool.get(ref);
			visitor.visit(rawObject);
		}
	}
	
	public void visitAllORefsInPool(ORefVisitor visitor) throws Exception
	{
		int objectType = visitor.getTypeToVisit();
		if (!containsAnyObjectsOfType(objectType))
			return;

		RawPool rawPool = getRawPoolForType(objectType);
		ORefList refList = rawPool.getSortedReflist();
		for (ORef ref : refList)
		{
			visitor.visit(ref);
		}
	}

	public ORef getSingletonRef(int objectType) throws Exception
	{
		RawPool pool = getRawPoolForType(objectType);
		if (pool.size() != 1)
			throw new Exception("Singleton pool was empty for type =" + objectType);
		
		return pool.getSortedReflist().getFirstElement();
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
