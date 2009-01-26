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

import java.awt.Color;

import javax.swing.table.TableCellRenderer;

import org.miradi.dialogs.tablerenderers.BasicTableCellRendererFactory;
import org.miradi.dialogs.tablerenderers.BudgetCostTreeTableCellRendererFactory;
import org.miradi.dialogs.tablerenderers.ChoiceItemTableCellRendererFactory;
import org.miradi.dialogs.tablerenderers.FontForObjectTypeProvider;
import org.miradi.dialogs.tablerenderers.MultiLineObjectTableCellRendererFactory;
import org.miradi.dialogs.tablerenderers.ProgressTableCellRendererFactory;
import org.miradi.dialogs.tablerenderers.RowColumnBaseObjectProvider;
import org.miradi.objects.BaseObject;
import org.miradi.project.CurrencyFormat;
import org.miradi.utils.TableWithColumnWidthAndSequenceSaver;

public class PlanningUpperMultiTable extends TableWithColumnWidthAndSequenceSaver implements RowColumnBaseObjectProvider
{
	public PlanningUpperMultiTable(PlanningTreeTable masterTreeToUse, PlanningTreeMultiTableModel model, FontForObjectTypeProvider fontProvider)
	{
		super(masterTreeToUse.getMainWindow(), model, UNIQUE_IDENTIFIER);
		setAutoResizeMode(AUTO_RESIZE_OFF);

		masterTree = masterTreeToUse;
		defaultRendererFactory = new MultiLineObjectTableCellRendererFactory(this, fontProvider);

		CurrencyFormat currencyFormatter = masterTree.getProject().getCurrencyFormatterWithCommas();
		currencyRendererFactory = new BudgetCostTreeTableCellRendererFactory(this, fontProvider, currencyFormatter);

		choiceRendererFactory = new ChoiceItemTableCellRendererFactory(this, fontProvider);
		
		progressRendererFactory = new ProgressTableCellRendererFactory(this, fontProvider);
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
		
		Color background = getCastedModel().getCellBackgroundColor(row, modelColumn);
		factory.setCellBackgroundColor(background);
		return factory;
	}

	private PlanningTreeMultiTableModel getCastedModel()
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

	private static final String UNIQUE_IDENTIFIER = "PlanningUpperMultiTable";
	
	private PlanningTreeTable masterTree;
	private BasicTableCellRendererFactory defaultRendererFactory;
	private BasicTableCellRendererFactory currencyRendererFactory;
	private BasicTableCellRendererFactory choiceRendererFactory;
	private BasicTableCellRendererFactory progressRendererFactory;
}
