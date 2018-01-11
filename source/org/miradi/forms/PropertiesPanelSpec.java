/* 
Copyright 2005-2018, Foundations of Success, Bethesda, Maryland
on behalf of the Conservation Measures Partnership ("CMP").
Material developed between 2005-2013 is jointly copyright by Beneficent Technology, Inc. ("The Benetech Initiative"), Palo Alto, California.

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



abstract public class PropertiesPanelSpec
{
	public int getPanelCount()
	{
		return 0;
	}
	
	public int getFieldRowCount()
	{
		return 0;
	}
	
	public FormRow getFormRow(int index) throws Exception
	{
		throw new Exception(getGenericErrorMessage());
	}

	public FieldPanelSpec getPanel(int index) throws Exception
	{
		throw new Exception(getGenericErrorMessage());
	}
	
	private String getGenericErrorMessage()
	{
		return "Sub classes need to override this method.";
	}
	
	public static final int TYPE_VERY_SHORT_STRING = 1;
	public static final int TYPE_FAIRLY_SHORT_STRING = 2;
	public static final int TYPE_SINGLE_LINE_STRING = 3;
	public static final int TYPE_EXPANDING_STRING = 4;
	public static final int TYPE_MULTILINE_STRING = 5;
	public static final int TYPE_SINGLE_LINE_READONLY_STRING = 6;

	public static final int TYPE_INTEGER = 11;
	public static final int TYPE_FLOAT = 12;
	public static final int TYPE_CURRENCY = 13;

	public static final int TYPE_DATE = 21;
	
	public static final int TYPE_SINGLE_CHOICE = 31;
	public static final int TYPE_MULTIPLE_CHOICE = 32;
	
}
