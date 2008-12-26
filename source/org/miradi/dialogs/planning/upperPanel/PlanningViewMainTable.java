package org.miradi.dialogs.planning.upperPanel;

import java.awt.Color;

import javax.swing.event.TableColumnModelEvent;
import javax.swing.table.TableColumn;

import org.miradi.dialogs.tablerenderers.BasicTableCellRendererFactory;
import org.miradi.dialogs.tablerenderers.BudgetCostTreeTableCellRendererFactory;
import org.miradi.dialogs.tablerenderers.ChoiceItemTableCellRendererFactory;
import org.miradi.dialogs.tablerenderers.FontForObjectTypeProvider;
import org.miradi.dialogs.tablerenderers.MultiLineObjectTableCellRendererFactory;
import org.miradi.dialogs.tablerenderers.ProgressTableCellRendererFactory;
import org.miradi.main.AppPreferences;
import org.miradi.main.EAM;
import org.miradi.main.MainWindow;
import org.miradi.objects.Assignment;
import org.miradi.objects.BaseObject;
import org.miradi.objects.Indicator;
import org.miradi.objects.Objective;
import org.miradi.objects.Strategy;
import org.miradi.objects.Task;
import org.miradi.project.CurrencyFormat;
import org.miradi.utils.TableWithTreeTableNodes;

public class PlanningViewMainTable extends TableWithTreeTableNodes
{
	public PlanningViewMainTable(MainWindow mainWindowToUse, PlanningViewMainTableModel modelToUse, FontForObjectTypeProvider fontProviderToUse)
	{
		super(mainWindowToUse, modelToUse, UNIQUE_IDENTIFIER);
		fontProvider = fontProviderToUse;
		currencyFormatter = getProject().getCurrencyFormatterWithCommas();
		setTableColumnRenderers();
	}

	private void setTableColumnRenderers()
	{
		for (int i  = 0; i < getColumnModel().getColumnCount(); ++i)
			setColumnRenderer(i);
	}

	private void setColumnRenderer(int column)
	{
		String columnTag = getColumnTag(column);
		BasicTableCellRendererFactory renderer = createRendererForColumn(columnTag);
		renderer.setCellBackgroundColor(getBackgroundColor(columnTag));

		TableColumn tableColumn = getColumnModel().getColumn(column);
		tableColumn.setCellRenderer(renderer);
	}

	private BasicTableCellRendererFactory createRendererForColumn(String columnTag)
	{
		if(columnTag.equals(Strategy.PSEUDO_TAG_LATEST_PROGRESS_REPORT_CODE) || columnTag.equals(Objective.PSEUDO_TAG_LATEST_PROGRESS_PERCENT_COMPLETE))
			return new ProgressTableCellRendererFactory(this, fontProvider);
		if(columnTag.equals(Task.PSEUDO_TAG_BUDGET_TOTAL))
			return new BudgetCostTreeTableCellRendererFactory(this, fontProvider, currencyFormatter);
		if(isQuestionColumn(columnTag))
			return new ChoiceItemTableCellRendererFactory(this, fontProvider);
		return new MultiLineObjectTableCellRendererFactory(this, fontProvider);
	}
	
	protected Color getBackgroundColor(String columnTag)
	{
		if (columnTag.equals(BaseObject.PSEUDO_TAG_WHO_TOTAL))
			return AppPreferences.RESOURCE_TABLE_BACKGROUND;
		
		if (columnTag.equals(Indicator.PSEUDO_TAG_METHODS))
			return AppPreferences.INDICATOR_COLOR;
		
		if(columnTag.equals(BaseObject.PSEUDO_TAG_WHEN_TOTAL))
			return AppPreferences.WORKPLAN_TABLE_BACKGROUND;
		
		if(columnTag.equals(Task.PSEUDO_TAG_BUDGET_TOTAL))
			return AppPreferences.BUDGET_TOTAL_TABLE_BACKGROUND;
		
		if(columnTag.equals(Assignment.PSEUDO_TAG_WORK_UNIT_TOTAL))
			return AppPreferences.WORKPLAN_TABLE_BACKGROUND;
			
		return Color.white;
	}
	
	public boolean isQuestionColumn(String columnTag)
	{
		if(columnTag.equals(Strategy.PSEUDO_TAG_RATING_SUMMARY))
			return true;
		
		if(columnTag.equals(Indicator.TAG_PRIORITY))
			return true;
		
		if(columnTag.equals(BaseObject.PSEUDO_TAG_LATEST_PROGRESS_REPORT_CODE))
			return true;
		
		return false;
	}
	
	@Override
	public void columnAdded(TableColumnModelEvent event)
	{
		super.columnAdded(event);
		
		// NOTE: If we are still in super constructor, don't do anything else
		if(getMainWindow() == null)
			return;
		
		
		int column = event.getToIndex();
		setColumnRenderer(column);
		try
		{
			reloadTable();
		}
		catch(Exception e)
		{
			EAM.logException(e);
		}
	}

	public static final String UNIQUE_IDENTIFIER = "PlanningViewMainTable";

	private FontForObjectTypeProvider fontProvider;
	private CurrencyFormat currencyFormatter;
}
