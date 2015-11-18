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

import org.miradi.migrations.forward.MigrationTo20;
import org.miradi.objecthelpers.ORef;
import org.miradi.objects.Strategy;

public class TestMigrationTo20 extends AbstractTestMigration
{
	public TestMigrationTo20(String name)
	{
		super(name);
	}
	
	public void testStrategyFieldsRemovedAfterReverseMigration() throws Exception
	{
		ORef strategyRef = getProject().createStrategy().getRef();
		testFieldsRemovedAfterReverseMigration(strategyRef);
	}
	
	public void testTaskFieldsRemovedAfterReverseMigration() throws Exception
	{
		Strategy strategy = getProject().createStrategy();
		ORef taskRef = getProject().createTask(strategy).getRef();
		testFieldsRemovedAfterReverseMigration(taskRef);
	}

	public void testIndicatorFieldsRemovedAfterReverseMigration() throws Exception
	{
		Strategy strategy = getProject().createStrategy();
		ORef indicatorRef = getProject().createIndicator(strategy).getRef();
		testFieldsRemovedAfterReverseMigration(indicatorRef);
	}

	private void testFieldsRemovedAfterReverseMigration(ORef objectRef) throws Exception
	{
		String sampleResourceData = getProject().createProjectResource().getRef().toString();
		getProject().fillObjectUsingCommand(objectRef, MigrationTo20.TAG_PLANNED_LEADER_RESOURCE, sampleResourceData);

		RawProject rawProject = reverseMigrate(new VersionRange(MigrationTo20.VERSION_TO));
        RawPool rawPoolForType = rawProject.getRawPoolForType(objectRef.getObjectType());
        for(ORef ref : rawPoolForType.keySet())
        {
            RawObject rawObject = rawPoolForType.get(ref);
            assertFalse("Field should have been removed during reverse migration?", rawObject.containsKey(MigrationTo20.TAG_PLANNED_LEADER_RESOURCE));
        }
	}

	@Override
	protected int getFromVersion()
	{
		return MigrationTo20.VERSION_FROM;
	}
	
	@Override
	protected int getToVersion()
	{
		return MigrationTo20.VERSION_TO;
	}
}
