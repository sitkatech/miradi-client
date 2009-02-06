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
	public FieldPanelSpec getPanel(int index)
	{
		return this;
	}

	public void addFormRow(FormRow rowToAdd)
	{
		rows.add(rowToAdd);
	}

	public FormRow getFormRow(int index)
	{
		return rows.get(index);
	}

	public boolean hasBorder()
	{
		return hasBorder;
	}

	public void setHasBorder()
	{
		hasBorder = true;
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

	protected void addChoiceField(int type, String fieldTag, ChoiceQuestion question)
	{
		addField(type, fieldTag, new FormFieldQuestionData(type, fieldTag, question));
	}

	protected void addLabelAndField(int type, String fieldTag)
	{
		addField(type, fieldTag, new FormFieldData(type, fieldTag));
	}
	
	private void addField(int type, String fieldTag, FormFieldData formFieldData)
	{
		FormFieldLabel label = new FormFieldLabel(type, fieldTag);
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
	private boolean hasBorder;
	private String translatedTitle;
}
