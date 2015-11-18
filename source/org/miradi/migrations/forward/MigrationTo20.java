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

import org.miradi.main.EAM;
import org.miradi.migrations.*;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.objects.Indicator;

import java.util.HashMap;
import java.util.Set;
import java.util.Vector;

public class MigrationTo20 extends AbstractMigration
{
	public MigrationTo20(RawProject rawProjectToUse)
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

		Vector<Integer> typesToVisit = getTypesToMigrate();

		for(Integer typeToVisit : typesToVisit)
		{
			final RemoveFieldsVisitor visitor = new RemoveFieldsVisitor(typeToVisit, reverseMigration);
			visitAllORefsInPool(visitor);
			final MigrationResult thisMigrationResult = visitor.getMigrationResult();
			if (migrationResult == null)
				migrationResult = thisMigrationResult;
			else
				migrationResult.merge(thisMigrationResult);
		}

		return migrationResult;
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
		return EAM.text("This migration adds a new planned leader resource field on Strategy, Task and Indicator objects.");
	}

	private Vector<Integer> getTypesToMigrate()
	{
		Vector<Integer> typesToMigrate = new Vector<Integer>();
		typesToMigrate.add(ObjectType.STRATEGY);
		typesToMigrate.add(ObjectType.TASK);
		typesToMigrate.add(ObjectType.INDICATOR);

		return typesToMigrate;
	}

	private class RemoveFieldsVisitor extends AbstractMigrationORefVisitor
	{
		public RemoveFieldsVisitor(int typeToVisit, boolean reverseMigration)
		{
			type = typeToVisit;
			isReverseMigration = reverseMigration;
			fieldsToLabelMap = createFieldsToLabelMapToModify();
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
				if (isReverseMigration)
					return removeFields(rawObject, fieldsToLabelMap);
			}

			return MigrationResult.createSuccess();
		}

		private HashMap<String, String> createFieldsToLabelMapToModify()
		{
			HashMap<String, String> fieldsToAdd = new HashMap<String, String>();
			fieldsToAdd.put(TAG_PLANNED_LEADER_RESOURCE, EAM.text("Planned Leader resource"));

			return fieldsToAdd;
		}

		private String createMessage(String message, String label)
		{
			if (label != null && label.length() > 0)
				return EAM.substituteSingleString(message, label);

			return "";
		}
		private MigrationResult removeFields(RawObject rawObject, HashMap<String, String> fieldsToLabelMap)
		{
			MigrationResult migrationResult = MigrationResult.createSuccess();
			Set<String> fieldsToRemove = fieldsToLabelMap.keySet();
			for(String tagToRemove : fieldsToRemove)
			{
				if (rawObject.containsKey(tagToRemove))
				{
					rawObject.remove(tagToRemove);
					String fieldName = fieldsToLabelMap.get(tagToRemove);
					String baseObjectlabel = createMessage(EAM.text("Label = %s"), rawObject.get(Indicator.TAG_LABEL));

					HashMap<String, String> tokenReplacementMap = new HashMap<String, String>();
					tokenReplacementMap.put("%label", baseObjectlabel);
					tokenReplacementMap.put("%fieldName", fieldName);
					String dataLossMessage = EAM.substitute(EAM.text("%fieldName will be removed. %label"), tokenReplacementMap);

					migrationResult.addDataLoss(dataLossMessage);
				}
			}
			return migrationResult;
		}

		private int type;
		private boolean isReverseMigration;
		private HashMap<String, String> fieldsToLabelMap;
	}

	public static final int VERSION_FROM = 19;
	public static final int VERSION_TO = 20;

	public static final String TAG_PLANNED_LEADER_RESOURCE = "PlannedLeaderResource";
}