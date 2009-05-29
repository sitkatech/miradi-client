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
package org.miradi.rtf;

import java.awt.image.BufferedImage;

import org.miradi.forms.FieldPanelSpec;
import org.miradi.forms.FormConstant;
import org.miradi.forms.FormFieldData;
import org.miradi.forms.FormFieldQuestionData;
import org.miradi.forms.FormImage;
import org.miradi.forms.FormFieldLabel;
import org.miradi.forms.FormItem;
import org.miradi.forms.FormRow;
import org.miradi.forms.PropertiesPanelSpec;
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
import org.miradi.questions.StatusQuestion;
import org.miradi.utils.CodeList;
import org.miradi.utils.StringMapData;

public class RtfFormExporter
{
	public RtfFormExporter(Project projectToUse, RtfWriter writerToUse, ORefList hierarchyRefsToUse)
	{
		project = projectToUse;
		writer = writerToUse;
		hierarchyRefs = hierarchyRefsToUse;
	}
	
	public RtfFormExporter(Project projectToUse, RtfWriter writerToUse, ORef refToUse)
	{
		this(projectToUse, writerToUse, new ORefList(refToUse));
	}
	
	public void exportForm(PropertiesPanelSpec propertiesPanelSpec) throws Exception
	{
		exportForm(propertiesPanelSpec, 0);
		writer.pageBreak();
	}
	
	public void exportForm(PropertiesPanelSpec propertiesPanelSpec, int indentation) throws Exception
	{
		for (int index = 0; index < propertiesPanelSpec.getFieldRowCount(); ++index)
		{
			FormRow formRow = ((FieldPanelSpec)propertiesPanelSpec).getFormRow(index);
			writeFormRowColumns(formRow, indentation);	
		}
		
		for (int index = 0; index < propertiesPanelSpec.getPanelCount(); ++index)
		{
			FieldPanelSpec fieldPanelSpec = propertiesPanelSpec.getPanel(index);
			if (fieldPanelSpec.hasBorder())
				writeTitle(fieldPanelSpec);
			
			exportForm(fieldPanelSpec, indentation);
		}
		
		writer.newParagraph();
	}

	private void writeTitle(FieldPanelSpec fieldPanelSpec) throws Exception
	{
		writer.startBlock();
		writer.writeHeading2Style();
		writer.writeEncoded(fieldPanelSpec.getTranslatedTitle());
		writer.writeParCommand();
		writer.endBlock();
		writer.newParagraph();
	}
	
	private void writeFormRowColumns(FormRow formRow, int indentation) throws Exception
	{
		StringBuffer encodedRowContent = new StringBuffer();
		StringBuffer rowFormatting = new StringBuffer(RtfWriter.ROW_HEADER);
		rowFormatting.append(RtfWriter.TABLE_LEFT_INDENT);
		rowFormatting.append(getWriter().convertToTwips(indentation));
		
		for (int leftColumn = 0; leftColumn < formRow.getLeftFormItemsCount(); ++leftColumn)
		{
			FormItem  formItem = formRow.getLeftFormItem(leftColumn);
			String formItemAsString = createFormItem(formRow, formItem);
			if (leftColumn != 0 && formItemHasValue(formItemAsString))
				encodedRowContent.append(FIELD_SPACING);
			
			encodedRowContent.append(formItemAsString);
		}
		
		encodedRowContent.append(RtfWriter.CELL_COMMAND);

		//FIXME medium: this is temprorarly done before freeze. Needs to be done as autofit.
		final int INCHES_FROM_LEFT_MARGIN_TO_FIRST_COLUMN_RIGHT_EDGE_MINUS_ONE = 2 - 1;
		final int INCHES_FROM_LEFT_MARGIN_TO_SECOND_COLUMN_RIGHT_EDGE_MINUS_ONE = 8 - 1;
		rowFormatting.append(getCellxCommand(INCHES_FROM_LEFT_MARGIN_TO_FIRST_COLUMN_RIGHT_EDGE_MINUS_ONE));
		
		for (int rightColumn = 0; rightColumn < formRow.getRightFormItemsCount(); ++rightColumn)
		{
			FormItem formItem = formRow.getRightFormItem(rightColumn);
			String formItemAsString = createFormItem(formRow, formItem);
			if (rightColumn != 0 && formItemHasValue(formItemAsString))
				encodedRowContent.append(FIELD_SPACING);
			
			encodedRowContent.append(formItemAsString);
		}
		
		rowFormatting.append(getCellxCommand(INCHES_FROM_LEFT_MARGIN_TO_SECOND_COLUMN_RIGHT_EDGE_MINUS_ONE));
		getWriter().writeRaw(rowFormatting.toString());
		encodedRowContent.append(RtfWriter.CELL_COMMAND);				
		getWriter().writeRaw(encodedRowContent.toString());
		getWriter().writeRaw(RtfWriter.ROW_COMMAND);
		getWriter().newLine();
	}

	private boolean formItemHasValue(String formItemAsString)
	{
		return formItemAsString.length() > 0;
	}

	private String createFormItem(FormRow formRow, FormItem formItem) throws Exception
	{
		if (formItem.isFormConstant())
		{
			FormConstant formConstant = (FormConstant) formItem;
			return writer.encode(formConstant.getConstant());				
		}
		else if (formItem.isFormFieldLabel())
		{
			return writer.encode(getFieldLabel((FormFieldLabel)formItem));					
		}
		else if (formItem.isFormFieldData())
		{
			String rawFieldData = getFieldData((FormFieldData) formItem, formRow);
			return writer.encode(rawFieldData);							
		}
		else if (formItem.isFormFieldImage())
		{
			BufferedImage image = ((FormImage)formItem).getImage();
			writer.writeImage(image);
		}
		else if (formItem.isFormQuestionFieldData())
		{
			FormFieldQuestionData formFieldQuestionData = (FormFieldQuestionData) formItem;
			String code = getFieldData((FormFieldQuestionData) formItem, formRow);
			ChoiceItem choiceItem = formFieldQuestionData.getQuestion().findChoiceByCode(code);
			if (choiceItem != null)
				return writer.encode(choiceItem.toString());
		}
		
		return "";
	}

	private String getFieldData(FormFieldData formFieldData, FormRow formRow)
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
		
		if (rawObjectData.isStringMapData())
			return createFromStringMapData((StringMapData) rawObjectData, formRow);
	
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
	
	private String createFromStringMapData(StringMapData stringMapData, FormRow formRow)
	{
		if (formRow.getLeftFormItemsCount() == 0)
			return "";
		
		FormItem  leftFormItem = formRow.getLeftFormItem(0);
		if (!leftFormItem.isFormConstant())
			return "";
		
		FormConstant formConstant = (FormConstant) leftFormItem;
		ChoiceQuestion question = getProject().getQuestion(StatusQuestion.class);
		ChoiceItem choiceItem = question.findChoiceByLabel(formConstant.getConstant());
		
		return stringMapData.getStringMap().get(choiceItem.getCode());
	}

	private String getFieldLabel(FormFieldLabel formFieldLabel)
	{
		return EAM.fieldLabel(formFieldLabel.getObjectType(), formFieldLabel.getObjectTag());
	}

	private String getCellxCommand(int uniqueRtfColumnId)
	{
		return getWriter().createCellxCommand(uniqueRtfColumnId);
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
		return hierarchyRefs;
	}
	
	private static final String FIELD_SPACING = "     ";
	
	private Project project;
	private RtfWriter writer;
	private ORefList hierarchyRefs;
}
