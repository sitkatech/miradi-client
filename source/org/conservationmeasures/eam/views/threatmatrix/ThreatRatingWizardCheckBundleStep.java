/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.threatmatrix;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;

import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.utils.HtmlBuilder;
import org.conservationmeasures.eam.utils.HtmlViewer;
import org.conservationmeasures.eam.utils.HyperlinkHandler;
import org.martus.swing.UiButton;
import org.martus.swing.UiLabel;
import org.martus.swing.Utilities;

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

	public boolean save() throws Exception
	{
		return true;
	}

	public void buttonPressed(String buttonName)
	{
		if(buttonName.equals("Next"))
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
			dlg.show();
		}
		else if(linkDescription.equals("Definition:OverallThreatRating"))
		{
			EAM.okDialog("Definition: Overall Threat Rating", new String[] {"The overall threat rating is..."});
		}
	}

	public void valueChanged(String widget, String newValue)
	{
	}
	
	static class CheckBundleText extends HtmlBuilder
	{
		public static String build()
		{
			return font("Arial", 
				table(tableRow(
					tableCell(
							heading("Check Overall Threat Rating") + 
							paragraph("Once you have completed your ratings for all three criteria, " +
									"the software automatically calculates the " +
									definition("Definition:OverallThreatRating", "overall threat rating",
											"The Overall Threat Rating is...") + 
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
							smallParagraph(bold("Overall Threat Rating -- ") +
									"The e-AM Software calculates threat ratings " +
									"using a rule-based system for combining the scope, severity, " +
									"and irreversibility criteria.  " +
									"These procedures involve specifying rules " +
									"as to how different parameters should be combined with one another. " +
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

			contents.add(new JScrollPane(new UiLabel("<html>" +
					"<p><h2>Explanation of How Overall Threat Ratings Are Calculated</h2></p>" +
					"<p></p>" +
					"The left-hand matrix shows the rule-based procedure for combining the rankings " +
					"<br></br>" +
					"for the Scope and Severity variables to get a ranking of " +
					"<a href='none'><em>Threat Magnitude</em></a>.  " +
					"<br></br>" +
					"Under these rules, if a threat is rated 'low' on either variable, " +
					"then the magnitude is 'low' overall.  " +
					"<br></br>" +
					"Threat magnitude is then combined with Irreversibility ratings " +
					"using the right-hand matrix " +
					"<br></br>" +
					"to get the " +
					"<a href='none'><em>Overall Threat Rating</em></a>" +
					"." +
					"</html>")), BorderLayout.BEFORE_FIRST_LINE);
			Box hbox = Box.createHorizontalBox();
			ImageIcon image1 = new ImageIcon("images/BundleRules1.png");
			hbox.add(new UiLabel("", image1, SwingConstants.LEFT), BorderLayout.CENTER);
			ImageIcon image2 = new ImageIcon("images/BundleRules2.png");
			hbox.add(new UiLabel("", image2, SwingConstants.LEFT), BorderLayout.CENTER);

			contents.add(hbox, BorderLayout.CENTER);
			contents.add(createButtonBar(), BorderLayout.AFTER_LAST_LINE);

			pack();
			setResizable(true);
			setModal(true);

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

	HtmlViewer htmlViewer;

}

