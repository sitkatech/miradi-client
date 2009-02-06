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
import org.miradi.objects.WcsProjectData;
import org.miradi.views.summary.WCSSummaryPanel;

public class WcsTabForm extends FieldPanelSpec
{
	public WcsTabForm()
	{
		setHasBorder();
		setTranslatedTitle(WCSSummaryPanel.PANEL_DESCRIPTION);
		
		addLabelAndField(WcsProjectData.getObjectType(), WcsProjectData.TAG_ORGANIZATIONAL_FOCUS);
		addLabelAndField(WcsProjectData.getObjectType(), WcsProjectData.TAG_ORGANIZATIONAL_LEVEL);
				
		FormRow swotAreaRow = new FormRow();
		swotAreaRow.addLeftFormItem(new FormConstant(EAM.text("SWOT")));
		swotAreaRow.addRightFormItem(new FormFieldLabel(WcsProjectData.getObjectType(), WcsProjectData.TAG_SWOT_COMPLETED));
		swotAreaRow.addRightFormItem(new FormFieldData(WcsProjectData.getObjectType(), WcsProjectData.TAG_SWOT_COMPLETED));
		
		swotAreaRow.addRightFormItem(new FormFieldLabel(WcsProjectData.getObjectType(), WcsProjectData.TAG_SWOT_URL));
		swotAreaRow.addRightFormItem(new FormFieldData(WcsProjectData.getObjectType(), WcsProjectData.TAG_SWOT_URL));
		addFormRow(swotAreaRow);

		FormRow stepAreaRow = new FormRow();
		stepAreaRow.addLeftFormItem(new FormConstant(EAM.text("STEP")));
		stepAreaRow.addRightFormItem(new FormFieldLabel(WcsProjectData.getObjectType(), WcsProjectData.TAG_STEP_COMPLETED));
		stepAreaRow.addRightFormItem(new FormFieldData(WcsProjectData.getObjectType(), WcsProjectData.TAG_STEP_COMPLETED));
		stepAreaRow.addRightFormItem(new FormFieldLabel(WcsProjectData.getObjectType(), WcsProjectData.TAG_STEP_URL));
		stepAreaRow.addRightFormItem(new FormFieldData(WcsProjectData.getObjectType(), WcsProjectData.TAG_STEP_URL));
		addFormRow(stepAreaRow);
	}
}
