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
package org.miradi.forms;

import java.util.Vector;

import javax.swing.Icon;

import org.miradi.forms.objects.FormFieldCodeListData;
import org.miradi.questions.ChoiceQuestion;
import org.miradi.utils.BufferedImageFactory;

public class FieldPanelSpec extends PropertiesPanelSpec
{
	public FieldPanelSpec()
	{
		rows = new Vector<FormRow>();
		translatedTitle = "";
	}
	
	@Override
	public int getFieldRowCount()
	{
		return rows.size();
	}
	
	@Override
	public FieldPanelSpec getPanel(int index) throws Exception
	{
		return this;
	}

	public void addFormRow(FormRow rowToAdd)
	{
		rows.add(rowToAdd);
	}

	@Override
	public FormRow getFormRow(int index) throws Exception
	{
		return rows.get(index);
	}

	public boolean hasTitle()
	{
		return getTranslatedTitle().length() > 0;
	}

	public String getTranslatedTitle()
	{
		return translatedTitle;
	}

	public void setTranslatedTitle(String translatedTitleToUse)
	{
		translatedTitle = translatedTitleToUse;
	}

	protected void addBlankHorizontalLine()
	{
		addFormRow(new FormRow());
	}
	
	protected void addCodeListField(int type, String fieldTag, ChoiceQuestion question)
	{
		addField(type, fieldTag, new FormFieldCodeListData(type, fieldTag, question));
	}

	protected void addCurrencyField(int type, String fieldTag)
	{
		addField(type, fieldTag, new FormCurrencyFieldData(type, fieldTag));
	}
	
	protected void addChoiceField(int type, String fieldTag, ChoiceQuestion question)
	{
		addField(type, fieldTag, new FormFieldQuestionData(type, fieldTag, question));
	}
	
	protected void addLabelAndField(int objectType, String fieldTag, int dataEntryType)
	{
		addField(objectType, fieldTag, new FormFieldData(objectType, fieldTag, dataEntryType));
	}
	
	protected void addLabelAndReadOnlySingeLineField(int objectType, String fieldTag)
	{
		addField(objectType, fieldTag, new FormFieldData(objectType, fieldTag, TYPE_SINGLE_LINE_READONLY_STRING));
	}
	
	protected void addLabelAndExternalProjectIdField(int objectType, String fieldTag, String externalProjectIdCode, String translatedLabelToUse)
	{
		addFieldWithCustomLabel(objectType, fieldTag, new FormFieldExternalProjectIdData(objectType, fieldTag, externalProjectIdCode), translatedLabelToUse);
	}
	
	protected void addMultipleTaxonomyWithEditButtonFields(int objectType, String fieldTag)
	{
		addFieldWithCustomLabel(objectType, fieldTag, new FormFieldMultipleTaxonomyWithEditButtonFields(objectType, fieldTag), "");
	}

	protected void addLabelAndField(int type, String fieldTag)
	{
		addField(type, fieldTag, new FormFieldData(type, fieldTag));
	}
	
	private void addField(int type, String fieldTag, FieldRelatedFormItem formFieldData)
	{
		FormFieldLabel label = new FormFieldLabel(type, fieldTag);
		addFormRow(label, formFieldData);
	}
	
	private void addFieldWithCustomLabel(int type, String fieldTag, FieldRelatedFormItem formFieldData, String translatedLabelToUse)
	{
		FormConstant label = new FormConstant(translatedLabelToUse);
		addFormRow(label, formFieldData);
	}

	public void addFormRow(FormItem label, FieldRelatedFormItem formFieldData)
	{
		addFormRow(new FormRow(label, formFieldData));
	}

	protected void addLabelAndFieldWithLabel(String translatedLabel, int type, String fieldTag)
	{
		addLabelAndFieldsWithLabels(translatedLabel, type, new String[]{fieldTag});
	}
	
	protected void addLabelAndFieldsWithLabels(String translatedLabel, int type, String[] tags)
	{
		FormRow formRow = new FormRow();
		addTags(formRow, translatedLabel, type, tags);
	}
	
	protected void addStandardNameRow(Icon icon, String translatedLabel, int type, String[] tags)
	{
		FormRow formRow = new FormRow();
		formRow.addLeftFormItem(new FormImage(BufferedImageFactory.getImage(icon)));
		addTags(formRow, translatedLabel, type, tags);
	}

	private void addTags(FormRow formRow, String translatedLabel, int type, String[] tags)
	{
		formRow.addLeftFormItem(new FormConstant(translatedLabel));
		for(int index = 0; index < tags.length; ++index)
		{
			formRow.addRightFormItem(new FormFieldLabel(type, tags[index]));
			formRow.addRightFormItem(new FormFieldData(type, tags[index]));
		}
		addFormRow(formRow);
	}
	
	protected void addLeftRightConstants(String leftSide, String rightSide)
	{
		FormRow formRow = new FormRow();
		formRow.addLeftFormItem(new FormConstant(leftSide));
		formRow.addRightFormItem(new FormConstant(rightSide));		
		addFormRow(formRow);
	}

	private Vector<FormRow> rows;
	private String translatedTitle;
}
