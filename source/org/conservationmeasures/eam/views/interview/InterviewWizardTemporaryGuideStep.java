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

public class InterviewWizardTemporaryGuideStep extends InterviewWizardStep implements HyperlinkHandler
{
	public InterviewWizardTemporaryGuideStep(WizardPanel wizardToUse)
	{
		super(wizardToUse);

		htmlViewer = new HtmlViewer("", this);
		JScrollPane scrollPane = new JScrollPane(htmlViewer);
		add(scrollPane);
	}

	void refresh() throws Exception
	{
		String htmlText = TemporaryGuide.build();
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

class TemporaryGuide extends HtmlBuilder
{
	public static String build()
	{
		return font("Arial", 
			wizardFrame(tableRow(
				tableCell(
						heading("Interview (demo screen shots only)") + 
						paragraph("The following interview screens will give you an idea of how the interview " +
			"will, in future versions, walk you through developing your project.") +
						paragraph(bold("To continue with the Interview demo, click on the 'Next' button.")) +
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
						smallHeading("Navigation Hints for the Interview") + 
						smallParagraph(("" + 
			"If you want to start working with a project, " +
			"switch to the Diagram View and start " +
			"building a conceptual model of your project. ") + 
					""))
			))
		);

	}
}
