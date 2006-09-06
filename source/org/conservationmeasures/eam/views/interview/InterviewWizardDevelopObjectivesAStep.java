/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.interview;

import javax.swing.JScrollPane;

import org.conservationmeasures.eam.utils.HtmlBuilder;
import org.conservationmeasures.eam.views.umbrella.WizardPanel;
import org.martus.swing.HtmlViewer;
import org.martus.swing.HyperlinkHandler;

public class InterviewWizardDevelopObjectivesAStep extends InterviewWizardStep implements HyperlinkHandler
{

	public InterviewWizardDevelopObjectivesAStep(WizardPanel wizardToUse)
	{
		super(wizardToUse);

		htmlViewer = new HtmlViewer("", this);
		JScrollPane scrollPane = new JScrollPane(htmlViewer);
		add(scrollPane);
	}

	void refresh() throws Exception
	{
		String htmlText = DevelopObjectivesAText.build();
		htmlViewer.setText(htmlText);
		invalidate();
		validate();
	}

	public void linkClicked(String linkDescription)
	{
	}

	public void valueChanged(String widget, String newValue)
	{
	}

	HtmlViewer htmlViewer;

}

class DevelopObjectivesAText extends HtmlBuilder
{
	public static String build()
	{
		final String[] objectiveNames = {
			"Diver Anchor Damage</option>",
			"Illegal Shark Fishing by Mainland Boats",
			"Unsustainable Legal Fishing by Locals",
			"Increased Water Temperatures",
			"Sewage",
			"Introduced Predators (Rats)",
			"Potential Oil Spills",
		};
		
		return font("Arial", 
				wizardFrame(tableRow(
				tableCell(
						heading("Step 2.1.  Plan Your Actions") + 
						paragraph(bold("Principle 2.1 A.  Develop clear goal and objectives")) +
						paragraph(bold("Task 3. Develop Objectives")) +
						newline() +
						paragraph("An " +
			"<a href='none'><em>objective</em></a> " +
			"is a specific statement detailing the desired accomplishments, " + 
			"milestones or outcomes of a project.  To develop a good objective, " + 
			"select one of your high ranked threats:") +
						dropDown("Objective", objectiveNames) +
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
					""))
			));

	}
}
