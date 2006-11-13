/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.diagram;

import org.conservationmeasures.eam.commands.CommandBeginTransaction;
import org.conservationmeasures.eam.commands.CommandDeleteObject;
import org.conservationmeasures.eam.commands.CommandEndTransaction;
import org.conservationmeasures.eam.commands.CommandSetObjectData;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objecthelpers.ConceptualModelNodeSet;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.ConceptualModelNode;
import org.conservationmeasures.eam.objects.EAMBaseObject;
import org.conservationmeasures.eam.objects.EAMObject;
import org.conservationmeasures.eam.project.ChainManager;
import org.conservationmeasures.eam.views.ObjectsDoer;

public abstract class DeleteAnnotationDoer extends ObjectsDoer
{
	abstract String[] getDialogText();
	abstract String getAnnotationIdListTag();

	public boolean isAvailable()
	{
		return (getObjects().length == 1);
	}

	public void doIt() throws CommandFailedException
	{
		if(!isAvailable())
			return;
		
		String tag = getAnnotationIdListTag();
		String[] dialogText = getDialogText();
	
		deleteAnnotationViaCommands(getSelectedNode(), tag, dialogText);
	}


	protected void deleteAnnotationViaCommands(ConceptualModelNode node, String annotationIdListTag, String[] confirmDialogText) throws CommandFailedException
	{
		EAMBaseObject annotationToDelete = (EAMBaseObject)getObjects()[0];
		int type = annotationToDelete.getType();
		BaseId idToRemove = annotationToDelete.getId();
	
		String[] buttons = {"Delete", "Retain", };
		if(!EAM.confirmDialog("Delete", confirmDialogText, buttons))
			return;
	
		getProject().executeCommand(new CommandBeginTransaction());
		try
		{
			getProject().executeCommand(CommandSetObjectData.createRemoveIdCommand(node, annotationIdListTag, idToRemove));
			ConceptualModelNodeSet nodesThatUseThisAnnotation = new ChainManager(getProject()).findNodesThatUseThisAnnotation(type, idToRemove);
			if(nodesThatUseThisAnnotation.size() == 0)
			{
				getProject().executeCommands(annotationToDelete.createCommandsToClear());
				getProject().executeCommand(new CommandDeleteObject(type, idToRemove));
			}
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
