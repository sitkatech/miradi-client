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

package org.miradi.migrations;

import java.util.HashMap;
import java.util.HashSet;

import org.miradi.main.EAM;
import org.miradi.objects.Indicator;
import org.miradi.utils.Translation;

abstract public class AbstractRemoveMultipleFieldMigration extends AbstractMigration
{
	public AbstractRemoveMultipleFieldMigration(RawProject rawProject, int typeToRemoveFieldsFromToUse)
	{
		super(rawProject);
		
		type = typeToRemoveFieldsFromToUse;
		fieldsToRemove = createFieldsToRemove();
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

	private static MigrationResult removeFields(RawObject rawObject, HashSet<String> fieldsToRemove)
	{
		MigrationResult migrationResult = MigrationResult.createSuccess();
		for(String tagToRemove : fieldsToRemove)
		{
			if (rawObject.containsKey(tagToRemove))
			{
				rawObject.remove(tagToRemove);
				String fieldBeingRemoved = Translation.fieldLabel(rawObject.getObjectType(), tagToRemove);
				String label = rawObject.get(Indicator.TAG_LABEL);
				HashMap<String, String> tokenReplacementMap = new HashMap<String, String>();
				tokenReplacementMap.put("%label", label);
				tokenReplacementMap.put("%fieldName", fieldBeingRemoved);
				String dataLossMessage = EAM.substitute(EAM.text("%fieldName will be removed, label = %label"), tokenReplacementMap);
				migrationResult.addDataLoss(dataLossMessage);
			}
		}
		return migrationResult;
	}
	
	protected class RemoveVisitor extends AbstractMigrationVisitor
	{
		public int getTypeToVisit()
		{
			return type;
		}

		@Override
		public MigrationResult internalVisit(RawObject rawObject) throws Exception
		{
			return removeFields(rawObject, fieldsToRemove);
		}
	}
	
	abstract protected HashSet<String> createFieldsToRemove();
	
	abstract public AbstractMigrationVisitor createMigrateForwardVisitor() throws Exception;
	
	abstract public AbstractMigrationVisitor createReverseMigrateVisitor() throws Exception;
	
	protected int type;
	private HashSet<String> fieldsToRemove;
}
