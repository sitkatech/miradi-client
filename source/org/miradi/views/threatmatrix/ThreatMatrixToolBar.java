/* 
Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
(on behalf of the Conservation Measures Partnership, "CMP") and 
Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 

This file is part of Miradi

Miradi is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License version 3, 
as published by the Free Software Foundation.

Miradi is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Miradi.  If not, see <http://www.gnu.org/licenses/>. 
*/ 
package org.miradi.views.threatmatrix;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComponent;

import org.miradi.actions.ActionHideCellRatings;
import org.miradi.actions.ActionShowCellRatings;
import org.miradi.actions.Actions;
import org.miradi.actions.views.ActionViewThreatMatrix;
import org.miradi.commands.CommandSetObjectData;
import org.miradi.dialogs.fieldComponents.ChoiceItemComboBox;
import org.miradi.main.EAM;
import org.miradi.main.EAMToolBar;
import org.miradi.main.MainWindow;
import org.miradi.objects.ProjectMetadata;
import org.miradi.project.Project;
import org.miradi.questions.ChoiceItem;
import org.miradi.questions.ChoiceQuestion;
import org.miradi.questions.ThreatRatingModeChoiceQuestion;
import org.miradi.utils.ToolBarButton;

public class ThreatMatrixToolBar extends EAMToolBar
{
	public ThreatMatrixToolBar(MainWindow mainWindowToUse, boolean isCellRatingVisible)
	{
		super(mainWindowToUse.getActions(), ActionViewThreatMatrix.class, createButtons(mainWindowToUse, isCellRatingVisible));
	}
	
	static JComponent[][] createButtons(MainWindow mainWindow, boolean isCellRatingVisible)
	{
		JComponent threatRatingModeCombo = getThreatRatingModeCombo(mainWindow.getProject(), mainWindow.getActions());
		if (mainWindow.getProject().getMetadata().getThreatRatingMode().equals(ThreatRatingModeChoiceQuestion.STRESS_BASED_CODE))
				return new JComponent[][]  {{threatRatingModeCombo}, };
		
		ToolBarButton cellRatingButton = getCellRatingsButton(mainWindow.getActions(), isCellRatingVisible);
		return new JComponent[][]  {{cellRatingButton}, {threatRatingModeCombo}, };
	}
	
	private static ToolBarButton getCellRatingsButton(Actions actions, boolean isCellRatingVisible)
	{
		if (isCellRatingVisible)
			return new ToolBarButton(actions, ActionHideCellRatings.class);
	
		return new ToolBarButton(actions, ActionShowCellRatings.class);
	}
	
	private static ChoiceItemComboBox getThreatRatingModeCombo(Project project, Actions actions)
	{
		ChoiceItem[] choices = ThreatRatingModeChoiceQuestion.getChoiceItems();
		ChoiceItemComboBox threatRatingModeCombo = new ChoiceItemComboBox(choices);	
		setSelectedChoice(project, threatRatingModeCombo);
		
		threatRatingModeCombo.addActionListener(new ThreatRatingModeComboBoxHandler(project));
		
		return threatRatingModeCombo;
	}

	private static void setSelectedChoice(Project project, ChoiceItemComboBox threatRatingModeCombo)
	{
		ChoiceQuestion question = project.getQuestion(ThreatRatingModeChoiceQuestion.class);
		if (project.isStressBaseMode())
		{
			ChoiceItem stressBaseModeChoice = question.findChoiceByCode(ThreatRatingModeChoiceQuestion.STRESS_BASED_CODE);
			threatRatingModeCombo.setSelectedItem(stressBaseModeChoice);
		}
		else
		{
			ChoiceItem simpleModeChoice = question.findChoiceByCode("");
			threatRatingModeCombo.setSelectedItem(simpleModeChoice);
		}
	}
	
	static class ThreatRatingModeComboBoxHandler implements ActionListener
	{
		public ThreatRatingModeComboBoxHandler(Project projectToUse)
		{
			project = projectToUse;
		}

		public void actionPerformed(ActionEvent event)
		{
			ChoiceItemComboBox combo = (ChoiceItemComboBox) event.getSource();
			ChoiceItem choiceItem = (ChoiceItem) combo.getSelectedItem();
			
			setThreatRatingMode(choiceItem);
		}

		private void setThreatRatingMode(ChoiceItem choiceItem)
		{
			try
			{	
				CommandSetObjectData setThreatRatingMode = new CommandSetObjectData(getProject().getMetadata().getRef(), ProjectMetadata.TAG_THREAT_RATING_MODE, choiceItem.getCode());
				getProject().executeCommand(setThreatRatingMode);
			}
			catch (Exception e)
			{
				EAM.logException(e);
				EAM.errorDialog(EAM.text("Error occurred while switching threat rating mode."));
			}
		}	
		
		private Project getProject()
		{
			return project;
		}
		
		private Project project;
	}
}

