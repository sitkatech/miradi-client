/* 
Copyright 2005-2011, Foundations of Success, Bethesda, Maryland 
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

package org.miradi.dialogs.planning;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;

import org.miradi.questions.ChoiceItem;
import org.miradi.questions.ChoiceQuestion;
import org.miradi.questions.StaticQuestionManager;
import org.miradi.questions.StatusQuestion;

public class ViabilityTableHeader extends JTableHeader
{
	public ViabilityTableHeader(TableWithExpandableColumnsInterface tableToUse)
	{
		super(tableToUse.getColumnModel());
	}
	
	@Override
	public TableCellRenderer getDefaultRenderer()
	{
		return new ColumnHeaderRenderer();
	}
	
	private class ColumnHeaderRenderer extends DefaultTableCellRenderer
	{
		public ColumnHeaderRenderer()
		{
			statusQuestion = StaticQuestionManager.getQuestion(StatusQuestion.class);
		}

		 @Override
		public Component getTableCellRendererComponent(JTable tableToUse, Object value, boolean isSelected, boolean hasFocus, int row, int column) 
		 {
			 JLabel renderer = (JLabel) super.getTableCellRendererComponent(tableToUse, value, isSelected, hasFocus, row, column);
			 renderer.setBorder(UIManager.getBorder("TableHeader.cellBorder"));
			 renderer.setHorizontalAlignment(JLabel.CENTER);
			 renderer.setBackground(tableToUse.getTableHeader().getBackground());
			 renderer.setForeground(tableToUse.getTableHeader().getForeground());
			
			 ChoiceItem choice = statusQuestion.findChoiceByLabel(renderer.getText());
			 if (choice != null)
			 {
				renderer.setBackground(choice.getColor());
			 	renderer.setForeground(Color.BLACK);
			 }
			
			 return renderer;
		 }

		 private ChoiceQuestion statusQuestion;
	}

}
