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

package org.miradi.xml.wcs;

import org.martus.util.UnicodeWriter;
import org.miradi.objects.BaseObject;
import org.miradi.objects.ObjectTreeTableConfiguration;
import org.miradi.questions.DiagramObjectDataInclusionQuestion;

public class PlanningViewConfigurationPoolExporter extends	BaseObjectPoolExporter
{
	public PlanningViewConfigurationPoolExporter(WcsXmlExporter wcsXmlExporterToUse)
	{
		super(wcsXmlExporterToUse, PLANNING_VIEW_CONFIGURATION, ObjectTreeTableConfiguration.getObjectType());
	}
	
	@Override
	protected void exportFields(UnicodeWriter writer, BaseObject baseObject) throws Exception
	{
		super.exportFields(writer, baseObject);
		
		writeCodeElementSameAsTag(baseObject, ObjectTreeTableConfiguration.TAG_DIAGRAM_DATA_INCLUSION, new DiagramObjectDataInclusionQuestion());
	}
}
