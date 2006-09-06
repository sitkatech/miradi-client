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

public class InterviewWizardDefineScopeBStep extends InterviewWizardStep implements HyperlinkHandler
{

	public InterviewWizardDefineScopeBStep(WizardPanel wizardToUse)
	{
		super(wizardToUse);

		htmlViewer = new HtmlViewer("", this);
		JScrollPane scrollPane = new JScrollPane(htmlViewer);
		add(scrollPane);
	}

	void refresh() throws Exception
	{
		String htmlText = DefineScopeBText.build();
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

class DefineScopeBText extends HtmlBuilder
{
	public static String build()
	{
		return font("Arial", 
			table(tableRow(
				tableCell(
						heading("Step 1.  Conceptualize") + 
						paragraph(bold("Principle 1A.  Be clear and specific about the issue or problem")) +
						paragraph(bold("Task 2. Define the scope of the area or theme")) +
						newline() +
						paragraph("Outline your " +
								definition("ProjectArea", "project area", "") +
								" on your project map.") +
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
