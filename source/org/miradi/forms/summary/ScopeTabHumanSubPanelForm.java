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
import org.miradi.views.summary.HumanStakeholderPanel;

public class ScopeTabHumanSubPanelForm extends FieldPanelSpec
{
	public ScopeTabHumanSubPanelForm()
	{
		setHasBorder();
		setTranslatedTitle(HumanStakeholderPanel.PANEL_DESCRIPTION);
		
		int type = ProjectMetadata.getObjectType();
		
		FormRow populationRow = new FormRow();
		populationRow.addLeftFormItem(new FormConstant(EAM.text("Label|Human Stakeholder Pop Size")));
		populationRow.addRightFormItem(new FormFieldData(type, ProjectMetadata.TAG_HUMAN_POPULATION));
		populationRow.addRightFormItem(new FormFieldLabel(type, ProjectMetadata.TAG_HUMAN_POPULATION_NOTES));
		populationRow.addRightFormItem(new FormFieldData(type, ProjectMetadata.TAG_HUMAN_POPULATION_NOTES));
		addFormRow(populationRow);

		addLabelAndField(type, ProjectMetadata.TAG_SOCIAL_CONTEXT);
	}
}
