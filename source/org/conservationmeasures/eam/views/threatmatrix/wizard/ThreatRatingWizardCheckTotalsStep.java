/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.threatmatrix.wizard;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JScrollPane;

import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.utils.HtmlBuilder;
import org.martus.swing.HtmlViewer;
import org.martus.swing.UiButton;
import org.martus.swing.Utilities;

public class ThreatRatingWizardCheckTotalsStep extends ThreatRatingWizardStep
{
	public ThreatRatingWizardCheckTotalsStep(ThreatRatingWizardPanel wizardToUse)
	{
		super(wizardToUse);
	}
	
	public String getText()
	{
		return CheckTotalsText.build();
	}

	public boolean save() throws Exception
	{
		return true;
	}

	public void buttonPressed(String buttonName)
	{
		if(buttonName.equals("Back"))
		{
			try
			{
				getThreatRatingWizard().setStep(ThreatRatingWizardPanel.CHOOSE_BUNDLE);
			}
			catch (Exception e)
			{
				EAM.logException(e);
			}
			return;
		}
		super.buttonPressed(buttonName);
	}

	public void linkClicked(String linkDescription)
	{
		if(linkDescription.equals(SHOW_RULES))
		{
			ShowBundleRulesDialog dlg = new ShowBundleRulesDialog();
			dlg.setVisible(true);
		}
	}

	static class CheckTotalsText extends HtmlBuilder
	{
		public static String build()
		{
			return font("Arial", 
				table(tableRow(
					tableCell(
							heading("Threat Rating Summary") + 
							paragraph("The table below contains a summary of your threat ratings. ") +
							list(
								listItem("The " +
										bold("column on the far right ") +
										"contains a summary of the overall rating of each threat across all targets.") +
								listItem("The " +
										bold("row on the bottom ") +
										"contains a summary of the overall threat rating for each target.") +
								listItem("Finally, the " +
										bold("lowest right hand cell") +
										" contains a summary of the threat rating for your entire project.")
							) +
							paragraph("These summary ratings are not entered directly, but are calculated as you rate the specific effects of threats on targets in the cells of the table.") +
							paragraph("You should check whether you agree with these summary ratings.  If any of them do not make sense, you may need to go back and adjust the ratings for the specific criteria.") +
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
							smallParagraph(bold("Summary Threat Ratings - ") +
									"The e-AM Software rolls up threat ratings " +
									"for each target and threat using another rule-based system " +
									"for combining ratings for each target-threat combination. " +
									anchorTag(SHOW_RULES, "Click here") + 
									" for a detailed description of this system.")
						)
					))
				);

		}
	}

	public class ShowBundleRulesDialog extends JDialog implements ActionListener
	{
		public ShowBundleRulesDialog()
		{
			super(EAM.mainWindow);
			Container contents = getContentPane();
			contents.setLayout(new BorderLayout());

			ImageIcon bundleSummaries = new ImageIcon("images/BundleSummaries.png");
			Dimension bundleSummariesSize = new Dimension(bundleSummaries.getIconWidth(), bundleSummaries.getIconHeight());
			HtmlViewer rules = new HtmlViewer("", null);
			rules.setText("<html>" +
					HtmlBuilder.font("Arial", 
					"<h2>Explanation of How Threat Rating Summaries Are Calculated</h2>" +
					"<p>The e-AM software uses the following rules for rolling up ratings " +
					"across targets and threats. " +
					"Multiple threats to individual targets and multiple target threat scores " +
					"are first summed together using the 3-5-7 rule:</p>" +
					"<ul>" +
					"<li>3 High rated threats are equivalent to 1 Very High-rated threat;</li>" +
					"<li>5 Medium rated threats are equivalent to 1 High-rated threat;</li>" +
					"<li>7 Low rated threats are equivalent to 1 Medium-rated threat</li>" +
					"</ul>" +
					
					"<p>Once multiple threats scores are summed together, " +
					"the overall threat status for a single target, for a threat, <" +
					"and the overall threat status for the whole project is calculated " +
					"using the 2-prime rule.  " +
					"This rule requires the equivalent of two Very High ratings " +
					"(e.g., one Very High and at least three High ratings) " +
					"for the overall rating to be Very High " +
					"and the equivalent of two High ratings " +
					"for the overall rating to be High.</p>" +

					"<p>For example, in the second row for the Housing threat, " +
					"there are 3 High ratings (which equals 1 Very High) " +
					"and 1 Very High rating.  Thus, the overall Threat Rank is Very High.  " +
					"Likewise, in the Upper Watershed Column, there are 6 High ratings, " +
					"which equal 2 Very High ratings.  " +
					"Thus, the overall rank for this target is Very High.</p>" +
					
					"<p>In this example, the bottom row contains the overall threat rating for each target.  " +
					"The far right-hand column contains the ratings for each threat across targets. " +
					"And finally, the cell in the lower right-hand corner contains the overall rating " +
					"for the project, which is calculated by rolling up the far-right hand column " +
					"using the 2-prime rule.</p>" +
					HtmlBuilder.newline() +
					HtmlBuilder.image("images/BundleSummaries.png", bundleSummariesSize)
					) + 
					"</html>");
			contents.add(new JScrollPane(rules), BorderLayout.CENTER);
			contents.add(createButtonBar(), BorderLayout.AFTER_LAST_LINE);

			pack();
			setResizable(true);
			setModal(true);
			setSize(900, 700);

		}

		private Box createButtonBar()
		{
			UiButton closeButton = new UiButton(EAM.text("Button|Close"));
			closeButton.addActionListener(this);
			getRootPane().setDefaultButton(closeButton);

			Box buttonBar = Box.createHorizontalBox();
			Component[] components = new Component[] {Box.createHorizontalGlue(), closeButton};
			Utilities.addComponentsRespectingOrientation(buttonBar, components);
			return buttonBar;
		}

		public void actionPerformed(ActionEvent event)
		{
			dispose();
		}
		
	}

	
	static final String SHOW_RULES = "ShowRules";
}

