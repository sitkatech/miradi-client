/* 
Copyright 2005-2018, Foundations of Success, Bethesda, Maryland
on behalf of the Conservation Measures Partnership ("CMP").
Material developed between 2005-2013 is jointly copyright by Beneficent Technology, Inc. ("The Benetech Initiative"), Palo Alto, California.

This file is part of Miradi

Miradi is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License version 3, 
as published by the Free Software Foundation.

Miradi is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Miradi.  If not, see <http://www.gnu.org/licenses/>. 
*/ 

package org.miradi.main;

import java.awt.Rectangle;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Vector;

import org.miradi.ids.BaseId;
import org.miradi.objecthelpers.ORef;
import org.miradi.project.Project;

public class TransferableMiradiListVersion4 extends AbstractTransferableMiradiList
{
	public TransferableMiradiListVersion4(Project projectToUse,	ORef diagramObjectRefCopiedFromToUse)
	{
		super(projectToUse, diagramObjectRefCopiedFromToUse);
	}
	
	@Override
	public boolean isLegacyTransferableMiradiList()
	{
		return false;
	}
	
	@Override
	public DataFlavor[] getTransferDataFlavors()
	{
		DataFlavor[] flavorArray = {miradiListDataFlavor, TransferableMiradiList.miradiListDataFlavor };
		return flavorArray;
	}
	
	@Override
	public boolean isDataFlavorSupported(DataFlavor flavor)
	{
		DataFlavor[] flavors = getTransferDataFlavors();
		for(int i = 0; i < flavors.length; ++i)
		{
			if(flavor.equals(flavors[i]))
				return true;
		}
		
		return false;
	}

	@Override
	public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException
	{
		if(isDataFlavorSupported(flavor))
			return this;
		
		throw new UnsupportedFlavorException(flavor);
	}
	
	private void writeObject(java.io.ObjectOutputStream out) throws IOException
	{
		ObjectOutputStream objectOut = new ObjectOutputStream(out);
		
		objectOut.writeInt(diagramObjectRefCopiedFrom.getObjectType());
		objectOut.writeInt(diagramObjectRefCopiedFrom.getObjectId().asInt());
		objectOut.writeObject(projectName);
		objectOut.writeObject(factorDeepCopies);
		objectOut.writeObject(diagramFactorDeepCopies);
		objectOut.writeObject(threatStressRatingCopies);
		objectOut.writeObject(factorLinkDeepCopies);
		objectOut.writeObject(diagramLinkDeepCopies);
		objectOut.writeObject(rectWithUpperMostLeftMostCorner);
	}

	private void readObject(java.io.ObjectInputStream in) throws IOException,
			ClassNotFoundException
	{
		diagramObjectRefCopiedFrom = ORef.INVALID;
		project = null;
		ObjectInputStream objectIn = new ObjectInputStream(in);
				
		int diagramType = objectIn.readInt();
		int diagramId = objectIn.readInt();
		diagramObjectRefCopiedFrom =  new ORef(diagramType, new BaseId(diagramId)); 
		
		projectName = (String) objectIn.readObject();
		factorDeepCopies = readStringVector(objectIn);
		diagramFactorDeepCopies = readStringVector(objectIn);
		threatStressRatingCopies = readStringVector(objectIn);
		factorLinkDeepCopies = readStringVector(objectIn);
		diagramLinkDeepCopies = readStringVector(objectIn);
		rectWithUpperMostLeftMostCorner = (Rectangle) objectIn.readObject();
	}

	// TODO: Is there a way to avoid suppressing warnings here?
	//try new Vector((Vector<String>) objectIn.readObject());
	//but it needs testing since it would no longer pass by reference
	@SuppressWarnings("unchecked")
	private Vector<String> readStringVector(ObjectInputStream objectIn) throws IOException,	ClassNotFoundException
	{
		return (Vector<String>) objectIn.readObject();
	}
	
	public static final DataFlavor miradiListDataFlavor = new DataFlavor(TransferableMiradiListVersion4.class, "Miradi Objects");

	static final long serialVersionUID = 1; 

}
