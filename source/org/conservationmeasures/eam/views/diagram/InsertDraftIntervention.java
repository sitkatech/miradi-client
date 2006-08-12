/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.diagram;

import org.conservationmeasures.eam.commands.CommandSetObjectData;
import org.conservationmeasures.eam.diagram.nodes.DiagramNode;
import org.conservationmeasures.eam.diagram.nodetypes.NodeType;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objects.ConceptualModelIntervention;
import org.conservationmeasures.eam.objects.ObjectType;

public class InsertDraftIntervention extends InsertNode
{
	public NodeType getTypeToInsert()
	{
		return DiagramNode.TYPE_INTERVENTION;
	}

	public String getInitialText()
	{
		return EAM.text("Label|New Draft Intervention");
	}

	void doExtraSetup(BaseId id) throws CommandFailedException
	{
		CommandSetObjectData setStatusCommand = new CommandSetObjectData(ObjectType.MODEL_NODE, id, ConceptualModelIntervention.TAG_STATUS, ConceptualModelIntervention.STATUS_DRAFT);
		getProject().executeCommand(setStatusCommand);
	}
	

}
