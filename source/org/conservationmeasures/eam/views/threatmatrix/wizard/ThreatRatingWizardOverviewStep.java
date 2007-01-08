/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.threatmatrix.wizard;

import org.conservationmeasures.eam.commands.CommandSwitchView;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.utils.HtmlBuilder;
import org.conservationmeasures.eam.views.diagram.DiagramView;

public class ThreatRatingWizardOverviewStep extends ThreatRatingWizardStep
{
	public ThreatRatingWizardOverviewStep(ThreatRatingWizardPanel wizardToUse) 
	{
		super(wizardToUse);
	}
	
	public String getText()
	{
		return OverviewText.build();
	}

	public boolean save() throws Exception
	{
		return true;
	}

	public void linkClicked(String linkDescription)
	{
		if(linkDescription.equals("View:Diagram"))
		{
			CommandSwitchView cmd = new CommandSwitchView(DiagramView.getViewName());
			Project project = ((ThreatRatingWizardPanel)getWizard()).getView().getProject();
			try
			{
				project.executeCommand(cmd);
			}
			catch (CommandFailedException e)
			{
				EAM.logException(e);
			}
		}
		else 
			super.linkClicked(linkDescription);
	}

}

class OverviewText extends HtmlBuilder
{
	public static String build()
	{
		return font("Arial", 
			wizardFrame(tableRow(
				tableCell(
						heading("Threat Rating") + 
						paragraph("The table below contains a summary of your threat ratings.  " +
							"Threat/Target combinations that have been rated " +
							"should appear colored in.") +
						paragraph("If there is a threat or target that is missing, " +
							"return to the " + 
							anchorTag("View:Diagram", "diagram view") + " to add it. " +
							"If a threat is not linked to a specific target, " +
							"you can create this by clicking on a blank cell in the table (note that this also creates a corresponding link in the diagram).") +
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
						smallHeading("Guide to the Threat Rating Summary Table") + 
						smallParagraph(("The " + bold("far left hand column ") +
								"is a listing of all threats and the " +
								bold("top row ") +
								"is a listing of all targets for your project. ") + 
						smallParagraph("The " +
								"cells of the table " +
								"show the overall threat ratings for each threat " +
								"on each target that it affects.  " +
								"You can click on a cell to bring up the specific criteria " +
								"that go into the overall threat ratings.") + 
						smallParagraph("Finally, the table contains three types of summary ratings:") +
						list(
							listItem(smallParagraph("The " +
									bold("column on the far right ") +
									"contains a summary of the overall rating of each threat across all targets.")) +
							listItem(smallParagraph("The " +
									bold("row on the bottom ") +
									"contains a summary of the overall threat rating for each target.")) + 
							listItem(smallParagraph("Finally, the " +
									bold("lowest right hand cell ") +
									"contains a summary of the threat rating for your entire project."))
							) +
						smallParagraph("These summary ratings are not entered directly, " +
								"but are calculated as you rate the specific effects " +
								"of threats on targets in the cells of the table.") +
					""))
				))
			);

	}
}
