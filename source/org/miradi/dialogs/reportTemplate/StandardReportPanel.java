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
package org.miradi.dialogs.reportTemplate;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.border.Border;

import org.miradi.dialogs.fieldComponents.PanelButton;
import org.miradi.dialogs.fieldComponents.PanelTitleLabel;
import org.miradi.layout.OneColumnPanel;
import org.miradi.layout.TwoColumnPanel;
import org.miradi.main.AppPreferences;
import org.miradi.main.EAM;
import org.miradi.main.MainWindow;
import org.miradi.project.Project;
import org.miradi.questions.ChoiceItem;
import org.miradi.questions.ChoiceQuestion;
import org.miradi.questions.ReportTemplateContentQuestion;
import org.miradi.utils.CodeList;
import org.miradi.utils.DefaultHyperlinkHandler;
import org.miradi.views.reports.doers.RunReportTemplateDoer;
import org.miradi.wizard.MiradiHtmlViewer;

public class StandardReportPanel extends TwoColumnPanel
{
	public StandardReportPanel(MainWindow mainWindowToUse)
	{
		mainWindow = mainWindowToUse;

		setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
		add(createReportsPanel());
		add(createHintPanel());
	}

	private JComponent createReportsPanel()
	{
		TwoColumnPanel reports = new TwoColumnPanel();
		reports.setBackground(getMainWindow().getAppPreferences().getDataPanelBackgroundColor());
		addStandardReport(reports, getFullReportCodeList(), EAM.text("Full Report"));
		addStandardReport(reports, getSummaryCodeList(), EAM.text("Summary Report"));
		addStandardReport(reports, ReportTemplateContentQuestion.DIAGRAM_VIEW_CONCEPTUAL_MODEL_TAB_CODE);
		addStandardReport(reports, ReportTemplateContentQuestion.DIAGRAM_VIEW_RESULTS_CHAINS_TAB_CODE);
		addStandardReport(reports, ReportTemplateContentQuestion.THREAT_RATING_VIEW_CODE);
		addStandardReport(reports, ReportTemplateContentQuestion.TARGET_VIABILITY_VIEW_VIABILITY_TAB_TABLE_CODE);
		addStandardReport(reports, ReportTemplateContentQuestion.PLANNING_VIEW_STRATEGIC_PLAN_CODE);
		addStandardReport(reports, ReportTemplateContentQuestion.PLANNING_VIEW_MONITORING_PLAN_CODE);	
		addStandardReport(reports, ReportTemplateContentQuestion.PLANNING_VIEW_WORK_PLAN_CODE);
		addStandardReport(reports, ReportTemplateContentQuestion.PROGRESS_REPORT_CODE);
		
		return reports;
	}
	
	private JComponent createHintPanel()
	{	
		String htmlFormatting = "<div class='DataPanel'>";
		String text = htmlFormatting + EAM.text("Select the report that you want to run. " +
				"Miradi will then prompt you to save the report as a Rich Text Format (RTF) file. " +
				"Once you have created this file, " +
				"you can edit or print it using a word processing program such as MS Word or Open Office Writer. " +
				"<br></br><br></br>" +
				"Note that if you edit the report in your word processor, " +
				"your changes will not be maintained the next time you generate a report. " +
				"As a result, try to make the edits in the appropriate Miradi fields where possible." +
				"<br></br><br></br>" +
				"<span class='hint'>" +
				"<b>Hint:</B> If you want to export a customized table, do not use report view. " +
				"Instead, in any view, configure the table and then use the " +
				"<code class='toolbarbutton'>&lt;File/ Export&nbsp;Current&nbsp;Page&nbsp;As.../ RTF&nbsp;File&gt;</code> " +
				"menu command from within that view." +
				"</span>");
		MiradiHtmlViewer helpfulText = new MiradiHtmlViewer(getMainWindow(), new DefaultHyperlinkHandler(getMainWindow()));
		helpfulText.setTextWithoutScrollingToMakeFieldVisible(text);
		int arbitraryWidth = 500;
		int height = helpfulText.getPreferredHeight(arbitraryWidth);
		helpfulText.setPreferredSize(new Dimension(arbitraryWidth, height));
		
		OneColumnPanel hintPanel = new OneColumnPanel();
		Border cushion = BorderFactory.createEmptyBorder(0, 50, 0, 0);
		Border titleBorder = BorderFactory.createTitledBorder(EAM.text("How Reports Work"));
		hintPanel.setBorder(BorderFactory.createCompoundBorder(cushion, titleBorder));
		hintPanel.setBackground(getMainWindow().getAppPreferences().getDataPanelBackgroundColor());
		hintPanel.add(new JLabel(" "));
		hintPanel.add(helpfulText);
		return hintPanel;
	}

	private void addStandardReport(TwoColumnPanel panel, String code)
	{
		CodeList standardReportCodes = new CodeList();
		standardReportCodes.add(code);
		standardReportCodes.add(ReportTemplateContentQuestion.LEGEND_TABLE_REPORT_CODE);
		ChoiceQuestion question = getContentQuestion();
		ChoiceItem choice = question.findChoiceByCode(code);
		addStandardReport(panel, standardReportCodes, choice.getLabel());
	}

	private void addStandardReport(TwoColumnPanel panel, CodeList standardReportCodeList, String standardReportLabel)
	{
		PanelButton runButton = new PanelButton(EAM.text("Run..."));
		runButton.addActionListener(new ActionHandler(standardReportCodeList));
		
		panel.add(new PanelTitleLabel(standardReportLabel));
		panel.add(runButton);
		setBackground(AppPreferences.getDataPanelBackgroundColor());
	}
	
	private CodeList getFullReportCodeList()
	{
		CodeList fullReportCodes = getContentQuestion().getAllCodes();
		fullReportCodes.removeCode(ReportTemplateContentQuestion.TARGET_VIABILITY_VIEW_VIABILITY_TAB_DETAILS_CODE);
		
		return fullReportCodes;
	}
	
	private CodeList getSummaryCodeList()
	{
		CodeList summaryCodes = new CodeList();
		summaryCodes.add(ReportTemplateContentQuestion.SUMMARY_VIEW_PROJECT_TAB_CODE);
		summaryCodes.add(ReportTemplateContentQuestion.SUMMARY_VIEW_TEAM_TAB_CODE);
		summaryCodes.add(ReportTemplateContentQuestion.SUMMARY_VIEW_ORGANIZATION_TAB_CODE);
		summaryCodes.add(ReportTemplateContentQuestion.SUMMARY_VIEW_SCOPE_TAB_CODE);
		summaryCodes.add(ReportTemplateContentQuestion.SUMMARY_VIEW_LOCATION_TAB_CODE);
		summaryCodes.add(ReportTemplateContentQuestion.SUMMARY_VIEW_PLANNING_TAB_CODE);
		summaryCodes.add(ReportTemplateContentQuestion.SUMMARY_VIEW_TNC_TAB_CODE);
		summaryCodes.add(ReportTemplateContentQuestion.SUMMARY_VIEW_WWF_TAB_CODE);
		summaryCodes.add(ReportTemplateContentQuestion.SUMMARY_VIEW_WCS_TAB_CODE);
		summaryCodes.add(ReportTemplateContentQuestion.SUMMARY_VIEW_RARE_TAB_CODE);
		summaryCodes.add(ReportTemplateContentQuestion.SUMMARY_VIEW_FOS_TAB_CODE);
		
		return summaryCodes;
	}

	private ChoiceQuestion getContentQuestion()
	{
		return getProject().getQuestion(ReportTemplateContentQuestion.class);
	}
	
	private Project getProject()
	{
		return getMainWindow().getProject();
	}
	
	private MainWindow getMainWindow()
	{
		return mainWindow;
	}

	class ActionHandler implements ActionListener
	{
		public ActionHandler(CodeList standardReportTemplateCodeListToUse)
		{
			standardReportTemplateCodeList =  standardReportTemplateCodeListToUse;
		}

		public void actionPerformed(ActionEvent e)
		{	
			RunReportTemplateDoer.runReport(getMainWindow(), standardReportTemplateCodeList);
		}

		private CodeList standardReportTemplateCodeList;		
	}

	private MainWindow mainWindow;
}

