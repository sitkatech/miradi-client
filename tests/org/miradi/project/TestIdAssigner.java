/* 
Copyright 2005-2009, Foundations of Success, Bethesda, Maryland 
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
package org.miradi.project;

import org.martus.util.TestCaseEnhanced;
import org.miradi.ids.BaseId;
import org.miradi.ids.IdAssigner;

public class TestIdAssigner extends TestCaseEnhanced
{
	public TestIdAssigner(String name)
	{
		super(name);
	}

	public void testBasics() throws Exception
	{
		IdAssigner idAssigner = new IdAssigner();

		assertEquals("didn't start at zero?", 0, idAssigner.takeNextId().asInt());
		assertEquals("didn't increment?", 1, idAssigner.takeNextId().asInt());
		
		assertEquals("didn't use next available?", 2, idAssigner.obtainRealId(new BaseId(-1)).asInt());
		assertEquals("didn't keep available value?", 3, idAssigner.obtainRealId(new BaseId(3)).asInt());
		BaseId force = new BaseId(100);
		idAssigner.obtainRealId(force);
		assertEquals("didn't update next available?", force.asInt()+1, idAssigner.takeNextId().asInt());
		
		idAssigner.clear();
		assertEquals("didn't reset to zero?", 0, idAssigner.takeNextId().asInt());
	}
	
}
