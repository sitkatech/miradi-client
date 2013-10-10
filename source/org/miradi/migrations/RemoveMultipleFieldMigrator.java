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

import java.util.HashSet;

public class RemoveMultipleFieldMigrator
{
	public RemoveMultipleFieldMigrator(int typeToUse, HashSet<String> fieldsToRemoveToUse)
	{
		type = typeToUse;
		fieldsToRemove = fieldsToRemoveToUse;
	}

	public AbstractMigrationVisitor migrateForward() throws Exception
	{
		return new ForwardRemoveVisitor();
	}
	
	public AbstractMigrationVisitor reverseMigrate() throws Exception
	{
		return new DoNothingReverseMigrationVisitor(type);
	}
	
	private static MigrationResult removeFields(RawObject rawObject, HashSet<String> fieldsToRemove)
	{
		MigrationResult migrationResult = MigrationResult.createUninitializedResult();
		for(String tagToRemove : fieldsToRemove)
		{
			if (rawObject.containsKey(tagToRemove))
			{
				rawObject.remove(tagToRemove);
				migrationResult.merge(migrationResult.createDataLoss());
			}
		}
		return migrationResult;
	}
	
	private class ForwardRemoveVisitor extends AbstractMigrationVisitor
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
	
	private int type;
	private HashSet<String> fieldsToRemove;
}
