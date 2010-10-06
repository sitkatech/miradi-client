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

import java.util.HashMap;

import org.miradi.layout.TwoColumnPanel;
import org.miradi.main.EAM;
import org.miradi.objects.Dashboard;
import org.miradi.project.Project;

public class ImplementActionsAndMonitoringDashboardTab extends AbstractDashboardTab
{
	public ImplementActionsAndMonitoringDashboardTab(Project projectToUse) throws Exception
	{
		super(projectToUse);
	}

	@Override
	protected String getMainDescriptionFileName()
	{
		return "dashboard/3.html";
	}

	@Override
	protected TwoColumnPanel createLeftPanel()
	{
		TwoColumnPanel leftMainPanel = new TwoColumnPanel();
		createHeaderRow(leftMainPanel, EAM.text("3. Implement Actions and Monitoring"), "", getMainDescriptionFileName());
		addSubHeaderRow(leftMainPanel, EAM.text("3A. Develop Short Term Work Plan"), DEVELOP_SHORT_TERM_WORK_PLAN_RIGHT_SIDE_FILENAME);
		
		createDetailActivitiesTasksAndResponsiblitiesRow(leftMainPanel);
		createActivitiesCountRow(leftMainPanel);
		createActivitiesAndTasksWithAssignmentsRow(leftMainPanel);
		createDetailMethodsTasksAndResponsibilitiesRow(leftMainPanel);
		createMethodsCountRow(leftMainPanel);
		createMethodsAndTasksWithAssignmentsRow(leftMainPanel);
		
		return leftMainPanel;
	}

	private void createMethodsAndTasksWithAssignmentsRow(TwoColumnPanel leftMainPanel)
	{
		HashMap<String, String> tokenReplacementMap = new HashMap<String, String>();
		tokenReplacementMap.put("%methodsAndTasksWithAssignmentsCount", getDashboardData(Dashboard.PSEUDO_METHODS_AND_TASKS_WITH_ASSIGNMENT_COUNT));
		tokenReplacementMap.put("%methodsAndTasksCount", getDashboardData(Dashboard.PSEUDO_METHODS_AND_TASKS_COUNT));
		String rightColumnTranslatedText = EAM.substitute(EAM.text("- %methodsAndTasksWithAssignmentsCount of %methodsAndTasksCount Methods and Tasks have assignments"), tokenReplacementMap);

		createDataRow(leftMainPanel, "", rightColumnTranslatedText, DEVELOP_SHORT_TERM_WORK_PLAN_RIGHT_SIDE_FILENAME);
	}

	private void createMethodsCountRow(TwoColumnPanel leftMainPanel)
	{
		HashMap<String, String> tokenReplacementMap = new HashMap<String, String>();
		tokenReplacementMap.put("%methodsCount", getDashboardData(Dashboard.PSEUDO_METHODS_COUNT));
		String rightColumnTranslatedText = EAM.substitute(EAM.text("- %methodsCount Total methods created"), tokenReplacementMap);

		createDataRow(leftMainPanel, "", rightColumnTranslatedText, DEVELOP_SHORT_TERM_WORK_PLAN_RIGHT_SIDE_FILENAME);		
	}

	private void createDetailMethodsTasksAndResponsibilitiesRow(TwoColumnPanel leftMainPanel)
	{
		String leftColumnTranslatedText = EAM.text("Detail Methods, Tasks and Responsibilities");
		HashMap<String, String> tokenReplacementMap = new HashMap<String, String>();
		tokenReplacementMap.put("%indicatorsWithMethodsCount", getDashboardData(Dashboard.PSEUDO_INDICATORS_WITH_METHODS_COUNT));
		tokenReplacementMap.put("%indicatorsCount", getDashboardData(Dashboard.PSEUDO_INDICATORS_COUNT));
		String rightColumnTranslatedText = EAM.substitute(EAM.text("- %indicatorsWithMethodsCount of %indicatorsCount Indicators have Methods."), tokenReplacementMap);

		createDataRow(leftMainPanel, leftColumnTranslatedText, rightColumnTranslatedText, DEVELOP_SHORT_TERM_WORK_PLAN_RIGHT_SIDE_FILENAME);
	}

	private void createActivitiesAndTasksWithAssignmentsRow(TwoColumnPanel leftMainPanel)
	{
		HashMap<String, String> tokenReplacementMap = new HashMap<String, String>();
		tokenReplacementMap.put("%activitiesAndTasksCount", getDashboardData(Dashboard.PSEUDO_ACTIVITIES_AND_TASKS_COUNT));
		tokenReplacementMap.put("%activitiesAndTasksWithAssignmentsCount", getDashboardData(Dashboard.PSEUDO_ACTIVITIES_AND_TASKS_WITH_ASSIGNMENTS_COUNT));
		String rightColumnTranslatedText = EAM.substitute(EAM.text("- %activitiesAndTasksWithAssignmentsCount of %activitiesAndTasksCount Activities and tasks have assignments"), tokenReplacementMap);

		createDataRow(leftMainPanel, "", rightColumnTranslatedText, DEVELOP_SHORT_TERM_WORK_PLAN_RIGHT_SIDE_FILENAME);
	}

	private void createActivitiesCountRow(TwoColumnPanel leftMainPanel)
	{
		HashMap<String, String> tokenReplacementMap = new HashMap<String, String>();
		tokenReplacementMap.put("%activitiesCount", getDashboardData(Dashboard.PSEUDO_ACTIVITIES_COUNT));
		String rightColumnTranslatedText = EAM.substitute(EAM.text("- %activitiesCount Total activities created"), tokenReplacementMap);

		createDataRow(leftMainPanel, "", rightColumnTranslatedText, DEVELOP_SHORT_TERM_WORK_PLAN_RIGHT_SIDE_FILENAME);
	}

	private void createDetailActivitiesTasksAndResponsiblitiesRow(TwoColumnPanel leftMainPanel)
	{
		String leftColumnTranslatedText = EAM.text("Detail Activities Tasks and Responsiblities");
		HashMap<String, String> tokenReplacementMap = new HashMap<String, String>();
		tokenReplacementMap.put("%strategiesWithActivitiesCount", getDashboardData(Dashboard.PSEUDO_STRATEGIES_WITH_ACTIVITIES_COUNT));
		tokenReplacementMap.put("%strategiesCount", getDashboardData(Dashboard.PSEUDO_STRATEGY_COUNT));
		String rightColumnTranslatedText = EAM.substitute(EAM.text("- %strategiesWithActivitiesCount of %strategiesCount Strategies have at least 1 Activity. "), tokenReplacementMap);

		createDataRow(leftMainPanel, leftColumnTranslatedText, rightColumnTranslatedText, DEVELOP_SHORT_TERM_WORK_PLAN_RIGHT_SIDE_FILENAME);
	}

	@Override
	public String getPanelDescription()
	{
		return EAM.text("Implement Actions and Monitoring");
	}
	
	private static final String DEVELOP_SHORT_TERM_WORK_PLAN_RIGHT_SIDE_FILENAME = "dashboard/3A.html";
}
