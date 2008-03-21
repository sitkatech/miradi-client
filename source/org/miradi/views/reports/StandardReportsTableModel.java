/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.views.reports;

import org.miradi.main.EAM;

public class StandardReportsTableModel extends ReportSelectionTableModel
{
	@Override
	public String getColumnName(int column)
	{
		return EAM.text("Standard Reports");
	}
	
	protected Report[] getAvailableReports()
	{
		return new Report[] {
				new Report("Full Project Report", "/reports/FullProjectReport.jasper"),
				new Report("  Project Summary Report", "/reports/ProjectSummaryParentReport.jasper"),
				new Report("  Conceptual Model Report", "/reports/AllConceptualModelsReport.jasper"),
				new Report("  Results Chains Report", "/reports/AllResultsChainsReport.jasper"),
				new Report("  Threat Rating Report", "/reports/ThreatRatingTableReport.jasper"),
				new Report("  Target Viability Report", "/reports/TargetViabilityTreeTable.jasper"),
				new Report("  Strategic Plan Report", "/reports/StrategicPlanReport.jasper"),
				new Report("  Monitoring Plan Report", "/reports/MonitoringPlanReport.jasper"),
				new Report("  Work Plan Report", "/reports/WorkPlanReport.jasper"),
		};
	}
	

}
