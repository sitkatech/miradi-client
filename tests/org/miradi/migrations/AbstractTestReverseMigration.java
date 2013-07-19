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

public class AbstractTestReverseMigration extends TestCaseWithProject
{
	public AbstractTestReverseMigration(String name)
	{
		super(name);
	}

	public void testMigrationTo4WithoutIndicators() throws Exception
	{
		verifyFutureStatusDataTransferToIndicator(0);
	}
	
	public void testMigrationTo4WithOneIndicator() throws Exception
	{
		verifyFutureStatusDataTransferToIndicator(1);
	}
	
	public void testMigrationTo4WithTwoIndicators() throws Exception
	{
		verifyFutureStatusDataTransferToIndicator(2);
	}

	//FIXME temporarly disabled. 
	private void verifyFutureStatusDataTransferToIndicator(int futureStatusCount) throws Exception
	{
//		Indicator indicator = getProject().createIndicator(getProject().createCause());
//		Vector<FutureStatus> futureStatuses = createFutureStatuses(indicator, futureStatusCount);
//		ProjectForTesting reverseMigratedProject = reverseMigrateProject();
//		assertTrue("Future statuses should have been deleted?", reverseMigratedProject.getFutureStatusPool().isEmpty());
//
//		ORefList migratedIndicatorRefs = reverseMigratedProject.getIndicatorPool().getORefList();
//		assertTrue("Incorrect indicator count after migration?", migratedIndicatorRefs.size() == 1);
//
//		Indicator migratedIndicator = Indicator.find(reverseMigratedProject, migratedIndicatorRefs.getFirstElement());
		
	}

//	private Vector<FutureStatus> createFutureStatuses(Indicator indicator, int futureStatusCount) throws Exception
//	{
//		Vector<FutureStatus> futureStatuses = new Vector<FutureStatus>(); 
//		for (int index = 0; index < futureStatusCount; ++index)
//		{
//			FutureStatus futureStatus = getProject().createAndPopulateFutureStatus(indicator);
//			futureStatuses.add(futureStatus);
//		}
//		
//		return futureStatuses;
//	}
//	
//	private ProjectForTesting reverseMigrateProject() throws Exception, IOException
//	{
//		ReverseMigrationManager migrationManager = new ReverseMigrationManager();
//		String projectAsString = ProjectSaverForTesting.createSnapShot(getProject(), new VersionRange(3, 3));
//		String migratedMpfFile = migrationManager.migrateReverse(projectAsString);
//
//		ProjectForTesting migratedProject = ProjectForTesting.createProjectWithoutDefaultObjects("MigratedProject");
//		ProjectLoader.loadProject(new UnicodeStringReader(migratedMpfFile), migratedProject);
//
//		return migratedProject;
//	}

}
