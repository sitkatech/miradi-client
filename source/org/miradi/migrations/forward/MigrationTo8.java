/* 
Copyright 2005-2018, Foundations of Success, Bethesda, Maryland
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

package org.miradi.migrations.forward;

import java.util.HashMap;

import org.miradi.main.EAM;
import org.miradi.migrations.AbstractMigration;
import org.miradi.migrations.AbstractMigrationVisitor;
import org.miradi.migrations.MigrationResult;
import org.miradi.migrations.RawObject;
import org.miradi.migrations.RawProject;
import org.miradi.objects.Indicator;
import org.miradi.schemas.IndicatorSchema;
import org.miradi.utils.Translation;

public class MigrationTo8 extends AbstractMigration
{
	public MigrationTo8(RawProject rawProject)
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
		final IndicatorVisitor visitor = new IndicatorVisitor();
		
		return visitAllObjectsInPool(visitor);
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
		return EAM.text("This migration removes indicator's unit field");
	}
	
	private class IndicatorVisitor extends AbstractMigrationVisitor
	{
		public int getTypeToVisit()
		{
			return IndicatorSchema.getObjectType();
		}

		@Override
		public MigrationResult internalVisit(RawObject indicator) throws Exception
		{
			if (indicator.containsKey(TAG_UNIT))
			{
				indicator.remove(TAG_UNIT);
				
				String indicatorLabel = indicator.get(Indicator.TAG_LABEL);
				HashMap<String, String> tokenReplacementMap = new HashMap<String, String>();
				tokenReplacementMap.put("%indicatorLabel", indicatorLabel);
				tokenReplacementMap.put("%fieldName", Translation.fieldLabel(indicator.getObjectType(), TAG_UNIT));
				final String dataLossMessage = EAM.substitute(EAM.text("%fieldName field data will be lost for %indicatorLabel (Indicator)"), tokenReplacementMap);

				return MigrationResult.createDataLoss(dataLossMessage);
			}

			return MigrationResult.createSuccess();
		}
	}
	
	private static final int VERSION_LOW = 7;
	private static final int VERSION_HIGH = 8;
	
	public static final String TAG_UNIT = "Unit";
}
