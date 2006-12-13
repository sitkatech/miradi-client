package org.conservationmeasures.eam.views.noproject.wizard;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.JPanel;

import org.conservationmeasures.eam.views.umbrella.WizardStep;
import org.martus.swing.HtmlViewer;
import org.martus.swing.UiScrollPane;

public class NoProjectWizardWelcomeStep extends NoProjectWizardStep
{
	public NoProjectWizardWelcomeStep(NoProjectWizardPanel wizardToUse) throws Exception
	{
		super(wizardToUse);
		JPanel panel = new JPanel(new GridLayout(1, 2));
		String newProject = WizardStep.loadHtmlFile(getClass(), "WelcomeNew.html");
		HtmlViewer newProjectHtmlViewer = new HtmlViewer(newProject, wizardToUse);
		newProjectHtmlViewer.setText(newProject);
		panel.add(newProjectHtmlViewer);
		
		panel.add(new UiScrollPane(projectList));
		
		add(panel, BorderLayout.CENTER);
	}

}
