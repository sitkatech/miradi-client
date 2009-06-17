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
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.EventObject;

import javax.swing.JTable;
import javax.swing.event.CellEditorListener;
import javax.swing.table.TableCellEditor;

import org.miradi.dialogfields.StandAloneCodeListComponent;
import org.miradi.dialogs.planning.upperPanel.PlanningUpperMultiTable;
import org.miradi.main.EAM;
import org.miradi.main.MainWindow;
import org.miradi.objects.BaseObject;
import org.miradi.project.Project;
import org.miradi.questions.ProjectResourceQuestion;

public class WhoColumnTableCellEditorFactory extends CodeListRendererFactory implements TableCellEditor 
{
	public WhoColumnTableCellEditorFactory(MainWindow mainWindowToUse, PlanningUpperMultiTable tableToUse)
	{
		super(tableToUse, new DefaultFontProvider(mainWindowToUse));

		mainWindow = mainWindowToUse;
		table = tableToUse;

		getRendererComponent().addMouseListener(new LeftClickHandler());
	}
	
	public Component getTableCellEditorComponent(JTable tableToUse, Object value, boolean isSelected, int row, int column) 
	{
		return super.getTableCellRendererComponent(tableToUse, value, isSelected, false, row, column);
	}

	public void addCellEditorListener(CellEditorListener l)
	{
	}

	public void cancelCellEditing()
	{
	}

	public Object getCellEditorValue()
	{
		return null;
	}

	public boolean isCellEditable(EventObject anEvent)
	{
		return true;
	}

	public void removeCellEditorListener(CellEditorListener l)
	{
	}

	public boolean shouldSelectCell(EventObject anEvent)
	{
		return true;
	}

	public boolean stopCellEditing()
	{
		return true;
	}

	class LeftClickHandler extends MouseAdapter
	{
		public void mouseClicked(MouseEvent e)
		{
			final BaseObject baseObjectForRow = getBaseObjectForRow(table.getSelectedRow(), table.getSelectedColumn());
			StandAloneCodeListComponent codeListEditor = new StandAloneCodeListComponent(baseObjectForRow, new ProjectResourceQuestion(getProject()));
			codeListEditor.showModalDialog(getMainWindow(), EAM.text("Project Resource"));
		}
	}

	public Project getProject()
	{
		return getMainWindow().getProject();
	}
	
	public MainWindow getMainWindow()
	{
		return mainWindow;
	}
	
	private PlanningUpperMultiTable table;
	private MainWindow mainWindow;
}
