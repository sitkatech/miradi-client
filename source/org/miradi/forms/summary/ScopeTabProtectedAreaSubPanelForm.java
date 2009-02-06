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
package org.miradi.forms.summary;

import org.miradi.forms.FieldPanelSpec;
import org.miradi.forms.FormConstant;
import org.miradi.forms.FormFieldData;
import org.miradi.forms.FormFieldLabel;
import org.miradi.forms.FormRow;
import org.miradi.main.EAM;
import org.miradi.objects.ProjectMetadata;
import org.miradi.objects.WcpaProjectData;
import org.miradi.views.summary.ProtectedAreaPanel;

public class ScopeTabProtectedAreaSubPanelForm extends FieldPanelSpec
{
	public ScopeTabProtectedAreaSubPanelForm()
	{
		setHasBorder();
		setTranslatedTitle(ProtectedAreaPanel.PANEL_DESCRIPTION);
		
		int type = ProjectMetadata.getObjectType();
		
		FormRow categoryRow = new FormRow();
		categoryRow.addLeftFormItem(new FormConstant(EAM.text("Label|Protected Area Categories")));
		categoryRow.addRightFormItem(new FormFieldData(type, ProjectMetadata.TAG_PROTECTED_AREA_CATEGORIES));
		categoryRow.addRightFormItem(new FormFieldLabel(type, ProjectMetadata.TAG_PROTECTED_AREA_CATEGORY_NOTES));
		categoryRow.addRightFormItem(new FormFieldData(type, ProjectMetadata.TAG_PROTECTED_AREA_CATEGORY_NOTES));
		addFormRow(categoryRow);
		
		addWcpaFields();
	}
	
	void addWcpaFields()
	{
		int type = WcpaProjectData.getObjectType();
		
		addLabelAndField(type, WcpaProjectData.TAG_LEGAL_STATUS);
		addLabelAndField(type, WcpaProjectData.TAG_LEGISLATIVE);
		addLabelAndField(type, WcpaProjectData.TAG_PHYSICAL_DESCRIPTION);
		addLabelAndField(type, WcpaProjectData.TAG_BIOLOGICAL_DESCRIPTION);
		addLabelAndField(type, WcpaProjectData.TAG_SOCIO_ECONOMIC_INFORMATION);
		addLabelAndField(type, WcpaProjectData.TAG_HISTORICAL_DESCRIPTION);
		addLabelAndField(type, WcpaProjectData.TAG_CULTURAL_DESCRIPTION);
		addLabelAndField(type, WcpaProjectData.TAG_ACCESS_INFORMATION);
		addLabelAndField(type, WcpaProjectData.TAG_VISITATION_INFORMATION);
		addLabelAndField(type, WcpaProjectData.TAG_CURRENT_LAND_USES);
		addLabelAndField(type, WcpaProjectData.TAG_MANAGEMENT_RESOURCES);				
		
	}
}
