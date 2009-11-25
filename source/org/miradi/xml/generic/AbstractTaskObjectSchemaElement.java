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

package org.miradi.xml.generic;

import org.miradi.objects.Task;
import org.miradi.xml.wcs.WcsXmlConstants;

abstract public class AbstractTaskObjectSchemaElement extends FactorObjectSchemaElement
{
	public AbstractTaskObjectSchemaElement(String objectTypeName)
	{
		super(objectTypeName);
		
		createIdListField(WcsXmlConstants.EXPENSE_IDS, XmlSchemaCreator.EXPENSE_ASSIGNMENT_ID_ELEMENT_NAME);
		createIdListField(Task.TAG_RESOURCE_ASSIGNMENT_IDS, XmlSchemaCreator.RESOURCE_ASSIGNMENT_ID_ELEMENT_NAME);
		createIdListField(WcsXmlConstants.PROGRESS_REPORT_IDS, XmlSchemaCreator.PROGRESS_REPORT_ID_ELEMENT_NAME);
	}
}
