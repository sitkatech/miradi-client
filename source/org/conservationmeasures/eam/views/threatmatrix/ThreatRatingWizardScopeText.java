/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.threatmatrix;

import org.conservationmeasures.eam.utils.HtmlBuilder;

public class ThreatRatingWizardScopeText extends HtmlBuilder
{
	public ThreatRatingWizardScopeText(String[] optionLabels, String currentValue)
	{
		
		text = font("Arial", table(
			tableRow(
				tableCell(
					heading("Rate the Scope of the Selected Threat") +
					indent(paragraph("Using the scale shown on the right, rate the " + 
							definition("Definition:Scope", "scope", "Scope is...") + 
							" of the threat on the target.  " +
							"You can click on the blue triangle to record any comments or assumptions.") +
							paragraph(bold("What is the scope of the threat on the target?")) +
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
					paragraph(bold("Scope - ") + "Most commonly defined spatially as " +
							"the geographic scope of impact on the conservation target " +
							"at the site that can reasonably be expected " +
							"within ten years under current circumstances " +
							"(i.e., given the continuation of the existing situation).") +
					indent(paragraph(bold("Very High: ") + "The threat is likely to be " +
								"very widespread or pervasive in its scope, " +
								"and affect the conservation target " +
								"throughout the target's occurrences at the site.") +
							paragraph(bold("High: ") + "The threat is likely to be " +
									"widespread in its scope, " +
									"and affect the conservation target " +
									"at many of its locations at the site.") +
							paragraph(bold("Medium: ") + "The threat is likely to be " +
									"localized in its scope, " +
									"and affect the conservation target " +
									"at some of the target's locations at the site.") +
							paragraph(bold("Low: ") + "The threat is likely to be " +
									"very localized in its scope, " +
									"and affect the conservation target " +
									"at a limited portion of the target's location at the site.")
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
