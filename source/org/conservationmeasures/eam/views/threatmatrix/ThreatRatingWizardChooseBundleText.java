/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */

package org.conservationmeasures.eam.views.threatmatrix;

import org.conservationmeasures.eam.utils.HtmlBuilder;

public class ThreatRatingWizardChooseBundleText extends HtmlBuilder
{
	public ThreatRatingWizardChooseBundleText(String[] threatNames, String selectedThreat, 
			String[] targetNames, String selectedTarget)
	{
		text = 
			font("Arial", 
				table(tableRow(
					tableCell(
						heading("Select Target and Threat to Work On") + 
						indent("Pick one of your targets identified in Step 1.2 and a threat that affects it.") +
						indent(table(
							tableRow(
									tableCell(bold("Which target do you want to work on?")) +
									tableCell("&nbsp;") +
									tableCell(bold("Which threat do you want to work on?"))
									) +
							tableRow(
									tableCell(dropDown("Target", targetNames, selectedTarget)) +
									tableCell("&nbsp;") +
									tableCell(dropDown("Threat", threatNames, selectedThreat))
									)
							)) +
						newline() +
						indent(table(
							tableRow(
								tableCell(button("Back", "&lt; Previous")) +
								tableCell("&nbsp;") +
								tableCell(button("Next", "Do Threat Rating &gt;")) 
								) +
							tableRow(
								tableCell("&nbsp;") +
								tableCell(button("Done", "Done with Threat Ratings")) + 
								tableCell("&nbsp;")
							)
							)) + 
						newline()
					) +
					tableCell(
						paragraph(bold("Hint:  ") +
								"If this is your first time, you might want to " +
								"start with one of your simpler targets and threats.")
					)
				))
			)
		;
	}
	
	public String getText()
	{
		return text;
	}
	
	String text;
}