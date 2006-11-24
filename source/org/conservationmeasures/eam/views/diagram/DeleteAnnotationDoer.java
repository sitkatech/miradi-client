/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.diagram;

import java.text.ParseException;
import java.util.Arrays;
import java.util.Vector;

import org.conservationmeasures.eam.commands.Command;
import org.conservationmeasures.eam.commands.CommandBeginTransaction;
import org.conservationmeasures.eam.commands.CommandDeleteObject;
import org.conservationmeasures.eam.commands.CommandEndTransaction;
import org.conservationmeasures.eam.commands.CommandSetObjectData;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objecthelpers.FactorSet;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.Factor;
import org.conservationmeasures.eam.objects.EAMBaseObject;
import org.conservationmeasures.eam.objects.EAMObject;
import org.conservationmeasures.eam.project.ChainManager;
import org.conservationmeasures.eam.project.Project;
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


	void deleteAnnotationViaCommands(Factor node, String annotationIdListTag, String[] confirmDialogText) throws CommandFailedException
	{
		EAMBaseObject annotationToDelete = (EAMBaseObject)getObjects()[0];
	
		String[] buttons = {"Delete", "Retain", };
		if(!EAM.confirmDialog("Delete", confirmDialogText, buttons))
			return;
	
		getProject().executeCommand(new CommandBeginTransaction());
		try
		{
			Command[] commands = buildCommandsToDeleteAnnotation(getProject(), node, annotationIdListTag, annotationToDelete);
			for(int i = 0; i < commands.length; ++i)
				getProject().executeCommand(commands[i]);
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
	
	public static Command[] buildCommandsToDeleteAnnotation(Project project, Factor node, String annotationIdListTag, EAMBaseObject annotationToDelete) throws CommandFailedException, ParseException, Exception
	{
		Vector commands = new Vector();
	
		int type = annotationToDelete.getType();
		BaseId idToRemove = annotationToDelete.getId();
		commands.add(CommandSetObjectData.createRemoveIdCommand(node, annotationIdListTag, idToRemove));
		FactorSet nodesThatUseThisAnnotation = new ChainManager(project).findNodesThatUseThisAnnotation(type, idToRemove);
		if(nodesThatUseThisAnnotation.size() == 1)
		{
			commands.addAll(Arrays.asList(annotationToDelete.createCommandsToClear()));
			commands.add(new CommandDeleteObject(type, idToRemove));
		}
		
		return (Command[])commands.toArray(new Command[0]);
	}

	public Factor getSelectedNode()
	{
		EAMObject selected = getView().getSelectedObject();
		if(selected == null)
			return null;
		
		if(selected.getType() != ObjectType.MODEL_NODE)
			return null;
		
		return (Factor)selected;
	}

}
