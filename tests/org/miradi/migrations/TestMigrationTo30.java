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

import org.miradi.migrations.forward.MigrationTo30;
import org.miradi.objecthelpers.ORef;
import org.miradi.schemas.TaskSchema;

public class TestMigrationTo30 extends AbstractTestMigration
{
	public TestMigrationTo30(String name)
	{
		super(name);
	}
	
	public void testFieldsRemovedAfterReverseMigration() throws Exception
	{
		getProject().createAndPopulateStrategy();

		RawProject rawProject = reverseMigrate(new VersionRange(MigrationTo30.VERSION_TO));
        RawPool rawTaskPool = rawProject.getRawPoolForType(TaskSchema.getObjectType());
        for(ORef ref : rawTaskPool.keySet())
        {
            RawObject rawTask = rawTaskPool.get(ref);
            assertFalse("Field should have been removed during reverse migration?", rawTask.containsKey(MigrationTo30.TAG_IS_MONITORING_ACTIVITY));
        }
	}
	
	@Override
	protected int getFromVersion()
	{
		return MigrationTo30.VERSION_FROM;
	}
	
	@Override
	protected int getToVersion()
	{
		return MigrationTo30.VERSION_TO;
	}
}
