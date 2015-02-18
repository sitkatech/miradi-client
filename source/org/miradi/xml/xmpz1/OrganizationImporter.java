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

package org.miradi.xml.xmpz1;

import org.miradi.objecthelpers.ORef;
import org.miradi.objects.Organization;
import org.miradi.schemas.OrganizationSchema;
import org.miradi.xml.wcs.Xmpz1XmlConstants;
import org.w3c.dom.Node;

public class OrganizationImporter extends AbstractBaseObjectPoolImporter
{
	public OrganizationImporter(Xmpz1XmlImporter importerToUse)
	{
		super(importerToUse, Xmpz1XmlConstants.ORGANIZATION, OrganizationSchema.getObjectType());
	}

	@Override
	protected void importFields(Node node, ORef destinationRef) throws Exception
	{
		super.importFields(node, destinationRef);
		
		importField(node, destinationRef, Organization.TAG_SHORT_LABEL);
		importField(node, destinationRef, Organization.TAG_ROLES_DESCRIPTION);
		importField(node, destinationRef, Organization.TAG_CONTACT_FIRST_NAME);
		importField(node, destinationRef, Organization.TAG_CONTACT_LAST_NAME);
		importField(node, destinationRef, Organization.TAG_EMAIL);
		importField(node, destinationRef, Organization.TAG_PHONE_NUMBER);
		importField(node, destinationRef, Organization.TAG_COMMENTS);
	}
}
