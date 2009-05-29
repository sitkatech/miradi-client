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
import java.awt.Dimension;

import javax.swing.JPanel;

import org.martus.swing.UiScrollPane;
import org.miradi.actions.Actions;
import org.miradi.dialogs.base.DataInputPanel;
import org.miradi.dialogs.base.MultiTablePanel;
import org.miradi.dialogs.fieldComponents.PanelTitleLabel;
import org.miradi.dialogs.treetables.MultiTreeTablePanel.ScrollPaneWithHideableScrollBar;
import org.miradi.layout.OneRowPanel;
import org.miradi.main.AppPreferences;
import org.miradi.main.CommandExecutedEvent;
import org.miradi.main.CommandExecutedListener;
import org.miradi.main.EAM;
import org.miradi.main.MainWindow;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objects.BaseObject;
import org.miradi.objects.TableSettings;
import org.miradi.views.umbrella.ObjectPicker;

import com.jhlabs.awt.GridLayoutPlus;

abstract public class AbstractAssignmentEditorComponent extends MultiTablePanel  implements CommandExecutedListener
{
	public AbstractAssignmentEditorComponent(MainWindow mainWindowToUse, ObjectPicker objectPickerToUse) throws Exception
	{
		super(mainWindowToUse);
		
		objectPicker = objectPickerToUse;
		
		setBackground(AppPreferences.getDataPanelBackgroundColor());
		createTables();
		addTables();
		addTablesToSelectionController();
		
		getProject().addCommandExecutedListener(this);
	}
	
	@Override
	public void dispose()
	{
		super.dispose();
		becomeInactive();
		getProject().removeCommandExecutedListener(this);
		abstractSummaryTable.dispose();
		assignmentDateUnitsTable.dispose();
	}
	
	public void commandExecuted(CommandExecutedEvent event)
	{
		try
		{
			if (event.isSetDataCommandWithThisTypeAndTag(TableSettings.getObjectType(), TableSettings.TAG_DATE_UNIT_LIST_DATA))
				respondToExpandOrCollapseColumnEvent();

			if (event.isSetDataCommand())
				dataWasChanged();
		}
		catch(Exception e)
		{
			EAM.logException(e);
			EAM.errorDialog(EAM.text("An unexpected error has occurred"));
		}		
	}
	
	protected Actions getActions()
	{
		return getMainWindow().getActions();
	}
		
	protected ObjectPicker getPicker()
	{
		return objectPicker;
	}
	
	static class AssignmentsComponentTableScrollPane extends ScrollPaneWithHideableScrollBar
	{
		public AssignmentsComponentTableScrollPane(AbstractComponentTable contents)
		{
			super(contents);
			setHorizontalScrollBarPolicy(HORIZONTAL_SCROLLBAR_ALWAYS);
			setVerticalScrollBarPolicy(VERTICAL_SCROLLBAR_NEVER);
			widthSetter = new PersistentWidthSetterComponent(contents.getMainWindow(), this, contents.getUniqueTableIdentifier(), getPreferredSize().width);
		}
		
		public PersistentWidthSetterComponent getWidthSetterComponent()
		{
			return widthSetter;
		}
		
		@Override
		public Dimension getPreferredSize()
		{
			final Dimension size = super.getPreferredSize();
			if(widthSetter != null)
				size.width = widthSetter.getControlledWidth();
			return size;
		}
		
		@Override
		public Dimension getSize()
		{
			final Dimension size = super.getSize();
			if(widthSetter != null)
				size.width = widthSetter.getControlledWidth();
			return size;
		}
		
		private PersistentWidthSetterComponent widthSetter;
	}
	
	@Override
	public void becomeActive()
	{
		super.becomeActive();
		abstractSummaryTable.becomeActive();
	}

	@Override
	public void becomeInactive()
	{
		abstractSummaryTable.becomeInactive();
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
	
		abstractSummaryTableModel.setObjectRefs(hierarchyToSelectedRef);
		assignmentDateUnitsTableModel.setObjectRefs(hierarchyToSelectedRef);
		
		abstractSummaryTableModel.fireTableDataChanged();
		assignmentDateUnitsTableModel.fireTableDataChanged();
	}

	private void savePendingEdits()
	{
		abstractSummaryTable.stopCellEditing();
		assignmentDateUnitsTable.stopCellEditing();
	}

	protected void addTables()
	{
		DataInputPanel tablesPanel = new DataInputPanel(getProject());
		tablesPanel.setLayout(new GridLayoutPlus(2, 4));
	
		tablesPanel.add(new PanelTitleLabel(""));
		tablesPanel.add(new PanelTitleLabel(""));
		ExpandAndCollapseColumnsButtonRow aboveTableButtonRow = new ExpandAndCollapseColumnsButtonRow(assignmentDateUnitsTable);
		tablesPanel.add(aboveTableButtonRow);
		tablesPanel.add(new PanelTitleLabel(""));
		
		addTableToPanel(tablesPanel, abstractSummaryTable);
		UiScrollPane dateUnitsTableScrollPanel = addTableToPanel(tablesPanel, assignmentDateUnitsTable);
		addToHorizontalController(dateUnitsTableScrollPanel);
		aboveTableButtonRow.setTableScrollPane(dateUnitsTableScrollPanel);
		
		add(tablesPanel, BorderLayout.CENTER);
		add(createButtonBar(), BorderLayout.BEFORE_FIRST_LINE);
	}
	
	private UiScrollPane addTableToPanel(JPanel tables, AbstractComponentTable table)
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
		selectionController.addTable(abstractSummaryTable);
		selectionController.addTable(assignmentDateUnitsTable);
	}

	protected void respondToExpandOrCollapseColumnEvent() throws Exception
	{
		assignmentDateUnitsTableModel.restoreDateUnits();
	}

	protected void dataWasChanged() throws Exception
	{
		abstractSummaryTableModel.dataWasChanged();
		
		abstractSummaryTable.rebuildColumnEditorsAndRenderers();
		abstractSummaryTable.repaint();
		
		assignmentDateUnitsTable.invalidate();
		assignmentDateUnitsTable.repaint();
	}

	private void setRef(ORef ref)
	{ 
		BaseObject baseObject = null;
		if (!ref.isInvalid())
			baseObject = BaseObject.find(getProject(), ref);
		
		//FIXME medium: need to this for all the tables.  not doing it now becuase resourcetable.stopCellEditing
		//throws command exec inside commandExected exceptions.  also these tables need to be inside a container
		//that way we just loop through the tbales.  
		assignmentDateUnitsTable.stopCellEditing();
		
		abstractSummaryTableModel.setBaseObject(baseObject);
	}

	public ORefList[] getSelectedHierarchies()
	{
		return abstractSummaryTable.getSelectedHierarchies();
	}

	protected JPanel createButtonBar()
	{
		OneRowPanel box = new OneRowPanel();
		box.setBackground(AppPreferences.getDataPanelBackgroundColor());
		box.setGaps(3);
		addButtons(box);
		
		return box;
	}
	
	abstract protected void createTables() throws Exception;
	
	abstract protected void addButtons(OneRowPanel box);

	private ObjectPicker objectPicker;
	protected AbstractSummaryTable abstractSummaryTable;
	protected AssignmentDateUnitsTable assignmentDateUnitsTable;
	protected AbstractSummaryTableModel abstractSummaryTableModel;
	protected AssignmentDateUnitsTableModel assignmentDateUnitsTableModel;
}
