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

package org.miradi.mpfMigrations;

import java.util.Collection;
import java.util.HashMap;

import org.miradi.ids.BaseId;
import org.miradi.ids.IdAssigner;
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
	
	public boolean containType(int type)
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

	private HashMap<Integer, RawPool> typeToRawPoolMap;
	private String exceptionLog;
	private String quarantineData;
	private HashMap<String, ThreatRatingBundle> threatRatingBundles;
	private String projectMetadataId;
	private long lastModifiedTime;
	private IdAssigner idAssigner;
}
