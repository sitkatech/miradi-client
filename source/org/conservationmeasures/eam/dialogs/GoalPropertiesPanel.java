/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.dialogs;

import org.conservationmeasures.eam.actions.Actions;
import org.conservationmeasures.eam.ids.GoalId;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.Goal;
import org.conservationmeasures.eam.project.Project;

public class GoalPropertiesPanel extends ObjectDataInputPanel
{
	public GoalPropertiesPanel(Project projectToUse, Actions actions, Goal goal) throws Exception
	{
		this(projectToUse, actions, (GoalId)goal.getId());
	}
	
	public GoalPropertiesPanel(Project projectToUse, Actions actions, GoalId idToShow) throws Exception
	{
		super(projectToUse, ObjectType.GOAL, idToShow);
		
		addField(createStringField("pseudoField"));
		addField(createStringField(Goal.TAG_SHORT_LABEL));
		addField(createStringField(Goal.TAG_LABEL));
		addField(createMultilineField(Goal.TAG_FULL_TEXT));
		updateFieldsFromProject();
	}

	public String getPanelDescription()
	{
		return EAM.text("Title|Goal Properties");
	}

}
