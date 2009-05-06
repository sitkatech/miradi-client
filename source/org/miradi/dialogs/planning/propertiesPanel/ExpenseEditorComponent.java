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

import java.awt.BorderLayout;

import javax.swing.JPanel;

import org.martus.swing.UiScrollPane;
import org.miradi.actions.ActionCreateExpense;
import org.miradi.actions.ActionDeleteExpense;
import org.miradi.layout.OneRowPanel;
import org.miradi.main.AppPreferences;
import org.miradi.main.MainWindow;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objects.BaseObject;
import org.miradi.views.umbrella.ObjectPicker;

//FIXME this class is duplicate and under constrcut
public class ExpenseEditorComponent extends AbstractMultiTablePanelEditorComponent
{
	public ExpenseEditorComponent(MainWindow mainWindowToUse, ObjectPicker objectPickerToUse) throws Exception
	{
		super(mainWindowToUse, objectPickerToUse);
		setBackground(AppPreferences.getDataPanelBackgroundColor());
		
		createTables();
		addTables();
		addTablesToSelectionController();
	}
	
	@Override
	public void dispose()
	{
		super.dispose();
		expenseSummaryTable.dispose();
		workUnitsTable.dispose();
	}
	
	@Override
	public void becomeActive()
	{
		super.becomeActive();
		expenseSummaryTable.becomeActive();
	}
	
	@Override
	public void becomeInactive()
	{
		expenseSummaryTable.becomeInactive();
		super.becomeInactive();
	}
	
	public void setObjectRefs(ORef[] hierarchyToSelectedRef)
	{
		savePendingEdits();
		
		if (hierarchyToSelectedRef.length == 0)
		{
			setRef(ORef.INVALID);
		}
		else
		{
			ORefList selectionHierarchyRefs = new ORefList(hierarchyToSelectedRef[0]);
			ORef baseObjectRef = selectionHierarchyRefs.get(0);
			setRef(baseObjectRef);
		}

		expenseSummaryTableModel.setObjectRefs(hierarchyToSelectedRef);
		workUnitsTableModel.setObjectRefs(hierarchyToSelectedRef);
		
		expenseSummaryTableModel.fireTableDataChanged();
		workUnitsTableModel.fireTableDataChanged();
	}
	
	private void savePendingEdits()
	{
		expenseSummaryTable.stopCellEditing();
		workUnitsTable.stopCellEditing();
	}

	private void createTables() throws Exception
	{
		expenseSummaryTableModel = new ExpenseSummaryTableModel(getProject());
		expenseSummaryTable = new ExpenseSummaryTable(getMainWindow(), expenseSummaryTableModel);
		
		workUnitsTableModel = new WorkUnitsTableModel(getProject(), expenseSummaryTableModel);
		workUnitsTable = new WorkUnitsTable(getMainWindow(), workUnitsTableModel);		
	}
	
	private void addTables()
	{
		OneRowPanel tables = new OneRowPanel();

		addTableToPanel(tables, expenseSummaryTable);
		addToHorizontalController(addTableToPanel(tables, workUnitsTable));
		
		add(tables, BorderLayout.CENTER);
		add(createButtonBar(), BorderLayout.BEFORE_FIRST_LINE);
	}
	
	private UiScrollPane addTableToPanel(OneRowPanel tables, AssignmentsComponentTable table)
	{
		addRowHeightControlledTable(table);
		AssignmentsComponentTableScrollPane scroller = new AssignmentsComponentTableScrollPane(table);
		addToVerticalController(scroller);
		tables.add(scroller);
		tables.add(scroller.getWidthSetterComponent());
		return scroller;
	}
	
	protected void addTablesToSelectionController()
	{
		selectionController.addTable(expenseSummaryTable);
		selectionController.addTable(workUnitsTable);
	}
	
	private JPanel createButtonBar()
	{
		OneRowPanel box = new OneRowPanel();
		box.setBackground(AppPreferences.getDataPanelBackgroundColor());
		box.setGaps(3);
		box.add(createObjectsActionButton(getActions().getObjectsAction(ActionCreateExpense.class), getPicker()));
		box.add(createObjectsActionButton(getActions().getObjectsAction(ActionDeleteExpense.class), expenseSummaryTable));
		
		return box;
	}
	
	protected void respondToExpandOrCollapseColumnEvent() throws Exception
	{
		workUnitsTableModel.restoreDateUnits();
	}

	protected void dataWasChanged() throws Exception
	{
		expenseSummaryTableModel.dataWasChanged();
		
		expenseSummaryTable.rebuildColumnEditorsAndRenderers();
		expenseSummaryTable.repaint();
		
		workUnitsTable.invalidate();
		workUnitsTable.repaint();
	}
	
	private void setRef(ORef ref)
	{ 
		BaseObject baseObject = null;
		if (!ref.isInvalid())
			baseObject = BaseObject.find(getProject(), ref);
		
		//FIXME need to this for all the tables.  not doing it now becuase resourcetable.stopCellEditing
		//throws command exec inside commandExected exceptions.  also these tables need to be inside a container
		//that way we just loop through the tbales.  
		workUnitsTable.stopCellEditing();
		
		expenseSummaryTableModel.setBaseObject(baseObject);
	}
	
	public ORefList[] getSelectedHierarchies()
	{
		return expenseSummaryTable.getSelectedHierarchies();
	}
	
	private ExpenseSummaryTable expenseSummaryTable;
	private WorkUnitsTable workUnitsTable;
	
	private ExpenseSummaryTableModel expenseSummaryTableModel;
	private WorkUnitsTableModel workUnitsTableModel;
}
