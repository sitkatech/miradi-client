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
import org.miradi.objects.ProjectResource;

public class ResourcePropertiesForm extends FieldPanelSpec
{
	public ResourcePropertiesForm()
	{
		int type = ProjectResource.getObjectType();
		addLabelAndField(type, ProjectResource.TAG_RESOURCE_TYPE);
		addLabelAndFieldsWithLabels(EAM.text("Label|Resource"), type, new String[]{ProjectResource.TAG_GIVEN_NAME, ProjectResource.TAG_SUR_NAME, ProjectResource.TAG_INITIALS});		
		addLabelAndFieldsWithLabels(EAM.text("Label|Roles (people only)"), type, new String[]{ProjectResource.TAG_ORGANIZATION, ProjectResource.TAG_POSITION, ProjectResource.TAG_LOCATION});
		addLabelAndFieldsWithLabels(EAM.text("Label|Phone Numbers"), type, new String[]{ProjectResource.TAG_PHONE_NUMBER, ProjectResource.TAG_PHONE_NUMBER_MOBILE, });
		addLabelAndFieldsWithLabels(" ", type, new String[]{ProjectResource.TAG_PHONE_NUMBER_HOME, ProjectResource.TAG_PHONE_NUMBER_OTHER, });
		addLabelAndField(type, ProjectResource.TAG_EMAIL);
		addLabelAndField(type, ProjectResource.TAG_ALTERNATIVE_EMAIL);
		addLabelAndFieldsWithLabels(EAM.text("Label|IM Address"), type, new String[]{ProjectResource.TAG_IM_ADDRESS, ProjectResource.TAG_IM_SERVICE, });
		addLabelAndField(type, ProjectResource.TAG_DATE_UPDATED);
		addLabelAndField(type, ProjectResource.TAG_COST_UNIT);
		addLabelAndField(type, ProjectResource.TAG_COST_PER_UNIT);
		addLabelAndField(type, ProjectResource.TAG_COMMENTS);
		addLabelAndField(type, ProjectResource.TAG_CUSTOM_FIELD_1);
		addLabelAndField(type, ProjectResource.TAG_CUSTOM_FIELD_2);
	}
}
