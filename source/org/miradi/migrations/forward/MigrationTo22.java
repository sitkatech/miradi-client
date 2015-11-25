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

import org.miradi.ids.BaseId;
import org.miradi.ids.IdList;
import org.miradi.main.EAM;
import org.miradi.migrations.*;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.schemas.ResourcePlanSchema;
import org.miradi.utils.HtmlUtilities;

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
				visitor = new CreateResourcePlansForEmptyResourceAssignmentsVisitor(typeToVisit);
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
		return EAM.text("This migration creates resource plan entries for resource assignments where there are no allocated work units.");
	}

	private class CreateResourcePlansForEmptyResourceAssignmentsVisitor extends AbstractMigrationORefVisitor
	{
		public CreateResourcePlansForEmptyResourceAssignmentsVisitor(int typeToVisit)
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
			if (rawObject != null)
			{
				// TODO: MRD-5939 - need to do the following:
				//	- for each parent object, look at all of it's resource assignments
				//	- if all of the assignments are 'empty', create resource plans for each
				//	- update parent object's list of resource plan ids
			}

			return MigrationResult.createSuccess();
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

	public static final String TAG_RESOURCE_PLAN_IDS = "PlanIds";
	public static final String TAG_LABEL = "Label";

	public static final int VERSION_FROM = 21;
	public static final int VERSION_TO = 22;
}