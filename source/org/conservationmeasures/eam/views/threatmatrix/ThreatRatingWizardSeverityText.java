/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.threatmatrix;

import org.conservationmeasures.eam.utils.HtmlBuilder;

public class ThreatRatingWizardSeverityText extends HtmlBuilder
{
	public ThreatRatingWizardSeverityText(String[] optionLabels, String currentValue)
	{
		
		text = font("Arial", table(
			tableRow(
				tableCell(
					heading("Rate the Severity of the Selected Threat") +
					indent(paragraph("Using the scale shown on the right, rate the " + 
							definition("Definition:Severity", "severity", "Severity is...") + 
							" of the threat on the target.  " +
							"You can click on the blue triangle to record any comments or assumptions.") +
							paragraph(bold("What is the severity of the threat on the target?")) +
					dropDown("value", optionLabels, currentValue) +
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
				) + 
				tableCell(
					smallParagraph(bold("Severity - ") + "The level of damage to the conservation target " +
						"that can reasonably be expected within ten years under current circumstances " +
						"(i.e., given the continuation of the existing situation).") +
					indent(smallParagraph(bold("Very High: ") + "The threat is likely to " +
							"destroy or eliminate the conservation target " +
							"over some portion of the target's occurrence at the site.") +
							smallParagraph(bold("High: ") + "The threat is likely to " +
									"seriously degrade the conservation target " +
									"over some portion of the target's occurrence at the site.") +
							smallParagraph(bold("Medium: ") + "The threat is likely to " +
									"moderately degrade the conservation target " +
									"over some portion of the target's occurrence at the site.") +
							smallParagraph(bold("Low: ") + "The threat is likely to " +
									"only slightly impair the conservation target " +
									"over some portion of the target's occurrence at the site.")
					)
				)
			)) 

		);
	}
	
	public String getText()
	{
		return text;
	}
	
	String text;
}
