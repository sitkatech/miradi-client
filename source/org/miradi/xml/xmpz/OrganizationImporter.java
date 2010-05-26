/* 
Copyright 2005-2010, Foundations of Success, Bethesda, Maryland 
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

package org.miradi.xml.xmpz;

import org.miradi.ids.BaseId;
import org.miradi.objecthelpers.ORef;
import org.miradi.objects.Organization;
import org.miradi.xml.AbstractXmpzObjectImporter;
import org.miradi.xml.wcs.WcsXmlConstants;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class OrganizationImporter extends AbstractXmpzObjectImporter
{
	public OrganizationImporter(XmpzXmlImporter importerToUse)
	{
		super(importerToUse, WcsXmlConstants.ORGANIZATION);
	}

	@Override
	public void importElement() throws Exception
	{
		String poolName = WcsXmlConstants.ORGANIZATION  + WcsXmlConstants.POOL_ELEMENT_TAG;
		NodeList nodes = getImporter().getNodes(getImporter().getRootNode(), new String[]{poolName, WcsXmlConstants.ORGANIZATION, });
		for (int index = 0; index < nodes.getLength(); ++index)
		{
			Node node = nodes.item(index);
			String intIdAsString = getImporter().getAttributeValue(node, WcsXmlConstants.ID);
			ORef ref = getProject().createObject(Organization.getObjectType(), new BaseId(intIdAsString));
			
			importField(node, WcsXmlConstants.ORGANIZATION, ref, Organization.TAG_LABEL);
			importField(node, WcsXmlConstants.ORGANIZATION, ref, Organization.TAG_SHORT_LABEL);
			importField(node, WcsXmlConstants.ORGANIZATION, ref, Organization.TAG_ROLES_DESCRIPTION);
			importField(node, WcsXmlConstants.ORGANIZATION, ref, Organization.TAG_CONTACT_FIRST_NAME);
			importField(node, WcsXmlConstants.ORGANIZATION, ref, Organization.TAG_CONTACT_LAST_NAME);
			importField(node, WcsXmlConstants.ORGANIZATION, ref, Organization.TAG_EMAIL);
			importField(node, WcsXmlConstants.ORGANIZATION, ref, Organization.TAG_PHONE_NUMBER);
			importField(node, WcsXmlConstants.ORGANIZATION, ref, Organization.TAG_COMMENTS);
		}
	}
}
