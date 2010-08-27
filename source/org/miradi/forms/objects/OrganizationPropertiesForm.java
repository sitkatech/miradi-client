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
package org.miradi.forms.objects;

import org.miradi.forms.FieldPanelSpec;
import org.miradi.main.EAM;
import org.miradi.objects.Organization;

public class OrganizationPropertiesForm extends FieldPanelSpec
{
	public OrganizationPropertiesForm()
	{
		int type = Organization.getObjectType();
		addLabelAndFieldsWithLabels(EAM.text("Label|Organization"), type, new String[]{Organization.TAG_LABEL, Organization.TAG_SHORT_LABEL});
		addLabelAndField(type, Organization.TAG_ROLES_DESCRIPTION);
		addLabelAndFieldsWithLabels(EAM.text("Label|Org Contact"), type, new String[]{Organization.TAG_CONTACT_FIRST_NAME, Organization.TAG_CONTACT_LAST_NAME});
		addLabelAndField(type, Organization.TAG_EMAIL);
		addLabelAndField(type, Organization.TAG_PHONE_NUMBER);
		addLabelAndField(type, Organization.TAG_COMMENTS);
	}
}
