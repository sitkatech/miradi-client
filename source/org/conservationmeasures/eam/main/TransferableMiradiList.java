/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.main;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.text.ParseException;
import java.util.Vector;

import org.conservationmeasures.eam.diagram.cells.EAMGraphCell;
import org.conservationmeasures.eam.diagram.cells.FactorCell;
import org.conservationmeasures.eam.diagram.cells.LinkCell;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objecthelpers.ObjectDeepCopier;
import org.conservationmeasures.eam.objects.BaseObject;
import org.conservationmeasures.eam.objects.DiagramFactor;
import org.conservationmeasures.eam.objects.DiagramLink;
import org.conservationmeasures.eam.objects.Factor;
import org.conservationmeasures.eam.objects.FactorLink;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.utils.EnhancedJsonObject;

public class TransferableMiradiList implements Transferable
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
		DataFlavor[] flavorArray = {miradiListDataFlavor };
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
		createFactorDeepCopies(deepCopier, diagramFactor);
	}

	private void createFactorDeepCopies(ObjectDeepCopier deepCopier, DiagramFactor diagramFactor)
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

	public Vector getFactorDeepCopies()
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

	public static DataFlavor miradiListDataFlavor = new DataFlavor(TransferableMiradiList.class, "Miradi Objects");

	String projectName;
	Project project;
	ORef diagramObjectRefCopiedFrom;

	Vector factorDeepCopies;
	Vector diagramFactorDeepCopies;
	Vector factorLinkDeepCopies;
	Vector diagramLinkDeepCopies;
	Rectangle rectWithUpperMostLeftMostCorner;
}
