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

import org.miradi.layout.TwoColumnPanel;
import org.miradi.main.EAM;
import org.miradi.project.Project;

public class AnalyzeAdaptAndUseTab extends AbstractDashboardTab
{
	public AnalyzeAdaptAndUseTab(Project projectToUse) throws Exception
	{
		super(projectToUse);
	}

	@Override
	protected String getMainDescriptionFileName()
	{
		return "dashboard/4.html";
	}

	@Override
	protected TwoColumnPanel createLeftPanel()
	{
		TwoColumnPanel leftMainPanel = new TwoColumnPanel();
		createHeaderRow(leftMainPanel, EAM.text("4. Analyze, Use and Adapt"), "", getMainDescriptionFileName());
		createSubHeaderRow(leftMainPanel, EAM.text("4A. Prepare Data for Analysis"), EAM.text("Develop Systems for Recording, Storing, Processing and Backing Up Project Data"), PREPARE_DATA_FOR_ANALYSIS_RIGTH_SIDE_FILENAME);
		createSubHeaderRow(leftMainPanel, EAM.text("4B. Analyze Results"), EAM.text("Analyze Project Results and Assumptions Analyze Operational and Financial Data Document Discussions and Decisions"), ANALYZE_RESULTS_RIGTH_SIDE_FILENAME);
		createSubHeaderRow(leftMainPanel, EAM.text("4C. Adapt Project Plan"), EAM.text("Revise Project Plan: Strategic Monitoring Operational Work Plan"), ADAPT_PROJECT_PLAN_RIGTH_SIDE_FILENAME);
		
		return leftMainPanel;
	}

	@Override
	public String getPanelDescription()
	{
		return EAM.text("Analyze, Use and Adapt");
	}
	
	private static final String PREPARE_DATA_FOR_ANALYSIS_RIGTH_SIDE_FILENAME = "dashboard/4A.html";
	private static final String ANALYZE_RESULTS_RIGTH_SIDE_FILENAME = "dashboard/4B.html";
	private static final String ADAPT_PROJECT_PLAN_RIGTH_SIDE_FILENAME = "dashboard/4C.html";
}
