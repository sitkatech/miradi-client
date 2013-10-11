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

import org.miradi.utils.BiDirectionalHashMap;


public class RenameMultipleFieldMigrator
{
	public RenameMultipleFieldMigrator(int typeToUse, BiDirectionalHashMap oldToNewTagMapToUse)
	{
		type = typeToUse;
		
		oldToNewTagMap = oldToNewTagMapToUse;
	}

	public AbstractMigrationVisitor migrateForward() throws Exception
	{
		return new ForwardRenameVisitor();
	}
	
	public AbstractMigrationVisitor reverseMigrate() throws Exception
	{
		return  new ReverseRenameVisitor();
	}
	
	private static MigrationResult renameFields(RawObject rawObject, BiDirectionalHashMap oldToNewTagMapToUse)
	{
		MigrationResult migrationResult = MigrationResult.createSuccess();
		HashSet<String> legacyTags = oldToNewTagMapToUse.getKeys();
		for(String legacyTag : legacyTags)
		{
			if (rawObject.containsKey(legacyTag))
			{
				String newTag = oldToNewTagMapToUse.getValue(legacyTag);
				String data = rawObject.get(legacyTag);
				rawObject.remove(legacyTag);
				rawObject.put(newTag, data);
				migrationResult.merge(migrationResult.createDataLoss());
			}
		}
		return migrationResult;
	}
	
	private class ForwardRenameVisitor extends AbstractMigrationVisitor
	{
		public int getTypeToVisit()
		{
			return type;
		}

		@Override
		public MigrationResult internalVisit(RawObject rawObject) throws Exception
		{
			return renameFields(rawObject, oldToNewTagMap);
		}
	}
	
	private class ReverseRenameVisitor extends AbstractMigrationVisitor
	{
		public int getTypeToVisit()
		{
			return type;
		}

		@Override
		public MigrationResult internalVisit(RawObject rawObject) throws Exception
		{
			return renameFields(rawObject, oldToNewTagMap.reverseMap());
		}
	}
	
	private int type;
	private BiDirectionalHashMap oldToNewTagMap;
}
