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

package org.miradi.migrations.forward;

import org.miradi.main.EAM;
import org.miradi.migrations.AbstractMigrationVisitor;
import org.miradi.migrations.AbstractSingleTypeMigration;
import org.miradi.migrations.MigrationResult;
import org.miradi.migrations.RawObject;
import org.miradi.migrations.RawProject;
import org.miradi.schemas.StrategySchema;

public class MigrationTo5 extends AbstractSingleTypeMigration
{
	public MigrationTo5(RawProject rawProjectToUse)
	{
		super(rawProjectToUse);
	}
	
	@Override
	protected MigrationResult migrateForward() throws Exception
	{
		final StrategyVisitor visitor = new StrategyVisitor();

		return visitAllObjectsInPool(visitor);
	}
	
	@Override
	protected MigrationResult reverseMigrate() throws Exception
	{
		final ReverseStrategyVisitor visitor = new ReverseStrategyVisitor();

		return visitAllObjectsInPool(visitor);
	}

	@Override
	protected int getToVersion()
	{
		return TO_VERSION;
	}

	@Override
	protected int getFromVersion() 
	{
		return FROM_VERSION;
	}
	
	@Override
	protected String getDescription()
	{
		return EAM.text("This migration changes default real status code to \"\"");
	}
	
	abstract private class AbstractStrategyVisitor extends AbstractMigrationVisitor
	{
		public int getTypeToVisit()
		{
			return StrategySchema.getObjectType();
		}
		
		@Override
		public MigrationResult internalVisit(RawObject rawObject) throws Exception
		{
			if (rawObject.containsKey(TAG_STATUS))
				updateDefaultRealStatusCode(rawObject);
			else
				updateNonExistingFieldWithoutDefaultValue(rawObject);
			
			return MigrationResult.createSuccess();
		}
		
		public void possiblyChangeDefaultStatusCode(RawObject strategy, final String defaultCodeToReplace, final String replacementDefaultCode)
		{
			String strategyStatusCode = strategy.get(TAG_STATUS);
			if (strategyStatusCode.equals(defaultCodeToReplace))
				strategy.put(TAG_STATUS, replacementDefaultCode);
		}
		
		protected void updateNonExistingFieldWithoutDefaultValue(RawObject rawObject)
		{
		}
		
		abstract protected void updateDefaultRealStatusCode(RawObject strategy);
	}
	
	private class StrategyVisitor extends AbstractStrategyVisitor
	{
		@Override
		protected void updateDefaultRealStatusCode(RawObject strategy)
		{
			possiblyChangeDefaultStatusCode(strategy, LEGACY_DEFAULT_STRATEGY_STATUS_REAL, "");
		}
	}
	
	private class ReverseStrategyVisitor extends AbstractStrategyVisitor
	{
		@Override
		protected void updateDefaultRealStatusCode(RawObject strategy)
		{
			possiblyChangeDefaultStatusCode(strategy, "", LEGACY_DEFAULT_STRATEGY_STATUS_REAL);
		}
		
		@Override
		protected void updateNonExistingFieldWithoutDefaultValue(RawObject strategy)
		{
			strategy.put(TAG_STATUS, LEGACY_DEFAULT_STRATEGY_STATUS_REAL);
		}
	}
	
	public static final int TO_VERSION = 5;
	public static final int FROM_VERSION = 4;
	public static final String LEGACY_DEFAULT_STRATEGY_STATUS_REAL = "Real";
	public static final String TAG_STATUS = "Status";
}
