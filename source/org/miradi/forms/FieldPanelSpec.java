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
package org.miradi.forms;

import java.util.Vector;

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

	protected void addLabelAndField(int type, String fieldTag)
	{
		FormFieldLabel label = new FormFieldLabel(type, fieldTag);
		FormFieldData data = new FormFieldData(type, fieldTag);
		addFormRow(new FormRow(label, data));
	}

	protected void addLabelAndFieldWithLabel(String translatedLabel, int type, String fieldTag)
	{
		addLabelAndFieldsWithLabels(translatedLabel, type, new String[]{fieldTag});
	}
	
	protected void addLabelAndFieldsWithLabels(String translatedLabel, int type, String[] strings)
	{
		FormRow formRow = new FormRow();
		formRow.addLeftFormItem(new FormConstant(translatedLabel));
		for(int index = 0; index < strings.length; ++index)
		{
			formRow.addRightFormItem(new FormFieldLabel(type, strings[index]));
			formRow.addRightFormItem(new FormFieldData(type, strings[index]));
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
