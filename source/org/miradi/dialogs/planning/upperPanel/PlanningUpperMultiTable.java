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

package org.miradi.dialogs.planning.upperPanel;

import java.awt.Color;
import java.util.Vector;

import javax.swing.Action;
import javax.swing.ListSelectionModel;
import javax.swing.table.TableCellRenderer;

import org.miradi.actions.ActionCollapseAllRows;
import org.miradi.actions.ActionDeletePlanningViewTreeNode;
import org.miradi.actions.ActionExpandAllRows;
import org.miradi.actions.Actions;
import org.miradi.dialogs.planning.MultiTableCollapseColumnAction;
import org.miradi.dialogs.planning.RightClickActionProvider;
import org.miradi.dialogs.planning.TableWithExpandableColumnsInterface;
import org.miradi.dialogs.planning.propertiesPanel.MultiTableExpandColumnAction;
import org.miradi.dialogs.planning.propertiesPanel.PlanningRightClickHandler;
import org.miradi.dialogs.tablerenderers.BasicTableCellRendererFactory;
import org.miradi.dialogs.tablerenderers.BudgetCostTreeTableCellRendererFactory;
import org.miradi.dialogs.tablerenderers.ChoiceItemTableCellRendererFactory;
import org.miradi.dialogs.tablerenderers.FontForObjectTypeProvider;
import org.miradi.dialogs.tablerenderers.MultiLineObjectTableCellRendererFactory;
import org.miradi.dialogs.tablerenderers.NumericTableCellRendererFactory;
import org.miradi.dialogs.tablerenderers.ProgressTableCellRendererFactory;
import org.miradi.dialogs.tablerenderers.RowColumnBaseObjectProvider;
import org.miradi.objects.BaseObject;
import org.miradi.project.CurrencyFormat;
import org.miradi.questions.EmptyChoiceItem;
import org.miradi.utils.TableWithColumnWidthAndSequenceSaver;

public class PlanningUpperMultiTable extends TableWithColumnWidthAndSequenceSaver implements RowColumnBaseObjectProvider, RightClickActionProvider, TableWithExpandableColumnsInterface
{
	public PlanningUpperMultiTable(PlanningTreeTable masterTreeToUse, PlanningTreeMultiTableModel model, FontForObjectTypeProvider fontProvider)
	{
		super(masterTreeToUse.getMainWindow(), model, masterTreeToUse.getUniqueTableIdentifier());
		setAutoResizeMode(AUTO_RESIZE_OFF);
		setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		setCellSelectionEnabled(true);

		masterTree = masterTreeToUse;
		defaultRendererFactory = new MultiLineObjectTableCellRendererFactory(this, fontProvider);

		CurrencyFormat currencyFormatter = masterTree.getProject().getCurrencyFormatterWithCommas();
		currencyRendererFactory = new BudgetCostTreeTableCellRendererFactory(this, fontProvider, currencyFormatter);

		choiceRendererFactory = new ChoiceItemTableCellRendererFactory(this, fontProvider);
		
		progressRendererFactory = new ProgressTableCellRendererFactory(this, fontProvider);
		
		doubleRendererFactory = new NumericTableCellRendererFactory(this, fontProvider);
		
		addMouseListener(new PlanningRightClickHandler(getMainWindow(), this, this));
	}
	
	@Override
	public TableCellRenderer getCellRenderer(int row, int tableColumn)
	{
		final int modelColumn = convertColumnIndexToModel(tableColumn);

		BasicTableCellRendererFactory factory = defaultRendererFactory;
		if(getCastedModel().isCurrencyColumn(modelColumn))
			factory = currencyRendererFactory;
		else if(getCastedModel().isChoiceColumn(modelColumn))
			factory = choiceRendererFactory;
		else if(getCastedModel().isProgressColumn(modelColumn))
			factory = progressRendererFactory;
		else if(getCastedModel().isWorkUnitColumn(modelColumn))
			factory = doubleRendererFactory;
		
		Color background = getCastedModel().getCellBackgroundColor(row, modelColumn);
		factory.setCellBackgroundColor(background);
		return factory;
	}

	public PlanningTreeMultiTableModel getCastedModel()
	{
		return (PlanningTreeMultiTableModel)getModel();
	}

	public BaseObject getBaseObjectForRowColumn(int row, int column)
	{
		return masterTree.getBaseObjectForRowColumn(row, column);
	}

	public int getProportionShares(int row)
	{
		return masterTree.getProportionShares(row);
	}

	public boolean areBudgetValuesAllocated(int row)
	{
		return masterTree.areBudgetValuesAllocated(row);
	}
	
	@Override
	public Object getValueAt(int row, int column)
	{
		final int modelColumn = convertColumnIndexToModel(column);
		if(getCastedModel().isWorkUnitColumn(modelColumn))
		{
			if (masterTree.getTree().isExpanded(row))
				return new EmptyChoiceItem();
		}
		
		return super.getValueAt(row, column);
	}
	
	@Override
	public String getColumnGroupCode(int tableColumn)
	{
		int modelColumn = convertColumnIndexToModel(tableColumn);
		return getCastedModel().getColumnGroupCode(modelColumn);
	}

	public Vector<Action> getActionsForRightClickMenu(int tableColumn)
	{
		int multiModelColumn = convertColumnIndexToModel(tableColumn);
		int modelColumn = getCastedModel().findColumnWithinSubTable(multiModelColumn);
		PlanningUpperTableModelInterface model = getCastedModel().getCastedModel(multiModelColumn);

		Vector<Action> actions = new Vector();
		actions.add(getActions().get(ActionDeletePlanningViewTreeNode.class));
		actions.add(null);
		actions.add(getActions().get(ActionExpandAllRows.class));
		actions.add(getActions().get(ActionCollapseAllRows.class));
		actions.add(null);
		if(model.isColumnExpandable(modelColumn))
			actions.add(new MultiTableExpandColumnAction(this));
		if(model.isColumnCollapsable(modelColumn))
			actions.add(new MultiTableCollapseColumnAction(this));
		return actions;
	}

	public int getColumnWidth(int tableColumnIndex)
	{
		return getColumnModel().getColumn(tableColumnIndex).getWidth();
	}

	public boolean isColumnCollapsable(int tableColumnIndex)
	{
		int modelColumn = getModelColumnWithinModel(tableColumnIndex);
		PlanningUpperTableModelInterface model = getModel(convertColumnIndexToModel(tableColumnIndex));
		return model.isColumnCollapsable(modelColumn);
	}

	public boolean isColumnExpandable(int tableColumnIndex)
	{
		int modelColumn = getModelColumnWithinModel(tableColumnIndex);
		PlanningUpperTableModelInterface model = getModel(convertColumnIndexToModel(tableColumnIndex));
		return model.isColumnExpandable(modelColumn);
	}

	public void respondToExpandOrCollapseColumnEvent(int tableColumnIndex) throws Exception
	{
		int modelColumn = getModelColumnWithinModel(tableColumnIndex);
		PlanningUpperTableModelInterface model = getModel(convertColumnIndexToModel(tableColumnIndex));
		model.respondToExpandOrCollapseColumnEvent(modelColumn);
	}

	private PlanningUpperTableModelInterface getModel(int modelColumnIndex)
	{
		PlanningUpperTableModelInterface model = getCastedModel().getCastedModel(modelColumnIndex);
		return model;
	}

	private int getModelColumnWithinModel(int tableColumnIndex)
	{
		int modelColumn = getCastedModel().findColumnWithinSubTable(convertColumnIndexToModel(tableColumnIndex));
		return modelColumn;
	}


	private Actions getActions()
	{
		return getMainWindow().getActions();
	}

	private PlanningTreeTable masterTree;
	private BasicTableCellRendererFactory defaultRendererFactory;
	private BasicTableCellRendererFactory currencyRendererFactory;
	private BasicTableCellRendererFactory choiceRendererFactory;
	private BasicTableCellRendererFactory progressRendererFactory;
	private BasicTableCellRendererFactory doubleRendererFactory;
}
