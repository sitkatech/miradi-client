/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.planning;

import java.awt.BorderLayout;

import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;

import org.conservationmeasures.eam.actions.ActionDeletePlanningViewTreeNode;
import org.conservationmeasures.eam.actions.ActionTreeNodeDown;
import org.conservationmeasures.eam.actions.ActionTreeNodeUp;
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
import org.conservationmeasures.eam.utils.MultiTableHorizontalScrollController;
import org.conservationmeasures.eam.utils.MultiTableVerticalScrollController;
import org.conservationmeasures.eam.utils.MultipleTableSelectionController;
import org.conservationmeasures.eam.views.TreeTableWithStateSaving;
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
		createBudgetTable(treeToUse);
		addSyncedAnnualsTotalsTable(treeToUse);
	}

	private void createBudgetTable(PlanningTreeTable treeTableToUse) throws Exception
	{
		annualTotalsModel = new PlanningViewBudgetAnnualTotalTableModel(getProject(), (TreeTableModelAdapter)treeTableToUse.getModel());
		annualTotalsTable = new PlanningViewBudgetAnnualTotalsTable(annualTotalsModel);
		new ModelUpdater((TreeTableModelAdapter)treeTableToUse.getModel(), annualTotalsModel);
	}
	
	private void addSyncedAnnualsTotalsTable(PlanningTreeTable treeTableToUse)
	{
		selectionController = new MultipleTableSelectionController();
		verticalController = new MultiTableVerticalScrollController();
		horizontalController = new MultiTableHorizontalScrollController();

		syncRowHeightSizes(treeTableToUse);

		UiScrollPane annualTotalsScrollPane = new UiScrollPane(annualTotalsTable);
		annualTotalsScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		add(annualTotalsScrollPane, BorderLayout.AFTER_LINE_ENDS);
	
		getTreeTableScrollPane().setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
		getTreeTableScrollPane().setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		verticalController.addTable(annualTotalsScrollPane);
		verticalController.addTable(getTreeTableScrollPane());
		
		horizontalController.addTable(annualTotalsScrollPane);
		horizontalController.addTable(getTreeTableScrollPane());
		
		selectionController.addTable(annualTotalsTable);
		selectionController.addTable(treeTableToUse);
	}

	private void syncRowHeightSizes(PlanningTreeTable treeTableToUse)
	{
		annualTotalsTable.setRowHeight(treeTableToUse.getRowHeight());
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
		if(!event.isSetDataCommand())
			return;
		
		CommandSetObjectData cmd = (CommandSetObjectData)event.getCommand();
		try
		{
			if(PlanningView.isRowOrColumnChangingCommand(cmd))
				rebuildEntireTreeTable();
			
			if(isTaskMove(cmd))
				rebuildEntireTreeTable();
		}
		catch(Exception e)
		{
			EAM.logException(e);
			EAM.errorDialog("Error occurred: " + e.getMessage());
		}
	}
	
	private boolean isTaskMove(CommandSetObjectData cmd)
	{
		int type = cmd.getObjectType();
		String tag = cmd.getFieldTag();
		if(type == Task.getObjectType() && tag.equals(Task.TAG_SUBTASK_IDS))
			return true;
		if(type == Strategy.getObjectType() && tag.equals(Strategy.TAG_ACTIVITY_IDS))
			return true;
		if(type == Indicator.getObjectType() && tag.equals(Indicator.TAG_TASK_IDS))
			return true;
		return false;
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
		restoreTreeExpansionState();

		selectObjectAfterSwingClearsItDueToTreeStructureChange(selectedRef);
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
	
	private MultiTableVerticalScrollController verticalController;
	private MultiTableHorizontalScrollController horizontalController;
	private MultipleTableSelectionController selectionController;
	private PlanningViewBudgetAnnualTotalTableModel annualTotalsModel;
	private PlanningViewBudgetAnnualTotalsTable annualTotalsTable;
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