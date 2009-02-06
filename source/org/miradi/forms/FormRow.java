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

import java.util.Vector;

public class FormRow
{
	public FormRow()
	{
		leftFormItems = new Vector();
		rightFormItems = new Vector();
	}
	
	public FormRow(FormItem leftFormItem, FormItem rightFormItem)
	{
		this();
		
		addLeftFormItem(leftFormItem);
		addRightFormItem(rightFormItem);
	}
	
	public void addLeftFormItem(FormItem leftFormItem)
	{
		leftFormItems.add(leftFormItem);
	}
	
	public void addRightFormItem(FormItem rightFormItem)
	{
		rightFormItems.add(rightFormItem);
	}

	public int getLeftFormItemsCount()
	{
		return leftFormItems.size();
	}
	
	public int getRightFormItemsCount()
	{
		return rightFormItems.size();
	}
	
	public FormItem getLeftFormItem(int index)
	{
		return leftFormItems.get(index);
	}
	
	public FormItem getRightFormItem(int index)
	{
		return rightFormItems.get(index);
	}
	
	private Vector<FormItem> leftFormItems;
	private Vector<FormItem> rightFormItems;
}
