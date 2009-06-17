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

import javax.swing.AbstractCellEditor;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;

import org.miradi.dialogfields.StandAloneCodeListComponent;
import org.miradi.dialogs.planning.upperPanel.PlanningUpperMultiTable;
import org.miradi.dialogs.tablerenderers.MultiLineObjectTableCellRendererFactory.TableCellHtmlRendererComponent;
import org.miradi.main.AppPreferences;
import org.miradi.main.EAM;
import org.miradi.main.MainWindow;
import org.miradi.objects.BaseObject;
import org.miradi.project.Project;
import org.miradi.questions.ProjectResourceQuestion;

public class WhoColumnTableCellEditorFactory extends AbstractCellEditor implements TableCellEditor
{
	public WhoColumnTableCellEditorFactory(MainWindow mainWindowToUse, PlanningUpperMultiTable tableToUse)
	{
		mainWindow = mainWindowToUse;
		table = tableToUse;

		rendererFactory = new MultiLineObjectTableCellRendererFactory(tableToUse, new DefaultFontProvider(mainWindowToUse));
		rendererFactory.setCellBackgroundColor(AppPreferences.RESOURCE_TABLE_BACKGROUND);

		TableCellHtmlRendererComponent rendererComponent = rendererFactory.getRendererComponent();
		rendererComponent.addMouseListener(new LeftClickHandler());
	}
	
	public Component getTableCellEditorComponent(JTable tableToUse, Object value, boolean isSelected, int row, int column) 
	{
		return rendererFactory.getTableCellRendererComponent(tableToUse, value, true, false, row, column);
	}

	class LeftClickHandler extends MouseAdapter
	{
		public void mouseClicked(MouseEvent e)
		{
			final BaseObject baseObjectForRow = table.getBaseObjectForRowColumn(table.getSelectedRow(), table.getSelectedColumn());
			StandAloneCodeListComponent codeListEditor = new StandAloneCodeListComponent(baseObjectForRow, new ProjectResourceQuestion(getProject()));
			codeListEditor.showModalDialog(getMainWindow(), EAM.text("Project Resource"));
		}
	}

	public Object getCellEditorValue()
	{
		return null;
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
	private MultiLineObjectTableCellRendererFactory rendererFactory;
}
