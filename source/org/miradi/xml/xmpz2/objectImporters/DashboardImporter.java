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

package org.miradi.xml.xmpz2.objectImporters;

import org.miradi.objecthelpers.CodeToChoiceMap;
import org.miradi.objecthelpers.CodeToCodeListMap;
import org.miradi.objecthelpers.CodeToUserStringMap;
import org.miradi.objecthelpers.ORef;
import org.miradi.objects.Dashboard;
import org.miradi.schemas.DashboardSchema;
import org.miradi.utils.CodeList;
import org.miradi.xml.xmpz2.Xmpz2XmlImporter;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class DashboardImporter extends AbstractXmpz2ObjectImporter
{
	public DashboardImporter(Xmpz2XmlImporter importerToUse)
	{
		super(importerToUse);
	}
	
	public void importFields() throws Exception
	{
		Node dashboardPoolNode = getImporter().getNode(getImporter().getRootNode(), getPoolName() + POOL_ELEMENT_TAG);
		Node dashboardNode = getImporter().getNode(dashboardPoolNode, getPoolName());
		Node statusesNode = getImporter().getNode(dashboardNode,  getPoolName() + DASHBOARD_STATUS_ENTRIES);
		NodeList statusNodes = getImporter().getNodes(statusesNode, new String[]{DASHBOARD_STATUS_ENTRY});
		
		CodeToChoiceMap userProgressMap = new CodeToChoiceMap();
		CodeToCodeListMap userFlagsMap = new CodeToCodeListMap();
		CodeToUserStringMap userCommentsMap = new CodeToUserStringMap();
		for (int index = 0; index < statusNodes.getLength(); ++index)
		{
			Node statusNode = statusNodes.item(index);
			String thirdLevelCode = getImporter().getAttributeValue(statusNode, KEY_ATTRIBUTE_NAME);
			Node progressCodeNode = getImporter().getNode(statusNode, DASHBOARD_PROGRESS);
			if (progressCodeNode != null)
			{
				String statusCode = progressCodeNode.getTextContent();
				userProgressMap.putChoiceCode(thirdLevelCode, statusCode);
			}
			
			CodeList flagCodes = getFlagsCodeList(statusNode);
			userFlagsMap.putCodeList(thirdLevelCode, flagCodes);
			
			Node commentsNode = getImporter().getNode(statusNode, DASHBOARD_COMMENTS);
			if (commentsNode != null)
			{
				String userComments = commentsNode.getTextContent();
				userCommentsMap.putUserString(thirdLevelCode, userComments);
			}
		}
		
		getImporter().setData(getDashboardRef(), Dashboard.TAG_PROGRESS_CHOICE_MAP, userProgressMap.toJsonString());
		getImporter().setData(getDashboardRef(), Dashboard.TAG_FLAGS_MAP, userFlagsMap.toJsonString());
		getImporter().setData(getDashboardRef(), Dashboard.TAG_COMMENTS_MAP, userCommentsMap.toJsonString());
	}
	
	protected ORef getDashboardRef()
	{
		return getSingletonObject(DashboardSchema.getObjectType());
	}

	public String getPoolName()
	{
		return DASHBOARD;
	}

	private CodeList getFlagsCodeList(Node flagsNode) throws Exception
	{
		return getImporter().getCodeList(flagsNode, DASHBOARD + DASHBOARD_FLAGS + CONTAINER_ELEMENT_TAG);
	}
}
