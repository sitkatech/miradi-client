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

import java.util.Set;

import org.martus.util.UnicodeStringReader;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ObjectType;

public class IndicatorFutureStatusDataToNewFutureStatusTypeMigration
{
	public static RawProject migrate(String mpfAsString) throws Exception
	{
		UnicodeStringReader reader  = new UnicodeStringReader(mpfAsString);
		RawProject rawProject = RawProjectLoader.loadProject(reader);
		
		return moveIndicatorFutureStatuses(rawProject);
	}

	private static RawProject moveIndicatorFutureStatuses(RawProject rawProject)
	{
		RawPool indicatorRawPool = rawProject.getRawPoolForType(ObjectType.INDICATOR);
		Set<ORef> indicatorRefs = indicatorRawPool.keySet();
		RawPool futureStatusPool = new RawPool();
		for(ORef indicatorRef : indicatorRefs)
		{
			RawObject indicator = indicatorRawPool.get(indicatorRef);
			RawObject newFutureStatus = moveFutureStatusFields(indicator);
			//FIXME urgent - needs to have a valid ref here. 
			futureStatusPool.put(ORef.INVALID, newFutureStatus);
		}
		
		if (futureStatusPool.size() > 0)
			rawProject.putTypeToNewPoolEntry(ObjectType.FUTURE_STATUS, futureStatusPool);
		
		return rawProject;
	}

	private static RawObject moveFutureStatusFields(RawObject indicator)
	{
		if (!hasAnyFutureStatusData(indicator))
			return null;
		
		IndicatorFutureStatusTagsToFutureStatusTagsMap indicatorFutureStatusTagsToFutureStatusTagMap = new IndicatorFutureStatusTagsToFutureStatusTagsMap();
		Set<String> indicatorFutureStatusTags = indicatorFutureStatusTagsToFutureStatusTagMap.getIndicatorFutureStatusTags();
		RawObject futurStatus = new RawObject();
		Set<String> fieldTags = indicator.keySet();
		for(String indicatorFutureStatusTag : indicatorFutureStatusTags)
		{
			if (fieldTags.contains(indicatorFutureStatusTag))
			{
				String data = indicator.get(indicatorFutureStatusTag);
				String futureStatusTag = indicatorFutureStatusTagsToFutureStatusTagMap.get(indicatorFutureStatusTag);
				
				futurStatus.put(futureStatusTag, data);
			}
		}
		
		return futurStatus;
	}

	private static boolean hasAnyFutureStatusData(RawObject indicator)
	{
		IndicatorFutureStatusTagsToFutureStatusTagsMap indicatorFutureStatusTagsToFutureStatusTagMap = new IndicatorFutureStatusTagsToFutureStatusTagsMap();
		Set<String> indicatorFutureStatusTags = indicatorFutureStatusTagsToFutureStatusTagMap.getIndicatorFutureStatusTags();
		Set<String> allIndicatorTags = indicator.keySet();
		for(String indicatorFutureStatusTag : indicatorFutureStatusTags)
		{			
			if (allIndicatorTags.contains(indicatorFutureStatusTag))
			{
				String data = indicator.get(indicatorFutureStatusTag);
				return data.length() > 0;
			}
		}
		
		return false;
	}
}
