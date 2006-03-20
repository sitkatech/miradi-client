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

public class ThreatRatingWizardOverviewStep extends ThreatRatingWizardStep implements HyperlinkHandler
{
	public ThreatRatingWizardOverviewStep(ThreatRatingWizardPanel wizardToUse) 
	{
		super(wizardToUse);
		
		htmlViewer = new HtmlViewer("", this);
		JScrollPane scrollPane = new JScrollPane(htmlViewer);
		add(scrollPane);
	}

	void refresh() throws Exception
	{
		String htmlText = OverviewText.build();
//		String htmlText = new ThreatRatingWizardWelcomeText(new String[0], null, new String[0], null).getText();
		htmlViewer.setText(htmlText);
		invalidate();
		validate();
	}

	boolean save() throws Exception
	{
		return true;
	}

	HtmlViewer htmlViewer;

	public void linkClicked(String linkDescription)
	{
	}

	public void valueChanged(String widget, String newValue)
	{
	}
}

class OverviewText extends HtmlBuilder
{
	public static String build()
	{
		return font("Arial", 
			table(tableRow(
				tableCell(
						heading("Threat Rating") + 
						paragraph("The table below contains a summary of your threat ratings.  " +
							"Threat/Target combinations that have been rated " +
							"should appear colored in.") +
						paragraph("If there is a threat or target that is missing, " +
							"return to the " + 
							anchorTag("View:Diagram", "diagram view") + " to add it. " +
							"If a threat is not linked to a specific target, " +
							"you can create this link in the diagram view as well.") +
						paragraph(bold("Click next to continue with the threat rating process.")) +
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
						heading("Guide to the Threat Rating Summary Table") + 
						paragraph("The " + bold("far left hand column ") +
								"is a listing of all threats and the " +
								bold("top row ") +
								"is a listing of all targets for your project. ") + 
						paragraph("The " +
								"cells of the table " +
								"show the overall threat ratings for each threat " +
								"on each target that it affects.  " +
								"You can click on a cell to bring up the specific criteria " +
								"that go into the overall threat ratings.") + 
						paragraph("Finally, the table contains three types of summary ratings:") +
						list(
							listItem("The " +
									bold("column on the far right ") +
									"contains a summary of the overall rating of each threat across all targets.") +
							listItem("The " +
									bold("row on the bottom ") +
									"contains a summary of the overall threat rating for each target.") + 
							listItem("Finally, the " +
									bold("lowest right hand cell ") +
									"contains a summary of the threat rating for your entire project.")
							) +
						paragraph("These summary ratings are not entered directly, " +
								"but are calculated as you rate the specific effects " +
								"of threats on targets in the cells of the table.") +
					"")
				))
			);

	}
}
