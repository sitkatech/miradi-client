/* 
Copyright 2005-2015, Foundations of Success, Bethesda, Maryland
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

package org.miradi.xml.generic;

import org.miradi.objects.Factor;


public class FactorObjectSchemaElement extends BaseObjectSchemaElementWithLabel
{
	public FactorObjectSchemaElement(String objectTypeName)
	{
		super(objectTypeName);

		createOptionalTextField(Factor.TAG_SHORT_LABEL);
		createOptionalTextField(getDetailsTag());
		createOptionalTextField(Factor.TAG_COMMENTS);
	}
	
	protected String getDetailsTag()
	{
		return Factor.TAG_TEXT;
	}
	
	protected static final String INDICATOR_TYPE_NAME = "Indicator";
	protected static final String OBJECTIVE_TYPE_NAME = "Objective";
}
