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
package org.miradi.dialogs.treeRelevancy;

import java.awt.BorderLayout;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ScrollPaneConstants;
import javax.swing.table.AbstractTableModel;

import org.miradi.dialogs.base.ColumnMarginResizeListenerValidator;
import org.miradi.dialogs.treetables.TreeTablePanel;
import org.miradi.dialogs.treetables.TreeTableWithStateSaving;
import org.miradi.main.CommandExecutedEvent;
import org.miradi.main.MainWindow;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objects.BaseObject;
import org.miradi.objects.Objective;
import org.miradi.utils.MultiTableRowHeightController;
import org.miradi.utils.MultiTableVerticalScrollController;
import org.miradi.utils.MultipleTableSelectionController;
import org.miradi.utils.TableWithRowHeightSaver;
import org.miradi.views.umbrella.PersistentHorizontalSplitPane;
import org.miradi.views.umbrella.PersistentNonPercentageHorizontalSplitPane;

import com.jhlabs.awt.GridLayoutPlus;

public class StrategyActivityRelevancyTreeTablePanel extends TreeTablePanel implements MouseWheelListener
{
	public static StrategyActivityRelevancyTreeTablePanel createStrategyActivityRelevancyTreeTablePanel(MainWindow mainWindowToUse, Objective objective) throws Exception
	{
		//TODO THis should be all upstream strategies for the objective
		ORefList nonDraftStrategyRefs = mainWindowToUse.getProject().getStrategyPool().getNonDraftStrategyRefs();
	
		RootRelevancyTreeTableNode rootNode = new RootRelevancyTreeTableNode(mainWindowToUse.getProject(), nonDraftStrategyRefs);
		StrategyActivityRelevancyTreeTableModel treeTableModel = new StrategyActivityRelevancyTreeTableModel(rootNode); 
		StrategyActivityRelevancyTreeTable treeTable = new StrategyActivityRelevancyTreeTable(mainWindowToUse, treeTableModel);
		
		return new StrategyActivityRelevancyTreeTablePanel(mainWindowToUse, treeTableModel, treeTable);
	}
	
	private StrategyActivityRelevancyTreeTablePanel(MainWindow mainWindowToUse, StrategyActivityRelevancyTreeTableModel modelToUse, TreeTableWithStateSaving treeTable) throws Exception
	{
		super(mainWindowToUse, treeTable);
		
		model = modelToUse;
		
		treeTableScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		treeTableScrollPane.addMouseWheelListener(this);

		rowHeightController = new MultiTableRowHeightController(getMainWindow());
		rowHeightController.addTable(treeTable);
		
		selectionController = new MultipleTableSelectionController();
		selectionController.addTable(treeTable);
		
		scrollController = new MultiTableVerticalScrollController();
		scrollController.addScrollPane(treeTableScrollPane);
		
		listenForColumnWidthChanges(getTree());
		
		strategyActivityRelevancyTableModel = new StrategyActivityRelevancyTableModel(mainWindowToUse.getProject(), treeTable);
		strategyActivityRelevancyTable = new StrategyActivityRelevancyTable(mainWindowToUse, strategyActivityRelevancyTableModel);
		mainTableScrollPane = integrateTable(treeTable, strategyActivityRelevancyTable);
				
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
		
		treePlusTablesPanel = new PersistentNonPercentageHorizontalSplitPane(this, mainWindowToUse, "StrategyActivityRelevancyTreeTablePanel");
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
	
	private ScrollPaneWithHideableScrollBar integrateTable(TreeTableWithStateSaving treeToUse, TableWithRowHeightSaver table)
	{
		ModelUpdater modelUpdater = new ModelUpdater((AbstractTableModel)table.getModel());
		treeToUse.getTreeTableAdapter().addTableModelListener(modelUpdater);
		
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
	
	private void rebuildEntireTreeTable() throws Exception
	{
		ORef selectedRef = ORef.INVALID;
		BaseObject[] selected = tree.getSelectedObjects();
		if(selected.length == 1)
			selectedRef = selected[0].getRef();
		int selectedRow = tree.getSelectionModel().getAnchorSelectionIndex();

			
		tree.rebuildTableCompletely();
		strategyActivityRelevancyTableModel.fireTableStructureChanged();
		
		// NOTE: The following rebuild the tree but don't touch the columns
		getPlanningModel().rebuildEntireTree();
		restoreTreeExpansionState();
		updateRightSideTablePanels();

		tree.selectObjectAfterSwingClearsItDueToTreeStructureChange(selectedRef, selectedRow);
	}
	
	private void updateRightSideTablePanels() throws Exception
	{
		tablesPanel.removeAll();
		tablesPanel.add(mainTableScrollPane);
		
		validate();
		repaint();
	}
	
	private StrategyActivityRelevancyTreeTableModel getPlanningModel()
	{
		return (StrategyActivityRelevancyTreeTableModel)getModel();
	}

	public void mouseWheelMoved(MouseWheelEvent e)
	{
		if(e.getScrollType() != e.WHEEL_UNIT_SCROLL)
			return;
		
		masterScrollBar.setValue(masterScrollBar.getValue() + e.getUnitsToScroll());
	}
	
	@Override
	protected GridLayoutPlus createButtonLayout()
	{
		return null;
	}

	@Override
	public void commandExecuted(CommandExecutedEvent event)
	{
	}
	
	private StrategyActivityRelevancyTableModel strategyActivityRelevancyTableModel;
	private StrategyActivityRelevancyTable strategyActivityRelevancyTable;
	private JScrollBar masterScrollBar;
	private PersistentHorizontalSplitPane treePlusTablesPanel;
	private JPanel treesPanel;
	private JPanel tablesPanel;
	
	private ScrollPaneWithHideableScrollBar treesScrollPane;
	private ScrollPaneWithHideableScrollBar mainTableScrollPane;
	
	private MultipleTableSelectionController selectionController;
	private MultiTableRowHeightController rowHeightController;
	private MultiTableVerticalScrollController scrollController;
}