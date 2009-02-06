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

import org.miradi.main.EAMTestCase;
import org.miradi.objects.Target;

public class TestFormRow extends EAMTestCase
{
	public TestFormRow(String name)
	{
		super(name);
	}
	
	public void testBasics()
	{
		FormRow formRow = new FormRow();
		assertEquals("left form items list not empty", 0, formRow.getLeftFormItemsCount());
		assertEquals("right form items list not empty", 0, formRow.getRightFormItemsCount());
		
		FormConstant formConstant = new FormConstant("SomeConstant");
		FormFieldLabel formFieldLabel = new FormFieldLabel(Target.getObjectType(), Target.TAG_LABEL);
		FormRow formRowWithLeftAndRight = new FormRow(formConstant, formFieldLabel);
		assertEquals("incorrect left form list size?", 1, formRowWithLeftAndRight.getLeftFormItemsCount());
		assertEquals("incorrect right form list size?", 1, formRowWithLeftAndRight.getRightFormItemsCount());
		
		formRowWithLeftAndRight.addRightFormItem(new FormFieldData(Target.getObjectType(), Target.TAG_LABEL));
		assertEquals("incorrect right form list size?", 2, formRowWithLeftAndRight.getRightFormItemsCount());
	}
}
