/* 
Copyright 2005-2021, Foundations of Success, Bethesda, Maryland
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

import org.miradi.migrations.forward.MigrationTo35;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.objects.Indicator;
import org.miradi.objects.Strategy;


public class TestMigrationTo35 extends AbstractTestMigration
{
	public TestMigrationTo35(String name)
	{
		super(name);
	}

	public void testMethodCreatedFromTasksByForwardMigration() throws Exception
	{
		Strategy strategy = getProject().createStrategy();
		getProject().createActivity(strategy);
		Indicator indicator = getProject().createIndicator(strategy);
		getProject().createAndPopulateMethod(indicator, "Method to Migrate");

		RawProject reverseMigratedProject = reverseMigrate(new VersionRange(MigrationTo35.VERSION_TO));

		verifyFullCircleMigrations(new VersionRange(34, 35));
	}

	public void testTasksCreatedFromMethodsByReverseMigration() throws Exception
	{
		Strategy strategy = getProject().createStrategy();
		Indicator indicator = getProject().createIndicator(strategy);
		getProject().createActivity(strategy);
		getProject().createAndPopulateMethod(indicator, "Method to Migrate");

		int taskCountBefore = getProject().getTaskPool().size();
		int methodCountBefore = getProject().getMethodPool().size();

		RawProject rawProject = reverseMigrate(new VersionRange(MigrationTo35.VERSION_TO));

		RawPool taskPool = rawProject.getRawPoolForType(ObjectType.TASK);
		assertNotNull(taskPool);
		assertEquals(taskPool.keySet().size(), taskCountBefore + 1);

		RawPool methodPool = rawProject.getRawPoolForType(ObjectType.METHOD);
		assertNotNull(methodPool);
		assertEquals(methodPool.keySet().size(), methodCountBefore - 1);
	}

	@Override
	protected int getFromVersion()
	{
		return MigrationTo35.VERSION_FROM;
	}
	
	@Override
	protected int getToVersion()
	{
		return MigrationTo35.VERSION_TO;
	}
}
