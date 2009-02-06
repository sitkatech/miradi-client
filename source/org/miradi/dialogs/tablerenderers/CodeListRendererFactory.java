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
package org.miradi.dialogs.tablerenderers;

import java.awt.Component;

import javax.swing.JComponent;
import javax.swing.JTable;

import org.miradi.main.MainWindow;
import org.miradi.questions.ChoiceQuestion;
import org.miradi.utils.CodeList;

public class CodeListRendererFactory extends MultiLineObjectTableCellRendererFactory
{
	public CodeListRendererFactory(MainWindow mainWindowToUse, RowColumnBaseObjectProvider providerToUse, FontForObjectTypeProvider fontProviderToUse)
	{
		super(providerToUse, fontProviderToUse);
	}

	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int tableColumn)
	{
		String labelText = getLabelText(value);
		JComponent renderer = (JComponent) super.getTableCellRendererComponent(table, labelText, isSelected, hasFocus, row, tableColumn);

		if(isSelected)
			renderer.setBackground(renderer.getBackground());

		return renderer;
	}
	
	@Override
	public int getPreferredHeight(JTable table, int row, int column, Object value)
	{
		String html = getLabelText(value);
		return super.getPreferredHeight(table, row, column, html);
	}

	private String getLabelText(Object value)
	{
		CodeList codeList = getCodeList(value);
		if(codeList == null)
			return "";
		
		return convertToCommaSeperatedValues(codeList); 
	}

	private String convertToCommaSeperatedValues(CodeList codeList)
	{
		String commaSeperatedValues = "";
		for (int i = 0; i < codeList.size(); ++i)
		{
			if (i > 0)
				commaSeperatedValues +=", ";
			
			commaSeperatedValues += question.getValue(codeList.get(i));
		}
		
		return commaSeperatedValues;
	}

	private CodeList getCodeList(Object value)
	{
		if(! (value instanceof CodeList) )
			return null;
		
		return (CodeList)value;
	}
	
	public void setQuestion(ChoiceQuestion questionToUse)
	{
		question = questionToUse;
	}
	
	private ChoiceQuestion question;

}
