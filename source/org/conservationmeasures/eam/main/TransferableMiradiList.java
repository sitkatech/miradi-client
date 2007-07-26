/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.main;

import java.util.Vector;

import org.conservationmeasures.eam.diagram.cells.EAMGraphCell;
import org.conservationmeasures.eam.diagram.cells.FactorCell;
import org.conservationmeasures.eam.diagram.cells.LinkCell;
import org.conservationmeasures.eam.objecthelpers.ObjectDeepCopier;
import org.conservationmeasures.eam.objects.DiagramFactor;
import org.conservationmeasures.eam.objects.DiagramLink;
import org.conservationmeasures.eam.objects.Factor;
import org.conservationmeasures.eam.objects.FactorLink;
import org.conservationmeasures.eam.project.Project;


public class TransferableMiradiList extends TransferableEamList
{
	public TransferableMiradiList(Project projectToUse)
	{
		super(projectToUse);
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
		Factor factor = factorCell.getUnderlyingObject();
		Vector factorJsonStrings = deepCopier.createDeepCopy(factor);
		factorDeepCopies.addAll(factorJsonStrings);

		DiagramFactor diagramFactor = factorCell.getDiagramFactor();
		Vector diagramFactorJsonStrings = deepCopier.createDeepCopy(diagramFactor);
		diagramFactorDeepCopies.addAll(diagramFactorJsonStrings);	
	}
	
	private void addFactorLinkDeepCopies(ObjectDeepCopier deepCopier, EAMGraphCell cell)
	{
		if (! cell.isFactorLink())
			return;
		
		LinkCell linkCell = (LinkCell) cell;
		FactorLink factorLink = linkCell.getFactorLink();
		Vector factorLinkJsonStrings = deepCopier.createDeepCopy(factorLink);
		factorLinkDeepCopies.addAll(factorLinkJsonStrings);

		DiagramLink diagramLink = linkCell.getDiagramFactorLink();
		Vector diagramLinkJsonStrings = deepCopier.createDeepCopy(diagramLink);
		diagramLinkDeepCopies.addAll(diagramLinkJsonStrings);	
	}
	
	//FIXME this is to switch between falvors while in transition 
	public boolean isEAMFlavorSupported()
	{
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
	
	Vector factorDeepCopies;
	Vector diagramFactorDeepCopies;
	Vector factorLinkDeepCopies;
	Vector diagramLinkDeepCopies;
}
