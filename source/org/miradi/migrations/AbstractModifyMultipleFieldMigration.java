/* 
Copyright 2005-2022, Foundations of Success, Bethesda, Maryland
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

import java.util.HashMap;
import java.util.Set;

import org.miradi.main.EAM;
import org.miradi.objects.Indicator;
import org.miradi.utils.StringUtilities;

abstract public class AbstractModifyMultipleFieldMigration extends AbstractMigration
{
	public AbstractModifyMultipleFieldMigration(RawProject rawProject, int typeToRemoveFieldsFromToUse)
	{
		super(rawProject);

		type = typeToRemoveFieldsFromToUse;
		fieldsToRemove = createFieldsToLabelMapToModify();
	}
	
	@Override
	protected MigrationResult migrateForward() throws Exception
	{
		AbstractMigrationVisitor visitor = createMigrateForwardVisitor();

		return visitAllObjectsInPool(visitor);
	}
	
	@Override
	protected MigrationResult reverseMigrate() throws Exception
	{
		AbstractMigrationVisitor visitor = createReverseMigrateVisitor();

		return visitAllObjectsInPool(visitor);
	}

	private static MigrationResult removeFields(RawObject rawObject, HashMap<String, String> fieldsToLabelMap, String dataLossMessageSuffix)
	{
		MigrationResult migrationResult = MigrationResult.createSuccess();
		Set<String> fieldsToRemove = fieldsToLabelMap.keySet();
		for(String tagToRemove : fieldsToRemove)
		{
			if (rawObject.containsKey(tagToRemove))
			{
				rawObject.remove(tagToRemove);
				String fieldName = fieldsToLabelMap.get(tagToRemove);
				String baseObjectLabel = createMessage(EAM.text("Name = %s"), rawObject.get(Indicator.TAG_LABEL));

				HashMap<String, String> tokenReplacementMap = new HashMap<String, String>();
				tokenReplacementMap.put("%label", baseObjectLabel);
				tokenReplacementMap.put("%fieldName", fieldName);
				String dataLossMessage = EAM.substitute(EAM.text("%fieldName will be removed. %label"), tokenReplacementMap);

				if (!StringUtilities.isNullOrEmpty(dataLossMessageSuffix))
					dataLossMessage += dataLossMessageSuffix;

				migrationResult.addDataLoss(dataLossMessage);
			}
		}
		return migrationResult;
	}
	
	private static String createMessage(String message, String label)
	{
		if (label != null && label.length() > 0)
			return EAM.substituteSingleString(message, label);

		return "";
	}
	
	protected class RemoveVisitor extends AbstractMigrationVisitor
	{
		public  RemoveVisitor()
		{
			this("");
		}

		public  RemoveVisitor(String dataLossMessageSuffixToUse)
		{
			dataLossMessageSuffix = dataLossMessageSuffixToUse;
		}

		public int getTypeToVisit()
		{
			return type;
		}
		public String dataLossMessageSuffix;

		@Override
		public MigrationResult internalVisit(RawObject rawObject) throws Exception
		{
			return removeFields(rawObject, fieldsToRemove, dataLossMessageSuffix);
		}
	}
	
	abstract protected HashMap<String, String> createFieldsToLabelMapToModify();
	
	abstract public AbstractMigrationVisitor createMigrateForwardVisitor() throws Exception;
	
	abstract public AbstractMigrationVisitor createReverseMigrateVisitor() throws Exception;
	
	protected int type;
	private HashMap<String, String> fieldsToRemove;
}
