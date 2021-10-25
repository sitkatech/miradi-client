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

import org.miradi.migrations.forward.MigrationTo10;
import org.miradi.objecthelpers.ORef;
import org.miradi.objects.TncProjectData;
import org.miradi.project.Project;

public class TestMigrationTo10 extends AbstractTestMigration
{
	public TestMigrationTo10(String name)
	{
		super(name);
	}
	
	public void testBasics() throws Exception
	{
		ORef tncProjectDataRef = getProject().getTncProjectDataRef();
		getProject().fillObjectUsingCommand(tncProjectDataRef, TncProjectData.TAG_MAKING_THE_CASE, SAMPLE_MAKING_THE_CASE_DATA);
		getProject().fillObjectUsingCommand(tncProjectDataRef, TncProjectData.TAG_CAPACITY_AND_FUNDING, SAMPLE_CAPACITY_AND_FUNDING_DATA);

		RawProject reverseMigratedProject = reverseMigrate(new VersionRange(MigrationTo10.VERSION_TO));
		assertEquals("Data should not have been changed during field rename?", SAMPLE_MAKING_THE_CASE_DATA, reverseMigratedProject.getData(tncProjectDataRef, MigrationTo10.LEGACY_TAG_MAKING_THE_CASE));
		assertEquals("Data should not have been changed during field rename?", SAMPLE_CAPACITY_AND_FUNDING_DATA, reverseMigratedProject.getData(tncProjectDataRef, MigrationTo10.LEGACY_TAG_CAPACITY_AND_FUNDING));
		
		migrateProject(reverseMigratedProject, new VersionRange(Project.VERSION_HIGH));
		assertEquals("Data should not have been changed during field rename?", SAMPLE_MAKING_THE_CASE_DATA, reverseMigratedProject.getData(tncProjectDataRef, MigrationTo10.TAG_OVERALL_PROJECT_GOAL));
		assertEquals("Data should not have been changed during field rename?", SAMPLE_CAPACITY_AND_FUNDING_DATA, reverseMigratedProject.getData(tncProjectDataRef, MigrationTo10.TAG_FINANCIAL_PLAN));
		
		verifyFullCircleMigrations(new VersionRange(9, 10));
	}
	
	@Override
	protected int getFromVersion()
	{
		return 9;
	}
	
	@Override
	protected int getToVersion()
	{
		return 10;
	}

	private static final String SAMPLE_MAKING_THE_CASE_DATA = "SampleMakingTheCaseData";
	private static final String SAMPLE_CAPACITY_AND_FUNDING_DATA = "SampleCapacityAndFundingData";
}
