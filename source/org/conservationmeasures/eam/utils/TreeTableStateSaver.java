/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.utils;

import java.text.ParseException;

import javax.swing.JTree;
import javax.swing.tree.TreePath;

import org.conservationmeasures.eam.commands.CommandSetObjectData;
import org.conservationmeasures.eam.main.CommandExecutedEvent;
import org.conservationmeasures.eam.main.CommandExecutedListener;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objecthelpers.ORefList;
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
	
	public void dispose()
	{
		project.removeCommandExecutedListener(this);
	}
	
	public void commandExecuted(CommandExecutedEvent event)
	{
		executeTreeStateRestore(event);
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
		ORefList objRefList = new ORefList();
		for (int i = 0; i < rowCount; i ++)
		{
			TreePath treePath = tree.getPathForRow(i);
			if (tree.isExpanded(treePath))
			{
				TreeTableNode node = (TreeTableNode)treePath.getLastPathComponent();
				ORef objectReference = node.getObjectReference();
				if (objectReference != null)
					objRefList.add(objectReference);
			}
		}
		saveExpandedPath(objRefList);
	}

	private void saveExpandedPath(ORefList  newObjRefList) throws Exception
	{
		ViewData viewData = project.getViewData(project.getCurrentView());		
		CommandSetObjectData cmd = new CommandSetObjectData(viewData.getType(), viewData.getId() ,ViewData.TAG_CURRENT_EXPANSION_LIST, newObjRefList.toString());
		project.executeCommand(cmd);
	}
	
	void setTreeExpansionState() throws Exception
	{
		ORefList objRefList = getExpandedNodeList();
		TreeTableNode root = (TreeTableNode)tree.getModel().getRoot();
		TreePath rootPath = new TreePath(root);
		possiblyChangeNodeExpansionState(objRefList, rootPath);
		isFirstTime =false;
	}

	private void possiblyChangeNodeExpansionState(ORefList objRefListToUse, TreePath thisPath)
	{
		TreeTableNode topLevelObject = (TreeTableNode)thisPath.getLastPathComponent();
		ORef topLevelObjRef = topLevelObject.getObjectReference();
		
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
	
	private ORefList getExpandedNodeList() throws Exception, ParseException
	{
		ViewData viewData = project.getViewData(project.getCurrentView());
		ORefList objRefList= new ORefList(viewData.getData(ViewData.TAG_CURRENT_EXPANSION_LIST));
		return objRefList;
	}
	
	boolean isFirstTime = true;
	boolean isListening = true;
	Project project;
	JTree tree;
}
