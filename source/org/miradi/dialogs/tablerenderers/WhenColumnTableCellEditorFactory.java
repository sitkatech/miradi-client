/* 
Copyright 2005-2010, Foundations of Success, Bethesda, Maryland 
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

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JComponent;
import javax.swing.JTable;

import org.miradi.dialogfields.editors.WhenEditorComponent;
import org.miradi.dialogs.base.ModelessDialogWithClose;
import org.miradi.dialogs.planning.upperPanel.PlanningUpperMultiTable;
import org.miradi.main.EAM;
import org.miradi.main.MainWindow;
import org.miradi.utils.MiradiScrollPane;
//FIXME this class has been duplicated from WhoColumnTableCellEditorF.  Need to pull up a common class
public class WhenColumnTableCellEditorFactory extends AbstractTableCellEditorFactory
{
	public WhenColumnTableCellEditorFactory(MainWindow mainWindowToUse, PlanningUpperMultiTable tableToUse)
	{
		mainWindow = mainWindowToUse;
		table = tableToUse;

		rendererFactory = new MultiLineObjectTableCellRendererOnlyFactory(tableToUse, new DefaultFontProvider(mainWindowToUse));

		JComponent rendererComponent = rendererFactory.getRendererComponent();
		rendererComponent.addMouseListener(new LeftClickHandler());
	}
	
	public Component getTableCellEditorComponent(JTable tableToUse, Object value, boolean isSelected, int row, int column) 
	{
		return rendererFactory.getTableCellEditorComponent(tableToUse, value, true, row, column);
	}

	public Object getCellEditorValue()
	{
		return null;
	}

	public MainWindow getMainWindow()
	{
		return mainWindow;
	}
	
	private class LeftClickHandler extends MouseAdapter
	{
		@Override
		public void mouseClicked(MouseEvent e)
		{
			table.stopCellEditing();

			WhenEditorComponent codeListEditor = new WhenEditorComponent();
			MiradiScrollPane codeListEditorScrollPane = new MiradiScrollPane(codeListEditor);
			ModelessDialogWithClose dialog = new ModelessDialogWithClose(getMainWindow(), EAM.text("When Editor..."));
			dialog.add(codeListEditorScrollPane, BorderLayout.CENTER);
			dialog.pack();
			getMainWindow().getCurrentView().showFloatingPropertiesDialog(dialog);
		}
	}
	
	private PlanningUpperMultiTable table;
	private MainWindow mainWindow;
	private MultiLineObjectTableCellRendererOnlyFactory rendererFactory;
}
