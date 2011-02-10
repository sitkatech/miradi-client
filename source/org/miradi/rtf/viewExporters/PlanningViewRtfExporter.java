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

import java.util.Collections;
import java.util.Vector;

import org.miradi.dialogs.AnalysisRowColumnProvider;
import org.miradi.dialogs.planning.AccountingCodeCoreRowColumnProvider;
import org.miradi.dialogs.planning.ActionPlanSubViewObjectiveBasedRowColumnProvider;
import org.miradi.dialogs.planning.ActionPlanSubViewStrategyBasedRowColumnProvider;
import org.miradi.dialogs.planning.BudgetCategoryOneCoreRowColumnProvider;
import org.miradi.dialogs.planning.BudgetCategoryTwoCoreRowColumnProvider;
import org.miradi.dialogs.planning.CustomTablePlanningTreeRowColumnProvider;
import org.miradi.dialogs.planning.FundingSourceCoreRowColumnProvider;
import org.miradi.dialogs.planning.MonitoringRowColumnProvider;
import org.miradi.dialogs.planning.ProgressReportRowColumnProvider;
import org.miradi.dialogs.planning.ProjectResourceRowColumnProvider;
import org.miradi.dialogs.planning.RowColumnProvider;
import org.miradi.dialogs.planning.WorkPlanCategoryTreeRowColumnProvider;
import org.miradi.dialogs.planning.WorkPlanRowColumnProviderWithBudgetColumns;
import org.miradi.dialogs.planning.propertiesPanel.PlanningViewMainModelExporter;
import org.miradi.dialogs.planning.propertiesPanel.WorkPlanBudgetDetailsTableModel;
import org.miradi.dialogs.planning.propertiesPanel.WorkPlanExpenseAmountsTableModel;
import org.miradi.dialogs.planning.propertiesPanel.WorkPlanWorkUnitsTableModel;
import org.miradi.dialogs.planning.treenodes.NewAbstractPlanningTreeNode;
import org.miradi.dialogs.planning.treenodes.NewPlanningRootNode;
import org.miradi.dialogs.planning.upperPanel.ExportablePlanningTreeTableModel;
import org.miradi.dialogs.planning.upperPanel.PlanningViewFutureStatusTableModel;
import org.miradi.dialogs.planning.upperPanel.PlanningViewMainTableModel;
import org.miradi.dialogs.planning.upperPanel.PlanningViewMeasurementTableModel;
import org.miradi.dialogs.planning.upperPanel.TreeTableModelExporter;
import org.miradi.dialogs.planning.upperPanel.rebuilder.PlanningCategoryTreeRebuilder;
import org.miradi.dialogs.planning.upperPanel.rebuilder.NormalTreeRebuilder;
import org.miradi.dialogs.treetables.NewBudgetCategoryTreeModel;
import org.miradi.main.MainWindow;
import org.miradi.objecthelpers.BaseObjectByFullNameSorter;
import org.miradi.objecthelpers.ORef;
import org.miradi.objects.Indicator;
import org.miradi.objects.Measurement;
import org.miradi.objects.ObjectTreeTableConfiguration;
import org.miradi.objects.PlanningTreeRowColumnProvider;
import org.miradi.project.Project;
import org.miradi.questions.ReportTemplateContentQuestion;
import org.miradi.questions.WorkPlanColumnConfigurationQuestion;
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
		if (reportTemplateContent.contains(ReportTemplateContentQuestion.ACTION_PLAN_OBJECTIVE_BASED_REPORT_CODE))
			exportReport(writer, new ActionPlanSubViewObjectiveBasedRowColumnProvider(getProject()), ReportTemplateContentQuestion.getActionPlanObjectiveBasedActionPlanLabel());
		
		if (reportTemplateContent.contains(ReportTemplateContentQuestion.ACTION_PLAN_STRATEGY_BASED_REPORT_CODE))
			exportReport(writer, new ActionPlanSubViewStrategyBasedRowColumnProvider(getProject()), ReportTemplateContentQuestion.getActionPlanStrategyBasedActionPlanLabel());
		
		if (reportTemplateContent.contains(ReportTemplateContentQuestion.PLANNING_VIEW_MONITORING_PLAN_CODE))
			exportReport(writer, new MonitoringRowColumnProvider(getProject()), ReportTemplateContentQuestion.getMonitoringPlanLabel());
		
		if (reportTemplateContent.contains(ReportTemplateContentQuestion.PLANNING_VIEW_WORK_PLAN_CODE))
			exportReport(writer, new WorkPlanRowColumnProviderWithBudgetColumns(getProject()), ReportTemplateContentQuestion.getWorkPlanLabel());
		
		if (reportTemplateContent.contains(ReportTemplateContentQuestion.PROGRESS_REPORT_CODE))
			exportReport(writer, new ProgressReportRowColumnProvider(), ReportTemplateContentQuestion.getProgressReportLabel());

		exportAnyCustomTables(writer, reportTemplateContent);
		
		exportWorkPlanCategoryTab(writer, reportTemplateContent, ReportTemplateContentQuestion.PLANNING_VIEW_RESOURCES_CODE, new ProjectResourceRowColumnProvider(getProject()), ReportTemplateContentQuestion.getResourcesLabel());
		exportWorkPlanCategoryTab(writer, reportTemplateContent,ReportTemplateContentQuestion.PLANNING_VIEW_ACCOUNTING_CODE_CODE, new AccountingCodeCoreRowColumnProvider(getProject()), ReportTemplateContentQuestion.getAccountingCodesLabel());
		exportWorkPlanCategoryTab(writer, reportTemplateContent, ReportTemplateContentQuestion.PLANNING_VIEW_FUNDING_SOURCE_CODE, new FundingSourceCoreRowColumnProvider(getProject()), ReportTemplateContentQuestion.getFundingSourcesLabel());
		exportWorkPlanCategoryTab(writer, reportTemplateContent, ReportTemplateContentQuestion.BUDGET_CATEGORY_ONE_CODE, new BudgetCategoryOneCoreRowColumnProvider(getProject()), ReportTemplateContentQuestion.getCategoryOneLabel());
		exportWorkPlanCategoryTab(writer, reportTemplateContent, ReportTemplateContentQuestion.BUDGET_CATEGORY_TWO_CODE, new BudgetCategoryTwoCoreRowColumnProvider(getProject()), ReportTemplateContentQuestion.getCategoryTwoLabel());
		exportWorkPlanCategoryTab(writer, reportTemplateContent, ReportTemplateContentQuestion.ANALYSIS_CODE, new AnalysisRowColumnProvider(getProject()), ReportTemplateContentQuestion.getAnalysisLabel());
	}

	private void exportAnyCustomTables(RtfWriter writer, CodeList reportTemplateContent) throws Exception
	{
		Vector<ObjectTreeTableConfiguration> configurations = extractCustomSections(reportTemplateContent);
		Collections.sort(configurations, new BaseObjectByFullNameSorter());
		
		for(ObjectTreeTableConfiguration configuration : configurations)
		{
			exportCustomSection(writer, configuration);
		}
	}

	private void exportCustomSection(RtfWriter writer, ObjectTreeTableConfiguration configuration) throws Exception
	{
		CustomTablePlanningTreeRowColumnProvider provider = new CustomTablePlanningTreeRowColumnProvider(getProject(), configuration);
		exportReport(writer, provider, configuration.getFullName());
	}

	private Vector<ObjectTreeTableConfiguration> extractCustomSections(CodeList reportTemplateContent)
	{
		String customPrefix = ReportTemplateContentQuestion.CUSTOM_TABLE_CODE_PREFIX;

		Vector<ObjectTreeTableConfiguration> configurations = new Vector<ObjectTreeTableConfiguration>();
		for(int index = 0; index < reportTemplateContent.size(); ++index)
		{
			String code = reportTemplateContent.get(index);
			if(code.startsWith(customPrefix))
			{
				ORef ref = ORef.createFromString(code.substring(customPrefix.length()));
				ObjectTreeTableConfiguration configuration = ObjectTreeTableConfiguration.find(getProject(), ref);
				if(configuration != null)
					configurations.add(configuration);
			}
		}
		return configurations;
	}

	private void exportWorkPlanCategoryTab(RtfWriter writer, CodeList reportTemplateContent, final String code,	final WorkPlanCategoryTreeRowColumnProvider rowColumnProvider,	String resourcesLabel) throws Exception
	{
		if (reportTemplateContent.contains(code))
			exportCategoryTab(writer, rowColumnProvider, resourcesLabel);
	}

	private void exportCategoryTab(RtfWriter writer, WorkPlanCategoryTreeRowColumnProvider rowColumnProvider, String translatedTableName) throws Exception
	{
		NewPlanningRootNode rootNode = new NewPlanningRootNode(getProject());
		PlanningCategoryTreeRebuilder treeRebuilder = new PlanningCategoryTreeRebuilder(getProject(), rowColumnProvider);
		treeRebuilder.rebuildTree(rootNode);
		NewBudgetCategoryTreeModel model = new NewBudgetCategoryTreeModel(getProject(), rootNode, rowColumnProvider, AbstractTableExporter.NO_UNIQUE_MODEL_IDENTIFIER);
		
		exportTab(writer, rowColumnProvider, translatedTableName, model);
	}
	
	private void exportTab(RtfWriter writer, RowColumnProvider rowColumnProvider, String translatedTableName, ExportablePlanningTreeTableModel model) throws Exception
	{
		MultiTableCombinedAsOneExporter multiExporter = createMultiModelExporter(getProject(), model, rowColumnProvider);	
		exportTableWithPageBreak(writer, multiExporter, translatedTableName);
	}
	
	public static MultiTableCombinedAsOneExporter createTables(Project project, PlanningTreeRowColumnProvider rowColumnProvider) throws Exception
	{
		NewAbstractPlanningTreeNode rootNode = new NewPlanningRootNode(project);
 		new NormalTreeRebuilder(project, rowColumnProvider).rebuildTree(rootNode);
		ExportablePlanningTreeTableModel model = new ExportablePlanningTreeTableModel(project, rootNode, rowColumnProvider, AbstractTableExporter.NO_UNIQUE_MODEL_IDENTIFIER);
		
		return createMultiModelExporter(project, model, rowColumnProvider);
	}
	
	private static MultiTableCombinedAsOneExporter createMultiModelExporter(Project project, ExportablePlanningTreeTableModel model, RowColumnProvider rowColumnProvider) throws Exception
	{
		MultiTableCombinedAsOneExporter multiModelExporter = new MultiTableCombinedAsOneExporter(project);
		multiModelExporter.addExportable(new TreeTableModelExporter(project, model));
		
		PlanningViewMainTableModel mainModel = new PlanningViewMainTableModel(project, model, rowColumnProvider);
		multiModelExporter.addExportable(new PlanningViewMainModelExporter(project, mainModel, model, model.getUniqueTreeTableModelIdentifier()));
		
		CodeList columnsToShow = rowColumnProvider.getColumnCodesToShow();
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
		if (columnsToShow.contains(WorkPlanColumnConfigurationQuestion.META_RESOURCE_ASSIGNMENT_COLUMN_CODE))
		{
			WorkPlanWorkUnitsTableModel workUnitsModel = new WorkPlanWorkUnitsTableModel(project, model, model.getUniqueTreeTableModelIdentifier());;
			multiModelExporter.addExportable(new PlanningViewMainModelExporter(project, workUnitsModel, model, workUnitsModel.getUniqueTableModelIdentifier()));
		}
		if (columnsToShow.contains(WorkPlanColumnConfigurationQuestion.META_EXPENSE_ASSIGNMENT_COLUMN_CODE))
		{
			WorkPlanExpenseAmountsTableModel expensesModel = new WorkPlanExpenseAmountsTableModel(project, model, model.getUniqueTreeTableModelIdentifier());;
			multiModelExporter.addExportable(new PlanningViewMainModelExporter(project, expensesModel, model, expensesModel.getUniqueTableModelIdentifier()));
		}
		if (columnsToShow.contains(WorkPlanColumnConfigurationQuestion.META_BUDGET_DETAIL_COLUMN_CODE))
		{
			WorkPlanBudgetDetailsTableModel budgetDetailsModel = new WorkPlanBudgetDetailsTableModel(project, model, model.getUniqueTreeTableModelIdentifier());;
			multiModelExporter.addExportable(new PlanningViewMainModelExporter(project, budgetDetailsModel, model, budgetDetailsModel.getUniqueTableModelIdentifier()));
		}
		
		return multiModelExporter;
	}
	
	private void exportReport(RtfWriter writer, PlanningTreeRowColumnProvider rowColumnProvider, String translatedTableName) throws Exception
	{
		exportTableWithPageBreak(writer, createTables(getProject(), rowColumnProvider), translatedTableName);
	}
}
