package org.conservationmeasures.eam.views.noproject.wizard;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.JPanel;

import org.conservationmeasures.eam.views.umbrella.SkeletonWizardStep;
import org.conservationmeasures.eam.views.umbrella.WizardHtmlViewer;
import org.conservationmeasures.eam.views.umbrella.WizardStep;
import org.martus.swing.HtmlViewer;
import org.martus.swing.UiScrollPane;

public class NoProjectWizardWelcomeStep extends SkeletonWizardStep
{
	public NoProjectWizardWelcomeStep(NoProjectWizardPanel wizardToUse) throws Exception
	{
		super(wizardToUse);

		String header = WizardStep.loadHtmlFile(getClass(), "WelcomeHeader.html");
		WizardHtmlViewer headerHtmlViewer = new WizardHtmlViewer(wizardToUse);
		headerHtmlViewer.setText(header);
		add(headerHtmlViewer, BorderLayout.BEFORE_FIRST_LINE);

		JPanel panel = new JPanel(new GridLayout(1, 2));
		String newProject = WizardStep.loadHtmlFile(getClass(), "WelcomeNew.html");
		HtmlViewer newProjectHtmlViewer = new HtmlViewer(newProject, wizardToUse);
		newProjectHtmlViewer.setText(newProject);
		panel.add(newProjectHtmlViewer);
		
		projectList = new ProjectList(wizardToUse);
		panel.add(new UiScrollPane(projectList));
		
		add(panel, BorderLayout.CENTER);
	}

	public void refresh() throws Exception
	{
		projectList.refresh();
	}
	
	ProjectList projectList;
}
