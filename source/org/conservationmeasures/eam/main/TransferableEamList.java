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

public class TransferableEamList implements Transferable 
{
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
	
	static DataFlavor eamListDataFlavor = new DataFlavor(TransferableEamList.class, "EAM Objects");
}
