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

public class Migration4 extends AbstractMigration
{
	@Override
	public RawProject forwardMigrate(RawProject rawProject) throws Exception
	{
		//FIXME urgent - MigrationManager should be responsible for incrementing
		rawProject.setCurrentVersionRange(new VersionRange(VERSION_LOW + 1, VERSION_HIGH + 1));
		return updateRealStrategyStatusCodes(rawProject);
	}
	
	private RawProject updateRealStrategyStatusCodes(RawProject rawProject)
	{
		if (!rawProject.containType(ObjectType.STRATEGY))
			return rawProject;
		
		RawPool strategyRawPool = rawProject.getRawPoolForType(ObjectType.STRATEGY);
		Set<ORef> strategyRefs = strategyRawPool.keySet();
		for(ORef strategyRef : strategyRefs)
		{
			RawObject strategy = strategyRawPool.get(strategyRef);
			if (strategy.containsKey(Strategy.TAG_STATUS))
				updateRealStrategyStatusCode(strategy);
		}
		
		return rawProject;
	}

	private void updateRealStrategyStatusCode(RawObject strategy)
	{
		String strategyStatusCode = strategy.get(Strategy.TAG_STATUS);
		if (strategyStatusCode.equals(LEGACY_DEFAULT_STRATEGY_STATUS_REAL))
			strategy.put(Strategy.TAG_STATUS, "");
	}

	public static boolean canMigrateThisVersion(VersionRange versionRange) throws Exception
	{
		return getMigratableVersionRange().doesContainHigh(versionRange.getHighVersion());
	}

	private static VersionRange getMigratableVersionRange() throws Exception
	{
		return new VersionRange(VERSION_LOW, VERSION_HIGH);
	}
	
	private static final int VERSION_LOW = 4;
	private static final int VERSION_HIGH = 4;

	public static final String LEGACY_DEFAULT_STRATEGY_STATUS_REAL = "Real";
}
