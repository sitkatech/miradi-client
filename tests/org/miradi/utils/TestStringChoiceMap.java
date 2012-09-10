/* 
Copyright 2005-2010, Foundations of Success, Bethesda, Maryland 
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

package org.miradi.utils;

import org.miradi.objecthelpers.AbstractStringKeyMap;
import org.miradi.objecthelpers.StringChoiceMap;

public class TestStringChoiceMap extends TestAbstractStringMap
{
	public TestStringChoiceMap(String name)
	{
		super(name);
	}

	@Override
	protected AbstractStringKeyMap createAbstractMap()
	{
		return new StringChoiceMap();
	}

	@Override
	protected AbstractStringKeyMap createAbstractMap(EnhancedJsonObject json)
	{
		return new StringChoiceMap(json);
	}
	
	public void testToString() throws Exception
	{
		StringChoiceMap list = (StringChoiceMap) createMapWithSampleData();
		assertEquals("Can't rount trip?", list, new StringChoiceMap(list));
	}
}
