/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.threatmatrix;

import javax.swing.JScrollPane;

import org.conservationmeasures.eam.utils.HtmlBuilder;
import org.conservationmeasures.eam.utils.HtmlViewer;
import org.conservationmeasures.eam.utils.HyperlinkHandler;

public class ThreatRatingWizardCheckBundleStep extends ThreatRatingWizardStep implements HyperlinkHandler
{
	public ThreatRatingWizardCheckBundleStep(ThreatRatingWizardPanel wizardToUse)
	{
		super(wizardToUse);

		htmlViewer = new HtmlViewer("", this);
		JScrollPane scrollPane = new JScrollPane(htmlViewer);
		add(scrollPane);
	}

	void refresh() throws Exception
	{
		String htmlText = CheckBundleText.build();
		htmlViewer.setText(htmlText);
		invalidate();
		validate();
	}

	boolean save() throws Exception
	{
		return true;
	}

	public void linkClicked(String linkDescription)
	{
	}

	public void valueChanged(String widget, String newValue)
	{
	}

	HtmlViewer htmlViewer;

}

class CheckBundleText extends HtmlBuilder
{
	public static String build()
	{
		return font("Arial", 
			table(tableRow(
				tableCell(
						heading("Check Overall Threat Rating") + 
						paragraph("Once you have completed your ratings for all three criteria, " +
								"the software automatically calculates the " +
								anchorTag("DefineOverallThreatRating", "overall threat rating") + 
								" for this target and threat combination.  " +
								"You should check whether you agree with this overall rating.  " +
								"If it does not make sense, " +
								"go back and adjust the ratings for the specific criteria.") +
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
						paragraph(bold("Overall Threat Rating -- ") +
								"The e-AM Software calculates threat ratings " +
								"using a rule-based system for combining the scope, severity, " +
								"and irreversibility criteria.  " +
								"These procedures involve specifying rules " +
								"as to how different parameters should be combined with one another. " +
								anchorTag("ShowRules", "Click here") + 
								" for a detailed description of this system.")
					)
				))
			);

	}
}
