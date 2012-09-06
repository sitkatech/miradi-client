/* 
Copyright 2005-2011, Foundations of Success, Bethesda, Maryland 
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
import org.miradi.dialogs.dashboard.DashboardRowDefinitionManager;
import org.miradi.objecthelpers.StringChoiceMap;
import org.miradi.objecthelpers.StringCodeListMap;
import org.miradi.objecthelpers.StringStringMap;
import org.miradi.objects.BaseObject;
import org.miradi.objects.Dashboard;
import org.miradi.utils.CodeList;

public class DashboardPoolExporter extends BaseObjectPoolExporter
{
	public DashboardPoolExporter(XmpzXmlExporter wcsXmlExporterToUse)
	{
		super(wcsXmlExporterToUse, DASHBOARD, Dashboard.getObjectType());
	}
	
	@Override
	protected void exportFields(UnicodeWriter writer, BaseObject baseObject) throws Exception
	{
		super.exportFields(writer, baseObject);
		
		Dashboard dashboard = (Dashboard) baseObject;
		StringStringMap commentsMap = dashboard.getCommentsMap();
		StringChoiceMap progressMap = dashboard.getProgressChoiceMap();
		StringCodeListMap flagsMap = dashboard.getFlagsMap();
		
		DashboardRowDefinitionManager manager = new DashboardRowDefinitionManager();
		CodeList allThirdLevelRowCodes = manager.getThirdLevelCodes();
		getWcsXmlExporter().writeStartElement(getPoolName() + DASHBOARD_STATUS_ENTRIES);
		for (int index = 0; index < allThirdLevelRowCodes.size(); ++index)
		{
			String thirdLevelCode = allThirdLevelRowCodes.get(index);
			boolean hasCommentsValue = commentsMap.contains(thirdLevelCode);
			boolean hasProgressValue = progressMap.contains(thirdLevelCode);
			boolean hasFlagValue = flagsMap.contains(thirdLevelCode);
			if (hasCommentsValue || hasProgressValue || hasFlagValue)
			{
				getWcsXmlExporter().writeStartElementWithAttribute(getWriter(), DASHBOARD_STATUS_ENTRY, KEY_ATTRIBUTE_NAME, thirdLevelCode);
					
				getWcsXmlExporter().writeOptionalElement(getWriter(), DASHBOARD_PROGRESS, progressMap.get(thirdLevelCode));
				getWcsXmlExporter().writeOptionalCodeListElement(DASHBOARD, DASHBOARD_FLAGS, new CodeList(flagsMap.get(thirdLevelCode)));
				getWcsXmlExporter().writeOptionalElement(getWriter(), DASHBOARD_COMMENTS, commentsMap.get(thirdLevelCode));
				
				getWcsXmlExporter().writeEndElement(DASHBOARD_STATUS_ENTRY);
			}
		}
		
		getWcsXmlExporter().writeEndElement(getPoolName() + DASHBOARD_STATUS_ENTRIES);
	}
}
