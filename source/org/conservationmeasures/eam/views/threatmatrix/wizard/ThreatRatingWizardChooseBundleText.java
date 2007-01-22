/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.threatmatrix.wizard;

import org.conservationmeasures.eam.utils.HtmlBuilder;

public class ThreatRatingWizardChooseBundleText extends HtmlBuilder
{
	public ThreatRatingWizardChooseBundleText(String[] threatNames, String selectedThreat, 
			String[] targetNames, String selectedTarget)
	{
		text = 
			font("Arial", 
				wizardFrame(tableRow(
					tableCell(
						heading("Select Target and Threat to Work On") + 
						indent("Pick one of your targets identified in Step 1.2 and a threat that affects it.") +
						indent(paragraph(bold("Which target do you want to work on?")) +
								indent(dropDown("Target", targetNames, selectedTarget)) +
								paragraph(bold("Which threat do you want to work on?")) +
								indent(dropDown("Threat", threatNames, selectedThreat))
								) +
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
					hintTableCell(
						smallParagraph(bold("Hint:  ") +
								"If this is your first time, you might want to " +
								"start with one of your simpler targets and threats."
								)
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