/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.task;


import javax.swing.BorderFactory;

import org.conservationmeasures.eam.commands.CommandSetObjectData;
import org.conservationmeasures.eam.dialogs.base.ObjectDataInputPanel;
import org.conservationmeasures.eam.dialogs.fieldComponents.PanelTitleLabel;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.main.CommandExecutedEvent;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.BaseObject;
import org.conservationmeasures.eam.objects.Task;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.questions.BudgetCostModeQuestion;
import org.martus.swing.UiLabel;

public class TaskPropertiesInputPanel extends ObjectDataInputPanel
{
	public TaskPropertiesInputPanel(Project projectToUse) throws Exception
	{
		this(projectToUse, BaseId.INVALID);
	}
	
	public TaskPropertiesInputPanel(Project projectToUse, BaseId idToEdit) throws Exception
	{
		super(projectToUse, ObjectType.TASK, idToEdit);
		project = projectToUse;
		setBorder(BorderFactory.createEtchedBorder());
		
		hasBothSubTaskAssignmentsWarningLabel = new PanelTitleLabel(EAM.text("NOTE: The budget total for this task is the sum of the budget totals of its subtasks. The resource assignments below are not included in this value."));
		addCommonFields();
	}
	
	public void dispose()
	{
		super.dispose();
	}

	private void addCommonFields()
	{
		addField(createStringField(ObjectType.TASK, Task.TAG_LABEL));
		addField(createChoiceField(Task.getObjectType(), new BudgetCostModeQuestion(Task.TAG_BUDGET_COST_MODE)));
		addField(createReadonlyTextField(Task.PSEUDO_TAG_BUDGET_COST_ROLLUP));
		addField(createCurrencyField(Task.getObjectType(), Task.TAG_BUDGET_COST_OVERRIDE));
		
		addLabel(new UiLabel(""));
		addLabel(hasBothSubTaskAssignmentsWarningLabel);
		updateFieldsFromProject();
	}

	public void setObjectRefs(ORef[] orefsToUse)
	{
		super.setObjectRefs(orefsToUse);
		updatedWarningMessageVisiblity(orefsToUse);		
	}

	
	public String getPanelDescription()
	{
		return EAM.text("Title|Task Properties");
	}
		
	public void updateTable()
	{
	}
	
	public void commandExecuted(CommandExecutedEvent event)
	{
		super.commandExecuted(event);
		if (event.getCommandName().equals(CommandSetObjectData.COMMAND_NAME))
			updateTable();
	}
	
	private void updatedWarningMessageVisiblity(ORef[] orefsToUse)
	{
		hasBothSubTaskAssignmentsWarningLabel.setVisible(isVisible(orefsToUse));
	}
			
	private boolean isVisible(ORef[] orefsToUse)
	{
		if (orefsToUse.length == 0)
			return false;
		
		ORef firstRef = orefsToUse[0];
		if(firstRef.isInvalid())
			return false;
		
		BaseObject foundObject = getProject().findObject(firstRef);
		if (foundObject.getType() != Task.getObjectType())
			return false;
		
		Task task = (Task) foundObject;
		if (task.getSubtaskCount() == 0 || task.getAssignmentRefs().size() == 0)
			return false;
		
		return true;
	}
	
	Project project;
	PanelTitleLabel hasBothSubTaskAssignmentsWarningLabel;
}