/* 
Copyright 2005-2014, Foundations of Success, Bethesda, Maryland
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

package org.miradi.migrations.forward;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.Vector;

import org.miradi.ids.BaseId;
import org.miradi.main.EAM;
import org.miradi.migrations.AbstractMigrationVisitor;
import org.miradi.migrations.AbstractSingleTypeMigration;
import org.miradi.migrations.IndicatorFutureStatusTagsToFutureStatusTagsMap;
import org.miradi.migrations.MigrationResult;
import org.miradi.migrations.RawObject;
import org.miradi.migrations.RawPool;
import org.miradi.migrations.RawProject;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.objects.Indicator;
import org.miradi.schemas.FutureStatusSchema;
import org.miradi.schemas.IndicatorSchema;

public class MigrationTo4 extends AbstractSingleTypeMigration
{
	public MigrationTo4(RawProject rawProjectToUse)
	{
		super(rawProjectToUse);
	}
	
	@Override
	protected MigrationResult migrateForward() throws Exception
	{
		final IndicatorVisitor visitor = new IndicatorVisitor();
		
		return visitAllObjectsInPool(visitor);
	}

	@Override
	protected MigrationResult reverseMigrate() throws Exception
	{
		final ReverseMigrationVisitor visitor = new ReverseMigrationVisitor();
		visitAllObjectsInPool(visitor);
		getRawProject().deleteEmptyPool(ObjectType.FUTURE_STATUS);
		
		return visitor.getMigrationResult();
	}

	@Override
	protected int getToVersion()
	{
		return 4;
	}
	
	@Override
	protected int getFromVersion() 
	{
		return 3;
	}
	
	@Override
	protected String getDescription()
	{
		return EAM.text("This migration creates new future status objects from indicator's future status fields");
	}
	
	private class IndicatorVisitor extends AbstractMigrationVisitor
	{
		public int getTypeToVisit()
		{
			return IndicatorSchema.getObjectType();
		}

		@Override
		public MigrationResult internalVisit(RawObject rawObject) throws Exception
		{
			if (!hasAnyFutureStatusData(rawObject))
				return MigrationResult.createSuccess();

			RawPool futureStatusPool = getOrCreateFutureStatusPool();
			RawObject newFutureStatus = new RawObject(FutureStatusSchema.getObjectType());
			moveFutureStatusFields(rawObject, newFutureStatus);
			final BaseId nextHighestId = getRawProject().getNextHighestId();
			final ORef newFutureStatusRef = new ORef(ObjectType.FUTURE_STATUS, nextHighestId);
			futureStatusPool.put(newFutureStatusRef, newFutureStatus);
			rawObject.put(TAG_FUTURE_STATUS_REFS, new ORefList(newFutureStatusRef));
			
			return MigrationResult.createSuccess();
		}

		private RawPool getOrCreateFutureStatusPool()
		{
			getRawProject().ensurePoolExists(FutureStatusSchema.getObjectType());	
				
			return  getRawProject().getRawPoolForType(FutureStatusSchema.getObjectType());
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
	
	private class ReverseMigrationVisitor extends AbstractMigrationVisitor
	{
		public int getTypeToVisit()
		{
			return IndicatorSchema.getObjectType();
		}

		@Override
		public MigrationResult internalVisit(RawObject indicator) throws Exception 
		{
			if (!indicator.containsKey(TAG_FUTURE_STATUS_REFS))
				return MigrationResult.createSuccess();
			
			ORefList futureStatusRefs = new ORefList(indicator.get(TAG_FUTURE_STATUS_REFS));
			if (futureStatusRefs.isEmpty())
				return MigrationResult.createSuccess();

			RawObject latestFutureStatus = getLatestFutureStatusRef(futureStatusRefs);
			moveFieldsFromFutureStatusToIndicator(indicator, latestFutureStatus);
			clearIndicatorFutureStatusField(indicator);
			deleteOrphanFutureStatuses(futureStatusRefs);
			if (futureStatusRefs.size() > 1)
			{
				String indicatorLabel = indicator.get(Indicator.TAG_LABEL);
				HashMap<String, String> tokenReplacementMap = new HashMap<String, String>();
				tokenReplacementMap.put("%indicatorLabel", indicatorLabel);
				tokenReplacementMap.put("%futureStatusCount", Integer.toString(futureStatusRefs.size()));
				final String dataLossMessage = EAM.substitute(EAM.text("%futureStatusCount future status(s) for indicator (%indicatorLabel) exist, only latest will be preserved"), tokenReplacementMap);
				
				return MigrationResult.createDataLoss(dataLossMessage);
			}
			
			return MigrationResult.createSuccess();
		}

		private void moveFieldsFromFutureStatusToIndicator(RawObject indicator, RawObject latestFutureStatus)
		{
			IndicatorFutureStatusTagsToFutureStatusTagsMap map = new IndicatorFutureStatusTagsToFutureStatusTagsMap();
			Set<String> indicatorFutureStatusTags = map.getIndicatorFutureStatusTags();
			for(String indicatorFutureStatusTag : indicatorFutureStatusTags)
			{
				String futureStatusTag = map.get(indicatorFutureStatusTag);
				String data = latestFutureStatus.get(futureStatusTag);
				if (data != null)
					indicator.put(indicatorFutureStatusTag, data);
			}
		}

		private void deleteOrphanFutureStatuses(ORefList futureStatusRefs) throws Exception
		{
			for(ORef futureStatusRef : futureStatusRefs)
			{
				getRawProject().deleteRawObject(futureStatusRef);	
			}
		}

		private void clearIndicatorFutureStatusField(RawObject indicator)
		{
			indicator.remove(TAG_FUTURE_STATUS_REFS);
		}

		private RawObject getLatestFutureStatusRef(ORefList futureStatusRefs)
		{
			Vector<RawObject> futureStatuses = new Vector<RawObject>();
			for(ORef futureStatusRef : futureStatusRefs)
			{
				futureStatuses.add(getRawProject().findObject(futureStatusRef));
			}
			
			Collections.sort(futureStatuses, new DateSorter());
			
			return futureStatuses.firstElement();
		}
	}
	
	private class DateSorter implements Comparator<RawObject>
	{
		public int compare(RawObject rawObject1, RawObject rawObject2)
		{
			String date1 = rawObject1.get(TAG_FUTURE_STATUS_DATE);
			String date2 = rawObject2.get(TAG_FUTURE_STATUS_DATE);
			if (date1 == null && date2 == null)
				return 0;
			
			if (date1 == null)
				return 1;
			
			if (date2 == null)
				return -1;
			
			return date2.compareTo(date1);
		}
	}
	
	private static final String TAG_FUTURE_STATUS_REFS = "FutureStatusRefs";
	private static final String TAG_FUTURE_STATUS_DATE = "Date";
}
