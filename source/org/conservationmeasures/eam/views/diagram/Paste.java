/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.diagram;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.TransferableEamList;
import org.conservationmeasures.eam.project.FactorCommandHelper;

public class Paste extends LocationDoer
{
	public boolean isAvailable()
	{
		if(!getProject().isOpen())
			return false;
		
		if (! isDiagramView())
			return false;
		
		Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		Transferable contents = clipboard.getContents(null);
		if(contents == null)
			return false;
		return contents.isDataFlavorSupported(TransferableEamList.eamListDataFlavor);
	}

	public void doIt() throws CommandFailedException
	{
		DiagramClipboard clipboard = getProject().getDiagramClipboard();
		try 
		{
			pasteEAMDataFlavor(clipboard);
			pasteMiradiDataFlavor(clipboard);
		} 
		catch (Exception e) 
		{
			EAM.logException(e);
			throw new CommandFailedException(e);
		} 
	}

	private void pasteEAMDataFlavor(DiagramClipboard clipboard) throws UnsupportedFlavorException, IOException, Exception
	{
		Transferable contents = clipboard.getContents(null);
		if(!contents.isDataFlavorSupported(TransferableEamList.eamListDataFlavor))
			return;
		TransferableEamList list = (TransferableEamList)contents.getTransferData(TransferableEamList.eamListDataFlavor);
		if(!list.getProjectFileName().equals(getProject().getFilename()))
		{
			EAM.notifyDialog(EAM.text("Paste between different Miradi projects not yet supported"));
			return;
		}
		
		FactorCommandHelper factorCommandHelper = new FactorCommandHelper(getProject(), getDiagramView().getDiagramModel());
		if (! factorCommandHelper.canPaste(list))
		{
			EAM.notifyDialog(EAM.text("Contributing Factors and Direct Threats cannot be pasted into a Results Chain; " +
										"Intermediate Results and Threat Reduction Results cannot be pasted into a Conceptual Model."));
			return;
		}
		
		pasteCellsIntoProject(list, factorCommandHelper);
		clipboard.incrementPasteCount();
	}

	private void pasteMiradiDataFlavor(DiagramClipboard clipboard) throws Exception
	{
		Transferable contents = clipboard.getContents(null);
		if(!contents.isDataFlavorSupported(TransferableEamList.miradiListDataFlavor))
			return;
		
//		FIXME nima copy/paste now add deep copies of selected objects
//		TransferableEamList list = (TransferableEamList)contents.getTransferData(TransferableEamList.miradiListDataFlavor);
//		FactorDataMap[] nodes = list.getArrayOfFactorDataMaps();
//		for (int i = 0; i < nodes.length; i++) 
//		{
//			FactorDataMap nodeData = nodes[i];
//			String label = nodeData.getLabel();
//			System.out.println("lanbel = " + label);
//		}
	}

	public void pasteCellsIntoProject(TransferableEamList list, FactorCommandHelper factorCommandHelper) throws Exception 
	{
		factorCommandHelper.pasteFactorsAndLinksIntoProject(list, getLocation());
	}
}
