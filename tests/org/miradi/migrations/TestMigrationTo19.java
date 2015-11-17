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

import org.miradi.migrations.forward.MigrationTo19;
import org.miradi.objecthelpers.ORef;
import org.miradi.objects.Strategy;
import org.miradi.project.Project;

public class TestMigrationTo19 extends AbstractTestMigration
{
	public TestMigrationTo19(String name)
	{
		super(name);
	}
	
	public void testFieldsRenamedAfterMigration() throws Exception
	{
		ORef strategyRef = getProject().createStrategy().getRef();
		String sampleResourceData = getProject().createProjectResource().getRef().toString();

		getProject().fillObjectUsingCommand(strategyRef, Strategy.TAG_ASSIGNED_LEADER_RESOURCE, sampleResourceData);

		RawProject reverseMigratedProject = reverseMigrate(new VersionRange(MigrationTo19.VERSION_TO));
		assertEquals("Data should not have been changed during field rename?", sampleResourceData, reverseMigratedProject.getData(strategyRef, MigrationTo19.LEGACY_TAG_LEADER_RESOURCE));

		migrateProject(reverseMigratedProject, new VersionRange(Project.VERSION_HIGH));
		assertEquals("Data should not have been changed during field rename?", sampleResourceData, reverseMigratedProject.getData(strategyRef, MigrationTo19.TAG_ASSIGNED_LEADER_RESOURCE));

		verifyFullCircleMigrations(new VersionRange(18, 19));
	}
	
	@Override
	protected int getFromVersion()
	{
		return MigrationTo19.VERSION_FROM;
	}
	
	@Override
	protected int getToVersion()
	{
		return MigrationTo19.VERSION_TO;
	}
}
