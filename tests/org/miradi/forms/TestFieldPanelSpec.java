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

public class TestFieldPanelSpec extends TestCaseEnhanced
{
	public TestFieldPanelSpec(String name)
	{
		super(name);
	}

	public void testBasics() throws Exception
	{
		FieldPanelSpec spec = new FieldPanelSpec();
		assertEquals("Not empty to start?", 0, spec.getFieldRowCount());
		
		FormRow firstRow = new FormRow();
		spec.addFormRow(firstRow);
		assertEquals("row not added?", 1, spec.getFieldRowCount());

		FormRow secondRow = new FormRow();
		spec.addFormRow(secondRow);
		assertEquals("second row not added?", 2, spec.getFieldRowCount());
		
		assertEquals("Wrong first row?", firstRow, spec.getFormRow(0));
		assertEquals("Wrong second row?", secondRow, spec.getFormRow(1));
	}
	
	public void testTitleAndBorder() throws Exception
	{
		FieldPanelSpec spec = new FieldPanelSpec();
		assertFalse("Had border by default?", spec.hasBorder());
		assertEquals("Had title by default?", "", spec.getTranslatedTitle());
	
		String SAMPLE_TITLE = "Test Title";
		spec.setHasBorder();
		assertTrue("Didn't get a border?", spec.hasBorder());
		spec.setTranslatedTitle(SAMPLE_TITLE);
		assertEquals("Didn't get a title?", SAMPLE_TITLE, spec.getTranslatedTitle());
	}
}
