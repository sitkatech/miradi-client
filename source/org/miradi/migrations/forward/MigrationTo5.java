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
import org.miradi.migrations.RawObject;
import org.miradi.migrations.RawObjectVisitor;
import org.miradi.migrations.RawProject;
import org.miradi.migrations.VersionRange;
import org.miradi.objects.Strategy;
import org.miradi.schemas.StrategySchema;

public class MigrationTo5 extends AbstractSingleTypeMigration
{
	public MigrationTo5(RawProject rawProjectToUse)
	{
		super(rawProjectToUse);
	}

	@Override
	public Vector<RawObjectVisitor> createRawObjectVisitors()
	{
		Vector<RawObjectVisitor> visitors = super.createRawObjectVisitors();
		visitors.add(new StrategyVisitor());
		
		return visitors;
	}

	@Override
	public int getTypeToMigrate()
	{
		return StrategySchema.getObjectType();
	}
	
	@Override
	public VersionRange getMigratableVersionRange() throws Exception
	{
		return new VersionRange(VERSION_LOW, VERSION_HIGH);
	}
	
	private class StrategyVisitor implements RawObjectVisitor
	{
		public void visit(RawObject rawObject) throws Exception
		{
			if (rawObject.containsKey(Strategy.TAG_STATUS))
				updateDefaultRealStatusCode(rawObject);
		}
		
		private void updateDefaultRealStatusCode(RawObject strategy)
		{
			String strategyStatusCode = strategy.get(Strategy.TAG_STATUS);
			if (strategyStatusCode.equals(LEGACY_DEFAULT_STRATEGY_STATUS_REAL))
				strategy.put(Strategy.TAG_STATUS, "");
		}
	}
	
	private static final int VERSION_LOW = 4;
	private static final int VERSION_HIGH = 4;

	public static final String LEGACY_DEFAULT_STRATEGY_STATUS_REAL = "Real";
}
