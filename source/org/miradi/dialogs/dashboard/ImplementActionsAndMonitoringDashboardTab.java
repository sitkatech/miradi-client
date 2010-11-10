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
import org.miradi.main.EAM;
import org.miradi.main.MainWindow;
import org.miradi.questions.ImplementActionsAndMonitoringQuestion;
import org.miradi.wizard.WizardManager;

public class ImplementActionsAndMonitoringDashboardTab extends LeftSideRightSideSplitterContainerTab
{
	private ImplementActionsAndMonitoringDashboardTab(MainWindow mainWindowToUse, OneFieldObjectDataInputPanel oneFieldObjectDataInputPanel) throws Exception
	{
		super(mainWindowToUse, oneFieldObjectDataInputPanel);
	}
	
	public static ImplementActionsAndMonitoringDashboardTab createTab(MainWindow mainWindowToUse) throws Exception
	{
		return new ImplementActionsAndMonitoringDashboardTab(mainWindowToUse, createLeftPanel(mainWindowToUse));
	}

	@Override
	protected AbstractRowDescriptionProvider getMainDescriptionFileName() throws Exception
	{
		return new HtmlResourceRowDescriptionProvider("dashboard/3.html");
	}

	@Override
	public String getPanelDescription()
	{
		return EAM.text("Implement Actions and Monitoring");
	}

	private static OneFieldObjectDataInputPanel createLeftPanel(MainWindow mainWindowToUse) throws Exception
	{
		WizardManager wizardManager = mainWindowToUse.getWizardManager();
		ImplementActionsAndMonitoringQuestion question = new ImplementActionsAndMonitoringQuestion(mainWindowToUse.getProject(), wizardManager);
		QuestionEditorWithHierarchichalRows component = new QuestionEditorWithHierarchichalRows(mainWindowToUse, question);
		
		return new OneFieldObjectDataInputPanel(mainWindowToUse.getProject(), component);
	}
}
