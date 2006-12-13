package org.conservationmeasures.eam.views.noproject.wizard;

import java.awt.BorderLayout;

import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.views.umbrella.SkeletonWizardStep;
import org.conservationmeasures.eam.views.umbrella.WizardHtmlViewer;

public class NoProjectWizardStep extends SkeletonWizardStep
{
	public NoProjectWizardStep(NoProjectWizardPanel wizardToUse) throws Exception
	{
		super(wizardToUse);

		String header = EAM.loadResourceFile(getClass(), "WelcomeHeader.html");
		WizardHtmlViewer headerHtmlViewer = new WizardHtmlViewer(wizardToUse);
		headerHtmlViewer.setText(header);
		add(headerHtmlViewer, BorderLayout.BEFORE_FIRST_LINE);

		projectList = new ProjectListPanel(wizardToUse);
	}

	public void refresh() throws Exception
	{
		projectList.refresh();
	}
	
	ProjectListPanel projectList;

}
