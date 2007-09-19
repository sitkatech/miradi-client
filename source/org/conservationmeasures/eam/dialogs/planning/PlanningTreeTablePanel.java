/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.planning;

import java.awt.BorderLayout;
import java.util.Arrays;
import java.util.Vector;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.SwingUtilities;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;

import org.conservationmeasures.eam.actions.ActionDeletePlanningViewTreeNode;
import org.conservationmeasures.eam.actions.ActionTreeNodeDown;
import org.conservationmeasures.eam.actions.ActionTreeNodeUp;
import org.conservationmeasures.eam.commands.CommandCreateObject;
import org.conservationmeasures.eam.commands.CommandDeleteObject;
import org.conservationmeasures.eam.commands.CommandSetObjectData;
import org.conservationmeasures.eam.dialogs.planning.propertiesPanel.PlanningViewBudgetAnnualTotalTableModel;
import org.conservationmeasures.eam.dialogs.planning.propertiesPanel.PlanningViewBudgetAnnualTotalsTable;
import org.conservationmeasures.eam.main.CommandExecutedEvent;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objects.BaseObject;
import org.conservationmeasures.eam.objects.Indicator;
import org.conservationmeasures.eam.objects.Strategy;
import org.conservationmeasures.eam.objects.Task;
import org.conservationmeasures.eam.utils.CodeList;
import org.conservationmeasures.eam.utils.MultiTableVerticalScrollController;
import org.conservationmeasures.eam.utils.MultipleTableSelectionController;
import org.conservationmeasures.eam.views.TreeTableNode;
import org.conservationmeasures.eam.views.TreeTableWithStateSaving;
import org.conservationmeasures.eam.views.planning.ColumnManager;
import org.conservationmeasures.eam.views.planning.PlanningView;
import org.conservationmeasures.eam.views.treeViews.TreeTablePanel;
import org.martus.swing.UiScrollPane;

import com.java.sun.jtreetable.TreeTableModelAdapter;

public class PlanningTreeTablePanel extends TreeTablePanel
{
	public static PlanningTreeTablePanel createPlanningTreeTablePanel(MainWindow mainWindowToUse) throws Exception
	{ 
		PlanningTreeModel model = new PlanningTreeModel(mainWindowToUse.getProject());
		PlanningTreeTable treeTable = new PlanningTreeTable(mainWindowToUse.getProject(), model);	
		
		return new PlanningTreeTablePanel(mainWindowToUse, treeTable, model);
	}
	
	private PlanningTreeTablePanel(MainWindow mainWindowToUse, PlanningTreeTable treeToUse, PlanningTreeModel modelToUse) throws Exception
	{
		super(mainWindowToUse, treeToUse, getButtonActions());
		model = modelToUse;
		splitter = new JSplitPane();
		add(splitter, BorderLayout.CENTER);
		splitter.setLeftComponent(getTreeTableScrollPane());
		
		selectionController = new MultipleTableSelectionController();
		verticalController = new MultiTableVerticalScrollController();
		
		getTreeTableScrollPane().setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		getTreeTableScrollPane().setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		
		createBudgetTable(treeToUse);
		rebuildSyncedAnnualsTotalsTable(treeToUse);
		updateSplitterRightSideContents();
	}

	private void createBudgetTable(PlanningTreeTable treeTableToUse) throws Exception
	{
		annualTotalsModel = new PlanningViewBudgetAnnualTotalTableModel(getProject(), (TreeTableModelAdapter)treeTableToUse.getModel());
		annualTotalsTable = new PlanningViewBudgetAnnualTotalsTable(annualTotalsModel);
		new ModelUpdater((TreeTableModelAdapter)treeTableToUse.getModel(), annualTotalsModel);
	}
	
	private void rebuildSyncedAnnualsTotalsTable(PlanningTreeTable treeTableToUse)
	{
		annualTotalsTable.setRowHeight(treeTableToUse.getRowHeight());
		annualTotalsScrollPane = new UiScrollPane(annualTotalsTable);
		annualTotalsScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
	
		verticalController.addTable(annualTotalsScrollPane);
		verticalController.addTable(getTreeTableScrollPane());
		
		selectionController.addTable(annualTotalsTable);
		selectionController.addTable(treeTableToUse);
	}

	private static Class[] getButtonActions()
	{
		return new Class[] {
			ActionDeletePlanningViewTreeNode.class,
			ActionTreeNodeUp.class,
			ActionTreeNodeDown.class,
		};
	}
	
	public void commandExecuted(CommandExecutedEvent event)
	{
		try
		{		
			if (doesCommandForceRebuild(event))
				rebuildEntireTreeTable();	
		}
		catch(Exception e)
		{
			EAM.logException(e);
			EAM.errorDialog("Error occurred: " + e.getMessage());
		}
		
	}
	
	private boolean doesCommandForceRebuild(CommandExecutedEvent event)
	{
		if(PlanningView.isRowOrColumnChangingCommand(event))
			return true;
		
		if(isTaskMove(event))
			return true;
		
		if(isSelectedObjectModification(event))
			return true;
		
		if(isCreate(event) || isDeleteCommand(event))
			return true;
		
		return false;
	}
	
	private boolean isDeleteCommand(CommandExecutedEvent event)
	{
		if (! event.isDeleteObjectCommand())
			return false;
		
		CommandDeleteObject deleteCommand = (CommandDeleteObject) event.getCommand();
		if (deleteCommand.getObjectType() != Task.getObjectType())
			return false;
		
		return true;
	}

	private boolean isCreate(CommandExecutedEvent event)
	{
		if (! event.isCreateObjectCommand())
			return false;

		CommandCreateObject createCommand = (CommandCreateObject) event.getCommand();
		if (createCommand.getObjectType() != Task.getObjectType())
			return false;
		
		return true;
	}

	//TODO this should use that getTasksTag (or something like that) method
	//from email :Please put a todo in isTaskMove that it should use that 
	//getTasksTag method (or whatever it's called) that I mentioned the 
	//other day. I know that one is my code not yours.
	private boolean isTaskMove(CommandExecutedEvent event)
	{
		if (! event.isSetDataCommand())
			return false;
		
		CommandSetObjectData setCommand = (CommandSetObjectData) event.getCommand();
		int type = setCommand.getObjectType();
		String tag = setCommand.getFieldTag();
		if(type == Task.getObjectType() && tag.equals(Task.TAG_SUBTASK_IDS))
			return true;
		if(type == Strategy.getObjectType() && tag.equals(Strategy.TAG_ACTIVITY_IDS))
			return true;
		if(type == Indicator.getObjectType() && tag.equals(Indicator.TAG_TASK_IDS))
			return true;
		return false;
	}
	
	private boolean isSelectedObjectModification(CommandExecutedEvent event)
	{
		if (! event.isSetDataCommand())
			return false;
		
		TreeTableNode node = getSelectedTreeNode();
		if (node == null)
			return false;
		
		BaseObject selectedObject = node.getObject(); 
		if (selectedObject == null)
			return false;
		
		String[] fieldTags = selectedObject.getFieldTags();
		Vector fields = new Vector(Arrays.asList(fieldTags));
		CommandSetObjectData setCommand = (CommandSetObjectData) event.getCommand();
		int setType = setCommand.getObjectType();
		String setField = setCommand.getFieldTag();
		
		boolean sameType = (selectedObject.getType() == setType);
		boolean containsField = (fields.contains(setField));
		return (sameType && containsField);
	}

	private void rebuildEntireTreeTable() throws Exception
	{
		ORef selectedRef = ORef.INVALID;
		BaseObject[] selected = tree.getSelectedObjects();
		if(selected.length == 1)
			selectedRef = selected[0].getRef();

		// TODO: Perhaps possibly detect exactly what changed and 
		// only rebuild the columns or the rows rather than always doing both
	
		// NOTE: The following rebuild the columns but don't touch the tree
		getPlanningModel().rebuildCodeList();
		tree.rebuildTableCompletely();
		
		// NOTE: The following rebuild the tree but don't touch the columns
		getPlanningModel().rebuildEntireTree();
		annualTotalsModel.fireTableDataChanged();
		restoreTreeExpansionState();
		updateSplitterRightSideContents();

		selectObjectAfterSwingClearsItDueToTreeStructureChange(selectedRef);
	}
	
	private void updateSplitterRightSideContents() throws Exception
	{
		CodeList columnsToShow = new CodeList(ColumnManager.getVisibleColumnCodes(getProject().getCurrentViewData()));
		if (columnsToShow.contains(Task.PSEUDO_TAG_TASK_TOTAL))
		{
			getTreeTableScrollPane().hideVerticalScrollBar();
			splitter.setRightComponent(annualTotalsScrollPane);
			validate();

			int proposedWidth = getTree().getPreferredSize().width;
			int reservedWidth = annualTotalsScrollPane.getPreferredSize().width;
			int currentWidth = getWidth();
			if(currentWidth > 0)
			{
				int maxWidth = currentWidth - reservedWidth;
				proposedWidth = Math.min(proposedWidth, maxWidth);
			}
			splitter.setDividerLocation(proposedWidth);
		}
		else
		{
			getTreeTableScrollPane().showVerticalScrollBar();
			splitter.setRightComponent(new JPanel());
			splitter.setDividerLocation(1.0);
			validate();
		}
		
	}

	private void selectObjectAfterSwingClearsItDueToTreeStructureChange(ORef selectedRef)
	{
		SwingUtilities.invokeLater(new Reselecter(tree, selectedRef));
	}
	
	class Reselecter implements Runnable
	{
		public Reselecter(TreeTableWithStateSaving treeTableToUse, ORef refToSelect)
		{
			treeTable = treeTableToUse;
			ref = refToSelect;
		}
		
		public void run()
		{
			treeTable.selectObject(ref);
		}
		
		TreeTableWithStateSaving treeTable;
		ORef ref;
	}

	private PlanningTreeModel getPlanningModel()
	{
		return (PlanningTreeModel)getModel();
	}

	private JSplitPane splitter;
	
	private MultiTableVerticalScrollController verticalController;
	private MultipleTableSelectionController selectionController;
	private PlanningViewBudgetAnnualTotalsTable annualTotalsTable;
	private PlanningViewBudgetAnnualTotalTableModel annualTotalsModel;
	private UiScrollPane annualTotalsScrollPane;
}

class ModelUpdater implements TableModelListener
{
	public ModelUpdater(TreeTableModelAdapter adapter, AbstractTableModel modelToUpdateToUse)
	{
		modelToUpdate = modelToUpdateToUse;
		adapter.addTableModelListener(this);
	}
	
	public void tableChanged(TableModelEvent e)
	{
		modelToUpdate.fireTableDataChanged();
	}
	
	AbstractTableModel modelToUpdate;
}