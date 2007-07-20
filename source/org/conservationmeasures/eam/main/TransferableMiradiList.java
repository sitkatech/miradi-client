/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.main;

import java.util.HashMap;
import java.util.Vector;

import org.conservationmeasures.eam.diagram.cells.EAMGraphCell;
import org.conservationmeasures.eam.diagram.cells.FactorCell;
import org.conservationmeasures.eam.diagram.cells.LinkCell;
import org.conservationmeasures.eam.objecthelpers.ObjectDeepCopier;
import org.conservationmeasures.eam.project.Project;


public class TransferableMiradiList extends TransferableEamList
{
	public TransferableMiradiList(Project projectToUse)
	{
		super(projectToUse);
	}

	private void clearMaps()
	{
		factorMap = new HashMap();
		diagramFactorMap = new HashMap();
		
		factorLinkMap = new HashMap();
		diagramLinkMap  = new HashMap();
	}
	
	public void storeData(Object[] cells)
	{
		clearMaps();
		ObjectDeepCopier deepCopier = new ObjectDeepCopier(project);
		int factorMapKey = 0;
		int linkMapKey = 0;
		for (int i = 0; i < cells.length; i++) 
		{
			EAMGraphCell cell = (EAMGraphCell)cells[i];
			try 
			{
				if (cell.isFactor())
				{
					Vector factorJsonStrings = deepCopier.createDeepCopy(((FactorCell)cell).getUnderlyingObject());
					factorMap.put(factorMapKey, factorJsonStrings);
					
					Vector diagramFactorJsonStrings = deepCopier.createDeepCopy(((FactorCell)cell).getDiagramFactor());
					diagramFactorMap.put(factorMapKey, diagramFactorJsonStrings);
					factorMapKey++;
				}

				if (cell.isFactorLink())
				{
					Vector factorLinkJsonStrings = deepCopier.createDeepCopy(((LinkCell) cell).getFactorLink());
					factorLinkMap.put(linkMapKey, factorLinkJsonStrings);
					
					Vector diagramLinkJsonStrings = deepCopier.createDeepCopy(((LinkCell) cell).getDiagramFactorLink());
					diagramLinkMap.put(linkMapKey, diagramLinkJsonStrings);
					linkMapKey++;
				}

			} 
			catch (Exception e) 
			{
				EAM.logException(e);
			}
		}
	}
	
	public HashMap getDiagramFactorMap()
	{
		return diagramFactorMap;
	}

	public HashMap getDiagramLinkMap()
	{
		return diagramLinkMap;
	}

	public HashMap getFactorLinkMap()
	{
		return factorLinkMap;
	}

	public HashMap getFactorMap()
	{
		return factorMap;
	}
	
	//FIXME this is to switch between falvors while in transition 
	public boolean isEAMFlavorSupported()
	{
		return false;
	}
	
	HashMap factorMap;
	HashMap diagramFactorMap;
	HashMap factorLinkMap;
	HashMap diagramLinkMap;
}
