/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.summary;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.JPanel;

import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objects.ProjectMetadata;
import org.conservationmeasures.eam.project.Project;
import org.martus.swing.UiLabel;
import org.martus.swing.UiTextField;

import com.jhlabs.awt.BasicGridLayout;

public class SummaryPanel extends JPanel
{
	public SummaryPanel(Project projectToUse)
	{
		super(new BasicGridLayout(0, 2));
		project = projectToUse;

		add(new UiLabel(EAM.text("Label|Filename:")));
		add(new UiLabel(project.getFilename()));
		add(new UiLabel(EAM.text("Label|Project Name:")));
		projectName = new UiTextField(50);
		projectName.setText(project.getMetadata().getProjectName());
		projectName.addFocusListener(new FocusHandler());
		add(projectName);
	}
	
	class FocusHandler implements FocusListener
	{
		public void focusGained(FocusEvent arg0)
		{
		}

		public void focusLost(FocusEvent arg0)
		{
			try
			{
				project.setMetadata(ProjectMetadata.TAG_PROJECT_NAME, projectName.getText());
			}
			catch (Exception e)
			{
				e.printStackTrace();
				EAM.errorDialog(EAM.text("Text|Error prevented saving"));
			}
		}

	}

	Project project;
	UiTextField projectName;
}
