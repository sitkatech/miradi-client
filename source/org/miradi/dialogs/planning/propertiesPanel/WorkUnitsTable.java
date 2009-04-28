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
package org.miradi.dialogs.planning.propertiesPanel;

import java.awt.Color;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Vector;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import org.miradi.commands.CommandSetObjectData;
import org.miradi.dialogs.fieldComponents.PanelTextField;
import org.miradi.dialogs.tablerenderers.BasicTableCellRendererFactory;
import org.miradi.dialogs.tablerenderers.DefaultFontProvider;
import org.miradi.dialogs.tablerenderers.NumericTableCellRendererFactory;
import org.miradi.main.AppPreferences;
import org.miradi.main.EAM;
import org.miradi.main.MainWindow;
import org.miradi.objectdata.DateUnitListData;
import org.miradi.objecthelpers.DateUnit;
import org.miradi.objects.TableSettings;
import org.miradi.utils.CodeList;
import org.miradi.utils.SingleClickAutoSelectCellEditor;

public class WorkUnitsTable extends AssignmentsComponentTable
{
	public WorkUnitsTable(MainWindow mainWindowToUse, WorkUnitsTableModel modelToUse) throws Exception
	{
		super(mainWindowToUse, modelToUse, UNIQUE_IDENTIFIER);
		setBackground(getColumnBackGroundColor(0));	
		setSingleCellEditor();
		addMouseListener(new RightClickHandler(this));
		setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		renderer = new NumericTableCellRendererFactory(modelToUse, new DefaultFontProvider(getMainWindow()));
	}
	
	private WorkUnitsTableModel getWorkUnitsTableModel()
	{
		return (WorkUnitsTableModel) getModel();
	}
	
	public TableCellRenderer getCellRenderer(int row, int column)
	{
		renderer.setCellBackgroundColor(getColumnBackGroundColor(column));
		return renderer;	
	}
	
	public Color getColumnBackGroundColor(int column)
	{
		return AppPreferences.WORKPLAN_TABLE_BACKGROUND;
	}

	private void setSingleCellEditor()
	{
		int colCount = getColumnCount();
		for (int i = 0; i < colCount; i++)
		{
			int modelColumn = convertColumnIndexToModel(i);
			TableColumn column = getColumnModel().getColumn(modelColumn);
			column.setCellEditor(new SingleClickAutoSelectCellEditor(new PanelTextField()));
		}
	}
	
	protected int getColumnWidth(int column)
	{
		return getColumnHeaderWidth(column);
	}
	
	public int getColumnAlignment()
	{
		return JLabel.RIGHT;
	}
	
	private boolean isDayColumnSelected()
	{
		int selectedColumnIndex = getSelectedColumn();
		if (isEmptySelection(selectedColumnIndex))
			return false;	
		
		DateUnit dateUnit = getWorkUnitsTableModel().getDateUnit(selectedColumnIndex);
		return dateUnit.isDay();
	}
	
	private boolean isSelectedDateUnitColumnExpanded()
	{
		int selectedColumnIndex = getSelectedColumn();
		if (isEmptySelection(selectedColumnIndex))
			return false;	
		
		try
		{
			Vector<DateUnit> currentDateUnits = getWorkUnitsTableModel().getCopyOfDateUnits();
			DateUnit dateUnit = currentDateUnits.get(selectedColumnIndex);
			if (dateUnit.hasSubDateUnits())
				return currentDateUnits.containsAll(dateUnit.getSubDateUnits());
			
			return currentDateUnits.contains(dateUnit);
		}
		catch(Exception e)
		{
			EAM.logException(e);
			return false;
			
		}
	}

	private void respondToExpandOrCollapseColumnEvent() throws Exception
	{
		int selectedColumnIndex = getSelectedColumn();
		if (isEmptySelection(selectedColumnIndex))
			return;
	
		Vector<DateUnit> currentDateUnits = getWorkUnitsTableModel().getCopyOfDateUnits();
		DateUnit dateUnit = currentDateUnits.get(selectedColumnIndex);
		Vector<DateUnit> subDateUnits = getProject().getProjectCalendar().getSubDateUnits(dateUnit);					
		if (currentDateUnits.containsAll(subDateUnits))
			recursivleyCollapseDateUnitAndItsSubDateUnits(currentDateUnits, dateUnit);
		else
			currentDateUnits.addAll(subDateUnits);
		
		saveColumnDateUnits(currentDateUnits);
	}

	private void recursivleyCollapseDateUnitAndItsSubDateUnits(Vector<DateUnit> currentDateUnits, DateUnit dateUnit) throws Exception
	{
		if (!dateUnit.hasSubDateUnits())
			return;
		
		Vector<DateUnit> subDateUnits = dateUnit.getSubDateUnits();
		currentDateUnits.removeAll(subDateUnits);
		for(DateUnit thisDateUnit : subDateUnits)
		{
			recursivleyCollapseDateUnitAndItsSubDateUnits(currentDateUnits, thisDateUnit);
		}
	}
	
	private boolean isEmptySelection(int selectedColumnIndex)
	{
		return selectedColumnIndex < 0;
	}
		
	public void saveColumnDateUnits(Vector<DateUnit> currentDateUnits) throws Exception
	{	
		CodeList dateUnits = DateUnitListData.convertToCodeList(currentDateUnits);
		TableSettings tableSettings = TableSettings.findOrCreate(getProject(), getWorkUnitsTableModel().getUniqueTableModelIdentifier());
		CommandSetObjectData setDateUnitsCommand = tableSettings.createCommandToUpdateDateUnitList(dateUnits);
		getProject().executeCommand(setDateUnitsCommand);
	}

	class RightClickHandler extends MouseAdapter
	{
		public RightClickHandler(JTable tableToUse)
		{
			table = tableToUse;
			expandAction = new ExpandHandler();
			collapseAction = new CollapseHandler();
		}
		
		public void mousePressed(MouseEvent event)
		{
			if(event.isPopupTrigger())
				doRightClickMenu(event);
		}

		public void mouseReleased(MouseEvent event)
		{
			if(event.isPopupTrigger())
				doRightClickMenu(event);
		}
		
		public void doRightClickMenu(MouseEvent event)
		{
			JPopupMenu popupMenu = new JPopupMenu();
			if (!isDayColumnSelected())
				addColpseExpandColumnMenuItems(popupMenu);
			
			Point clickLocation  = event.getPoint();
			popupMenu.show(table, (int)clickLocation.getX(), (int)clickLocation.getY());
		}

		private void addColpseExpandColumnMenuItems(JPopupMenu popupMenu)
		{
			if (isSelectedDateUnitColumnExpanded())
				popupMenu.add(new JMenuItem(collapseAction));
			else
				popupMenu.add(new JMenuItem(expandAction));
		}
		
		private JTable table;
		private Action expandAction;
		private Action collapseAction;
	}
	
	class ExpandHandler extends AbstractAction
	{
		public ExpandHandler()
		{
			super(EAM.text("Expand Selected Column"));
		}
		
		public void actionPerformed(ActionEvent event)
		{
			try
			{
				respondToExpandOrCollapseColumnEvent();
			}
			catch(Exception e)
			{
				EAM.logException(e);
			}
		}	
	}
	
	class CollapseHandler extends AbstractAction
	{
		public CollapseHandler()
		{
			super(EAM.text("Collapse Selected Column"));
		}
		
		public void actionPerformed(ActionEvent event)
		{
			try
			{
				respondToExpandOrCollapseColumnEvent();
			}
			catch(Exception e)
			{
				EAM.logException(e);
			}
		}	
	}
	
	public static final String UNIQUE_IDENTIFIER = "WorkUnitsTable";

	private BasicTableCellRendererFactory renderer;
}
