/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */

package org.conservationmeasures.eam.main;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.Vector;

import org.conservationmeasures.eam.diagram.EAMGraphCell;
import org.conservationmeasures.eam.diagram.nodes.DiagramLinkage;
import org.conservationmeasures.eam.diagram.nodes.DiagramNode;
import org.conservationmeasures.eam.diagram.nodes.LinkageDataMap;
import org.conservationmeasures.eam.diagram.nodes.NodeDataMap;

public class TransferableEamList implements Transferable 
{
	public TransferableEamList (Object[] cells)
	{
		super();
		linkages = new Vector();
		nodes = new Vector();
		storeData(cells);
	}
	
	private void storeData(Object[] cells)
	{
		for (int i = 0; i < cells.length; i++) 
		{
			EAMGraphCell cell = (EAMGraphCell)cells[i];
			try {
				if(cell.isLinkage())
				{
					linkages.add(((DiagramLinkage)cell).createLinkageDataMap());
				}
				if(cell.isNode())
				{
					nodes.add(((DiagramNode)cell).createNodeDataMap());
				}
			} 
			catch (Exception e) 
			{
				e.printStackTrace();
			}
		}
	}
	
	public DataFlavor[] getTransferDataFlavors() 
	{
		DataFlavor[] flavorArray = {eamListDataFlavor};
		return flavorArray;
	}

	public boolean isDataFlavorSupported(DataFlavor flavor) 
	{
		DataFlavor[] flavors = getTransferDataFlavors();
		for(int i = 0; i < flavors.length; ++i)
			if(flavor.equals(flavors[i]))
				return true;
		return false;
	}

	public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException 
	{
		if(isDataFlavorSupported(flavor))
			return this;
		throw new UnsupportedFlavorException(flavor);
	}
	
	public LinkageDataMap[] getLinkageDataCells()
	{
		return (LinkageDataMap[])linkages.toArray(new LinkageDataMap[0]);
	}
	
	public NodeDataMap[] getNodeDataCells()
	{
		return (NodeDataMap[])nodes.toArray(new NodeDataMap[0]);
	}

	public static DataFlavor eamListDataFlavor = new DataFlavor(TransferableEamList.class, "EAM Objects");
	Vector linkages;
	Vector nodes;
}
