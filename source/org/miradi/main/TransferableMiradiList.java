/* 
Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
(on behalf of the Conservation Measures Partnership, "CMP") and 
Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 

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

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.text.ParseException;
import java.util.Vector;

import org.miradi.diagram.cells.EAMGraphCell;
import org.miradi.diagram.cells.FactorCell;
import org.miradi.diagram.cells.LinkCell;
import org.miradi.ids.BaseId;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ObjectDeepCopier;
import org.miradi.objects.BaseObject;
import org.miradi.objects.DiagramFactor;
import org.miradi.objects.DiagramLink;
import org.miradi.objects.Factor;
import org.miradi.objects.FactorLink;
import org.miradi.project.Project;
import org.miradi.utils.EnhancedJsonObject;

public class TransferableMiradiList implements Transferable, Serializable
{
	public TransferableMiradiList(Project projectToUse, ORef diagramObjectRefCopiedFromToUse)
	{
		super();
		project = projectToUse;
		projectName = project.getFilename();
		diagramObjectRefCopiedFrom = diagramObjectRefCopiedFromToUse;
	}

	public String getProjectFileName()
	{
		return projectName;
	}

	public DataFlavor[] getTransferDataFlavors()
	{
		DataFlavor[] flavorArray = {miradiListDataFlavor, DataFlavor.stringFlavor };
		return flavorArray;
	}

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

	public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException
	{
		if(isDataFlavorSupported(flavor))
			return this;
		
		throw new UnsupportedFlavorException(flavor);
	}

	private void clear()
	{
		factorDeepCopies = new Vector();
		diagramFactorDeepCopies = new Vector();
		
		factorLinkDeepCopies = new Vector();
		diagramLinkDeepCopies = new Vector();
	}

	public void storeData(Object[] cells)
	{
		clear();
		ObjectDeepCopier deepCopier = new ObjectDeepCopier(project);
		for (int i = 0; i < cells.length; i++) 
		{
			EAMGraphCell cell = (EAMGraphCell)cells[i];
			addFactorDeepCopies(deepCopier, cell);
			addFactorLinkDeepCopies(deepCopier, cell);
		}
	}

	private void addFactorDeepCopies(ObjectDeepCopier deepCopier, EAMGraphCell cell)
	{
		if (! cell.isFactor())
			return;
		
		FactorCell factorCell = (FactorCell) cell;
		DiagramFactor diagramFactor = factorCell.getDiagramFactor();
		addFactorDeepCopies(deepCopier, diagramFactor);
	}

	private void addFactorDeepCopies(ObjectDeepCopier deepCopier, DiagramFactor diagramFactor)
	{
		Factor factor = diagramFactor.getWrappedFactor();		
		Vector factorJsonStrings = deepCopier.createDeepCopy(factor);
		factorDeepCopies.addAll(factorJsonStrings);
	
		Vector diagramFactorJsonStrings = deepCopier.createDeepCopy(diagramFactor);
		diagramFactorDeepCopies.addAll(diagramFactorJsonStrings);
		
		addToUpperMostLeftMostCorner(diagramFactor);
	}

	private void addToUpperMostLeftMostCorner(DiagramFactor diagramFactor)
	{
		Point location = diagramFactor.getLocation();
		if (rectWithUpperMostLeftMostCorner == null)
			rectWithUpperMostLeftMostCorner = new Rectangle(location);
		
		rectWithUpperMostLeftMostCorner.add(location);
	}

	private void addFactorLinkDeepCopies(ObjectDeepCopier deepCopier, EAMGraphCell cell)
	{
		if (! cell.isFactorLink())
			return;
		
		LinkCell linkCell = (LinkCell) cell;
		FactorLink factorLink = linkCell.getFactorLink();
		Vector factorLinkJsonStrings = deepCopier.createDeepCopy(factorLink);
		factorLinkDeepCopies.addAll(factorLinkJsonStrings);
	
		DiagramLink diagramLink = linkCell.getDiagramLink();
		Vector diagramLinkJsonStrings = deepCopier.createDeepCopy(diagramLink);
		diagramLinkDeepCopies.addAll(diagramLinkJsonStrings);	
	}
	
	public boolean atleastOneFactorExists() throws ParseException
	{
		if (project == null)
			return false;
		
		for (int i = 0; i < factorDeepCopies.size(); ++i)
		{
			String jsonAsString = (String) factorDeepCopies.get(i);
			EnhancedJsonObject json = new EnhancedJsonObject(jsonAsString);
			int objectToBeFoundType = json.getInt("Type");
			BaseId objectToBeFoundId = json.getId(BaseObject.TAG_ID);
			BaseObject foundObject = project.findObject(new ORef(objectToBeFoundType, objectToBeFoundId));
			if (foundObject != null)
				return true;
		}
		
		return false;
	}

	
	public Vector getDiagramFactorDeepCopies()
	{
		return diagramFactorDeepCopies;
	}

	public Vector<String> getFactorDeepCopies()
	{
		return factorDeepCopies;
	}

	public Vector getDiagramLinkDeepCopies()
	{
		return diagramLinkDeepCopies;
	}

	public Vector getFactorLinkDeepCopies()
	{
		return factorLinkDeepCopies;
	}
	
	public Point getUpperMostLeftMostCorner()
	{
		if (rectWithUpperMostLeftMostCorner == null)
			return new Point(0, 0);
		
		return rectWithUpperMostLeftMostCorner.getLocation();
	}
	
	public ORef getDiagramObjectRefCopiedFrom()
	{
		return diagramObjectRefCopiedFrom;
	}

	private void writeObject(java.io.ObjectOutputStream out)  throws IOException
	{
		ObjectOutputStream objectOut = new ObjectOutputStream(out);
		
		objectOut.writeInt(diagramObjectRefCopiedFrom.getObjectType());
		objectOut.writeObject(projectName);
		objectOut.writeObject(factorDeepCopies);
		objectOut.writeObject(diagramFactorDeepCopies);
		objectOut.writeObject(factorLinkDeepCopies);
		objectOut.writeObject(diagramLinkDeepCopies);
		objectOut.writeObject(rectWithUpperMostLeftMostCorner);
	}
	
	private void readObject(java.io.ObjectInputStream in)      throws IOException, ClassNotFoundException
	{
		
		diagramObjectRefCopiedFrom = ORef.INVALID;
		project = null;
		ObjectInputStream objectIn = new ObjectInputStream(in);
				
		int diagramType = objectIn.readInt();
		diagramObjectRefCopiedFrom =  new ORef(diagramType, BaseId.INVALID);
		
		projectName = (String) objectIn.readObject();
		factorDeepCopies = (Vector) objectIn.readObject();
		diagramFactorDeepCopies = (Vector) objectIn.readObject();
		factorLinkDeepCopies = (Vector) objectIn.readObject();
		diagramLinkDeepCopies = (Vector) objectIn.readObject();
		rectWithUpperMostLeftMostCorner = (Rectangle) objectIn.readObject();
	}
		
	public static DataFlavor miradiListDataFlavor = new DataFlavor(TransferableMiradiList.class, "Miradi Objects");

	private Project project;
	
	private ORef diagramObjectRefCopiedFrom;
	private String projectName;
	private Vector factorDeepCopies;
	private Vector diagramFactorDeepCopies;
	private Vector factorLinkDeepCopies;
	private Vector diagramLinkDeepCopies;
	private Rectangle rectWithUpperMostLeftMostCorner;
	
	static final long serialVersionUID = 1; 
}
