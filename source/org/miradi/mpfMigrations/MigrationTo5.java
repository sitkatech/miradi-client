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

package org.miradi.mpfMigrations;

import java.util.Set;

import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.objects.Strategy;

public class MigrationTo5 extends AbstractMigration
{
	@Override
	public RawProject forwardMigrate(RawProject rawProject) throws Exception
	{
		return updateRealStrategyStatusCodes(rawProject);
	}
	
	private RawProject updateRealStrategyStatusCodes(RawProject rawProject)
	{
		if (!rawProject.containsAnyObjectsOfType(ObjectType.STRATEGY))
			return rawProject;
		
		RawPool strategyRawPool = rawProject.getRawPoolForType(ObjectType.STRATEGY);
		Set<ORef> strategyRefs = strategyRawPool.keySet();
		for(ORef strategyRef : strategyRefs)
		{
			RawObject strategy = strategyRawPool.get(strategyRef);
			if (strategy.containsKey(Strategy.TAG_STATUS))
				updateDefaultRealStatusCode(strategy);
		}
		
		return rawProject;
	}

	private void updateDefaultRealStatusCode(RawObject strategy)
	{
		String strategyStatusCode = strategy.get(Strategy.TAG_STATUS);
		if (strategyStatusCode.equals(LEGACY_DEFAULT_STRATEGY_STATUS_REAL))
			strategy.put(Strategy.TAG_STATUS, "");
	}

	@Override
	public VersionRange getMigratableVersionRange() throws Exception
	{
		return new VersionRange(VERSION_LOW, VERSION_HIGH);
	}
	
	private static final int VERSION_LOW = 4;
	private static final int VERSION_HIGH = 4;

	public static final String LEGACY_DEFAULT_STRATEGY_STATUS_REAL = "Real";
}
