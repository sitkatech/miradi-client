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

import org.miradi.migrations.forward.MigrationTo32;
import org.miradi.objects.Indicator;
import org.miradi.objects.Strategy;
import org.miradi.project.Project;

public class TestMigrationTo32 extends AbstractTestMigration
{
	public TestMigrationTo32(String name)
	{
		super(name);
	}
	
	public void testFieldsAddedAfterForwardMigration() throws Exception
	{
		Strategy strategy = getProject().createAndPopulateStrategy();
		Indicator indicator = getProject().createAndPopulateIndicator(strategy);

		RawProject rawProject = reverseMigrate(new VersionRange(MigrationTo32.VERSION_TO));
		migrateProject(rawProject, new VersionRange(Project.VERSION_HIGH));

        RawObject rawIndicator = rawProject.findObject(indicator.getRef());
        assertNotNull(rawIndicator);
        assertTrue("Field should have been added during forward migration?", rawIndicator.containsKey(MigrationTo32.TAG_RELEVANT_STRATEGY_ACTIVITY_SET));
	}
	
	public void testFieldsRemovedAfterReverseMigration() throws Exception
	{
		Strategy strategy = getProject().createAndPopulateStrategy();
		Indicator indicator = getProject().createAndPopulateIndicator(strategy);

		RawProject rawProject = reverseMigrate(new VersionRange(MigrationTo32.VERSION_TO));

        RawObject rawIndicator = rawProject.findObject(indicator.getRef());
        assertNotNull(rawIndicator);
        assertFalse("Field should have been removed during reverse migration?", rawIndicator.containsKey(MigrationTo32.TAG_RELEVANT_STRATEGY_ACTIVITY_SET));
	}

	@Override
	protected int getFromVersion()
	{
		return MigrationTo32.VERSION_FROM;
	}
	
	@Override
	protected int getToVersion()
	{
		return MigrationTo32.VERSION_TO;
	}
}
