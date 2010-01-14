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

import javax.swing.AbstractCellEditor;
import javax.swing.JTable;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

import org.miradi.questions.ChoiceItem;
import org.miradi.questions.ChoiceQuestion;
import org.miradi.utils.QuestionPopupEditorComponent;

public class QuestionPopupCellEditorAndRendererFactory extends AbstractCellEditor implements TableCellEditor, TableCellRenderer
{
	public QuestionPopupCellEditorAndRendererFactory(ListSelectionListener selectionHandlerToUse, ChoiceQuestion questionToUse) throws Exception 
	{
	    super();
	    
	    //TODO add some sort of text.  Text will appear to the left of the choice item popup section
	    questionEditor = new QuestionPopupEditorComponent(selectionHandlerToUse, questionToUse, "");
	}
	
	public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int r, int c)
	{
		ChoiceItem choiceItem = (ChoiceItem) value;
		questionEditor.setText(choiceItem.getCode());
		
		return questionEditor;
	}

	public Object getCellEditorValue()
	{
		return questionEditor.getText();
	}
	
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
	{
		ChoiceItem choiceItem = (ChoiceItem) value;
		questionEditor.setText(choiceItem.getCode());
		
		return questionEditor;
	}
	
	private QuestionPopupEditorComponent questionEditor;
}
