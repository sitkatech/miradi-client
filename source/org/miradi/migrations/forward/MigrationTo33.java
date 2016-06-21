/* 
Copyright 2005-2015, Foundations of Success, Bethesda, Maryland
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

package org.miradi.migrations.forward;

import org.martus.util.MultiCalendar;
import org.miradi.ids.BaseId;
import org.miradi.ids.IdList;
import org.miradi.main.EAM;
import org.miradi.migrations.*;
import org.miradi.objecthelpers.DateUnit;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.schemas.ResourceAssignmentSchema;
import org.miradi.schemas.TimeframeSchema;
import org.miradi.utils.DateRange;
import org.miradi.utils.DateUnitEffort;
import org.miradi.utils.DateUnitEffortList;
import org.miradi.utils.HtmlUtilities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

public class MigrationTo33 extends AbstractMigration
{
	public MigrationTo33(RawProject rawProjectToUse)
	{
		super(rawProjectToUse);
	}

	@Override
	protected MigrationResult migrateForward() throws Exception
	{
		return migrate(false);
	}

	@Override
	protected MigrationResult reverseMigrate() throws Exception
	{
		return migrate(true);
	}

	private MigrationResult migrate(boolean reverseMigration) throws Exception
	{
		MigrationResult migrationResult = MigrationResult.createUninitializedResult();
		AbstractMigrationORefVisitor visitor;
		Vector<Integer> typesToVisit = getTypesToMigrate();

		for(Integer typeToVisit : typesToVisit)
		{
			if (reverseMigration)
				visitor = new RemoveTimeframesVisitor(typeToVisit);
			else
				visitor = new CreateTimeframesForResourceAssignmentsVisitor(typeToVisit);
			visitAllORefsInPool(visitor);
			final MigrationResult thisMigrationResult = visitor.getMigrationResult();
			if (migrationResult == null)
				migrationResult = thisMigrationResult;
			else
				migrationResult.merge(thisMigrationResult);
		}

		return migrationResult;
	}

	private Vector<Integer> getTypesToMigrate()
	{
		Vector<Integer> typesToMigrate = new Vector<Integer>();
		typesToMigrate.add(ObjectType.STRATEGY);
		typesToMigrate.add(ObjectType.TASK);

		return typesToMigrate;
	}

	@Override
	protected int getToVersion()
	{
		return VERSION_TO;
	}

	@Override
	protected int getFromVersion()
	{
		return VERSION_FROM;
	}

	@Override
	protected String getDescription()
	{
		return EAM.text("This migration creates timeframe entries for resource assignments (where a resource or date range is specified).");
	}

	private class CreateTimeframesForResourceAssignmentsVisitor extends AbstractMigrationORefVisitor
	{
		public CreateTimeframesForResourceAssignmentsVisitor(int typeToVisit)
		{
			type = typeToVisit;
		}

		public int getTypeToVisit()
		{
			return type;
		}

		@Override
		public MigrationResult internalVisit(ORef rawObjectRef) throws Exception
		{
			RawObject rawObject = getRawProject().findObject(rawObjectRef);
			if (rawObject != null && rawObject.containsKey(TAG_RESOURCE_ASSIGNMENT_IDS))
			{
				IdList timeframeIdList = new IdList(TimeframeSchema.getObjectType());
				RawPool timeframePool = getOrCreateTimeframePool();

				ArrayList<RawObject> resourceAssignments = getResourceAssignments(rawObject);

				DateUnitEffortList timeframeDateUnitEffortList = buildTimeframeDateUnitEffortList(resourceAssignments);

				for (RawObject resourceAssignment : resourceAssignments)
				{
					if (resourceAssignment.containsKey(TAG_DATEUNIT_EFFORTS) || resourceAssignment.containsKey(TAG_RESOURCE_ID))
					{
						RawObject newTimeframe = new RawObject(TimeframeSchema.getObjectType());
						newTimeframe.setData(TAG_DATEUNIT_EFFORTS, timeframeDateUnitEffortList.toJson().toString());
						final BaseId nextHighestId = getRawProject().getNextHighestId();
						final ORef newTimeframeRef = new ORef(ObjectType.TIMEFRAME, nextHighestId);
						timeframePool.put(newTimeframeRef, newTimeframe);
						timeframeIdList.add(nextHighestId);
					}
				}

				if (!timeframeIdList.isEmpty())
					rawObject.setData(TAG_TIMEFRAME_IDS, timeframeIdList.toJson().toString());
			}

			return MigrationResult.createSuccess();
		}

		private DateUnitEffortList buildTimeframeDateUnitEffortList(ArrayList<RawObject> resourceAssignments) throws Exception
		{
			DateUnitEffortList timeframeDateUnitEffortList = new DateUnitEffortList();

			DateRange timeframeDateRange = null;
			boolean foundAtLeastOneProjectTotalDateUnit = false;

			for (RawObject resourceAssignment : resourceAssignments)
			{
				if (resourceAssignment.containsKey(TAG_DATEUNIT_EFFORTS))
				{
					DateUnitEffortList resourceAssignmentDateUnitEffortList = new DateUnitEffortList(resourceAssignment.get(TAG_DATEUNIT_EFFORTS));

					for (int index = 0; index < resourceAssignmentDateUnitEffortList.size(); ++index)
					{
						DateUnitEffort resourceAssignmentDateUnitEffort = resourceAssignmentDateUnitEffortList.getDateUnitEffort(index);
						if (resourceAssignmentDateUnitEffort.getDateUnit().isProjectTotal())
						{
							foundAtLeastOneProjectTotalDateUnit = true;
						}
						else if (resourceAssignmentDateUnitEffort.getDateUnit().isDay())
						{
							DateUnit timeframeDateUnit = createMonthDateUnit(resourceAssignmentDateUnitEffort.getDateUnit());
							timeframeDateRange = addToDateRange(timeframeDateRange, timeframeDateUnit);
						}
						else
						{
							timeframeDateRange = addToDateRange(timeframeDateRange, resourceAssignmentDateUnitEffort.getDateUnit());
						}
					}
				}
			}

			if (foundAtLeastOneProjectTotalDateUnit)
			{
				DateUnit timeframeDateUnit = new DateUnit();
				DateUnitEffort timeframeDateUnitEffort = new DateUnitEffort(timeframeDateUnit, 0);
				timeframeDateUnitEffortList.add(timeframeDateUnitEffort);
			}
			else if (timeframeDateRange != null)
			{
				DateRange startDateRange = new DateRange(timeframeDateRange.getStartDate(), timeframeDateRange.getStartDate());
				DateUnit startDateUnit = DateUnit.createFromDateRange(startDateRange);

				DateUnit timeframeStartDateUnit = createMonthDateUnit(startDateUnit);
				DateUnitEffort timeframeStartDateUnitEffort = new DateUnitEffort(timeframeStartDateUnit, 0);
				timeframeDateUnitEffortList.add(timeframeStartDateUnitEffort);

				DateRange endDateRange = new DateRange(timeframeDateRange.getEndDate(), timeframeDateRange.getEndDate());
				DateUnit endDateUnit = DateUnit.createFromDateRange(endDateRange);

				DateUnit timeframeEndDateUnit = createMonthDateUnit(endDateUnit);
				DateUnitEffort timeframeEndDateUnitEffort = new DateUnitEffort(timeframeEndDateUnit, 0);
				timeframeDateUnitEffortList.add(timeframeEndDateUnitEffort);
			}

			return timeframeDateUnitEffortList;
		}

		private DateUnit createMonthDateUnit(DateUnit dateUnit)
		{
			MultiCalendar cal = MultiCalendar.createFromGregorianYearMonthDay(dateUnit.getYear(), dateUnit.getMonth(), 1);
			return DateUnit.createMonthDateUnit(cal.toIsoDateString());
		}

		private DateRange addToDateRange(DateRange dateRangeToAddTo, DateUnit dateUnit) throws Exception
		{
			if (dateRangeToAddTo == null)
				return dateUnit.asDateRange();
			else
				return DateRange.combine(dateRangeToAddTo, dateUnit.asDateRange());
		}

		private RawPool getOrCreateTimeframePool()
		{
			getRawProject().ensurePoolExists(TimeframeSchema.getObjectType());
			return getRawProject().getRawPoolForType(TimeframeSchema.getObjectType());
		}

		private ArrayList<RawObject> getResourceAssignments(RawObject rawObject) throws Exception
		{
			ArrayList<RawObject> resourceAssignments = new ArrayList<RawObject>();

			IdList resourceAssignmentIdList = new IdList(ResourceAssignmentSchema.getObjectType(), rawObject.get(TAG_RESOURCE_ASSIGNMENT_IDS));

			for(BaseId resourceAssignmentId : resourceAssignmentIdList.asVector())
			{
				ORef resourceAssignmentRef = new ORef(ResourceAssignmentSchema.getObjectType(), resourceAssignmentId);
				RawObject rawResourceAssignment = getRawProject().findObject(resourceAssignmentRef);
				if (rawResourceAssignment != null)
					resourceAssignments.add(rawResourceAssignment);
			}

			return resourceAssignments;
		}

		private int type;
	}

	private class RemoveTimeframesVisitor extends AbstractMigrationORefVisitor
	{
		public RemoveTimeframesVisitor(int typeToVisit)
		{
			type = typeToVisit;
		}

		public int getTypeToVisit()
		{
			return type;
		}

		@Override
		public MigrationResult internalVisit(ORef rawObjectRef) throws Exception
		{
			MigrationResult migrationResult = MigrationResult.createSuccess();

			RawObject rawObject = getRawProject().findObject(rawObjectRef);
			if (rawObject != null && rawObject.containsKey(TAG_TIMEFRAME_IDS))
			{
				IdList timeframeIdList = new IdList(TimeframeSchema.getObjectType(), rawObject.get(TAG_TIMEFRAME_IDS));

				if (timeframeIdList.isEmpty())
					return MigrationResult.createSuccess();

				for(BaseId timeframeId : timeframeIdList.asVector())
				{
					ORef timeframeRef = new ORef(TimeframeSchema.getObjectType(), timeframeId);
					getRawProject().deleteRawObject(timeframeRef);
				}

				rawObject.remove(TAG_TIMEFRAME_IDS);

				String label = HtmlUtilities.convertHtmlToPlainText(rawObject.get(TAG_LABEL));
				String baseObjectLabel = createMessage(EAM.text("Name = %s"), label);

				HashMap<String, String> tokenReplacementMap = new HashMap<String, String>();
				tokenReplacementMap.put("%label", baseObjectLabel);
				tokenReplacementMap.put("%fieldName", TAG_TIMEFRAME_IDS_READABLE);
				String dataLossMessage = EAM.substitute(EAM.text("%fieldName will be removed. %label"), tokenReplacementMap);
				migrationResult.addDataLoss(dataLossMessage);
			}

			return migrationResult;
		}

		private String createMessage(String message, String label)
		{
			if (label != null && label.length() > 0)
				return EAM.substituteSingleString(message, label);

			return "";
		}

		private int type;
	}

	public static final String TAG_RESOURCE_ASSIGNMENT_IDS = "AssignmentIds";
	public static final String TAG_DATEUNIT_EFFORTS = "Details";
	public static final String TAG_RESOURCE_ID = "ResourceId";
	public static final String TAG_TIMEFRAME_IDS = "TimeframeIds";
	public static final String TAG_TIMEFRAME_IDS_READABLE = "Timeframe";
	public static final String TAG_LABEL = "Label";

	public static final int VERSION_FROM = 32;
	public static final int VERSION_TO = 33;
}