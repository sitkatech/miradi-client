/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.main;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.Vector;

import org.conservationmeasures.eam.diagram.cells.EAMGraphCell;
import org.conservationmeasures.eam.diagram.cells.FactorCell;
import org.conservationmeasures.eam.diagram.cells.FactorDataMap;
import org.conservationmeasures.eam.diagram.cells.FactorLinkDataMap;
import org.conservationmeasures.eam.diagram.factortypes.FactorType;
import org.conservationmeasures.eam.objects.Factor;
import org.conservationmeasures.eam.project.Project;

public class TransferableEamList implements Transferable
{
	public TransferableEamList(Project projectToUse)
	{
		super();
		project = projectToUse;
		projectName = project.getFilename();
	}

	public TransferableEamList(String projectFileName, Object[] cells)
	{
		super();
		projectName = projectFileName;
		links = new Vector();
		factors = new Vector();
		storeData(cells);
	}

	public String getProjectFileName()
	{
		return projectName;
	}

	protected void storeData(Object[] cells)
	{
		for(int i = 0; i < cells.length; i++)
		{
			EAMGraphCell cell = (EAMGraphCell) cells[i];
			try
			{
				if(cell.isFactorLink())
				{
					links.add(cell.getDiagramFactorLink().createLinkageDataMap());
				}
				if(cell.isFactor())
				{
					Factor factor = ((FactorCell) cell).getUnderlyingObject();
					FactorType factorType = factor.getNodeType();
					FactorDataMap factorDataMap = cell.getDiagramFactor().createFactorDataMap(factorType.toString(), factor.getLabel());
					factors.add(factorDataMap);
				}
			}
			catch(Exception e)
			{
				EAM.logException(e);
			}
		}
	}

	public DataFlavor[] getTransferDataFlavors()
	{
		DataFlavor[] flavorArray = { eamListDataFlavor, miradiListDataFlavor };
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

	public FactorLinkDataMap[] getArrayOfFactorLinkDataMaps()
	{
		return (FactorLinkDataMap[]) links.toArray(new FactorLinkDataMap[0]);
	}

	public FactorDataMap[] getArrayOfFactorDataMaps()
	{
		return (FactorDataMap[]) factors.toArray(new FactorDataMap[0]);
	}

	// FIXME this is to switch between falvors while in transition
	public static final boolean IS_EAM_FLAVOR = true;

	public static DataFlavor eamListDataFlavor = new DataFlavor(TransferableEamList.class, "EAM Objects");
	public static DataFlavor miradiListDataFlavor = new DataFlavor(TransferableEamList.class, "Miradi Objects");

	String projectName;
	Vector links;
	Vector factors;
	Project project;
}
