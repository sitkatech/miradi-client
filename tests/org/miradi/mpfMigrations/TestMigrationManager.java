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

package org.miradi.mpfMigrations;

import org.miradi.main.TestCaseWithProject;

public class TestMigrationManager extends TestCaseWithProject
{
	public TestMigrationManager(String name)
	{
		super(name);
	}
	
	public void testGetMigrationType() throws Exception
	{
		verifyType(MigrationManager.NO_MIGRATION, 10, 10, 5, 5);
		verifyType(MigrationManager.NO_MIGRATION, 10, 20, 15, 15);
		verifyType(MigrationManager.NO_MIGRATION, 10, 20, 20, 40);
		verifyType(MigrationManager.NO_MIGRATION, 10, 20, 40, 40);

		verifyType(MigrationManager.MIGRATION, 10, 20, 15, 40);
	}

	private void verifyType(String expectedMigrationType, int miradiLowVersion, int miradiHighVersion, int mpfLowVersion, int mpfHighVersion) throws Exception
	{
		VersionRange miradiVersionRange = new VersionRange(miradiLowVersion, miradiHighVersion);
		VersionRange mpfVersionRange = new VersionRange(mpfLowVersion, mpfHighVersion);
		assertEquals("incorrect migration type?", expectedMigrationType, MigrationManager.getMigrationType(miradiVersionRange, mpfVersionRange));
	}
}
