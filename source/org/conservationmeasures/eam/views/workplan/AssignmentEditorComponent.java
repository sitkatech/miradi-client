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

import org.conservationmeasures.eam.actions.ActionAssignResource;
import org.conservationmeasures.eam.actions.ActionRemoveAssignment;
import org.conservationmeasures.eam.actions.Actions;
import org.conservationmeasures.eam.dialogs.DisposablePanel;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.Task;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.utils.FastScrollPane;
import org.conservationmeasures.eam.utils.ObjectsActionButton;
import org.conservationmeasures.eam.views.budget.AssignmentTableModelSplittableShell;
import org.conservationmeasures.eam.views.budget.BudgetTable;
import org.conservationmeasures.eam.views.umbrella.ObjectPicker;

import com.jhlabs.awt.GridLayoutPlus;

abstract public class AssignmentEditorComponent extends DisposablePanel
{
	public AssignmentEditorComponent(Actions actions, Project projectToUse, ObjectPicker objectPickerToUse, AssignmentTableModelSplittableShell lockedModelToUse, AssignmentTableModelSplittableShell scrollModelToUse) throws Exception
	{
		super(new BorderLayout());
		project = projectToUse;
		objectPicker = objectPickerToUse;
		lockedModel = lockedModelToUse;
		scrollModel = scrollModelToUse;

		lockedTable = new BudgetTable(project, lockedModel);
		scrollTable = new BudgetTable(project, scrollModel);
				
		JScrollPane scrollPane = createScrollPaneWithFixedHeader();
		add(scrollPane, BorderLayout.CENTER);
		add(createButtonBar(actions), BorderLayout.LINE_END);
	}

	public void setTaskId(BaseId taskId)
	{ 
		lockedTable.stopCellEditing();
		scrollTable.stopCellEditing();

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
		
		lockedTable.repaint();
		scrollTable.repaint();
	}
	
	protected JPanel createButtonBar(Actions actions)
	{
		GridLayoutPlus layout = new GridLayoutPlus(0, 1);
		JPanel box = new JPanel(layout);
		
		ObjectsActionButton addButton = createObjectsActionButton(actions.getObjectsAction(ActionAssignResource.class), objectPicker);
		box.add(addButton);
		
		ObjectsActionButton removeButton = createObjectsActionButton(actions.getObjectsAction(ActionRemoveAssignment.class), lockedTable);
		box.add(removeButton);
		
		return box;
	}

	protected JScrollPane createScrollPaneWithFixedHeader()
	{
		new SelectionInSyncKeeper(lockedTable, scrollTable);
		JScrollPane scrollPane = new FastScrollPane(scrollTable);
	
		JViewport viewport = new JViewport();
		viewport.setView(lockedTable);
		viewport.setPreferredSize(lockedTable.getPreferredSize());
		scrollPane.setRowHeader(viewport);
		scrollPane.setCorner(JScrollPane.UPPER_LEFT_CORNER, lockedTable.getTableHeader());
		
		return scrollPane;
	}
	
	class SelectionInSyncKeeper implements ListSelectionListener 
	{
	      public SelectionInSyncKeeper(JTable table1ToUse, JTable table2ToUse) 
	      {
	    	  table1 = table1ToUse;
	    	  table2 = table2ToUse;
	    	  
	    	  table1.getSelectionModel().addListSelectionListener(this);
	    	  table2.getSelectionModel().addListSelectionListener(this);
	      }
	      
	      public void valueChanged(ListSelectionEvent e) 
	      {
	    	  int table1SelectedRow = table1.getSelectedRow();
	    	  int table2SelectedRow = table2.getSelectedRow();
	    	  
	    	  if (table1SelectedRow == table2SelectedRow)
	    		  return;
	    	  
	    	  if (e.getSource().equals(table1.getSelectionModel()))
	    	    setSelectedRow(table2, table1SelectedRow);
	    	  
	    	  if (e.getSource().equals(table2.getSelectionModel()))
	    	    setSelectedRow(table1, table2SelectedRow);
	      }
	      
	      private void setSelectedRow(JTable tableToUse, int rowToSelect)
	      {
	    	  if (rowToSelect < 0)
	    		  return;
	    	  
	    	  tableToUse.setRowSelectionInterval(rowToSelect, rowToSelect);
	      }
	      
	      private JTable table1;
	      private JTable table2;
	   }


	Project project;
	
	protected BudgetTable lockedTable;
	protected BudgetTable scrollTable;
	protected AssignmentTableModelSplittableShell lockedModel;
	protected AssignmentTableModelSplittableShell scrollModel;
		
	protected ObjectPicker objectPicker;
	
}