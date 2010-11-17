/* 
Copyright 2005-2010, Foundations of Success, Bethesda, Maryland 
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

package org.miradi.dialogs.dashboard;

import org.miradi.dialogfields.QuestionEditorWithHierarchichalRows;
import org.miradi.dialogs.base.OneFieldObjectDataInputPanel;
import org.miradi.main.MainWindow;
import org.miradi.questions.OpenStandardsPlanActionsAndMonitoringQuestion;
import org.miradi.wizard.WizardManager;

public class PlanActionsAndMonitoringTab extends PanelWithDescriptionPanel
{
	private PlanActionsAndMonitoringTab(MainWindow mainWindowToUse, OneFieldObjectDataInputPanel oneFieldObjectDataInputPanel) throws Exception
	{
		super(mainWindowToUse, oneFieldObjectDataInputPanel);
	}
	
	public static PlanActionsAndMonitoringTab createTab(MainWindow mainWindowToUse) throws Exception
	{
		return new PlanActionsAndMonitoringTab(mainWindowToUse, createLeftPanel(mainWindowToUse));
	}

	@Override
	protected AbstractLongDescriptionProvider getDefaultDescriptionProvider() throws Exception
	{
		return new HtmlResourceLongDescriptionProvider(OpenStandardsPlanActionsAndMonitoringQuestion.getHeaderDescriptionFileName());
	}
	
	@Override
	public String getPanelDescription()
	{
		return OpenStandardsPlanActionsAndMonitoringQuestion.getHeaderLabel();
	}

	private static OneFieldObjectDataInputPanel createLeftPanel(MainWindow mainWindowToUse) throws Exception
	{
		WizardManager wizardManager = mainWindowToUse.getWizardManager();
		OpenStandardsPlanActionsAndMonitoringQuestion question = new OpenStandardsPlanActionsAndMonitoringQuestion(mainWindowToUse.getProject(), wizardManager);
		QuestionEditorWithHierarchichalRows component = new QuestionEditorWithHierarchichalRows(mainWindowToUse, question);
		
		return new OneFieldObjectDataInputPanel(mainWindowToUse.getProject(), component);
	}
}

