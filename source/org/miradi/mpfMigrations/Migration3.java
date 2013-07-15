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

import java.util.HashSet;
import java.util.Set;

import org.miradi.ids.BaseId;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.objects.Indicator;

public class Migration3 extends AbstractMigration
{
	@Override
	public RawProject forwardMigrate(RawProject rawProject) throws Exception
	{
		return moveIndicatorFutureStatusesToNewFutureStatus(rawProject);
	}

	private static RawProject moveIndicatorFutureStatusesToNewFutureStatus(RawProject rawProject) throws Exception
	{
		if (!rawProject.containType(ObjectType.INDICATOR))
			return rawProject;
		
		RawPool indicatorRawPool = rawProject.getRawPoolForType(ObjectType.INDICATOR);
		Set<ORef> indicatorRefs = indicatorRawPool.keySet();
		RawPool futureStatusPool = new RawPool();
		for(ORef indicatorRef : indicatorRefs)
		{
			RawObject indicator = indicatorRawPool.get(indicatorRef);
			if (hasAnyFutureStatusData(indicator))
			{
				RawObject newFutureStatus = new RawObject();
				moveFutureStatusFields(indicator, newFutureStatus);
				final BaseId nextHighestId = rawProject.getNextHighestId();
				final ORef newFutureStatusRef = new ORef(ObjectType.FUTURE_STATUS, nextHighestId);
				futureStatusPool.put(newFutureStatusRef, newFutureStatus);
				indicator.put(Indicator.TAG_FUTURE_STATUS_REFS, new ORefList(newFutureStatusRef));
			}
		}
		
		if (futureStatusPool.size() > 0)
			rawProject.putTypeToNewPoolEntry(ObjectType.FUTURE_STATUS, futureStatusPool);
		
		return rawProject;
	}

	private static void moveFutureStatusFields(RawObject indicator, RawObject futurStatus)
	{
		IndicatorFutureStatusTagsToFutureStatusTagsMap indicatorFutureStatusTagsToFutureStatusTagMap = new IndicatorFutureStatusTagsToFutureStatusTagsMap();
		Set<String> indicatorFutureStatusTags = indicatorFutureStatusTagsToFutureStatusTagMap.getIndicatorFutureStatusTags();
		Set<String> fieldTags = indicator.keySet();
		Set<String> futureStatusFieldsToRemoveFromIndicator = new HashSet<String>();
		for(String indicatorFutureStatusTag : indicatorFutureStatusTags)
		{
			if (fieldTags.contains(indicatorFutureStatusTag))
			{
				String data = indicator.get(indicatorFutureStatusTag);
				String futureStatusTag = indicatorFutureStatusTagsToFutureStatusTagMap.get(indicatorFutureStatusTag);
				
				futurStatus.put(futureStatusTag, data);
				futureStatusFieldsToRemoveFromIndicator.add(indicatorFutureStatusTag);
			}
		}

		for(String futureStatusTagToRemove : futureStatusFieldsToRemoveFromIndicator)
		{
			indicator.remove(futureStatusTagToRemove);
		}
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

	@Override
	public VersionRange getMigratableVersionRange() throws Exception
	{
		return new VersionRange(VERSION_LOW, VERSION_HIGH);
	}
	
	private static final int VERSION_LOW = 3;
	private static final int VERSION_HIGH = 3;
}
