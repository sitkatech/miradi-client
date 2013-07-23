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

import java.util.Vector;

import org.miradi.migrations.AbstractSingleTypeMigration;
import org.miradi.migrations.AbstractVisitor;
import org.miradi.migrations.RawObject;
import org.miradi.migrations.RawObjectVisitor;
import org.miradi.migrations.RawProject;
import org.miradi.migrations.VersionRange;
import org.miradi.schemas.StrategySchema;

public class MigrationTo5 extends AbstractSingleTypeMigration
{
	public MigrationTo5(RawProject rawProjectToUse)
	{
		super(rawProjectToUse);
	}

	@Override
	public Vector<RawObjectVisitor> createRawObjectForwardMigrationVisitors()
	{
		Vector<RawObjectVisitor> visitors = super.createRawObjectForwardMigrationVisitors();
		visitors.add(new StrategyVisitor());
		
		return visitors;
	}
	
	@Override
	public VersionRange getPostForwardMigrationVersionRange() throws Exception
	{
		return getMigratableVersionRange().incrementByOne();
	}
	
	@Override
	public VersionRange getPostReverseMigrationVersionRange() throws Exception
	{
		return getMigratableVersionRange().decrementByOne();
	}
	
	@Override
	public VersionRange getMigratableVersionRange() throws Exception
	{
		return new VersionRange(VERSION_LOW, VERSION_HIGH);
	}

	abstract private class AbstractStrategyVisitor extends AbstractVisitor
	{
		public int getTypeToVisit()
		{
			return StrategySchema.getObjectType();
		}
		
		@Override
		public void internalVisit(RawObject rawObject) throws Exception
		{
			if (rawObject.containsKey(TAG_STATUS))
				updateDefaultRealStatusCode(rawObject);
		}
		
		public void possiblyChangeDefaultStatusCode(RawObject strategy, final String defaultCodeToReplace, final String replacementDefaultCode)
		{
			String strategyStatusCode = strategy.get(TAG_STATUS);
			if (strategyStatusCode.equals(defaultCodeToReplace))
				strategy.put(TAG_STATUS, replacementDefaultCode);
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
	
	private static final int VERSION_LOW = 4;
	private static final int VERSION_HIGH = 4;

	public static final String LEGACY_DEFAULT_STRATEGY_STATUS_REAL = "Real";
	private static final String TAG_STATUS = "Status";
}
