/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.actions;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;

import org.conservationmeasures.eam.commands.CommandDeleteLinkage;
import org.conservationmeasures.eam.commands.CommandDeleteNode;
import org.conservationmeasures.eam.commands.CommandDiagramMove;
import org.conservationmeasures.eam.commands.CommandSetNodeText;
import org.conservationmeasures.eam.diagram.DiagramModel;
import org.conservationmeasures.eam.diagram.nodes.EAMGraphCell;
import org.conservationmeasures.eam.diagram.nodes.Linkage;
import org.conservationmeasures.eam.diagram.nodes.Node;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.main.Project;

public class ActionDelete extends MainWindowAction
{
	public ActionDelete(MainWindow mainWindowToUse)
	{
		super(mainWindowToUse, getLabel(), "icons/delete.gif");
	}

	private static String getLabel()
	{
		return EAM.text("Action|Delete");
	}

	public void doAction(ActionEvent event) throws CommandFailedException
	{
		DiagramModel model = getMainWindow().getProject().getDiagramModel();
		Object[] selectedCells = getMainWindow().getDiagramComponent().getSelectionCells();

		Set linkages = new HashSet();
		Vector nodes = new Vector();
		for(int i=0; i < selectedCells.length; ++i)
		{
			EAMGraphCell cell = (EAMGraphCell)selectedCells[i];
			if(cell.isLinkage())
			{
				if(!linkages.contains(cell))
					linkages.add(cell);
			}
			else if(cell.isNode())
			{
				linkages.addAll(model.getLinkages((Node)cell));
				nodes.add(cell);
			}
		}
		
		Iterator iter = linkages.iterator();
		while(iter.hasNext())
		{
			Linkage linkage = (Linkage)iter.next(); 
			deleteLinkage(linkage);			
		}
		
		for(int i=0; i < nodes.size(); ++i)
		{
			deleteNode((Node)nodes.get(i));			
		}
	}
	
	private void deleteLinkage(Linkage linkageToDelete) throws CommandFailedException
	{
		Project project = getMainWindow().getProject();
		int id = project.getDiagramModel().getLinkageId(linkageToDelete);
		CommandDeleteLinkage command = new CommandDeleteLinkage(id);
		project.executeCommand(command);
	}

	private void deleteNode(Node nodeToDelete) throws CommandFailedException
	{
		Project project = getMainWindow().getProject();
		int id = project.getDiagramModel().getNodeId(nodeToDelete);

		Point location = nodeToDelete.getLocation();
		CommandDiagramMove moveToZeroZero = new CommandDiagramMove(-location.x, -location.y, new int[] {id});
		CommandSetNodeText clearText = new CommandSetNodeText(id, "");
		CommandDeleteNode command = new CommandDeleteNode(id);
		
		project.executeCommand(moveToZeroZero);
		project.executeCommand(clearText);
		project.executeCommand(command);
	}

}

