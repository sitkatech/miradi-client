/* 
Copyright 2005-2011, Foundations of Success, Bethesda, Maryland 
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

package org.miradi.objects;

import org.miradi.main.TestCaseWithProject;
import org.miradi.objecthelpers.AbstractStringKeyMap;

public class TestDashboardStatusMapsCacher extends TestCaseWithProject
{
	public TestDashboardStatusMapsCacher(String name)
	{
		super(name);
	}
	
	public void testValidCashAfterProjectOpen() throws Exception
	{
		initializeCache();
		getProject().createAndAddFactorToDiagram(Cause.getObjectType());
		AbstractStringKeyMap cachedMap = getProject().getCachedDashboardEffectiveMap();
		AbstractStringKeyMap nonCachedMap = getProject().getDashboardEffectiveMapCacheManager().getCalculatedEffectiveStatusMap();
		assertEquals("Cached map is not in sync with dashbard's effective status map?", nonCachedMap, cachedMap);
	}

	private void initializeCache() throws Exception
	{
		getProject().getCachedDashboardEffectiveMap();
	}
}
