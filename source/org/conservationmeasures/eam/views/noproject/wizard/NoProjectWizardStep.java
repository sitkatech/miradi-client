/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.noproject.wizard;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.wizard.SkeletonWizardStep;
import org.conservationmeasures.eam.wizard.WizardHtmlViewer;

public class NoProjectWizardStep extends SkeletonWizardStep
{
	public NoProjectWizardStep(NoProjectWizardPanel wizardToUse) throws Exception
	{
		super(wizardToUse);

		String header = EAM.loadResourceFile(getClass(), "WelcomeHeader.html");
		WizardHtmlViewer headerHtmlViewer = new WizardHtmlViewer(wizardToUse.getMainWindow(), wizardToUse);
		headerHtmlViewer.setText(header);

		String introText = EAM.loadResourceFile(getClass(), "WelcomeIntroduction.html");
		introHtmlViewer = new WizardHtmlViewer(wizardToUse.getMainWindow(), wizardToUse);
		introHtmlViewer.setText(introText);

		JPanel headerBox = new JPanel(new BorderLayout());
		headerBox.add(headerHtmlViewer, BorderLayout.BEFORE_FIRST_LINE);
		headerBox.add(introHtmlViewer, BorderLayout.CENTER);
		add(headerBox, BorderLayout.BEFORE_FIRST_LINE);

		projectList = new ProjectListPanel(wizardToUse.getMainWindow(), wizardToUse);
		
		setBackground(Color.WHITE);
		setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
	}
	
	public void refresh() throws Exception
	{
		introHtmlViewer.setText(introHtmlViewer.getText());
		projectList.refresh();
	}
	
	WizardHtmlViewer introHtmlViewer;
	ProjectListPanel projectList;
}
