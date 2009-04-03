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

import org.miradi.dialogs.accountingcode.AccountingCodePoolTableModel;
import org.miradi.dialogs.fundingsource.FundingSourcePoolTableModel;
import org.miradi.dialogs.planning.ProgressReportRowColumnProvider;
import org.miradi.dialogs.planning.propertiesPanel.PlanningViewMainModelExporter;
import org.miradi.dialogs.planning.upperPanel.ExportablePlanningTreeTableModel;
import org.miradi.dialogs.planning.upperPanel.PlanningViewBudgetAnnualTotalTableModel;
import org.miradi.dialogs.planning.upperPanel.PlanningViewFutureStatusTableModel;
import org.miradi.dialogs.planning.upperPanel.PlanningViewMainTableModel;
import org.miradi.dialogs.planning.upperPanel.PlanningViewMeasurementTableModel;
import org.miradi.dialogs.planning.upperPanel.TreeTableModelExporter;
import org.miradi.dialogs.resource.ResourcePoolTableModel;
import org.miradi.main.MainWindow;
import org.miradi.objects.Indicator;
import org.miradi.objects.Measurement;
import org.miradi.objects.Task;
import org.miradi.project.Project;
import org.miradi.questions.ReportTemplateContentQuestion;
import org.miradi.rtf.RtfWriter;
import org.miradi.utils.CodeList;
import org.miradi.utils.MultiTableCombinedAsOneExporter;
import org.miradi.views.planning.ColumnManager;
import org.miradi.views.planning.RowManager;

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
			exportReport(writer, RowManager.getStrategicPlanRows(), ColumnManager.getStrategicPlanColumns(), ReportTemplateContentQuestion.getStrategicPlanLabel());
		
		if (reportTemplateContent.contains(ReportTemplateContentQuestion.PLANNING_VIEW_MONITORING_PLAN_CODE))
			exportReport(writer, RowManager.getMonitoringPlanRows(), ColumnManager.getMonitoringPlanColumns(), ReportTemplateContentQuestion.getMonitoringPlanLabel());
		
		if (reportTemplateContent.contains(ReportTemplateContentQuestion.PLANNING_VIEW_WORK_PLAN_CODE))
			exportReport(writer, RowManager.getWorkPlanRows(), ColumnManager.getWorkPlanColumns(), ReportTemplateContentQuestion.getWorkPlanLabel());
		
		if (reportTemplateContent.contains(ReportTemplateContentQuestion.PROGRESS_REPORT_CODE))
			exportReport(writer, new ProgressReportRowColumnProvider().getRowListToShow(), ColumnManager.getProgressReportColumns(), ReportTemplateContentQuestion.getProgressReportLabel());
		
		if (reportTemplateContent.contains(ReportTemplateContentQuestion.PLANNING_VIEW_RESOURCES_TAB_CODE))
			exportResourcesTab(writer);
		
		if (reportTemplateContent.contains(ReportTemplateContentQuestion.PLANNING_VIEW_ACCOUNTING_CODE_TAB_CODE))
			exportAccountingCodesTab(writer);
		
		if (reportTemplateContent.contains(ReportTemplateContentQuestion.PLANNING_VIEW_FUNDING_SOURCE_TAB_CODE))
			exportFundingSourceTab(writer);
	}

	private void exportResourcesTab(RtfWriter writer) throws Exception
	{
		exportObjectTableModel(writer, new ResourcePoolTableModel(getProject()), ReportTemplateContentQuestion.getResourcesLabel());
	}

	private void exportAccountingCodesTab(RtfWriter writer) throws Exception
	{
		exportObjectTableModel(writer, new AccountingCodePoolTableModel(getProject()), ReportTemplateContentQuestion.getAccountingCodesLabel());
	}

	private void exportFundingSourceTab(RtfWriter writer) throws Exception
	{
		exportObjectTableModel(writer, new FundingSourcePoolTableModel(getProject()), ReportTemplateContentQuestion.getFundingSourcesLabel());
	}
	
	public static MultiTableCombinedAsOneExporter createTables(Project project, CodeList rowsToShow, CodeList columnsToShow) throws Exception
	{
		MultiTableCombinedAsOneExporter multiModelExporter = new MultiTableCombinedAsOneExporter();
		ExportablePlanningTreeTableModel model = new ExportablePlanningTreeTableModel(project, rowsToShow, columnsToShow);
		multiModelExporter.addExportable(new TreeTableModelExporter(project, model));
		
		PlanningViewMainTableModel mainModel = new PlanningViewMainTableModel(project, model, columnsToShow);
		multiModelExporter.addExportable(new PlanningViewMainModelExporter(mainModel, model));
			
		if (columnsToShow.contains(Task.PSEUDO_TAG_TASK_BUDGET_DETAIL))
		{
			PlanningViewBudgetAnnualTotalTableModel annualTotalsModel = new PlanningViewBudgetAnnualTotalTableModel(project, model);	
			multiModelExporter.addExportable(new PlanningViewMainModelExporter(annualTotalsModel, model));
		}
		if (columnsToShow.contains(Measurement.META_COLUMN_TAG))
		{
			PlanningViewMeasurementTableModel measurementModel = new PlanningViewMeasurementTableModel(project, model);
			multiModelExporter.addExportable(new PlanningViewMainModelExporter(measurementModel, model));
		}
		if (columnsToShow.contains(Indicator.META_COLUMN_TAG))
		{
			PlanningViewFutureStatusTableModel futureStatusModel = new PlanningViewFutureStatusTableModel(project, model);
			multiModelExporter.addExportable(new PlanningViewMainModelExporter(futureStatusModel, model));
		}
		
		return multiModelExporter;
	}
	
	private void exportReport(RtfWriter writer, CodeList rowsToShow, CodeList columnsToShow, String translatedTableName) throws Exception
	{
		exportTable(writer, createTables(getProject(), rowsToShow, columnsToShow), translatedTableName);
	}
}
