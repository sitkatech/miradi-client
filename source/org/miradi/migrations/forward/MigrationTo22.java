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
import org.miradi.schemas.ResourcePlanSchema;
import org.miradi.utils.DateRange;
import org.miradi.utils.DateUnitEffort;
import org.miradi.utils.DateUnitEffortList;
import org.miradi.utils.HtmlUtilities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

public class MigrationTo22 extends AbstractMigration
{
	public MigrationTo22(RawProject rawProjectToUse)
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
				visitor = new RemoveResourcePlansVisitor(typeToVisit);
			else
				visitor = new CreateResourcePlansForResourceAssignmentsVisitor(typeToVisit);
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
		typesToMigrate.add(ObjectType.INDICATOR);

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
		return EAM.text("This migration creates resource plan entries for resource assignments (where a resource or date range is specified).");
	}

	private class CreateResourcePlansForResourceAssignmentsVisitor extends AbstractMigrationORefVisitor
	{
		public CreateResourcePlansForResourceAssignmentsVisitor(int typeToVisit)
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
				IdList resourcePlanIdList = new IdList(ResourcePlanSchema.getObjectType());
				RawPool resourcePlanPool = getOrCreateResourcePlanPool();

				ArrayList<RawObject> resourceAssignments = getResourceAssignments(rawObject);

				DateUnitEffortList resourcePlanDateUnitEffortList = buildResourcePlanDateUnitEffortList(resourceAssignments);

				for (RawObject resourceAssignment : resourceAssignments)
				{
					if (resourceAssignment.containsKey(TAG_DATEUNIT_EFFORTS) || resourceAssignment.containsKey(TAG_RESOURCE_ID))
					{
						RawObject newResourcePlan = new RawObject(ResourcePlanSchema.getObjectType());
						newResourcePlan.setData(TAG_DATEUNIT_EFFORTS, resourcePlanDateUnitEffortList.toJson().toString());
						if (resourceAssignment.containsKey(TAG_RESOURCE_ID))
							newResourcePlan.setData(TAG_RESOURCE_ID, resourceAssignment.getData(TAG_RESOURCE_ID));
						final BaseId nextHighestId = getRawProject().getNextHighestId();
						final ORef newResourcePlanRef = new ORef(ObjectType.RESOURCE_PLAN, nextHighestId);
						resourcePlanPool.put(newResourcePlanRef, newResourcePlan);
						resourcePlanIdList.add(nextHighestId);
					}
				}

				if (!resourcePlanIdList.isEmpty())
					rawObject.setData(TAG_RESOURCE_PLAN_IDS, resourcePlanIdList.toJson().toString());
			}

			if (rawObject != null && rawObject.containsKey(TAG_ASSIGNED_LEADER_RESOURCE))
			{
				String data = rawObject.get(TAG_ASSIGNED_LEADER_RESOURCE);
				rawObject.setData(TAG_PLANNED_LEADER_RESOURCE, data);
			}

			return MigrationResult.createSuccess();
		}

		private DateUnitEffortList buildResourcePlanDateUnitEffortList(ArrayList<RawObject> resourceAssignments) throws Exception
		{
			DateUnitEffortList resourcePlanDateUnitEffortList = new DateUnitEffortList();

			DateRange resourcePlanDateRange = null;
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
							DateUnit resourceAssignmentDateUnit = resourceAssignmentDateUnitEffort.getDateUnit();
							MultiCalendar cal = MultiCalendar.createFromGregorianYearMonthDay(resourceAssignmentDateUnit.getYear(), resourceAssignmentDateUnit.getMonth(), 1);
							DateUnit resourcePlanDateUnit = DateUnit.createMonthDateUnit(cal.toIsoDateString());
							resourcePlanDateRange = addToDateRange(resourcePlanDateRange, resourcePlanDateUnit);
						}
						else
						{
							resourcePlanDateRange = addToDateRange(resourcePlanDateRange, resourceAssignmentDateUnitEffort.getDateUnit());
						}
					}
				}
			}

			if (foundAtLeastOneProjectTotalDateUnit)
			{
				DateUnit resourcePlanDateUnit = new DateUnit();
				DateUnitEffort resourcePlanDateUnitEffort = new DateUnitEffort(resourcePlanDateUnit, 0);
				resourcePlanDateUnitEffortList.add(resourcePlanDateUnitEffort);
			}
			else if (resourcePlanDateRange != null)
			{
				DateUnit resourcePlanDateUnit = DateUnit.createFromDateRange(resourcePlanDateRange);
				DateUnitEffort resourcePlanDateUnitEffort = new DateUnitEffort(resourcePlanDateUnit, 0);
				resourcePlanDateUnitEffortList.add(resourcePlanDateUnitEffort);
			}

			return resourcePlanDateUnitEffortList;
		}

		private DateRange addToDateRange(DateRange dateRangeToAddTo, DateUnit dateUnit) throws Exception
		{
			if (dateRangeToAddTo == null)
				return dateUnit.asDateRange();
			else
				return DateRange.combine(dateRangeToAddTo, dateUnit.asDateRange());
		}

		private RawPool getOrCreateResourcePlanPool()
		{
			getRawProject().ensurePoolExists(ResourcePlanSchema.getObjectType());
			return getRawProject().getRawPoolForType(ResourcePlanSchema.getObjectType());
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

	private class RemoveResourcePlansVisitor extends AbstractMigrationORefVisitor
	{
		public RemoveResourcePlansVisitor(int typeToVisit)
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
			if (rawObject != null && rawObject.containsKey(TAG_RESOURCE_PLAN_IDS))
			{
				IdList resourcePlanIdList = new IdList(ResourcePlanSchema.getObjectType(), rawObject.get(TAG_RESOURCE_PLAN_IDS));

				if (resourcePlanIdList.isEmpty())
					return MigrationResult.createSuccess();

				for(BaseId resourcePlanId : resourcePlanIdList.asVector())
				{
					ORef resourcePlanRef = new ORef(ResourcePlanSchema.getObjectType(), resourcePlanId);
					getRawProject().deleteRawObject(resourcePlanRef);
				}

				rawObject.remove(TAG_RESOURCE_PLAN_IDS);

				String label = HtmlUtilities.convertHtmlToPlainText(rawObject.get(TAG_LABEL));
				String baseObjectLabel = createMessage(EAM.text("Label = %s"), label);

				HashMap<String, String> tokenReplacementMap = new HashMap<String, String>();
				tokenReplacementMap.put("%label", baseObjectLabel);
				tokenReplacementMap.put("%fieldName", TAG_RESOURCE_PLAN_IDS);
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

	public static final String TAG_ASSIGNED_LEADER_RESOURCE = "AssignedLeaderResource";
	public static final String TAG_PLANNED_LEADER_RESOURCE = "PlannedLeaderResource";
	public static final String TAG_RESOURCE_ASSIGNMENT_IDS = "AssignmentIds";
	public static final String TAG_DATEUNIT_EFFORTS = "Details";
	public static final String TAG_RESOURCE_ID = "ResourceId";
	public static final String TAG_RESOURCE_PLAN_IDS = "PlanIds";
	public static final String TAG_LABEL = "Label";

	public static final int VERSION_FROM = 21;
	public static final int VERSION_TO = 22;
}