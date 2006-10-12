/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.summary;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.Box;
import javax.swing.JPanel;

import org.conservationmeasures.eam.actions.ActionEditTeam;
import org.conservationmeasures.eam.ids.IdList;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.objects.ProjectMetadata;
import org.conservationmeasures.eam.objects.ProjectResource;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.utils.InvalidDateException;
import org.conservationmeasures.eam.utils.InvalidNumberException;
import org.martus.swing.UiButton;
import org.martus.swing.UiLabel;
import org.martus.swing.UiTextField;

import com.jhlabs.awt.BasicGridLayout;

public class SummaryPanel extends JPanel
{
	public SummaryPanel(MainWindow mainWindowToUse)
	{
		super(new BasicGridLayout(0, 2));
		mainWindow = mainWindowToUse;

		add(new UiLabel(EAM.text("Label|Filename:")));
		add(new UiLabel(getProject().getFilename()));
		
		add(new UiLabel(EAM.text("Label|Project Name:")));
		projectName = new UiTextField(50);
		projectName.setText(getProject().getMetadata().getProjectName());
		projectName.addFocusListener(new StringDataFocusHandler(ProjectMetadata.TAG_PROJECT_NAME, projectName));
		add(projectName);
		
		add(new UiLabel(EAM.text("Label|Project Scope:")));
		projectScope = new UiTextField(50);
		projectScope.setText(getProject().getMetadata().getProjectScope());
		projectScope.addFocusListener(new StringDataFocusHandler(ProjectMetadata.TAG_PROJECT_SCOPE, projectScope));
		add(projectScope);
		
		add(new UiLabel(EAM.text("Label|Project Vision:")));
		projectVision = new UiTextField(50);
		projectVision.setText(getProject().getMetadata().getProjectVision());
		projectVision.addFocusListener(new StringDataFocusHandler(ProjectMetadata.TAG_PROJECT_VISION, projectVision));
		add(projectVision);
		
		add(new UiLabel(EAM.text("Label|Start Date:")));
		startDate = new UiTextField(10);
		startDate.setText(getProject().getMetadata().getStartDate());
		startDate.addFocusListener(new StartDateFocusHandler());
		add(startDate);

		add(new UiLabel(EAM.text("Label|Data Effective Date:")));
		effectiveDate = new UiTextField(10);
		effectiveDate.setText(getProject().getMetadata().getEffectiveDate());
		effectiveDate.addFocusListener(new EffectiveDateFocusHandler());
		add(effectiveDate);

		add(new UiLabel(EAM.text("Label|Size in Hectares:")));
		sizeInHectares = new UiTextField(10);
		sizeInHectares.setText(getProject().getMetadata().getSizeInHectares());
		sizeInHectares.addFocusListener(new NumberDataFocusHandler(ProjectMetadata.TAG_SIZE_IN_HECTARES, sizeInHectares));
		add(sizeInHectares);
		
		IdList teamResourceIds = getProject().getMetadata().getTeamResourceIdList();
		ProjectResource[] resources = getProject().getResources(teamResourceIds);
		add(new UiLabel(EAM.text("Label|Team Members:")));
		Box teamPanel = Box.createHorizontalBox();
		teamPanel.add(new UiLabel(ProjectResource.getResourcesAsHtml(resources)));
		UiButton editTeamButton = new UiButton(mainWindow.getActions().get(ActionEditTeam.class));
		teamPanel.add(editTeamButton);
		add(teamPanel);
	}
	
	Project getProject()
	{
		return mainWindow.getProject();
	}
	
	private void save(String tag, UiTextField field)
	{
		String newValue = field.getText();
		String existing = getProject().getMetadata().getData(tag);
		try
		{
			if(!existing.equals(newValue))
			{
				getProject().setMetadata(tag, newValue);
			}
		}
		catch (InvalidDateException e)
		{
			EAM.errorDialog(EAM.text("Text|Dates must be in YYYY-MM-DD format"));
			field.setText(existing);
			field.requestFocus();
		}
		catch (InvalidNumberException e)
		{
			EAM.errorDialog(EAM.text("Text|Must be numeric"));
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
		public void focusGained(FocusEvent event)
		{
		}
	}
	
	class StringDataFocusHandler extends FocusHandler
	{
		public StringDataFocusHandler(String tagToUse, UiTextField componentToUse)
		{
			tag = tagToUse;
			component = componentToUse;
		}
		
		public void focusLost(FocusEvent event)
		{
			save(tag, component);
		}
		
		String tag;
		UiTextField component;
	}
	
	class NumberDataFocusHandler extends FocusHandler
	{
		public NumberDataFocusHandler(String tagToUse, UiTextField componentToUse)
		{
			tag = tagToUse;
			component = componentToUse;
		}
		
		public void focusLost(FocusEvent event)
		{
			save(tag, component);
			component.setText(getProject().getMetadata().getData(tag));
		}
		
		String tag;
		UiTextField component;
	}
	
	class StartDateFocusHandler extends FocusHandler
	{
		public void focusLost(FocusEvent event)
		{
			save(ProjectMetadata.TAG_START_DATE, startDate);
			startDate.setText(getProject().getMetadata().getStartDate());
		}
	}

	class EffectiveDateFocusHandler extends FocusHandler
	{
		public void focusLost(FocusEvent event)
		{
			save(ProjectMetadata.TAG_DATA_EFFECTIVE_DATE, effectiveDate);
			effectiveDate.setText(getProject().getMetadata().getEffectiveDate());
		}
	}

	MainWindow mainWindow;
	UiTextField projectName;
	UiTextField projectScope;
	UiTextField projectVision;
	UiTextField startDate;
	UiTextField effectiveDate;
	UiTextField sizeInHectares;
}
