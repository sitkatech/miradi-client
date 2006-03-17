/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */

package org.conservationmeasures.eam.views.threatmatrix;

import org.conservationmeasures.eam.utils.HtmlBuilder;

public class ThreatRatingWizardWelcomeText extends HtmlBuilder
{
	public ThreatRatingWizardWelcomeText(String[] threatNames, String[] targetNames)
	{
		text = 
			font("Arial", 
				heading("Select One Target and Threat to Work On") + 
				indent("Pick on of your targets identified in Step 1.2 " +
					"and a threat that affects it. " +
					"You might want to start with one of your simpler targets or threats") +
				indent(table(
					tableRow(
							tableCell(bold("Which of the following targets " +
									"do you want to start with?")) +
							tableCell("&nbsp;") +
							tableCell(bold("Which of the following threats " +
									"do you want to start with?"))
							) +
					tableRow(
							tableCell(dropDown("Target", targetNames)) +
							tableCell("&nbsp;") +
							tableCell(dropDown("Threat", threatNames))
							)
					)) +
				newline() +
				indent(table(
					tableRow(
						tableCell(button("Back", "&lt; Previous")) +
						tableCell("&nbsp;") +
						tableCell(button("Next", "Next &gt;")) 
						)
					)) + 
				newline()
			)
		;
	}
	
	public String getText()
	{
		return text;
	}
	
	String text;
}