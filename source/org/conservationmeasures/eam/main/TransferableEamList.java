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

import org.conservationmeasures.eam.diagram.nodes.EAMGraphCell;
import org.conservationmeasures.eam.diagram.nodes.LinkageData;
import org.conservationmeasures.eam.diagram.nodes.NodeData;

public class TransferableEamList implements Transferable 
{
	public TransferableEamList (EAMGraphCell[] cells)
	{
		super();
		linkages = new Vector();
		nodes = new Vector();
		storeData(cells);
	}
	
	private void storeData(EAMGraphCell[] cells)
	{
		for (int i = 0; i < cells.length; i++) 
		{
			try {
				if(cells[i].isLinkage())
				{
					linkages.add(new LinkageData(cells[i]));
				}
				if(cells[i].isNode())
				{
					nodes.add(new NodeData(cells[i]));
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
	
	public LinkageData[] getLinkageDataCells()
	{
		return (LinkageData[])linkages.toArray(new LinkageData[0]);
	}
	
	public NodeData[] getNodeDataCells()
	{
		return (NodeData[])nodes.toArray(new NodeData[0]);
	}

	static DataFlavor eamListDataFlavor = new DataFlavor(TransferableEamList.class, "EAM Objects");
	Vector linkages;
	Vector nodes;
}
