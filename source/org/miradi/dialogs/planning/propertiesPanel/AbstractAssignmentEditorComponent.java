/* 
Copyright 2005-2021, Foundations of Success, Bethesda, Maryland
on behalf of the Conservation Measures Partnership ("CMP").
Material developed between 2005-2013 is jointly copyright by Beneficent Technology, Inc. ("The Benetech Initiative"), Palo Alto, California.

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

import com.jhlabs.awt.GridLayoutPlus;
import org.martus.swing.UiScrollPane;
import org.miradi.actions.Actions;
import org.miradi.dialogs.base.DataInputPanel;
import org.miradi.dialogs.base.MultiTablePanel;
import org.miradi.layout.OneRowPanel;
import org.miradi.main.*;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.objects.BaseObject;
import org.miradi.objects.Strategy;
import org.miradi.objects.Task;
import org.miradi.views.umbrella.ObjectPicker;

import javax.swing.*;
import java.awt.*;

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
	}
	
	public void commandExecuted(CommandExecutedEvent event)
	{
		try
		{
			if (eventForcesRebuild(event))
				rebuild();
		}
		catch(Exception e)
		{
			EAM.alertUserOfNonFatalException(e);
		}		
	}

	private boolean eventForcesRebuild(CommandExecutedEvent event)
	{
		if (event.isSetDataCommandWithThisTypeAndTag(ObjectType.STRATEGY, Strategy.TAG_RESOURCE_ASSIGNMENT_IDS))
			return true;

		if (event.isSetDataCommandWithThisTypeAndTag(ObjectType.TASK, Task.TAG_RESOURCE_ASSIGNMENT_IDS))
			return true;

		return false;
	}

	protected Actions getActions()
	{
		return getMainWindow().getActions();
	}
		
	protected ObjectPicker getPicker()
	{
		return objectPicker;
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
	
		abstractAssignmentSummaryTableModel.setObjectRefs(new ORefList(hierarchyToSelectedRef));
		abstractAssignmentSummaryTableModel.fireTableDataChanged();
	}

	private void savePendingEdits()
	{
		abstractSummaryTable.stopCellEditing();
	}

	private void addTables()
	{
		DataInputPanel tablesPanel = new DataInputPanel(getProject());
		tablesPanel.setLayout(new GridLayoutPlus(2, 4));
	
		tablesPanel.add(new FillerPanel());
		tablesPanel.add(new FillerPanel());
		tablesPanel.add(new FillerPanel());
		tablesPanel.add(new FillerPanel());

		addTableToPanel(tablesPanel, abstractSummaryTable);

		add(tablesPanel, BorderLayout.CENTER);
		add(createButtonBar(), BorderLayout.BEFORE_FIRST_LINE);
	}
	
	private UiScrollPane addTableToPanel(JPanel tables, AbstractAssignmentDetailsTable table)
	{
		addRowHeightControlledTable(table);
		ComponentTableScrollPane scroller = new ComponentTableScrollPane(table);
		addToVerticalController(scroller);
		tables.add(scroller);
		tables.add(scroller.getWidthSetterComponent());
		return scroller;
	}

	private void addTablesToSelectionController()
	{
		selectionController.addTable(abstractSummaryTable);
	}

	private void rebuild() throws Exception
	{
		abstractAssignmentSummaryTableModel.dataWasChanged();
		
		abstractSummaryTable.rebuildColumnEditorsAndRenderers();
		abstractSummaryTable.repaint();
	}

	private void setRef(ORef ref)
	{ 
		BaseObject baseObject = null;
		if (!ref.isInvalid())
			baseObject = BaseObject.find(getProject(), ref);
		
		abstractAssignmentSummaryTableModel.setBaseObject(baseObject);
	}

	public ORefList[] getSelectedHierarchies()
	{
		return abstractSummaryTable.getSelectedHierarchies();
	}

	private JPanel createButtonBar()
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
	protected AbstractAssignmentDetailsMainTable abstractSummaryTable;
	protected AbstractAssignmentSummaryTableModel abstractAssignmentSummaryTableModel;
}
