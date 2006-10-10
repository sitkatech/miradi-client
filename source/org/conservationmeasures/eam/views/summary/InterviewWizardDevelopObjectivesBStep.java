/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.summary;

import org.conservationmeasures.eam.utils.HtmlBuilder;
import org.conservationmeasures.eam.views.umbrella.WizardPanel;

public class InterviewWizardDevelopObjectivesBStep extends InterviewWizardStep
{

	public InterviewWizardDevelopObjectivesBStep(WizardPanel wizardToUse)
	{
		super(wizardToUse);
	}
	
	public String getText()
	{
		return DevelopObjectivesBText.build();
	}

}

class DevelopObjectivesBText extends HtmlBuilder
{
	public static String build()
	{
		return font("Arial", 
				wizardFrame(tableRow(
				tableCell(
						heading("Step 2.1.  Plan Your Actions") + 
						paragraph(bold("Principle 2.1 A.  Develop clear goal and objectives")) +
						paragraph(bold("Task 3. Develop Objectives")) +
						newline() +
						paragraph("The threat you have selected to address is: 'Cutting Trees'") +
						paragraph("Write a draft " +
			definition("Objective", "objective", "") +
			" about how you hope to change this factor.  " +
			"Then modify this draft objective to make sure it meets the criteria of being " +
			definition("ImpactOriented", "impact oriented", "") +
			", " +
			definition("Measureable", "measureable", "") +
			", " +
			definition("Specific", "specific", "") +
			", " +
			definition("Realistic", "realistic", "") +
			", " +
			definition("TimeLimited", "time-limited", "") +
			", and " +
			definition("Achievable", "achievable", "") +
			".") +
			editor(8, 40) +
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
