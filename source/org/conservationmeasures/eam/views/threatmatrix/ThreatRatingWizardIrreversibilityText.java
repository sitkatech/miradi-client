/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.threatmatrix;

import org.conservationmeasures.eam.utils.HtmlBuilder;

public class ThreatRatingWizardIrreversibilityText extends HtmlBuilder
{
	public ThreatRatingWizardIrreversibilityText(String[] optionLabels, String currentValue)
	{
		
		text = font("Arial", table(
			tableRow(
				tableCell(
					heading("Rate the Irreversibility of the Selected Threat") +
					indent(paragraph("Using the scale shown on the right, rate the " +
							definition("Definition:Irreversibility", "irreversibility", 
									"Irreversibility is...") + 
							" of the threat is on the target. " +
							"You can click on the blue triangle to record any comments or assumptions.") +
							paragraph(bold("What is the irreversibility of the threat on the target?")) +
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
					paragraph(bold("Irreversibility - ") + "The degree to which the effects of a threat " +
							"can be restored.") +
					indent(paragraph(bold("Very High: ") + "The effects of the threat are not reversible " +
							"	(e.g., wetlands converted to a shopping center).") +
							paragraph(bold("High: ") + "The effects of the threat are technically reversible, " +
									"but not practically affordable (e.g., wetland converted to agriculture).") +
							paragraph(bold("Medium: ") + "The effects of the threat are reversible " +
									"with a reasonable commitment of resources " +
									"(e.g., ditching and draining of wetland).") +
							paragraph(bold("Low: ") + "The effects of the threat are easily reversible " +
									"at relatively low cost (e.g., off-road vehicles trespassing in wetland).")
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
