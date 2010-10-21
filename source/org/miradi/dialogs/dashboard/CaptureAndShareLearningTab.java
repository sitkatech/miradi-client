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

public class CaptureAndShareLearningTab extends AbstractDashboardTab
{
	public CaptureAndShareLearningTab(Project projectToUse) throws Exception
	{
		super(projectToUse);
	}

	@Override
	protected String getMainDescriptionFileName()
	{
		return "dashboard/5.html";
	}

	@Override
	protected TwoColumnPanel createLeftPanel()
	{
		TwoColumnPanel leftMainPanel = new TwoColumnPanel();
		createHeaderRow(leftMainPanel, EAM.text("5. Capture and Share Learning"), "", getMainDescriptionFileName());
		createSubHeaderRow(leftMainPanel, EAM.text("5A. Document Learning"), EAM.text("Document Key Results and Lessons"), PREPARE_DATA_FOR_ANALYSIS_RIGTH_SIDE_FILENAME);
		createSubHeaderRow(leftMainPanel, EAM.text("5B. Share Learning"), EAM.text("Identify Key Audiences Develop Communications Strategy Report to Project Team and Stakeholders Develop and Share Communication Products Use Other’s Communication Products"), ANALYZE_RESULTS_RIGTH_SIDE_FILENAME);
		createSubHeaderRow(leftMainPanel, EAM.text("5C. Create a Learning Enviornment"), EAM.text("Share Feedback Formally or Informally Conduct Evaluations... Demonstrate Commitment... Provide Safe Environment... Share Successes and Failures..."), ADAPT_PROJECT_PLAN_RIGTH_SIDE_FILENAME);
		
		return leftMainPanel;
	}

	@Override
	public String getPanelDescription()
	{
		return EAM.text("Capture and Share Learning");
	}
	
	private static final String PREPARE_DATA_FOR_ANALYSIS_RIGTH_SIDE_FILENAME = "dashboard/5A.html";
	private static final String ANALYZE_RESULTS_RIGTH_SIDE_FILENAME = "dashboard/5B.html";
	private static final String ADAPT_PROJECT_PLAN_RIGTH_SIDE_FILENAME = "dashboard/5C.html";
}
