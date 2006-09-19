/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.diagram;

import org.conservationmeasures.eam.diagram.nodes.DiagramNode;
import org.conservationmeasures.eam.diagram.nodetypes.NodeType;
import org.conservationmeasures.eam.exceptions.CommandFailedException;

public class CreateCluster extends InsertNode
{
	public NodeType getTypeToInsert()
	{
		return DiagramNode.TYPE_CLUSTER;
	}

	public String getInitialText()
	{
		return "";
	}
	
	public boolean isAvailable()
	{
		return false;
//		DiagramNode[] selectedNodes = getProject().getOnlySelectedNodes();
//		if(selectedNodes.length == 0)
//			return false;
//		
//		for(int i = 0; i < selectedNodes.length; ++i)
//		{
//			if(selectedNodes[i].isCluster())
//				return false;
//			if(selectedNodes[i].getParent() != null)
//				return false;
//		}
//		
//		return true;
	}

	public void doIt() throws CommandFailedException
	{
//		if(!isAvailable())
//			return;
//		
//		String[] buttons = {"Yes", "No",};
//		String[] text = {
//				"The Factor Grouping feature is experimental, and doesn't work very well yet.",
//				"It could even cause this project to become unusable.",
//				"Are you sure you want to try creating a Group?",
//		};
//		if(!EAM.confirmDialog("Experimental Feature", text, buttons))
//			return;
//		
//		try
//		{
//			DiagramNode[] selectedNodes = getProject().getOnlySelectedNodes();
//			BaseId id = insertNodeItself();
//			if(selectedNodes.length > 0)
//				capturePreviouslySelectedNodes(id, selectedNodes);
//		}
//		catch (Exception e)
//		{
//			throw new CommandFailedException(e);
//		}
//
	}

//	void doExtraSetup(BaseId id) throws CommandFailedException
//	{
//		Dimension originalSize = DiagramNode.getDefaultSize();
//		int newWidth = originalSize.width * 120 / 100;
//		int newHeight = originalSize.height * 320 / 100;
//		Point size = getProject().getSnapped(new Point(newWidth, newHeight));
//		Dimension newSize = new Dimension(size.x, size.y);
//		CommandSetNodeSize cmd = new CommandSetNodeSize(id, newSize, originalSize);
//		getProject().executeCommand(cmd);
//	}
//	
//	void capturePreviouslySelectedNodes(BaseId clusterId, DiagramNode[] nodes) throws CommandFailedException
//	{
//		IdList memberIds = new IdList();
//		for(int i = 0; i < nodes.length; ++i)
//		{
//			memberIds.add(nodes[i].getDiagramNodeId());
//		}
//		CommandSetObjectData capture = new CommandSetObjectData(ObjectType.MODEL_NODE, clusterId, 
//				ConceptualModelCluster.TAG_MEMBER_IDS, memberIds.toString());
//		getProject().executeCommand(capture);
//	}
	
}
