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

import org.miradi.migrations.forward.MigrationTo17;
import org.miradi.project.ProjectForTesting;

public class TestMigrationTo17 extends AbstractTestMigration
{
	public TestMigrationTo17(String name)
	{
		super(name);
	}

	public void testCannotMigrateWhenBiophysicalFactorPresent() throws Exception
	{
		ProjectForTesting projectForTesting = getProject();
		projectForTesting.populateEverything();
		MigrationResult migrationResult = reverseMigrateReturnMigrationResult(projectForTesting, new VersionRange(MigrationTo17.TO_VERSION));
		assertTrue("Should not be able to reverse migrate (test project has Biophysical Factor / Result)", migrationResult.cannotMigrate());
	}

	public void testCanMigrateWhenNoBiophysicalFactorPresent() throws Exception
	{
		MigrationResult migrationResult = reverseMigrateReturnMigrationResult(new VersionRange(MigrationTo17.TO_VERSION));
		assertFalse("Should be able to reverse migrate (test project has no Biophysical Factor / Result)", migrationResult.cannotMigrate());
	}

	@Override
	protected int getFromVersion()
	{
		return MigrationTo17.FROM_VERSION;
	}
	
	@Override
	protected int getToVersion()
	{
		return MigrationTo17.TO_VERSION;
	}
}
