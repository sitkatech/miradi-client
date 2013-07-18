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

package org.miradi.mpfMigrations.forwardMigrations;

import java.util.HashSet;
import java.util.Set;

import org.miradi.ids.BaseId;
import org.miradi.mpfMigrations.AbstractForwardMigration;
import org.miradi.mpfMigrations.IndicatorFutureStatusTagsToFutureStatusTagsMap;
import org.miradi.mpfMigrations.RawObject;
import org.miradi.mpfMigrations.RawObjectVisitor;
import org.miradi.mpfMigrations.RawPool;
import org.miradi.mpfMigrations.RawProject;
import org.miradi.mpfMigrations.VersionRange;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.objects.Indicator;
import org.miradi.schemas.FutureStatusSchema;
import org.miradi.schemas.IndicatorSchema;

public class MigrationTo4 extends AbstractForwardMigration
{
	public MigrationTo4(RawProject rawProjectToUse)
	{
		super(rawProjectToUse);
	}
	
	@Override
	public VersionRange getMigratableVersionRange() throws Exception
	{
		return new VersionRange(VERSION_LOW, VERSION_HIGH);
	}

	@Override
	protected RawObjectVisitor createRawObjectVisitor()
	{
		return new IndicatorVisitor();
	}
	
	@Override
	protected int getTypeToMigrate()
	{
		return IndicatorSchema.getObjectType();
	}
	
	private class IndicatorVisitor implements RawObjectVisitor
	{
		public void visit(RawObject rawObject)
		{
			RawPool futureStatusPool = getFutureStatusPool();
			if (hasAnyFutureStatusData(rawObject))
			{
				RawObject newFutureStatus = new RawObject();
				moveFutureStatusFields(rawObject, newFutureStatus);
				final BaseId nextHighestId = getRawProject().getNextHighestId();
				final ORef newFutureStatusRef = new ORef(ObjectType.FUTURE_STATUS, nextHighestId);
				futureStatusPool.put(newFutureStatusRef, newFutureStatus);
				rawObject.put(Indicator.TAG_FUTURE_STATUS_REFS, new ORefList(newFutureStatusRef));
			}
			
			if (futureStatusPool.size() > 0)
				getRawProject().putTypeToNewPoolEntry(ObjectType.FUTURE_STATUS, futureStatusPool);
		}

		private RawPool getFutureStatusPool()
		{
			final RawPool futureStatusPool = getRawProject().getRawPoolForType(FutureStatusSchema.getObjectType());
			if (futureStatusPool == null)
				return new RawPool();
			
			return futureStatusPool;
		}
		
		private void moveFutureStatusFields(RawObject indicator, RawObject futurStatus)
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

		private boolean hasAnyFutureStatusData(RawObject indicator)
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
	
	private static final int VERSION_LOW = 3;
	private static final int VERSION_HIGH = 3;
}
