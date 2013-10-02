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

package org.miradi.migrations.forward;

import org.miradi.main.EAM;
import org.miradi.migrations.AbstractMigration;
import org.miradi.migrations.AbstractMigrationVisitor;
import org.miradi.migrations.MigrationResult;
import org.miradi.migrations.RawObject;
import org.miradi.migrations.RawProject;
import org.miradi.migrations.VersionRange;
import org.miradi.schemas.MeasurementSchema;

public class MigrationTo9 extends AbstractMigration
{
	public MigrationTo9(RawProject rawProject)
	{
		super(rawProject);
	}

	@Override
	protected MigrationResult migrateForward() throws Exception
	{
		return MigrationResult.createSuccess();
	}
	
	@Override
	protected MigrationResult reverseMigrate() throws Exception
	{
		final MeasurementVisitor visitor = new MeasurementVisitor();
		visitAllObjectsInPool(visitor);
		
		return visitor.getMigrationResult();
	}

	@Override
	protected VersionRange getMigratableVersionRange() throws Exception
	{
		return new VersionRange(VERSION_LOW, VERSION_HIGH);
	}
	
	@Override
	protected int getToVersion()
	{
		return VERSION_HIGH;
	}
	
	@Override
	protected int getFromVersion() 
	{
		return VERSION_LOW;
	}
	
	@Override
	protected String getDescription()
	{
		return EAM.text("This migration removes new measuremt fields: Sample size, Sample Precision, Sample Precision Type and clears status confidence" +
						"if the choice is Sampling Based");
	}
	
	private class MeasurementVisitor extends AbstractMigrationVisitor
	{
		public int getTypeToVisit()
		{
			return MeasurementSchema.getObjectType();
		}

		@Override
		public MigrationResult internalVisit(RawObject rawObject) throws Exception
		{
			MigrationResult migrationResult = MigrationResult.createSuccess();
			if (rawObject.containsKey(TAG_SAMPLE_PRECISION))
			{
				rawObject.remove(TAG_SAMPLE_PRECISION);
				migrationResult = migrationResult.createDataLoss();
			}
			if (rawObject.containsKey(TAG_SAMPLE_SIZE))
			{
				rawObject.remove(TAG_SAMPLE_SIZE);
				migrationResult = migrationResult.createDataLoss();
			}
			if (rawObject.containsKey(TAG_SAMPLE_PRECISION_TYPE))
			{
				rawObject.remove(TAG_SAMPLE_PRECISION_TYPE);
				migrationResult = migrationResult.createDataLoss();
			}
			
			String statusConfidence = rawObject.getData(TAG_STATUS_CONFIDENCE);
			if (statusConfidence != null && statusConfidence.equals(SAMPLING_BASED_CODE))
			{
				rawObject.setData(TAG_STATUS_CONFIDENCE, "");
				migrationResult = migrationResult.createDataLoss();
			}
			
			return migrationResult;
		}
	}
	
	private static final String TAG_STATUS_CONFIDENCE = "StatusConfidence";
	private static final String SAMPLING_BASED_CODE = "SamplingBased";
	private static final String TAG_SAMPLE_SIZE ="SampleSize";
	private static final String TAG_SAMPLE_PRECISION ="SamplePrecision";
	private static final String TAG_SAMPLE_PRECISION_TYPE ="SamplePrecisionType";
	
	private static final int VERSION_LOW = 8;
	private static final int VERSION_HIGH = 9;
}
