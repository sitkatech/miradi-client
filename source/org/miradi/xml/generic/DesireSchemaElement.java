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

import org.miradi.objects.Desire;
import org.miradi.objects.Goal;
import org.miradi.objects.Objective;

public class DesireSchemaElement extends BaseObjectSchemaElement
{
	public DesireSchemaElement(String objectTypeNameToUse)
	{
		super(objectTypeNameToUse);
		
		createTextField(Objective.TAG_SHORT_LABEL);
		createTextField(Desire.TAG_FULL_TEXT);
		createIdListField("RelevantIndicatorIds", "Indicator");
		createIdListField("RelevantStrategyIds", "Strategy");
		createIdListField("RelevantActivityIds", XmlSchemaCreator.ACTIVITY_ID_ELEMENT_NAME);
		createIdListField("ProgressPercentIds", XmlSchemaCreator.PROGRESS_PERCENT_ID_ELEMENT_NAME);
		createTextField(Goal.TAG_COMMENTS);
	}
}
