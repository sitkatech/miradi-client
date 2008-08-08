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
import org.miradi.objectdata.ChoiceData;
import org.miradi.objectdata.CodeListData;
import org.miradi.objectdata.ObjectData;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objects.BaseObject;
import org.miradi.project.Project;
import org.miradi.questions.ChoiceItem;
import org.miradi.questions.ChoiceQuestion;
import org.miradi.utils.AbstractTableExporter;
import org.miradi.utils.CodeList;

public class RtfFormExporter
{
	public RtfFormExporter(Project projectToUse, RtfWriter writerToUse, ORefList refsToUse)
	{
		project = projectToUse;
		writer = writerToUse;
		refs = refsToUse;
	}
	
	//FIXME finish this method
	public void writePropertiesForms(AbstractTableExporter tableExporter)
	{
		for (int index = 0; index < tableExporter.getRowCount(); ++index)
		{
			
		}
	}
		
	public void exportForm(PanelHolderSpec panelHolderSpec) throws Exception
	{
		for (int index = 0; index < panelHolderSpec.getPanelCount(); ++index)
		{
			FieldPanelSpec fieldPanelSpec = panelHolderSpec.getPanel(index);
			if (fieldPanelSpec.hasBorder())
				writeTitle(fieldPanelSpec);
			
			exportForm(fieldPanelSpec);
			writer.newParagraph();
		}
	}

	private void writeTitle(FieldPanelSpec fieldPanelSpec) throws Exception
	{
		writer.writeln(fieldPanelSpec.getTranslatedTitle());
		writer.newParagraph();
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
		StringBuffer rowFormatting = new StringBuffer("{\\trowd \\trautofit1 \\intbl ");
		
		int uniqueRtfColumnId = 1;
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
				rowContent.append(getFieldLabel((FormFieldLabel)formItem) + FIELD_SPACING);					
			}
		}
		
		rowContent.append(getCellCommand());
		rowFormatting.append(getCellxCommand(++uniqueRtfColumnId));
		
		for (int rightColumn = 0; rightColumn < formRow.getRightFormItemsCount(); ++rightColumn)
		{
			FormItem formItem = formRow.getRightFormItem(rightColumn);
			if (formItem.isFormConstant())
			{
				FormConstant formConstant = (FormConstant) formItem;
				rowContent.append(formConstant.getConstant() + FIELD_SPACING);				
			}
			if (formItem.isFormFieldLabel())
			{
				rowContent.append(getFieldLabel((FormFieldLabel)formItem) + FIELD_SPACING);							
			}
			if (formItem.isFormFieldData())
			{
				rowContent.append(getFieldData((FormFieldData) formItem) + FIELD_SPACING);							
			}
		}
		
		rowContent.append(getCellCommand());				
		rowFormatting.append(getCellxCommand(++uniqueRtfColumnId));
		
		rowContent.append("}");
		rowFormatting.append(" \\row }");
		
		getWriter().writeln(rowContent.toString());
		getWriter().writeln(rowFormatting.toString());
	}

	private String getFieldData(FormFieldData formFieldData)
	{
		ORef ref = getRefs().getRefForType(formFieldData.getObjectType());
		BaseObject baseObject = getProject().findObject(ref);
		String fieldTag = formFieldData.getObjectTag();
		if (baseObject.isPseudoField(fieldTag))
			return baseObject.getPseudoData(fieldTag);

		ObjectData rawObjectData = baseObject.getField(fieldTag);
		if (rawObjectData.isCodeListData())
			return createFromCodeList((CodeListData) rawObjectData);
		
		if (rawObjectData.isChoiceItemData())
			return createFromChoiceData((ChoiceData) rawObjectData);
	
		return rawObjectData.get();
	}

	private String createFromChoiceData(ChoiceData choiceData)
	{
		ChoiceQuestion question = choiceData.getChoiceQuestion();
		String code = choiceData.get();
		ChoiceItem choiceItem = question.findChoiceByCode(code);
		return choiceItem.getLabel();
	}

	private String createFromCodeList(CodeListData codeListData)
	{
		StringBuffer choices = new StringBuffer();
		CodeList codeList = codeListData.getCodeList();
		ChoiceQuestion question = codeListData.getChoiceQuestion();
		for (int index = 0; index < codeList.size(); ++index)
		{
			ChoiceItem choiceItem = question.findChoiceByCode(codeList.get(index));
			choices.append(choiceItem.getLabel());
			choices.append(FIELD_SPACING);
		}
		
		return choices.toString();
	}

	private String getFieldLabel(FormFieldLabel formFieldLabel)
	{
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
	
	private static final String FIELD_SPACING = "     ";
	
	private Project project;
	private RtfWriter writer;
	private ORefList refs;
}
