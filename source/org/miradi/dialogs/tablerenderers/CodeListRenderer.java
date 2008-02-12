/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.dialogs.tablerenderers;

import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JTable;

import org.miradi.questions.ChoiceQuestion;
import org.miradi.utils.CodeList;

public class CodeListRenderer extends TableCellRendererForObjects
{
	public CodeListRenderer(RowColumnBaseObjectProvider providerToUse, FontForObjectTypeProvider fontProviderToUse)
	{
		super(providerToUse, fontProviderToUse);
	}

	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int tableColumn)
	{
		JLabel renderer = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, tableColumn);
		String labelText = getLabelText(value);

		if(isSelected)
			renderer.setBackground(renderer.getBackground());

		renderer.setText(labelText);
		
		return renderer;
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
