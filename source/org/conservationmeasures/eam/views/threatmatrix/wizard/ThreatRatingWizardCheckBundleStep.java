/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.threatmatrix.wizard;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;

import org.conservationmeasures.eam.dialogs.EAMDialog;
import org.conservationmeasures.eam.main.EAM;
import org.martus.swing.ResourceImageIcon;
import org.martus.swing.UiButton;
import org.martus.swing.UiLabel;
import org.martus.swing.Utilities;

public class ThreatRatingWizardCheckBundleStep extends ThreatRatingWizardStep
{
	public ThreatRatingWizardCheckBundleStep(ThreatRatingWizardPanel wizardToUse)
	{
		super(wizardToUse);
	}
	

	public void buttonPressed(String buttonName)
	{
		if(buttonName.equals("Next"))
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


	public class ShowBundleRulesDialog extends EAMDialog implements ActionListener
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
			ResourceImageIcon image1 = new ResourceImageIcon("images/BundleRules1.png");
			hbox.add(new UiLabel("", image1, SwingConstants.LEFT), BorderLayout.CENTER);
			ResourceImageIcon image2 = new ResourceImageIcon("images/BundleRules2.png");
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

	
	public String getResourceFileName()
	{
		return HTML_FILENAME;
	}

	String HTML_FILENAME = "ThreatRatingCheckBundle.html";
	
	static final String SHOW_RULES = "ShowRules";

}

