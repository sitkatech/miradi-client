/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.interview;

import org.conservationmeasures.eam.utils.HtmlBuilder;
import org.conservationmeasures.eam.views.umbrella.WizardPanel;

public class InterviewWizardDevelopObjectivesAStep extends InterviewWizardStep
{

	public InterviewWizardDevelopObjectivesAStep(WizardPanel wizardToUse)
	{
		super(wizardToUse);
	}
	
	public String getText()
	{
		return DevelopObjectivesAText.build();
	}

}

class DevelopObjectivesAText extends HtmlBuilder
{
	public static String build()
	{
		final String[] objectiveNames = {
			"Diver Anchor Damage</option>",
			"Illegal Shark Fishing by Mainland Boats",
			"Unsustainable Legal Fishing by Locals",
			"Increased Water Temperatures",
			"Sewage",
			"Introduced Predators (Rats)",
			"Potential Oil Spills",
		};
		
		return font("Arial", 
				wizardFrame(tableRow(
				tableCell(
						heading("Step 2.1.  Plan Your Actions") + 
						paragraph(bold("Principle 2.1 A.  Develop clear goal and objectives")) +
						paragraph(bold("Task 3. Develop Objectives")) +
						newline() +
						paragraph("An " +
			"<a href='none'><em>objective</em></a> " +
			"is a specific statement detailing the desired accomplishments, " + 
			"milestones or outcomes of a project.  To develop a good objective, " + 
			"select one of your high ranked threats:") +
						dropDown("Objective", objectiveNames) +
						indent(table(
							tableRow(
								tableCell(button("Back", "&lt; Previous")) +
								tableCell("&nbsp;") +
								tableCell(button("Next", "Next &gt;")) 
								)
							)) + 
						newline() +
						"") +
				tableCell(
						smallHeading("Navigation Hints for the Interview") + 
					""))
			));

	}
}
