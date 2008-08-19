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
package org.miradi.dialogs.reportTemplate;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.miradi.dialogs.fieldComponents.PanelButton;
import org.miradi.dialogs.fieldComponents.PanelTitleLabel;
import org.miradi.layout.TwoColumnPanel;
import org.miradi.main.AppPreferences;
import org.miradi.main.EAM;
import org.miradi.main.MainWindow;
import org.miradi.project.Project;
import org.miradi.questions.ChoiceItem;
import org.miradi.questions.ChoiceQuestion;
import org.miradi.questions.ReportTemplateContentQuestion;
import org.miradi.utils.CodeList;
import org.miradi.views.reports.doers.RunReportTemplateDoer;

public class StandardReportPanel extends TwoColumnPanel
{
	public StandardReportPanel(MainWindow mainWindowToUse)
	{
		mainWindow = mainWindowToUse;
		createPanel();
	}

	private void createPanel()
	{
		addStandardReport(ReportTemplateContentQuestion.PLANNING_VIEW_STRATEGIC_PLAN_CODE);
		addStandardReport(ReportTemplateContentQuestion.PLANNING_VIEW_MONITORING_PLAN_CODE);	
		addStandardReport(ReportTemplateContentQuestion.PLANNING_VIEW_WORK_PLAN_CODE);
	}

	private void addStandardReport(String code)
	{
		ChoiceQuestion question = getProject().getQuestion(ReportTemplateContentQuestion.class);
		ChoiceItem choice = question.findChoiceByCode(code);
		PanelButton runButton = new PanelButton(EAM.text("Run..."));
		runButton.addActionListener(new ActionHandler(choice));
		
		add(new PanelTitleLabel(choice.getLabel()));
		add(runButton);
		setBackground(AppPreferences.getDataPanelBackgroundColor());
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
		public ActionHandler(ChoiceItem reportTemplateChoice)
		{
			standardReportTemplateCodeList = new CodeList();
			standardReportTemplateCodeList.add(reportTemplateChoice.getCode());
		}

		public void actionPerformed(ActionEvent e)
		{	
			RunReportTemplateDoer.runReport(getMainWindow(), standardReportTemplateCodeList);
		}

		private CodeList standardReportTemplateCodeList;		
	}

	private MainWindow mainWindow;
}

