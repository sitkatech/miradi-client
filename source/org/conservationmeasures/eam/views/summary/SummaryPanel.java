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
import org.conservationmeasures.eam.utils.InvalidDateException;
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
		projectName.addFocusListener(new ProjectNameFocusHandler());
		add(projectName);
		
		add(new UiLabel(EAM.text("Label|Start Date:")));
		startDate = new UiTextField(10);
		startDate.setText(project.getMetadata().getStartDate());
		startDate.addFocusListener(new StartDateFocusHandler());
		add(startDate);
	}
	
	private void save(String tag, UiTextField field)
	{
		String newValue = field.getText();
		String existing = project.getMetadata().getData(tag);
		try
		{
			if(!existing.equals(newValue))
			{
				project.setMetadata(tag, newValue);
			}
		}
		catch (InvalidDateException e)
		{
			EAM.errorDialog(EAM.text("Text|Dates must be in YYYY-MM-DD format"));
			field.setText(existing);
			field.requestFocus();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			EAM.errorDialog(EAM.text("Text|Error prevented saving"));
		}
	}

	abstract class FocusHandler implements FocusListener
	{
		public void focusGained(FocusEvent arg0)
		{
		}
	}
	
	class ProjectNameFocusHandler extends FocusHandler
	{
		public void focusLost(FocusEvent arg0)
		{
			save(ProjectMetadata.TAG_PROJECT_NAME, projectName);
		}
	}

	class StartDateFocusHandler extends FocusHandler
	{
		public void focusLost(FocusEvent arg0)
		{
			save(ProjectMetadata.TAG_START_DATE, startDate);
			startDate.setText(project.getMetadata().getStartDate());
		}
	}

	Project project;
	UiTextField projectName;
	UiTextField startDate;
}
