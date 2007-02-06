/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.workplan;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JViewport;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.conservationmeasures.eam.actions.ActionAddAssignment;
import org.conservationmeasures.eam.actions.ActionRemoveAssignment;
import org.conservationmeasures.eam.actions.Actions;
import org.conservationmeasures.eam.dialogs.DisposablePanel;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.IdList;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.Task;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.views.budget.BudgetTable;
import org.conservationmeasures.eam.views.budget.BudgetTableUnitsModel;
import org.conservationmeasures.eam.views.umbrella.ObjectPicker;

import com.jhlabs.awt.GridLayoutPlus;

public class AssignmentEditorComponent extends DisposablePanel
{
	public AssignmentEditorComponent(Actions actions, Project projectToUse, ObjectPicker objectPickerToUse) throws Exception
	{
		super(new BorderLayout());
		
		project = projectToUse;
		objectPicker = objectPickerToUse;

		unitsModel = new BudgetTableUnitsModel(project, new IdList());
				
		JScrollPane scrollPane = createScrollPaneWithFixedHeader();
		add(scrollPane, BorderLayout.CENTER);
		add(createButtonBar(actions), BorderLayout.EAST);
	}

	public void dispose()
	{
		super.dispose();
	}
	
	public void setTaskId(BaseId taskId)
	{ 
		lockedTable.setTask(taskId);
		scrollTable.setTask(taskId);
		Task task = (Task)project.findObject(ObjectType.TASK, taskId);
		
		lockedModel.setTask(task);
		scrollModel.setTask(task);
	}

	public void dataWasChanged()
	{
		lockedModel.dataWasChanged();
		scrollModel.dataWasChanged();
		
		lockedTable.cancelCellEditing();
		scrollTable.cancelCellEditing();
	}
	
	JPanel createButtonBar(Actions actions)
	{
		GridLayoutPlus layout = new GridLayoutPlus(0, 1);
		JPanel box = new JPanel(layout);
		box.add(createObjectsActionButton(actions.getObjectsAction(ActionRemoveAssignment.class), lockedTable));
		box.add(createObjectsActionButton(actions.getObjectsAction(ActionAddAssignment.class), objectPicker));
		return box;
	}
	
	private JScrollPane createScrollPaneWithFixedHeader()
	{
		lockedModel = new WorkPlanTableModelLockedHeaderRows(unitsModel);
		scrollModel = new WorkPlanTableModelScrollableHeaderRows(unitsModel);

		lockedTable = new BudgetTable(project, lockedModel);
		scrollTable = new BudgetTable(project, scrollModel);
		
		lockedTable.addListSelectionListener(new SelectionSyncKeeper(lockedTable, scrollTable, true));
		scrollTable.addListSelectionListener(new SelectionSyncKeeper(lockedTable, scrollTable, false));
		
		JScrollPane scrollPane = new JScrollPane(scrollTable);
	
		JViewport viewport = new JViewport();
		viewport.setView(lockedTable);
		viewport.setPreferredSize(lockedTable.getPreferredSize());
		scrollPane.setRowHeader(viewport);
		scrollPane.setCorner(JScrollPane.UPPER_LEFT_CORNER, lockedTable.getTableHeader());
		
		return scrollPane;
	}

	BudgetTable lockedTable;
	BudgetTable scrollTable;
	
	BudgetTableUnitsModel unitsModel;
	WorkPlanTableModelLockedHeaderRows lockedModel;
	WorkPlanTableModelScrollableHeaderRows scrollModel;
	
	Project project;
	ObjectPicker objectPicker;
}

//FIXME the below code is a duplicate of the code in the BudgetEditorCom....  Must refactor
class SelectionSyncKeeper implements ListSelectionListener
{
	public SelectionSyncKeeper(JTable lockedTableToUse, JTable scrollTableToUse, boolean isLockedTableToUse)
	{
		lockedTable = lockedTableToUse;
		scrollTable = scrollTableToUse;
		isLockedTable = isLockedTableToUse;
	}
	
	public void valueChanged(ListSelectionEvent event)
	{
		try
		{
			checkSelection(isLockedTable);
		}
		catch (Exception e)
		{
			//FIXME we dont care about exception, right?
			EAM.logException(e);
		}
	}

	private void checkSelection(boolean islockedTable) throws Exception 
	{
		int lockedSelectedIndex = lockedTable.getSelectedRow();
		int selectedIndex = scrollTable.getSelectedRow();

		if (lockedSelectedIndex != selectedIndex)
			setSeclection(islockedTable, lockedSelectedIndex, selectedIndex);
	}

	private void setSeclection(boolean islockedTable, int lockedSelectedIndex, int selectedIndex) throws Exception
	{
		if (islockedTable)
			scrollTable.setRowSelectionInterval(lockedSelectedIndex, lockedSelectedIndex);
		else 
			lockedTable.setRowSelectionInterval(selectedIndex, selectedIndex);
	}

	private JTable lockedTable;
	private JTable scrollTable;
	private boolean isLockedTable;
}


