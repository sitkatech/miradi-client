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

import org.miradi.main.TestCaseWithProject;
import org.miradi.migrations.forward.ForwardMigrationManager;

public class TestMigrationManager extends TestCaseWithProject
{
	public TestMigrationManager(String name)
	{
		super(name);
	}
	
	public void testGetMigrationType() throws Exception
	{
		VersionRange tenTwentyRange = new VersionRange(10, 20);
		VersionRange twentyFourtyRange = new VersionRange(20, 40);
		VersionRange fourtyFourtyRange = new VersionRange(40);
		VersionRange fifteenFourtyRange = new VersionRange(15, 40);
		verifyType(ForwardMigrationManager.NO_MIGRATION, tenTwentyRange, twentyFourtyRange);
		verifyType(ForwardMigrationManager.TOO_NEW_TO_MIGRATE, tenTwentyRange, fourtyFourtyRange);
		verifyType(ForwardMigrationManager.NO_MIGRATION, tenTwentyRange, fifteenFourtyRange);
		verifyType(ForwardMigrationManager.NO_MIGRATION, tenTwentyRange, tenTwentyRange);
		
		VersionRange FifteenFifteenRange = new VersionRange(15);
		verifyType(ForwardMigrationManager.NO_MIGRATION, tenTwentyRange, FifteenFifteenRange);
		verifyType(ForwardMigrationManager.MIGRATION, fourtyFourtyRange, FifteenFifteenRange);
	}

	private void verifyType(int expectedMigrationType, VersionRange miradiVersionRange, VersionRange mpfVersionRange) throws Exception
	{
		assertEquals("incorrect migration type?", expectedMigrationType, ForwardMigrationManager.getMigrationType(miradiVersionRange, mpfVersionRange));
	}
}
