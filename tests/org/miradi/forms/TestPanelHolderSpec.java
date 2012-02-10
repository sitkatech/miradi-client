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
package org.miradi.forms;

import org.martus.util.TestCaseEnhanced;

public class TestPanelHolderSpec extends TestCaseEnhanced
{
	public TestPanelHolderSpec(String name)
	{
		super(name);
	}

	public void testBasics() throws Exception
	{
		PanelHolderSpec holder = new PanelHolderSpec();
		assertEquals("not empty to start?", 0, holder.getPanelCount());
		
		FieldPanelSpec panel1 = new FieldPanelSpec();
		holder.addPanel(panel1);
		assertEquals("didn't add panel?", 1, holder.getPanelCount());
		
		FieldPanelSpec panel2 = new FieldPanelSpec();
		holder.addPanel(panel2);
		assertEquals("didn't add second panel?", 2, holder.getPanelCount());
		
		FieldPanelSpec panel = holder.getPanel(0);
		assertEquals("wrong panel order?", panel1, panel);
	}
}
