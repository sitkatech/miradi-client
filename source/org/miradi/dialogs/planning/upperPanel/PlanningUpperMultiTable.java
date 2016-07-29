/* 
Copyright 2005-2015, Foundations of Success, Bethesda, Maryland
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

package org.miradi.dialogs.planning.upperPanel;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.Action;
import javax.swing.DefaultCellEditor;
import javax.swing.ListSelectionModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

import org.miradi.actions.*;
import org.miradi.dialogs.fieldComponents.ChoiceItemComboBox;
import org.miradi.dialogs.planning.FullTimeEmployeeDaysPerYearAction;
import org.miradi.dialogs.planning.MultiTableCollapseColumnAction;
import org.miradi.dialogs.planning.RightClickActionProvider;
import org.miradi.dialogs.planning.TableHeaderWithExpandCollapseIcons;
import org.miradi.dialogs.planning.TableWithExpandableColumnsInterface;
import org.miradi.dialogs.planning.propertiesPanel.AssignmentDateUnitsTable;
import org.miradi.dialogs.planning.propertiesPanel.MultiTableExpandColumnAction;
import org.miradi.dialogs.planning.propertiesPanel.PlanningRightClickHandler;
import org.miradi.dialogs.planning.propertiesPanel.PlanningViewAbstractTreeTableSyncedTableModel;
import org.miradi.dialogs.tablerenderers.*;
import org.miradi.main.MainWindow;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objecthelpers.ORefSet;
import org.miradi.objects.*;
import org.miradi.questions.ChoiceQuestion;
import org.miradi.questions.CustomPlanningColumnsQuestion;
import org.miradi.questions.StaticQuestionManager;
import org.miradi.utils.TableWithColumnWidthAndSequenceSaver;

public class PlanningUpperMultiTable extends TableWithColumnWidthAndSequenceSaver implements RowColumnSelectionProvider, RightClickActionProvider, TableWithExpandableColumnsInterface
{
	public PlanningUpperMultiTable(MainWindow mainWindowToUse, PlanningTreeTable masterTreeToUse, PlanningTreeMultiTableModel model)
	{
		super(mainWindowToUse, model, model.getUniqueTableModelIdentifier(), model);
		
		setAutoResizeMode(AUTO_RESIZE_OFF);
		setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		setCellSelectionEnabled(true);
		setTableHeaderRenderer();

		masterTree = masterTreeToUse;
		resourceRefsFilter = new ORefSet();

		DefaultFontProvider defaultFontProvider = new DefaultFontProvider(getMainWindow());
		FontForObjectProvider objectFontProvider = new PlanningViewFontProvider(getMainWindow());

		defaultRendererFactory = new MultiLineObjectTableCellRendererOnlyFactory(mainWindowToUse, this, objectFontProvider);
		currencyRendererFactory = new BudgetCostTreeTableCellRendererFactory(this, objectFontProvider);
		choiceRendererFactory = new ChoiceItemTableCellRendererFactory(this, objectFontProvider);
		appendedLabelsOnSingleLineRendererFactory = new SingleLineAppendedLabelsObjectTableCellRendererOnlyFactory(mainWindowToUse, this, objectFontProvider);
		progressRendererFactory = new ProgressTableCellRendererFactory(this, objectFontProvider);
		doubleRendererFactory = new NumericTableCellRendererFactory(this, objectFontProvider);
		whenAssignedColumnTableCellEditorFactory = new SingleLineObjectTableCellEditorOrRendererFactory(this, defaultFontProvider);
		whoAssignedColumnTableCellEditorFactory = new WhoAssignedTableCellPopupEditorOrRendererFactory(getMainWindow(), this, objectFontProvider, resourceRefsFilter);
		timeframeColumnTableCellEditorFactory = new TimeframeTableCellPopupEditorOrRendererFactory(mainWindowToUse, this, objectFontProvider);
		singleLineTextCellEditorFactory = new SingleLineObjectTableCellEditorOrRendererFactory(this, objectFontProvider);
		multiLineTextCellEditorFactor = new ExpandingReadonlyTableCellEditorOrRendererFactory(mainWindowToUse, this, objectFontProvider);

		addMouseListener(new PlanningRightClickHandler(getMainWindow(), this, this));
	}

	protected void setTableHeaderRenderer()
	{
		setTableHeader(new TableHeaderWithExpandCollapseIcons(this));
	}
	
	@Override
	public TableCellEditor getCellEditor(int row, int tableColumn)
	{
		int modelColumn = convertColumnIndexToModel(tableColumn);
		String columnTag = getCastedModel().getColumnTag(modelColumn);

		if (columnTag.equals(CustomPlanningColumnsQuestion.META_ASSIGNED_WHO_TOTAL))
			return new WhoAssignedTableCellPopupEditorOrRendererFactory(getMainWindow(), this, new PlanningViewFontProvider(getMainWindow()), resourceRefsFilter);
		
		if (getCastedModel().isTimeframeColumn(modelColumn))
			return new TimeframeTableCellPopupEditorOrRendererFactory(getMainWindow(), this, new PlanningViewFontProvider(getMainWindow()));

		Class cellQuestionClass = getCastedModel().getCellQuestion(row, modelColumn);
		if (cellQuestionClass !=  null)
		{
			ChoiceQuestion question = StaticQuestionManager.getQuestion(cellQuestionClass);
			ChoiceItemComboBox comboBox = new ChoiceItemComboBox(question);
			return new DefaultCellEditor(comboBox);
		}
		
		if (getCastedModel().isFormattedEditableColumn(modelColumn))
		{
			return multiLineTextCellEditorFactor;
		}
		
		return getSingleLineTextCellEditorFactory();
	}

	protected SingleLineObjectTableCellEditorOrRendererFactory getSingleLineTextCellEditorFactory()
	{
		return singleLineTextCellEditorFactory;
	}

	@Override
	public int getDefaultColumnWidth(int tableColumn, String columnTag, int columnHeaderWidth)
	{
		final int modelColumn = convertColumnIndexToModel(tableColumn);
		if (getCastedModel().isDateUnitColumn(modelColumn))
		{
			PlanningViewAbstractTreeTableSyncedTableModel castedModel = getCastedModel().getCastedModel(modelColumn);
			int subModelColumn = getCastedModel().findColumnWithinSubTable(modelColumn);
			return AssignmentDateUnitsTable.getDefaultColumnWidth(castedModel, subModelColumn, getColumnGroupCode(tableColumn), columnHeaderWidth);
		}
		
		return super.getDefaultColumnWidth(tableColumn, columnTag, columnHeaderWidth);
	}
	
	@Override
	public TableCellRenderer getCellRenderer(int row, int tableColumn)
	{
		final int modelColumn = convertColumnIndexToModel(tableColumn);

		BasicTableCellEditorOrRendererFactory factory = defaultRendererFactory;
		if(getCastedModel().isCurrencyColumn(modelColumn))
			factory = currencyRendererFactory;
		else if(getCastedModel().isChoiceColumn(modelColumn))
			factory = choiceRendererFactory;
		else if(getCastedModel().isProgressColumn(modelColumn))
			factory = progressRendererFactory;
		else if(getCastedModel().isDateUnitColumn(modelColumn))
			factory = doubleRendererFactory;
		else if (getCastedModel().isTimeframeColumn(modelColumn))
			factory = timeframeColumnTableCellEditorFactory;
		else if (getCastedModel().isAssignedWhenColumn(modelColumn))
			factory = whenAssignedColumnTableCellEditorFactory;
		else if (getCastedModel().isAssignedWhoColumn(modelColumn))
			factory = whoAssignedColumnTableCellEditorFactory;
		else if (getCastedModel().isAppendedLabelsOnSingleLineColumn(modelColumn))
			factory = appendedLabelsOnSingleLineRendererFactory;

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

	@Override
	public String getColumnGroupCode(int tableColumn)
	{
		int modelColumn = convertColumnIndexToModel(tableColumn);
		return getCastedModel().getColumnGroupCode(modelColumn);
	}

	public Vector<Action> getActionsForRightClickMenu(int row, int tableColumn)
	{
		int multiModelColumn = convertColumnIndexToModel(tableColumn);
		int modelColumn = getCastedModel().findColumnWithinSubTable(multiModelColumn);
		PlanningViewAbstractTreeTableSyncedTableModel model = getCastedModel().getCastedModel(multiModelColumn);

		Vector<Action> actions = new Vector<Action>();
		BaseObject baseObject = getBaseObjectForRowColumn(row, tableColumn);
		actions.addAll(getActionsForBaseObject(baseObject, getActions()));
		actions.add(getActions().get(ActionDeletePlanningViewTreeNode.class));
		actions.add(null);
		actions.add(getActions().get(ActionExpandAllRows.class));
		actions.add(getActions().get(ActionCollapseAllRows.class));
		actions.add(null);
		actions.addAll(getActionsForAssignments());
		if(model.isColumnExpandable(modelColumn))
			actions.add(new MultiTableExpandColumnAction(this));
		if(model.isColumnCollapsible(modelColumn))
			actions.add(new MultiTableCollapseColumnAction(this));
		if(model.isFullTimeEmployeeFractionAvailable(row, modelColumn))
			actions.add(new FullTimeEmployeeDaysPerYearAction(this));
		return actions;
	}

	protected ArrayList<Action> getActionsForBaseObject(BaseObject baseObject, Actions availableActions)
	{
		return new ArrayList<Action>();
	}

	protected ArrayList<Action> getActionsForAssignments()
	{
		return new ArrayList<Action>();
	}

	public int getColumnWidth(int tableColumnIndex)
	{
		return getColumnModel().getColumn(tableColumnIndex).getWidth();
	}

	public boolean isColumnCollapsible(int tableColumnIndex)
	{
		int modelColumn = getModelColumnWithinModel(tableColumnIndex);
		PlanningViewAbstractTreeTableSyncedTableModel model = getModel(convertColumnIndexToModel(tableColumnIndex));
		return model.isColumnCollapsible(modelColumn);
	}

	public boolean isColumnExpandable(int tableColumnIndex)
	{
		int modelColumn = getModelColumnWithinModel(tableColumnIndex);
		PlanningViewAbstractTreeTableSyncedTableModel model = getModel(convertColumnIndexToModel(tableColumnIndex));
		return model.isColumnExpandable(modelColumn);
	}

	public void respondToExpandOrCollapseColumnEvent(int tableColumnIndex) throws Exception
	{
		int modelColumn = getModelColumnWithinModel(tableColumnIndex);
		PlanningViewAbstractTreeTableSyncedTableModel model = getModel(convertColumnIndexToModel(tableColumnIndex));
		model.respondToExpandOrCollapseColumnEvent(modelColumn);
		saveColumnState();
		updateToReflectNewColumns();
	}

	private PlanningViewAbstractTreeTableSyncedTableModel getModel(int modelColumnIndex)
	{
		return getCastedModel().getCastedModel(modelColumnIndex);
	}

	private int getModelColumnWithinModel(int tableColumnIndex)
	{
		int modelColumn = getCastedModel().findColumnWithinSubTable(convertColumnIndexToModel(tableColumnIndex));
		return modelColumn;
	}
	
	protected Actions getActions()
	{
		return getMainWindow().getActions();
	}
	
	public ORefList getObjectHierarchy(int row, int column)
	{
		throw new RuntimeException("Method is currently unused and has no implementation");
	}

	public void setResourcesFilter(ORefSet resourceRefFiltersToUse)
	{
		resourceRefsFilter = resourceRefFiltersToUse;
	}

	private PlanningTreeTable masterTree;
	private BasicTableCellEditorOrRendererFactory defaultRendererFactory;
	private BasicTableCellEditorOrRendererFactory currencyRendererFactory;
	private BasicTableCellEditorOrRendererFactory choiceRendererFactory;
	private BasicTableCellEditorOrRendererFactory appendedLabelsOnSingleLineRendererFactory;
	private BasicTableCellEditorOrRendererFactory progressRendererFactory;
	private BasicTableCellEditorOrRendererFactory doubleRendererFactory;
	private SingleLineObjectTableCellEditorOrRendererFactory whenAssignedColumnTableCellEditorFactory;
	private WhoAssignedTableCellPopupEditorOrRendererFactory whoAssignedColumnTableCellEditorFactory;
	private TimeframeTableCellPopupEditorOrRendererFactory timeframeColumnTableCellEditorFactory;
	private SingleLineObjectTableCellEditorOrRendererFactory singleLineTextCellEditorFactory;
	private ExpandingReadonlyTableCellEditorOrRendererFactory multiLineTextCellEditorFactor;
	protected ORefSet resourceRefsFilter;
}
