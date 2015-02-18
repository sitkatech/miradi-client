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

package org.miradi.migrations;

import java.util.HashMap;

abstract public class AbstractUpdateUiFieldData extends AbstractMigration
{
	public AbstractUpdateUiFieldData(RawProject rawProjectToUse, int typeToUse, String tagToUse, HashMap<String, String> fromToDataMapToUse)
	{
		super(rawProjectToUse);
		
		type = typeToUse;
		tag = tagToUse;
		fromToDataMap = fromToDataMapToUse;
	}

	@Override
	protected MigrationResult migrateForward() throws Exception
	{
		return visitAllObjectsInPool(new UpdateFieldDataVisitor());
	}

	@Override
	protected MigrationResult reverseMigrate() throws Exception
	{
		return MigrationResult.createSuccess();
	}
	
	
	private class UpdateFieldDataVisitor extends AbstractMigrationVisitor
	{
		public int getTypeToVisit()
		{
			return type;
		}

		@Override
		protected MigrationResult internalVisit(RawObject rawObject) throws Exception
		{
			String existingData = rawObject.get(tag);
			if (!fromToDataMap.containsKey(existingData))
				return MigrationResult.createSuccess();
			
			String replacementData = fromToDataMap.get(existingData);
			rawObject.put(tag, replacementData);
			
			return MigrationResult.createSuccess();
		}
	}
	
	private int type;
	private String tag;
	private HashMap<String, String> fromToDataMap;
}
