/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.summary;

import org.conservationmeasures.eam.utils.HtmlBuilder;
import org.conservationmeasures.eam.views.umbrella.WizardPanel;

public class InterviewWizardDefineScopeAStep extends InterviewWizardStep
{

	public InterviewWizardDefineScopeAStep(WizardPanel wizardToUse)
	{
		super(wizardToUse);
	}

	public String getText()
	{
		return DefineScopeAText.build();
	}

}

class DefineScopeAText extends HtmlBuilder
{
	public static String build()
	{
		return font("Arial", 
				wizardFrame(tableRow(
				tableCell(
						heading("Step 1.  Conceptualize") + 
						paragraph(bold("Principle 1A.  Be clear and specific about the issue or problem")) +
						paragraph(bold("Task 2. Define the scope of the area or theme")) +
						newline() +
						paragraph("Most conservation projects will focus on a defined geographic " +
			definition("ProjectArea", "project area", "") + 
			"that contains the biodiversity that is of interest.  " + 
			"In a few cases, a conservation project may not focus on biodiversity in a specific area, " + 
			"but instead will have a " +
			definition("Theme", "theme", "") +
			"that focuses on a population of wide-ranging animals, " + 
			"such as migratory birds.") +
						paragraph("Describe in a few sentences the project area or theme for your project:") +
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
