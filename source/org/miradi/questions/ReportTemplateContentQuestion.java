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
package org.miradi.questions;

public class ReportTemplateContentQuestion extends StaticChoiceQuestion
{
	public ReportTemplateContentQuestion()
	{
		super(getContentChoices());
	}

	static ChoiceItem[] getContentChoices()
	{
		return new ChoiceItem[] {
				new ChoiceItem("SummaryViewProjectTab", "Project"),
				new ChoiceItem("SummaryViewTeamTab", "Team"),
				new ChoiceItem("SummaryViewOrganizationTab", "Oranization"),
				new ChoiceItem("SummaryViewScopeTab", "Scope"),
				new ChoiceItem("SummaryViewLocationTab", "Location"),
				new ChoiceItem("SummaryViewPlanningTab", "Planning"),
				new ChoiceItem("SummaryViewTncTab", "TNC"),
				new ChoiceItem("SummaryViewWwfTab", "WWF"),
				new ChoiceItem("SummaryViewWcsTab", "WCS"),
				new ChoiceItem("SummaryViewRareTab", "RARE"),
				new ChoiceItem("SummaryViewFosTab", "FOS"),
				
				new ChoiceItem("DiagramView", "Conceptual Models"),
				new ChoiceItem("DiagramView", "Results Chains"),
				
				new ChoiceItem("ViabilityView", "Viability"),
				
				new ChoiceItem("ThreatRatingsView", "Simple"),
				new ChoiceItem("ThreatRatingsView", "Stress Based"),
				
				new ChoiceItem("PlanningView", "Planning"),
				new ChoiceItem("PlanningView", "Resources"),
				new ChoiceItem("PlanningView", "Accounting Codes"),
				new ChoiceItem("PlanningView", "Funding Source"),
		};
	}
}
