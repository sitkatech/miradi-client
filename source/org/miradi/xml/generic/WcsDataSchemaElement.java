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

import org.miradi.objects.WcsProjectData;

public class WcsDataSchemaElement extends ObjectSchemaElement
{
	public WcsDataSchemaElement()
	{
		super(XmlConstants.WCS_PROJECT_DATA);
		
		createOptionalTextField(WcsProjectData.TAG_ORGANIZATIONAL_FOCUS);
		createOptionalTextField(WcsProjectData.TAG_ORGANIZATIONAL_LEVEL);
		createOptionalTextField(WcsProjectData.TAG_SWOT_COMPLETED);
		createOptionalTextField(WcsProjectData.TAG_SWOT_URL);
		createOptionalTextField(WcsProjectData.TAG_STEP_COMPLETED);
		createOptionalTextField(WcsProjectData.TAG_STEP_URL);
	}
}
