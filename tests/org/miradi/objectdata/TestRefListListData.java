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

package org.miradi.objectdata;

import java.util.Vector;

import org.miradi.main.EAMTestCase;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;

public class TestRefListListData extends EAMTestCase
{
	public TestRefListListData(String name)
	{
		super(name);
	}
	
	public void testConvertToRefListVector() throws Exception
	{
		RefListListData refListListData1 = new RefListListData("SomeTag");
		assertEquals("list should be empty?", 0, refListListData1.size());
		refListListData1.addList(new ORefList());
		Vector<ORefList> converted1 = refListListData1.convertToRefListVector();
		assertTrue("wrong reflist retrieved?", converted1.contains(new ORefList()));
		assertEquals("wrong reflist retrieved?", new ORefList().toString(), refListListData1.get(0).toString());
		
		ORefList refList = new ORefList(ORef.INVALID);
		RefListListData refListListData2 = new RefListListData("SomeTag");
		refListListData2.addList(refList);
		Vector<ORefList> converted2 = refListListData2.convertToRefListVector();
		assertTrue("wrong reflist retrieved?", converted2.contains(refList));
		assertEquals("wrong reflist retrieved?", refList.toString(), refListListData2.get(0).toString());
	}
}
