/* 
Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
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
package org.miradi.forms;

import org.martus.util.TestCaseEnhanced;

public class TestFieldPanelSpec extends TestCaseEnhanced
{
	public TestFieldPanelSpec(String name)
	{
		super(name);
	}

	public void testBasics() throws Exception
	{
		FieldPanelSpec spec = new FieldPanelSpec();
		assertEquals("Not empty to start?", 0, spec.getRowCount());
		
		FormRow firstRow = new FormRow();
		spec.addRow(firstRow);
		assertEquals("row not added?", 1, spec.getRowCount());
	}
}
