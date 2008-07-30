/* 
Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
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
package org.miradi.dialogs.planning.upperPanel;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import javax.swing.BoundedRangeModel;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;

import org.miradi.actions.ActionDeletePlanningViewTreeNode;
import org.miradi.actions.ActionTreeCreateActivity;
import org.miradi.actions.ActionTreeCreateMethod;
import org.miradi.actions.ActionTreeCreateTask;
import org.miradi.actions.ActionTreeNodeDown;
import org.miradi.actions.ActionTreeNodeUp;
import org.miradi.actions.ActionTreeShareActivity;
import org.miradi.actions.ActionTreeShareMethod;
import org.miradi.commands.CommandSetObjectData;
import org.miradi.dialogs.base.ColumnMarginResizeListenerValidator;
import org.miradi.dialogs.tablerenderers.PlanningViewFontProvider;
import org.miradi.dialogs.treetables.TreeTablePanelWithFourButtonColumns;
import org.miradi.dialogs.treetables.TreeTablePanel.ScrollPaneWithHideableScrollBar;
import org.miradi.main.CommandExecutedEvent;
import org.miradi.main.EAM;
import org.miradi.main.MainWindow;
import org.miradi.objecthelpers.ORef;
import org.miradi.objects.Assignment;
import org.miradi.objects.BaseObject;
import org.miradi.objects.Factor;
import org.miradi.objects.Indicator;
import org.miradi.objects.KeyEcologicalAttribute;
import org.miradi.objects.Measurement;
import org.miradi.objects.Strategy;
import org.miradi.objects.Task;
import org.miradi.utils.CodeList;
import org.miradi.utils.ExportableTableInterface;
import org.miradi.utils.FastScrollBar;
import org.miradi.utils.MiradiScrollPane;
import org.miradi.utils.MultiTableCombinedAsOneExporter;
import org.miradi.utils.MultiTableRowHeightController;
import org.miradi.utils.MultiTableVerticalScrollController;
import org.miradi.utils.MultipleTableSelectionController;
import org.miradi.utils.TableWithRowHeightSaver;
import org.miradi.views.planning.ColumnManager;
import org.miradi.views.planning.PlanningView;
import org.miradi.views.umbrella.PersistentHorizontalSplitPane;
import org.miradi.views.umbrella.PersistentNonPercentageHorizontalSplitPane;

public class PlanningTreeTablePanel extends TreeTablePanelWithFourButtonColumns implements MouseWheelListener
{
	public static PlanningTreeTablePanel createPlanningTreeTablePanelWithoutButtons(MainWindow mainWindowToUse) throws Exception
	{
		Class[] noButtons = new Class[0];
		return createPlanningTreeTablePanel(mainWindowToUse, noButtons);
	}
	
	public static PlanningTreeTablePanel createPlanningTreeTablePanel(MainWindow mainWindowToUse) throws Exception
	{
		return createPlanningTreeTablePanel(mainWindowToUse, getButtonActions());
	}
	
	public static PlanningTreeTablePanel createPlanningTreeTablePanel(MainWindow mainWindowToUse, Class[] buttonActions) throws Exception
	{
		PlanningTreeTableModel model = new PlanningTreeTableModel(mainWindowToUse.getProject());
		PlanningTreeTable treeTable = new PlanningTreeTable(mainWindowToUse, model);	
		
		return new PlanningTreeTablePanel(mainWindowToUse, treeTable, model, buttonActions);
	}
	
	private PlanningTreeTablePanel(MainWindow mainWindowToUse, PlanningTreeTable treeToUse, PlanningTreeTableModel modelToUse, Class[] buttonActions) throws Exception
	{
		super(mainWindowToUse, treeToUse, buttonActions);
		model = modelToUse;
		
		// NOTE: Replace tree scroll pane created by super constructor
		ScrollPaneWithHideableScrollBar newTreeScrollPane = new ScrollPaneWithHideableScrollBar(getTree());
		newTreeScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		newTreeScrollPane.hideVerticalScrollBar();
		newTreeScrollPane.addMouseWheelListener(this);
		treeTableScrollPane = newTreeScrollPane;

		rowHeightController = new MultiTableRowHeightController();
		rowHeightController.addTable(treeToUse);
		
		selectionController = new MultipleTableSelectionController();
		selectionController.addTable(treeToUse);
		
		scrollController = new MultiTableVerticalScrollController();
		scrollController.addScrollPane(treeTableScrollPane);
		
		multiTableExporter = new MultiTableCombinedAsOneExporter();		
		fontProvider = new PlanningViewFontProvider();
		
		listenForColumnWidthChanges(getTree());
		
		mainModel = new PlanningViewMainTableModel(getProject(), treeToUse);
		mainTable = new PlanningViewMainTable(mainWindowToUse, mainModel, fontProvider);
		mainTableScrollPane = integrateTable(treeToUse, mainTable);

		annualTotalsModel = new PlanningViewBudgetAnnualTotalTableModel(getProject(), treeToUse);
		annualTotalsTable = new PlanningViewBudgetAnnualTotalsTable(mainWindowToUse, annualTotalsModel, fontProvider);
		annualTotalsScrollPane = integrateTable(treeToUse, annualTotalsTable);
		
		measurementModel = new PlanningViewMeasurementTableModel(getProject(), treeToUse);
		measurementTable = new PlanningViewMeasurementTable(mainWindowToUse, measurementModel, fontProvider);
		measurementScrollPane = integrateTable(treeToUse, measurementTable);
		
		futureStatusModel = new PlanningViewFutureStatusTableModel(getProject(), treeToUse);
		futureStatusTable = new PlanningViewFutureStatusTable(mainWindowToUse, futureStatusModel, fontProvider);
		futureStatusScrollPane = integrateTable(treeToUse, futureStatusTable);
		
		
		treesPanel = new ShrinkToFitVerticallyHorizontalBox();
		treesPanel.add(treeTableScrollPane);
		treesScrollPane = new ScrollPaneWithHideableScrollBar(treesPanel);
		treesScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		treesScrollPane.hideVerticalScrollBar();
		
		tablesPanel = new ShrinkToFitVerticallyHorizontalBox();
		ScrollPaneWithHideableScrollBar tablesScrollPane = new ScrollPaneWithHideableScrollBar(tablesPanel);
		tablesScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		tablesScrollPane.hideVerticalScrollBar();

		masterScrollBar = new MasterVerticalScrollBar(treeTableScrollPane);
		scrollController.addScrollBar(masterScrollBar);
		
		treePlusTablesPanel = new PersistentNonPercentageHorizontalSplitPane(this, mainWindowToUse, "PlanningViewTreesPlusTables");
		treePlusTablesPanel.setDividerSize(5);
		// FIXME: Remove this when persistence actually works!
		treePlusTablesPanel.setDividerLocationWithoutNotifications(200);
		treePlusTablesPanel.setTopComponent(treesScrollPane);
		treePlusTablesPanel.setBottomComponent(tablesScrollPane);
		treePlusTablesPanel.setOneTouchExpandable(false);

		// NOTE: Replace treeScrollPane that super constructor put in CENTER
		add(treePlusTablesPanel, BorderLayout.CENTER);
		add(masterScrollBar, BorderLayout.AFTER_LINE_ENDS);
		
		rebuildEntireTreeTable();
	}
	
	private ScrollPaneWithHideableScrollBar integrateTable(PlanningTreeTable treeToUse, TableWithRowHeightSaver table)
	{
		ModelUpdater modelUpdater = new ModelUpdater((AbstractTableModel)table.getModel());
		treeToUse.getTreeTableAdapter().addTableModelListener(modelUpdater);
		table.restoreSavedRowHeight();
		
		selectionController.addTable(table);
		rowHeightController.addTable(table);
		listenForColumnWidthChanges(table);

		ScrollPaneWithHideableScrollBar scrollPane = new ScrollPaneNoExtraWidth(table);
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.hideVerticalScrollBar();
		scrollPane.addMouseWheelListener(this);

		scrollController.addScrollPane(scrollPane);
		
		return scrollPane;
	}

	private void listenForColumnWidthChanges(JTable table)
	{
		table.getColumnModel().addColumnModelListener(new ColumnMarginResizeListenerValidator(this));
	}
	
	private static Class[] getButtonActions()
	{
		return new Class[] {
			ActionTreeCreateActivity.class,
			ActionTreeCreateMethod.class,
			ActionTreeCreateTask.class,			
			ActionTreeNodeUp.class,
			
			ActionTreeShareActivity.class,
			ActionTreeShareMethod.class,
			ActionDeletePlanningViewTreeNode.class,
			ActionTreeNodeDown.class,
		};
	}
	
	public void commandExecuted(CommandExecutedEvent event)
	{
		try
		{		
			if (doesCommandForceRebuild(event))
				rebuildEntireTreeTable();
			
			if(isSelectedObjectModification(event, Assignment.getObjectType()))
				validate();
		
			repaintToGrowIfTreeIsTaller();
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
		
		if(didAffectTaskInTree(event))
			return true;
		
		if (didAffectIndicatorInTree(event))
			return true;
		
		return false;
	}
	
	private boolean didAffectIndicatorInTree(CommandExecutedEvent event)
	{
		if (! event.isSetDataCommand())
			return false;
		
		CommandSetObjectData setCommand = (CommandSetObjectData) event.getCommand();
		int type = setCommand.getObjectType();
		String tag = setCommand.getFieldTag();
		if(Factor.isFactor(type) && tag.equals(Factor.TAG_INDICATOR_IDS))
			return true;
		
		if(type == KeyEcologicalAttribute.getObjectType() && tag.equals(KeyEcologicalAttribute.TAG_INDICATOR_IDS))
			return true;
				
		return false;
	}

	//TODO this should use that getTasksTag (or something like that) method
	//from email :Please put a todo in isTaskMove that it should use that 
	//getTasksTag method (or whatever it's called) that I mentioned the 
	//other day. I know that one is my code not yours.
	private boolean didAffectTaskInTree(CommandExecutedEvent event)
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
	
	private void rebuildEntireTreeTable() throws Exception
	{
		ORef selectedRef = ORef.INVALID;
		BaseObject[] selected = tree.getSelectedObjects();
		if(selected.length == 1)
			selectedRef = selected[0].getRef();
		int selectedRow = tree.getSelectionModel().getAnchorSelectionIndex();

		// TODO: Perhaps possibly detect exactly what changed and 
		// only rebuild the columns or the rows rather than always doing both
	
		// NOTE: The following rebuild the columns but don't touch the tree
		getPlanningModel().updateColumnsToShow();
		tree.rebuildTableCompletely();

		mainModel.updateColumnsToShow();
		
		// NOTE: The following rebuild the tree but don't touch the columns
		getPlanningModel().rebuildEntireTree();
		annualTotalsModel.fireTableDataChanged();
		measurementModel.fireTableDataChanged();
		futureStatusModel.fireTableDataChanged();
		restoreTreeExpansionState();
		updateRightSideTablePanels();

		tree.selectObjectAfterSwingClearsItDueToTreeStructureChange(selectedRef, selectedRow);
	}
	
	private void updateRightSideTablePanels() throws Exception
	{
		tablesPanel.removeAll();
		multiTableExporter.clear();

		multiTableExporter.addExportable(getTree());
		addTable(mainTableScrollPane, mainTable);

		CodeList columnsToShow = new CodeList(ColumnManager.getVisibleColumnCodes(getProject().getCurrentViewData()));
		if (columnsToShow.contains(Task.PSEUDO_TAG_TASK_BUDGET_DETAIL))
			addTable(annualTotalsScrollPane, annualTotalsTable);

		if (columnsToShow.contains(Measurement.META_COLUMN_TAG))
			addTable(measurementScrollPane, measurementTable);

		if (columnsToShow.contains(Indicator.META_COLUMN_TAG))
			addTable(futureStatusScrollPane, futureStatusTable);

		
		validate();
		repaint();
	}
	
	private void addTable(MiradiScrollPane scrollPane, ExportableTableInterface table)
	{
		tablesPanel.add(scrollPane);
		multiTableExporter.addExportable(table);
	}

	private PlanningTreeTableModel getPlanningModel()
	{
		return (PlanningTreeTableModel)getModel();
	}
	
	public ExportableTableInterface getTableForExporting()
	{
		return multiTableExporter;
	}
	
	public static JComponent createPrintablePlanningTreeTablePanel(MainWindow mainWindow) throws Exception
	{
		PlanningTreeTablePanel wholePanel = createPlanningTreeTablePanelWithoutButtons(mainWindow);

		JPanel reformatted = new JPanel(new BorderLayout());
		reformatted.add(wholePanel.tree, BorderLayout.BEFORE_LINE_BEGINS);
		reformatted.add(wholePanel.tablesPanel, BorderLayout.CENTER);
		
		wholePanel.dispose();
		return reformatted;
	}

	public void mouseWheelMoved(MouseWheelEvent e)
	{
		if(e.getScrollType() != e.WHEEL_UNIT_SCROLL)
			return;
		
		masterScrollBar.setValue(masterScrollBar.getValue() + e.getUnitsToScroll());
	}

	private PlanningViewFontProvider fontProvider;
	private PersistentHorizontalSplitPane treePlusTablesPanel;
	private JPanel treesPanel;
	private JPanel tablesPanel;
	
	private PlanningViewMainTableModel mainModel;
	private PlanningViewMainTable mainTable;
	private PlanningViewBudgetAnnualTotalsTable annualTotalsTable;
	private PlanningViewBudgetAnnualTotalTableModel annualTotalsModel;
	private PlanningViewMeasurementTable measurementTable;
	private PlanningViewMeasurementTableModel measurementModel;
	private PlanningViewFutureStatusTable futureStatusTable;
	private PlanningViewFutureStatusTableModel futureStatusModel;

	private JScrollBar masterScrollBar;
	private ScrollPaneWithHideableScrollBar treesScrollPane;
	private ScrollPaneWithHideableScrollBar mainTableScrollPane;
	private ScrollPaneWithHideableScrollBar annualTotalsScrollPane;
	private ScrollPaneWithHideableScrollBar measurementScrollPane;
	private ScrollPaneWithHideableScrollBar futureStatusScrollPane;
	
	private MultipleTableSelectionController selectionController;
	private MultiTableRowHeightController rowHeightController;
	private MultiTableCombinedAsOneExporter multiTableExporter;
	private MultiTableVerticalScrollController scrollController;
}

class ModelUpdater implements TableModelListener
{
	public ModelUpdater(AbstractTableModel modelToUpdateToUse)
	{
		modelToUpdate = modelToUpdateToUse;
	}
	
	public void tableChanged(TableModelEvent e)
	{
		modelToUpdate.fireTableDataChanged();
	}
	
	private AbstractTableModel modelToUpdate;
}

class MasterVerticalScrollBar extends FastScrollBar implements ChangeListener
{
	MasterVerticalScrollBar(JScrollPane baseRangeOn)
	{
		super(VERTICAL);
		baseRangeOn.getVerticalScrollBar().getModel().addChangeListener(this);
		otherScrollBar = baseRangeOn.getVerticalScrollBar();
	}

	public void stateChanged(ChangeEvent e)
	{
		updateRange();
	}

	private void updateRange()
	{
		BoundedRangeModel ourModel = getModel();
		BoundedRangeModel otherModel = otherScrollBar.getModel();
		ourModel.setMinimum(otherModel.getMinimum());
		ourModel.setMaximum(otherModel.getMaximum());
		ourModel.setExtent(otherModel.getExtent());
	}

	private JScrollBar otherScrollBar;
}

class ShrinkToFitVerticallyHorizontalBox extends JPanel
{
	ShrinkToFitVerticallyHorizontalBox()
	{
		BoxLayout layout = new BoxLayout(this, BoxLayout.LINE_AXIS);
		setLayout(layout);
	}
	
	@Override
	public void setPreferredSize(Dimension preferredSize)
	{
		overriddenPreferredSize = preferredSize;
	}
	
	@Override
	public Dimension getPreferredSize()
	{
		if(overriddenPreferredSize != null)
			return overriddenPreferredSize;
		
		Dimension size = new Dimension(super.getPreferredSize());
		Container parent = getParent();
		if(parent == null)
			return size;
		
		setPreferredSize(new Dimension(0,0));
		Dimension max = parent.getPreferredSize();
		setPreferredSize(null);
		size.height = Math.min(size.height, max.height);
		return size;
	}
	
	private Dimension overriddenPreferredSize;
}

class ScrollPaneNoExtraWidth extends ScrollPaneWithHideableScrollBar
{
	public ScrollPaneNoExtraWidth(Component component)
	{
		super(component);
	}

	@Override
	public Dimension getMaximumSize()
	{
		Dimension max = super.getMaximumSize();
		max.width = getPreferredSize().width;
		return max;
	}
	
}