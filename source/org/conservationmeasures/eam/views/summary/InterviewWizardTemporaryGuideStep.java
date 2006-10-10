/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.summary;

import org.conservationmeasures.eam.utils.HtmlBuilder;
import org.conservationmeasures.eam.views.umbrella.WizardPanel;

public class InterviewWizardTemporaryGuideStep extends InterviewWizardStep
{
	public InterviewWizardTemporaryGuideStep(WizardPanel wizardToUse)
	{
		super(wizardToUse);
	}
	
	public String getText()
	{
		return TemporaryGuide.build();
	}

}

class TemporaryGuide extends HtmlBuilder
{
	public static String build()
	{
		return font("Arial", 
			wizardFrame(tableRow(
				tableCell(
						heading("Interview (demo screen shots only)") + 
						paragraph("The following interview screens will give you an idea of how the interview " +
			"will, in future versions, walk you through developing your project.") +
						paragraph(bold("To continue with the Interview demo, click on the 'Next' button.")) +
						newline() +
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
						smallParagraph(("" + 
			"If you want to start working with a project, " +
			"switch to the Diagram View and start " +
			"building a conceptual model of your project. ") + 
					""))
			))
		);

	}
}
