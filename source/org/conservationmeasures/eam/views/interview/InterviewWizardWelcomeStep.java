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

public class InterviewWizardWelcomeStep extends InterviewWizardStep implements HyperlinkHandler
{
	public InterviewWizardWelcomeStep(WizardPanel wizardToUse)
	{
		super(wizardToUse);

		htmlViewer = new HtmlViewer("", this);
		JScrollPane scrollPane = new JScrollPane(htmlViewer);
		add(scrollPane);
	}

	void refresh() throws Exception
	{
		String htmlText = WelcomeText.build();
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

class WelcomeText extends HtmlBuilder
{
	public static String build()
	{
		return font("Arial", 
			wizardFrame(tableRow(
				tableCell(
						heading("Interview (demo screen shots only)") + 
						paragraph("This interview will walk you through each step of the CMP Open Standards.") +
						paragraph(bold("Click next to continue with the interview process.")) +
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
						smallParagraph(("The lefthand column typically contains the instructions " +
								"for the current step.  The righthand column (this one) usually contains " +
								"definitions, explanations, or user hints.") +
								"is a listing of all targets for your project. ") + 
						smallParagraph("Use the 'Back' and 'Next' buttons to move to the previous or " +
								"next step.   Note that clicking next automatically saves any work ") + 
						smallParagraph("Use the 'Step-by-Step' Menu to jump to anther step " +
								"in the overall process. The following buttons can be found on most pages and provide:") +
						list(
							listItem(smallParagraph(bold("Get More Info: ") +
									"More detailed guidance about the current step.")) +
							listItem(smallParagraph(bold("Examples: ") +
									"Relevant excerpts from other projects.")) + 
							listItem(smallParagraph(bold("Workshop Hints: ") +
									"Ideas for how to present this material in a workshop setting, " +
									"primarily using non-computerized methods."))
							) +
						smallParagraph("Once you become experienced with a given step, " +
								"you can use the splitter to hide the interview screen and " +
								"just work directly with the relevant views of your project.") +
					""))
			));

	}
}
