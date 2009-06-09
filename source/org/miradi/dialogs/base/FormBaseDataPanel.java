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

package org.miradi.dialogs.base;

import org.miradi.forms.FieldPanelSpec;
import org.miradi.forms.FormFieldData;
import org.miradi.forms.FormItem;
import org.miradi.forms.FormRow;
import org.miradi.forms.PropertiesPanelSpec;
import org.miradi.objecthelpers.ORef;
import org.miradi.project.Project;

//FIXME urgent - this class is still under construction
public class FormBaseDataPanel extends ObjectDataInputPanel
{
	public FormBaseDataPanel(Project projectToUse, PropertiesPanelSpec propertiesPanelSpec)
	{
		super(projectToUse, ORef.INVALID);
		
		addSubPanels(propertiesPanelSpec);
		addFields(propertiesPanelSpec);
		updateFieldsFromProject();
	}

	private void addSubPanels(PropertiesPanelSpec propertiesPanelSpec)
	{
		for (int index = 0; index < propertiesPanelSpec.getPanelCount(); ++index)
		{
			FieldPanelSpec fieldPanelSpec = propertiesPanelSpec.getPanel(index);
			addSubPanel(fieldPanelSpec);
		}
	}

	private void addSubPanel(FieldPanelSpec fieldPanelSpec)
	{
		for (int index = 0; index < fieldPanelSpec.getPanelCount(); ++index)
		{
			FieldPanelSpec thisFieldPanelSpec = fieldPanelSpec.getPanel(index);
			addFields(thisFieldPanelSpec);
		}
	}

	private void addFields(PropertiesPanelSpec fieldPanelSpec)
	{
		for (int index = 0; index < fieldPanelSpec.getFieldRowCount(); ++index)
		{
			FormRow formRow = fieldPanelSpec.getFormRow(index);
			addFormRow(formRow);
		}
	}

	private void addFormRow(FormRow formRow)
	{
		for (int index = 0; index < formRow.getLeftFormItemsCount(); ++index)
		{
			FormItem formItem = formRow.getLeftFormItem(index);
			if (formItem.isFormFieldData())
			{
				FormFieldData leftFormFieldData = (FormFieldData) formItem;
				addField(createStringField(leftFormFieldData.getObjectType(), leftFormFieldData.getObjectTag()));
			}
		}
		
		for (int index = 0; index < formRow.getRightFormItemsCount(); ++index)
		{
			FormItem rightFormItem = formRow.getRightFormItem(index);
			if (rightFormItem.isFormFieldData())
			{
				FormFieldData formFieldData = (FormFieldData) rightFormItem;
				addField(createStringField(formFieldData.getObjectType(), formFieldData.getObjectTag()));
			}
		}
	}

	@Override
	public String getPanelDescription()
	{
		return "FormBasedDataPanel";
	}
}
