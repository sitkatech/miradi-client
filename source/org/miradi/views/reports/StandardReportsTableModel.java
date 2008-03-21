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
				new Report("  Threat Rating Report", "/reports/ThreatRatingTableReportParentReport.jasper"),
				new Report("  Target Viability Report", "/reports/TargetViabilityTreeTableParentReport.jasper"),
				new Report("  Strategic Plan Report", "/reports/StrategicPlanReportParentReport.jasper"),
				new Report("  Monitoring Plan Report", "/reports/MonitoringPlanReportParentReport.jasper"),
				new Report("  Work Plan Report", "/reports/WorkPlanParentReport.jasper"),
		};
	}
	

}
