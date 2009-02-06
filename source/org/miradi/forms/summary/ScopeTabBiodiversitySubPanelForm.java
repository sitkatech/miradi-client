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
import org.miradi.views.summary.BiodiversityPanel;

public class ScopeTabBiodiversitySubPanelForm extends FieldPanelSpec
{
	public ScopeTabBiodiversitySubPanelForm()
	{
		setHasBorder();
		setTranslatedTitle(BiodiversityPanel.PANEL_DESCRIPTION);
		
		int type = ProjectMetadata.getObjectType();
		
		FormRow areaRow = new FormRow();
		areaRow.addLeftFormItem(new FormConstant(EAM.text("Label|Biodiversity Area (hectares)")));
		areaRow.addRightFormItem(new FormFieldData(type, ProjectMetadata.TAG_PROJECT_AREA));
		areaRow.addRightFormItem(new FormFieldLabel(type, ProjectMetadata.TAG_PROJECT_AREA_NOTES));
		areaRow.addRightFormItem(new FormFieldData(type, ProjectMetadata.TAG_PROJECT_AREA_NOTES));
		addFormRow(areaRow);

		addLabelAndField(type, ProjectMetadata.TAG_RED_LIST_SPECIES);
		addLabelAndField(type, ProjectMetadata.TAG_OTHER_NOTABLE_SPECIES);

	}
}
