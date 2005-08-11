/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.diagram;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;

import javax.swing.Action;

import org.conservationmeasures.eam.actions.ActionContextualHelp;
import org.conservationmeasures.eam.actions.ActionRedo;
import org.conservationmeasures.eam.actions.ActionUndo;
import org.conservationmeasures.eam.commands.CommandDiagramMove;
import org.conservationmeasures.eam.diagram.nodes.CellViewFactory;
import org.conservationmeasures.eam.diagram.nodes.EAMGraphCell;
import org.conservationmeasures.eam.diagram.nodes.Node;
import org.conservationmeasures.eam.main.ComponentWithContextMenu;
import org.conservationmeasures.eam.main.KeyBinder;
import org.conservationmeasures.eam.main.MainWindow;
import org.jgraph.JGraph;

public class DiagramView extends JGraph implements ComponentWithContextMenu
{
	public DiagramView(MainWindow mainWindowToUse, DiagramModel model)
	{
		super(model);
		mainWindow = mainWindowToUse;
		diagramContextMenuHandler = new DiagramContextMenuHandler(this);
		getGraphLayoutCache().setFactory(new CellViewFactory());
		installKeyBindings();
		MouseHandler mouseHandler = new MouseHandler(this);
		addMouseListener(mouseHandler);
		addGraphSelectionListener(mouseHandler);
		setDisconnectable(false);
		setDisconnectOnMove(false);
		disableInPlaceEditing();
		setBendable(false);
	}

	private void disableInPlaceEditing() 
	{
		setEditClickCount(0);
	}

	public MainWindow getMainWindow()
	{
		return mainWindow;
	}
	
	public DiagramModel getDiagramModel()
	{
		return (DiagramModel)getModel();
	}
	
	public void nodesWereMoved(int deltaX, int deltaY, int[] ids)
	{
		mainWindow.recordCommand(new CommandDiagramMove(deltaX, deltaY, ids));
	}
	
	public void showContextMenu(MouseEvent e)
	{
		diagramContextMenuHandler.showContextMenu(e);
	}
	
	public Node getSelectedNode()
	{
		if (getSelectionCount() != 1)
			return null;
		EAMGraphCell selectedCell = (EAMGraphCell)getSelectionCell();
		if(!selectedCell.isNode())
			return null;
		return (Node)selectedCell;
	}
	private void installKeyBindings()
	{
		Action helpAction = new ActionContextualHelp(mainWindow);
		KeyBinder.bindKey(this, KeyEvent.VK_F1, KeyBinder.KEY_MODIFIER_NONE, helpAction);
		Action undoAction = new ActionUndo(mainWindow);
		KeyBinder.bindKey(this, KeyEvent.VK_Z, KeyBinder.KEY_MODIFIER_CTRL, undoAction);
		Action redoAction = new ActionRedo(mainWindow);
		KeyBinder.bindKey(this, KeyEvent.VK_Y, KeyBinder.KEY_MODIFIER_CTRL, redoAction);
	}
	
	public Vector getAllSelectedCellsWithLinkages() 
	{
		Object[] selectedCells = getSelectionCells();
		Vector selectedRelatedCells = new Vector();
		for(int i=0; i < selectedCells.length; ++i)
		{
			EAMGraphCell cell = (EAMGraphCell)selectedCells[i];
			if(cell.isLinkage())
			{
				if(!selectedRelatedCells.contains(cell))
					selectedRelatedCells.add(cell);
			}
			else if(cell.isNode())
			{
				DiagramModel model = mainWindow.getProject().getDiagramModel();
				Set linkages = model.getLinkages((Node)cell);
				for (Iterator iter = linkages.iterator(); iter.hasNext();) 
				{
					EAMGraphCell link = (EAMGraphCell) iter.next();
					if(selectedRelatedCells.contains(link))
						selectedRelatedCells.add(link);
				}
				selectedRelatedCells.addAll(linkages);
				selectedRelatedCells.add(cell);
			}
		}
		return selectedRelatedCells;
	}

	MainWindow mainWindow;
	DiagramContextMenuHandler diagramContextMenuHandler;
}

