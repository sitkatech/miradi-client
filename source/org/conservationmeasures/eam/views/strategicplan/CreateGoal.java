package org.conservationmeasures.eam.views.strategicplan;

import org.conservationmeasures.eam.commands.CommandBeginTransaction;
import org.conservationmeasures.eam.commands.CommandCreateObject;
import org.conservationmeasures.eam.commands.CommandEndTransaction;
import org.conservationmeasures.eam.commands.CommandSetObjectData;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.ConceptualModelNode;
import org.conservationmeasures.eam.objects.EAMObject;
import org.conservationmeasures.eam.views.ViewDoer;

public class CreateGoal extends ViewDoer
{
	public boolean isAvailable()
	{
		return (getSelectedNode() != null);
	}

	public void doIt() throws CommandFailedException
	{
		if(!isAvailable())
			return;
		
		ConceptualModelNode node = getSelectedNode();
		
		getProject().executeCommand(new CommandBeginTransaction());
		try
		{
			CommandCreateObject create = new CommandCreateObject(ObjectType.GOAL);
			getProject().executeCommand(create);
			BaseId createdId = create.getCreatedId();
			getProject().executeCommand(CommandSetObjectData.createAppendIdCommand(node, node.TAG_GOAL_IDS, createdId));
		}
		catch(Exception e)
		{
			EAM.logException(e);
			throw new CommandFailedException(e);
		}
		finally
		{
			getProject().executeCommand(new CommandEndTransaction());
		}
	}
	
	public ConceptualModelNode getSelectedNode()
	{
		EAMObject selected = getView().getSelectedObject();
		if(selected == null)
			return null;
		
		if(selected.getType() != ObjectType.MODEL_NODE)
			return null;
		
		return (ConceptualModelNode)selected;
	}

}