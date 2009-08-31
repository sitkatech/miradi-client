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
package org.miradi.rtf.viewExporters;

import org.miradi.dialogs.planning.AccountingCodeCoreRowColumnProvider;
import org.miradi.dialogs.planning.FundingSourceCoreRowColumnProvider;
import org.miradi.dialogs.planning.MonitoringRowColumnProvider;
import org.miradi.dialogs.planning.ProgressReportRowColumnProvider;
import org.miradi.dialogs.planning.ProjectResourceRowColumnProvider;
import org.miradi.dialogs.planning.RowColumnProvider;
import org.miradi.dialogs.planning.StrategicRowColumnProvider;
import org.miradi.dialogs.planning.WorkPlanRowColumnProviderWithBudgetColumns;
import org.miradi.dialogs.planning.propertiesPanel.BudgetDetailsTableModel;
import org.miradi.dialogs.planning.propertiesPanel.ExpenseAmountsTableModel;
import org.miradi.dialogs.planning.propertiesPanel.PlanningViewMainModelExporter;
import org.miradi.dialogs.planning.propertiesPanel.PlanningWorkUnitsTableModel;
import org.miradi.dialogs.planning.upperPanel.AccountingCodeTreeTableModel;
import org.miradi.dialogs.planning.upperPanel.ExportablePlanningTreeTableModel;
import org.miradi.dialogs.planning.upperPanel.FundingSourceTreeTableModel;
import org.miradi.dialogs.planning.upperPanel.PlanningViewFutureStatusTableModel;
import org.miradi.dialogs.planning.upperPanel.PlanningViewMainTableModel;
import org.miradi.dialogs.planning.upperPanel.PlanningViewMeasurementTableModel;
import org.miradi.dialogs.planning.upperPanel.ProjectResourceTreeTableModel;
import org.miradi.dialogs.planning.upperPanel.TreeTableModelExporter;
import org.miradi.main.MainWindow;
import org.miradi.objects.Indicator;
import org.miradi.objects.Measurement;
import org.miradi.project.Project;
import org.miradi.questions.CustomPlanningColumnsQuestion;
import org.miradi.questions.ReportTemplateContentQuestion;
import org.miradi.rtf.RtfWriter;
import org.miradi.utils.AbstractTableExporter;
import org.miradi.utils.CodeList;
import org.miradi.utils.MultiTableCombinedAsOneExporter;

public class PlanningViewRtfExporter extends RtfViewExporter
{
	public PlanningViewRtfExporter(MainWindow mainWindow)
	{
		super(mainWindow);
	}
	
	@Override
	public void exportView(RtfWriter writer, CodeList reportTemplateContent) throws Exception
	{
		if (reportTemplateContent.contains(ReportTemplateContentQuestion.PLANNING_VIEW_STRATEGIC_PLAN_CODE))
			exportReport(writer, new StrategicRowColumnProvider(), ReportTemplateContentQuestion.getStrategicPlanLabel());
		
		if (reportTemplateContent.contains(ReportTemplateContentQuestion.PLANNING_VIEW_MONITORING_PLAN_CODE))
			exportReport(writer, new MonitoringRowColumnProvider(), ReportTemplateContentQuestion.getMonitoringPlanLabel());
		
		if (reportTemplateContent.contains(ReportTemplateContentQuestion.PLANNING_VIEW_WORK_PLAN_CODE))
			exportReport(writer, new WorkPlanRowColumnProviderWithBudgetColumns(getProject()), ReportTemplateContentQuestion.getWorkPlanLabel());
		
		if (reportTemplateContent.contains(ReportTemplateContentQuestion.PROGRESS_REPORT_CODE))
			exportReport(writer, new ProgressReportRowColumnProvider(), ReportTemplateContentQuestion.getProgressReportLabel());
		
		if (reportTemplateContent.contains(ReportTemplateContentQuestion.PLANNING_VIEW_RESOURCES_TAB_CODE))
			exportResourcesTab(writer, new ProjectResourceRowColumnProvider(), ReportTemplateContentQuestion.getResourcesLabel());
		
		if (reportTemplateContent.contains(ReportTemplateContentQuestion.PLANNING_VIEW_ACCOUNTING_CODE_TAB_CODE))
			exportAccountingCodeTab(writer, new AccountingCodeCoreRowColumnProvider(), ReportTemplateContentQuestion.getAccountingCodesLabel());
		
		if (reportTemplateContent.contains(ReportTemplateContentQuestion.PLANNING_VIEW_FUNDING_SOURCE_TAB_CODE))
			exportFundingSourceTab(writer, new FundingSourceCoreRowColumnProvider(), ReportTemplateContentQuestion.getFundingSourcesLabel());
	}

	private void exportResourcesTab(RtfWriter writer, RowColumnProvider rowColumnProvider, String translatedTableName) throws Exception
	{
		ExportablePlanningTreeTableModel model = ProjectResourceTreeTableModel.createProjectResourceTreeTableModel(getProject(), rowColumnProvider.getColumnListToShow(), AbstractTableExporter.NO_UNIQUE_MODEL_IDENTIFIER);
		exportTab(writer, rowColumnProvider, translatedTableName, model);
	}

	private void exportAccountingCodeTab(RtfWriter writer, RowColumnProvider rowColumnProvider, String translatedTableName) throws Exception
	{
		ExportablePlanningTreeTableModel model = AccountingCodeTreeTableModel.createAccountingCodeTreeTableModel(getProject(), rowColumnProvider.getColumnListToShow(), AbstractTableExporter.NO_UNIQUE_MODEL_IDENTIFIER);
		exportTab(writer, rowColumnProvider, translatedTableName, model);
	}
	
	private void exportFundingSourceTab(RtfWriter writer, RowColumnProvider rowColumnProvider, String translatedTableName) throws Exception
	{
		ExportablePlanningTreeTableModel model = FundingSourceTreeTableModel.createFundingSourceTreeTableModel(getProject(), rowColumnProvider.getColumnListToShow(), AbstractTableExporter.NO_UNIQUE_MODEL_IDENTIFIER);
		exportTab(writer, rowColumnProvider, translatedTableName, model);
	}
	
	private void exportTab(RtfWriter writer, RowColumnProvider rowColumnProvider, String translatedTableName, ExportablePlanningTreeTableModel model) throws Exception
	{
		MultiTableCombinedAsOneExporter multiExporter = createMultiModelExporter(getProject(), model, rowColumnProvider);	
		exportTable(writer, multiExporter, translatedTableName);
	}
	
	public static MultiTableCombinedAsOneExporter createTables(Project project, RowColumnProvider rowColumnProvider) throws Exception
	{
		ExportablePlanningTreeTableModel model = new ExportablePlanningTreeTableModel(project, rowColumnProvider, AbstractTableExporter.NO_UNIQUE_MODEL_IDENTIFIER);
		
		return createMultiModelExporter(project, model, rowColumnProvider);
	}
	
	private static MultiTableCombinedAsOneExporter createMultiModelExporter(Project project, ExportablePlanningTreeTableModel model, RowColumnProvider rowColumnProvider) throws Exception
	{
		MultiTableCombinedAsOneExporter multiModelExporter = new MultiTableCombinedAsOneExporter(project);
		multiModelExporter.addExportable(new TreeTableModelExporter(project, model));
		
		PlanningViewMainTableModel mainModel = new PlanningViewMainTableModel(project, model, rowColumnProvider);
		multiModelExporter.addExportable(new PlanningViewMainModelExporter(project, mainModel, model, model.getUniqueTreeTableModelIdentifier()));
		
		CodeList columnsToShow = rowColumnProvider.getColumnListToShow();
		if (columnsToShow.contains(Measurement.META_COLUMN_TAG))
		{
			PlanningViewMeasurementTableModel measurementModel = new PlanningViewMeasurementTableModel(project, model);
			multiModelExporter.addExportable(new PlanningViewMainModelExporter(project, measurementModel, model, measurementModel.getUniqueTableModelIdentifier()));
		}
		if (columnsToShow.contains(Indicator.META_COLUMN_TAG))
		{
			PlanningViewFutureStatusTableModel futureStatusModel = new PlanningViewFutureStatusTableModel(project, model);
			multiModelExporter.addExportable(new PlanningViewMainModelExporter(project, futureStatusModel, model, futureStatusModel.getUniqueTableModelIdentifier()));
		}
		if (columnsToShow.contains(CustomPlanningColumnsQuestion.META_RESOURCE_ASSIGNMENT_COLUMN_CODE))
		{
			PlanningWorkUnitsTableModel workUnitsModel = new PlanningWorkUnitsTableModel(project, model, model.getUniqueTreeTableModelIdentifier());;
			multiModelExporter.addExportable(new PlanningViewMainModelExporter(project, workUnitsModel, model, workUnitsModel.getUniqueTableModelIdentifier()));
		}
		if (columnsToShow.contains(CustomPlanningColumnsQuestion.META_EXPENSE_ASSIGNMENT_COLUMN_CODE))
		{
			ExpenseAmountsTableModel expensesModel = new ExpenseAmountsTableModel(project, model, model.getUniqueTreeTableModelIdentifier());;
			multiModelExporter.addExportable(new PlanningViewMainModelExporter(project, expensesModel, model, expensesModel.getUniqueTableModelIdentifier()));
		}
		if (columnsToShow.contains(CustomPlanningColumnsQuestion.META_BUDGET_DETAIL_COLUMN_CODE))
		{
			BudgetDetailsTableModel budgetDetailsModel = new BudgetDetailsTableModel(project, model, model.getUniqueTreeTableModelIdentifier());;
			multiModelExporter.addExportable(new PlanningViewMainModelExporter(project, budgetDetailsModel, model, budgetDetailsModel.getUniqueTableModelIdentifier()));
		}
		
		return multiModelExporter;
	}
	
	private void exportReport(RtfWriter writer, RowColumnProvider rowColumnProvider, String translatedTableName) throws Exception
	{
		exportTable(writer, createTables(getProject(), rowColumnProvider), translatedTableName);
	}
}
