/* 
Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
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
package org.miradi.rtf;

import org.miradi.forms.FieldPanelSpec;
import org.miradi.forms.FormConstant;
import org.miradi.forms.FormFieldData;
import org.miradi.forms.FormFieldLabel;
import org.miradi.forms.FormItem;
import org.miradi.forms.FormRow;
import org.miradi.forms.PanelHolderSpec;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.project.Project;

public class RtfFormExporter
{
	public RtfFormExporter(Project projectToUse, RtfWriter writerToUse, ORefList refsToUse)
	{
		project = projectToUse;
		writer = writerToUse;
		refs = refsToUse;
	}
	
	public void exportForm(PanelHolderSpec panelHolderSpec) throws Exception
	{
		for (int index = 0; index < panelHolderSpec.getPanelCount(); ++index)
		{
			exportForm(panelHolderSpec.getPanel(index));
		}
	}
	
	public void exportForm(FieldPanelSpec fieldPanelSpec) throws Exception
	{
		for (int row = 0; row < fieldPanelSpec.getRowCount(); ++row)
		{
			FormRow formRow = fieldPanelSpec.getFormRow(row);
			writeFormRowColumns(formRow);
		}
	}

	private void writeFormRowColumns(FormRow formRow) throws Exception
	{
		StringBuffer rowContent = new StringBuffer("{");
		StringBuffer rowFormating = new StringBuffer("{\\trowd \\trautofit1 \\intbl ");
		
		int uniqueRtfColumnId = 1;
		String FIELD_SPACING = " ";
		for (int leftColumn = 0; leftColumn < formRow.getLeftFormItemsCount(); ++leftColumn)
		{
			FormItem  formItem = formRow.getLeftFormItem(leftColumn);
			if (formItem.isFormConstant())
			{
				FormConstant formConstant = (FormConstant) formItem;
				rowContent.append(formConstant.getConstant() + FIELD_SPACING);				
			}
			else if (formItem.isFormFieldLabel())
			{
				rowContent.append(getFieldLabel(formItem) + FIELD_SPACING);					
			}
		}
		
		rowContent.append(getCellCommand());
		rowFormating.append(getCellxCommand(++uniqueRtfColumnId));
		
		for (int rightColumn = 0; rightColumn < formRow.getRightFormItemsCount(); ++rightColumn)
		{
			FormItem formItem = formRow.getRightFormItem(rightColumn);
			if (formItem.isFormFieldLabel())
			{
				rowContent.append(getFieldLabel(formItem) + FIELD_SPACING);							
			}
			if (formItem.isFormFieldData())
			{
				rowContent.append(getFieldData(formItem) + FIELD_SPACING);							
			}
		}
		
		rowContent.append(getCellCommand());				
		rowFormating.append(getCellxCommand(++uniqueRtfColumnId));
		
		rowContent.append("}");
		rowFormating.append(" \\row }");
		
		getWriter().writeln(rowContent.toString());
		getWriter().writeln(rowFormating.toString());
	}

	private String getFieldData(FormItem formItem)
	{
		FormFieldData formFieldData = (FormFieldData) formItem;
		ORef ref = getRefs().getRefForType(formFieldData.getObjectType());
		return getProject().getObjectData(ref, formFieldData.getObjectTag());
	}

	private String getFieldLabel(FormItem formItem)
	{
		FormFieldLabel formFieldLabel = (FormFieldLabel) formItem;
		return EAM.fieldLabel(formFieldLabel.getObjectType(), formFieldLabel.getObjectTag());
	}

	private String getCellCommand()
	{
		return " \\cell ";
	}

	private String getCellxCommand(int uniqueRtfColumnId)
	{
		return "\\cellx" + uniqueRtfColumnId + " ";
	}
	
	private Project getProject()
	{
		return project;
	}
	
	private RtfWriter getWriter()
	{
		return writer;
	}
	
	private ORefList getRefs()
	{
		return refs;
	}
	
	private Project project;
	private RtfWriter writer;
	private ORefList refs;
}
