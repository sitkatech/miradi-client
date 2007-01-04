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
import javax.swing.JScrollPane;

import org.conservationmeasures.eam.dialogs.EAMDialog;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.utils.HtmlBuilder;
import org.martus.swing.HtmlViewer;
import org.martus.swing.ResourceImageIcon;
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
				getThreatRatingWizard().jump(ThreatRatingWizardChooseBundle.class);
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
		else 
			super.linkClicked(linkDescription);
	}

	static class CheckTotalsText extends HtmlBuilder
	{
		public static String build()
		{
			return font("Arial", 
				wizardFrame(tableRow(
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

	public class ShowBundleRulesDialog extends EAMDialog implements ActionListener
	{
		public ShowBundleRulesDialog()
		{
			super(EAM.mainWindow);
			setResizable(true);
			setModal(true);

			Container contents = getContentPane();
			contents.setLayout(new BorderLayout());

			ResourceImageIcon bundleSummaries = new ResourceImageIcon("images/BundleSummaries.png");
			Dimension bundleSummariesSize = new Dimension(bundleSummaries.getIconWidth(), bundleSummaries.getIconHeight());
			HtmlViewer rules = new HtmlViewer("", null);
			rules.setText("<html>" +
					HtmlBuilder.font("Arial", 
					"<h2>Explanation of How Threat Rating Summaries Are Calculated</h2>" +
					"<p>The e-AM software uses a combination of rules for rolling up ratings " +
					"across targets and threats, and for the project as a whole. </p>" +
					
					"<p>As shown in the grid below, " +
					"the bottom row contains the overall ratings for each target, and " +
					"the far right-hand column contains the ratings for each threat. " +
					"Finally, the cell in the lower right-hand corner contains the overall rating " +
					"for the project. Normally the overall project rating is based on rolling up " +
					"the threat ratings in the right-most column, using the 3-5-7 and 2-Prime rules.</p>" +

					"<h3>3-5-7 Rule</h3>" +
					"<p>Multiple threats to individual targets and multiple target threat scores " +
					"are first summed together using the 3-5-7 rule:</p>" +
					"<ul>" +
					"<li>3 High rated threats are equivalent to 1 Very High-rated threat;</li>" +
					"<li>5 Medium rated threats are equivalent to 1 High-rated threat;</li>" +
					"<li>7 Low rated threats are equivalent to 1 Medium-rated threat</li>" +
					"</ul>" +
					
					"<p>In the example below, the second row shows the Housing threat. " +
					"There are 3 High ratings (which equals 1 Very High) " +
					"and 1 Very High rating, so it is treated as if it had two Very High ratings.  " +
					"In the Lone Chapparal Column, there are 5 Medium ratings (which equals one High), " +
					"plus one High, for a total equivalent of 2 High ratings.  </p>" +
					
					"<h3>2-Prime Rule</h3>" +
					"<p>After the 3-5-7 rule has been applied, the 2-prime rule is used to determine" +
					"the rolled up rating for a target, a threat, or for the whole project.  " +
					"This rule requires the equivalent of two ratings at a certain level for the " +
					"end result to be that level. For example, there would have to be the equivalent of at least " +
					"two Very High ratings to produce a Very High result, or " +
					"two ratings of Medium or above to produce a Medium result.</p>" +
					
					"<p>In the example below, the Housing threat row has the equivalent of " +
					"two Very High ratings (due to the 3-5-7 rule), so the result is Very High. " +
					"The Recreational Vehicles row has one Medium rating and one Low. Since it " +
					"does not have two or more Mediums, the result is Low.</p>" +
					
					"<h3>Majority Override</h3>" +
					"<p>The Majority Override rule ensures that the overall project rating is not " +
					"reduced too much by the other rules. Normally, the overall project rating is " +
					"a rollup of the threat ratings, using the rules above. " +
					"However, if a majority of the targets have a rating higher than that computed rollup, " +
					"then that majority rating is used instead.</p>" +
					
					"<p>For example, if the result of using the 3-5-7 and 2-prime rules gave a " +
					"project ratign of Medium, but 4 out of the 6 targets had at least one rating of at High (or Very High), " +
					"then the Majority Override rule would take effect and the overall project rating " +
					"would be High.</p>" +

					HtmlBuilder.newline() +
					HtmlBuilder.image("images/BundleSummaries.png", bundleSummariesSize)
					) + 
					"</html>");
			contents.add(new JScrollPane(rules), BorderLayout.CENTER);
			contents.add(createButtonBar(), BorderLayout.AFTER_LAST_LINE);

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

