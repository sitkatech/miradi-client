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
				new Report(" Project Summary Report", "/reports/ProjectSummary.jasper"),
				new Report(" Conceptual Model Report", "/reports/AllConceptualModelsReport.jasper"),
				new Report(" Results Chains Report", "/reports/AllResultsChainsReport.jasper"),
				new Report(" Threat Rating Report", "/reports/ThreatRatingTableReport.jasper"),
				new Report(" Target Viability Report", "/reports/TargetViabilityTreeTable.jasper"),
				new Report(" Strategic Plan Report", "/reports/StrategicPlanReport.jasper"),
				new Report(" Monitoring Plan Report", "/reports/MonitoringPlanReport.jasper"),
				new Report(" Work Plan Report", "/reports/WorkPlanReport.jasper"),
				new Report("  TNC Summary Report", "/reports/TncSummaryReport.jasper"),
				new Report("  WWF Summary Report", "/reports/WwfSummaryReport.jasper"),
				new Report("  WCS Summary Report", "/reports/WcsSummaryReport.jasper"),
				new Report("  RARE Summary Report", "/reports/RareSummaryReport.jasper"),
				new Report("  FOS Summary Report", "/reports/FosSummaryReport.jasper"),
				new Report("  Other Org  Summary Report", "/reports/OtherOrgSummaryReport.jasper"),
		};
	}
	

}
