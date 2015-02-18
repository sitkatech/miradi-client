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

import org.miradi.migrations.forward.MigrationTo9;
import org.miradi.objects.Measurement;
import org.miradi.project.ProjectSaverForTesting;
import org.miradi.questions.StatusConfidenceQuestion;
import org.miradi.schemas.MeasurementSchema;

public class TestMigrationTo9 extends AbstractTestMigration
{
	public TestMigrationTo9(String name)
	{
		super(name);
	}
	
	public void testNoDataLossMigration() throws Exception
	{
		getProject().createMeasurement();
		MigrationResult migrationResult = migrateProject(getProject(), new VersionRange(9), new VersionRange(8));
		assertTrue("Incorrect migration result?", migrationResult.didSucceed());
		assertFalse("Incorrect migration result?", migrationResult.didLoseData());
	}		
	
	public void testStatusConfidenceSamplingBasedChoiceIsCleared() throws Exception
	{
		MigrationResult migrationResult = verifyStatusConfidenceCode("", MigrationTo9.SAMPLING_BASED_CODE);
		assertTrue("Incorrect migration result?", migrationResult.didLoseData());
	}
	
	public void testStatusConfidenceNonSamplingBasedChoiceIsNotCleared() throws Exception
	{
		MigrationResult migrationResult = verifyStatusConfidenceCode(StatusConfidenceQuestion.RAPID_ASSESSMENT_CODE, StatusConfidenceQuestion.RAPID_ASSESSMENT_CODE);
		assertFalse("Incorrect migration result?", migrationResult.didLoseData());
	}

	public void testDataLossMigration() throws Exception
	{
		verifyValueIsReverseMigrated(MigrationTo9.TAG_SAMPLE_PRECISION);
		verifyValueIsReverseMigrated(MigrationTo9.TAG_SAMPLE_PRECISION_TYPE);
		verifyValueIsReverseMigrated(MigrationTo9.TAG_SAMPLE_SIZE);
	}

	private MigrationResult verifyStatusConfidenceCode(final String expectedStatucConfidenceCode, final String actualStatucConfidenceCode) throws Exception
	{
		Measurement measurement = getProject().createMeasurement();
		getProject().fillObjectUsingCommand(measurement, MigrationTo9.TAG_STATUS_CONFIDENCE, actualStatucConfidenceCode);
		
		String projectAsString = ProjectSaverForTesting.createSnapShot(getProject(), new VersionRange(9));
		final RawProject projectToMigrate = RawProjectLoader.loadProject(projectAsString);
		MigrationResult migrationResult = migrateProject(projectToMigrate, new VersionRange(8));
		
		RawPool measurementPool = projectToMigrate.getRawPoolForType(MeasurementSchema.getObjectType());
		RawObject migratedMeasurement = measurementPool.get(measurement.getRef());
		String migratedData = migratedMeasurement.get(MigrationTo9.TAG_STATUS_CONFIDENCE);
		
		assertEquals("Status confidence sampling based code was not cleared", expectedStatucConfidenceCode, migratedData);
		
		return migrationResult;
	}
	
	private void verifyValueIsReverseMigrated(final String tag) throws Exception
	{
		Measurement measurement = getProject().createMeasurement();
		getProject().fillObjectUsingCommand(measurement, tag, "999");
		MigrationResult migrationResult = migrateProject(getProject(), new VersionRange(9), new VersionRange(8));
		assertTrue("Incorrect migration result?", migrationResult.didLoseData());
	}
	
	@Override
	protected int getFromVersion()
	{
		return 8;
	}
	
	@Override
	protected int getToVersion()
	{
		return 9;
	}
}
