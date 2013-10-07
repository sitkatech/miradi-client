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

import org.miradi.migrations.forward.MigrationTo8;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objects.Indicator;
import org.miradi.project.ProjectForTesting;

public class TestMigrationTo8 extends AbstractTestMigration
{
	public TestMigrationTo8(String name)
	{
		super(name);
	}
	
	public void testWithoutIndicator() throws Exception
	{
		assertTrue("There should no indicators in project?", getProject().getIndicatorPool().isEmpty());
		verifyFullCircleMigrations(createVersionRange());
	}
	
	public void testWithEmptyIndicator() throws Exception
	{
		getProject().createIndicatorWithCauseParent();
		verifyFullCircleMigrations(createVersionRange());
	}
	
	public void testWithIndicatorWithEmptyUnitsField() throws Exception
	{
		Indicator indicator = getProject().createIndicatorWithCauseParent();
		getProject().fillObjectUsingCommand(indicator, MigrationTo8.TAG_UNIT, "");
		ProjectForTesting migratedProject = verifyFullCircleMigrations(createVersionRange());
		ORefList indicatorRefs = migratedProject.getIndicatorPool().getORefList();
		assertEquals("Incorrect indicator count", 1, indicatorRefs.size());
		ORef indicatorRef = indicatorRefs.getFirstElement();
		Indicator migratedIndicator = Indicator.find(migratedProject, indicatorRef);
		assertEquals("Indicator units field should be empty", "", migratedIndicator.getData(MigrationTo8.TAG_UNIT));
	}

	private VersionRange createVersionRange() throws Exception
	{
		return new VersionRange(8);
	}
}
