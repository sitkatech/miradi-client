/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.utils;

import java.text.ParseException;

import javax.swing.JTree;
import javax.swing.tree.TreePath;

import org.conservationmeasures.eam.commands.Command;
import org.conservationmeasures.eam.commands.CommandSetObjectData;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.main.CommandExecutedEvent;
import org.conservationmeasures.eam.main.CommandExecutedListener;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objecthelpers.ObjectReference;
import org.conservationmeasures.eam.objecthelpers.ObjectReferenceList;
import org.conservationmeasures.eam.objects.ViewData;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.views.TreeTableNode;

public class TreeTableStateSaver implements CommandExecutedListener
{
	public TreeTableStateSaver(Project projectToUse, JTree treeToUse)
	{
		tree = treeToUse;
		project = projectToUse;
		project.addCommandExecutedListener(this);
	}
	
	public void commandExecuted(CommandExecutedEvent event)
	{
		executeTreeStateRestore(event);
	}
	
	public void commandUndone(CommandExecutedEvent event)
	{
		executeTreeStateRestore(event);
	}
	public void commandFailed(Command command, CommandFailedException e)
	{
	}

	private void executeTreeStateRestore(CommandExecutedEvent event)
	{
		if(!event.getCommandName().equals(CommandSetObjectData.COMMAND_NAME))
			return;
		try
		{
			restoreTreeState();
		}
		catch(Exception e)
		{
			EAM.logException(e);
			EAM.errorDialog(EAM.text("Unexpected Error has occured"));
		}
	}
	
	public void saveTreeExpansionState() throws Exception
	{
		if (!isListening)
			return;

		int rowCount = tree.getRowCount();
		ObjectReferenceList objRefList = new ObjectReferenceList();
		for (int i = 0; i < rowCount; i ++)
		{
			TreePath treePath = tree.getPathForRow(i);
			if (tree.isExpanded(treePath))
			{
				TreeTableNode node = (TreeTableNode)treePath.getLastPathComponent();
				ObjectReference objectReference = node.getObjectReference();
				if (objectReference != null)
					objRefList.add(objectReference);
			}
		}
		saveExpandedPath(objRefList);
	}

	private void saveExpandedPath(ObjectReferenceList  newObjRefList) throws Exception
	{
		ViewData viewData = project.getViewData(project.getCurrentView());		
		CommandSetObjectData cmd = new CommandSetObjectData(viewData.getType(), viewData.getId() ,ViewData.TAG_CURRENT_EXPANSION_LIST, newObjRefList.toString());
		project.executeCommand(cmd);
	}
	
	void setTreeExpansionState() throws Exception
	{
		ObjectReferenceList objRefList = getExpandedNodeList();
		TreeTableNode root = (TreeTableNode)tree.getModel().getRoot();
		TreePath rootPath = new TreePath(root);
		possiblyChangeNodeExpansionState(objRefList, rootPath);
		isFirstTime =false;
	}

	private void possiblyChangeNodeExpansionState(ObjectReferenceList objRefListToUse, TreePath thisPath)
	{
		TreeTableNode topLevelObject = (TreeTableNode)thisPath.getLastPathComponent();
		ObjectReference topLevelObjRef = topLevelObject.getObjectReference();
		
		if ( ! (objRefListToUse.contains(topLevelObjRef) || topLevelObjRef == null))
		{
			tree.collapsePath(thisPath);
			return;
		}
		
		tree.expandPath(thisPath);
		
		for(int childIndex = 0; childIndex < topLevelObject.getChildCount(); ++childIndex)
		{
			TreeTableNode secondLevelObject = topLevelObject.getChild(childIndex);
			TreePath secondLevelPath = thisPath.pathByAddingChild(secondLevelObject);
			possiblyChangeNodeExpansionState(objRefListToUse, secondLevelPath);
		}
	}

	public void restoreTreeState() throws Exception
	{
		isListening = false;
		try
		{
				setTreeExpansionState();
		}
		finally
		{
				isListening = true;
		}
	}
	
	private ObjectReferenceList getExpandedNodeList() throws Exception, ParseException
	{
		ViewData viewData = project.getViewData(project.getCurrentView());
		ObjectReferenceList objRefList= new ObjectReferenceList(viewData.getData(ViewData.TAG_CURRENT_EXPANSION_LIST));
		return objRefList;
	}
	
	boolean isFirstTime = true;
	boolean isListening = true;
	Project project;
	JTree tree;
}
