/* 
Copyright 2005-2012, Foundations of Success, Bethesda, Maryland 
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

package org.miradi.xml.xmpz2;

import org.miradi.dialogs.dashboard.DashboardRowDefinitionManager;
import org.miradi.objecthelpers.CodeToChoiceMap;
import org.miradi.objecthelpers.CodeToCodeListMap;
import org.miradi.objecthelpers.CodeToUserStringMap;
import org.miradi.objects.BaseObject;
import org.miradi.objects.Dashboard;
import org.miradi.schemas.BaseObjectSchema;
import org.miradi.utils.CodeList;

public class DashboardExporter extends BaseObjectExporter
{
	public DashboardExporter(Xmpz2XmlUnicodeWriter writerToUse)
	{
		super(writerToUse);
	}
	
	@Override
	protected void writeFields(final BaseObject baseObject,	BaseObjectSchema baseObjectSchema) throws Exception
	{
		writeDashboardElement((Dashboard)baseObject);
	}
	
	private void writeDashboardElement(Dashboard dashboard) throws Exception
	{
		final CodeToUserStringMap commentsMap = dashboard.getCommentsMap();
		final CodeToChoiceMap progressMap = dashboard.getProgressChoiceMap();
		final CodeToCodeListMap flagsMap = dashboard.getFlagsMap();

		DashboardRowDefinitionManager manager = new DashboardRowDefinitionManager();
		final CodeList allThirdLevelRowCodes = manager.getThirdLevelCodes();
		final String parentElementName = getWriter().appendChildNameToParentName(DASHBOARD, DASHBOARD_STATUS_ENTRIES);
		getWriter().writeStartElement(parentElementName);
		for (String thirdLevelCode : allThirdLevelRowCodes)
		{
			final boolean hasCommentsValue = commentsMap.contains(thirdLevelCode);
			final boolean hasProgressValue = progressMap.contains(thirdLevelCode);
			final boolean hasFlagValue = flagsMap.contains(thirdLevelCode);
			if (hasCommentsValue || hasProgressValue || hasFlagValue)
			{
				getWriter().writeStartElementWithAttribute(DASHBOARD_STATUS_ENTRY, KEY_ATTRIBUTE_NAME, thirdLevelCode);

				getWriter().writeElement(DASHBOARD_PROGRESS, progressMap.getChoiceCode(thirdLevelCode));
				getWriter().writeCodeListElement(getWriter().appendChildNameToParentName(DASHBOARD, DASHBOARD_FLAGS), flagsMap.getCodeList(thirdLevelCode));
				getWriter().writeElement(DASHBOARD_COMMENTS, commentsMap.getUserString(thirdLevelCode));

				getWriter().writeEndElement(DASHBOARD_STATUS_ENTRY);
			}
		}

		getWriter().writeEndElement(parentElementName);
	}
}
