/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.diagram;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.Transferable;

import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.TransferableEamList;
import org.conservationmeasures.eam.main.TransferableMiradiList;
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
			Transferable contents = clipboard.getContents(null);
			//FIXME remove check after transition
			if (TransferableEamList.IS_EAM_FLAVOR)
				return;

			if(!contents.isDataFlavorSupported(TransferableEamList.miradiListDataFlavor))
				return;
			
			TransferableMiradiList list = (TransferableMiradiList)contents.getTransferData(TransferableEamList.miradiListDataFlavor);
			FactorCommandHelper factorCommandHelper = new FactorCommandHelper(getProject(), getDiagramView().getDiagramModel());
			if (! factorCommandHelper.canPaste(list))
			{
				EAM.notifyDialog(EAM.text("Contributing Factors and Direct Threats cannot be pasted into a Results Chain; " +
											"Intermediate Results and Threat Reduction Results cannot be pasted into a Conceptual Model."));
				return;
			}

			factorCommandHelper.pasteMiradiDataFlavor(list, getLocation());
			clipboard.incrementPasteCount();
		} 
		catch (Exception e) 
		{
			EAM.logException(e);
			throw new CommandFailedException(e);
		} 
	}

//TODO remove code after transistion to miradi flavor (keeping code for comparison reasons, making sure everthing is restored)
//	private void pasteEAMDataFlavor(DiagramClipboard clipboard) throws UnsupportedFlavorException, IOException, Exception
//	{
//		FIXME temp swith beween transitions of two flavors
//		if (! TransferableEamList.IS_EAM_FLAVOR)
//			return;
//
//		Transferable contents = clipboard.getContents(null);
//		if(!contents.isDataFlavorSupported(TransferableEamList.eamListDataFlavor))
//			return;
//		TransferableEamList list = (TransferableEamList)contents.getTransferData(TransferableEamList.eamListDataFlavor);
//		if(!list.getProjectFileName().equals(getProject().getFilename()))
//		{
//			EAM.notifyDialog(EAM.text("Paste between different Miradi projects not yet supported"));
//			return;
//		}
//		
//		FactorCommandHelper factorCommandHelper = new FactorCommandHelper(getProject(), getDiagramView().getDiagramModel());
//		if (! factorCommandHelper.canPaste(list))
//		{
//			EAM.notifyDialog(EAM.text("Contributing Factors and Direct Threats cannot be pasted into a Results Chain; " +
//										"Intermediate Results and Threat Reduction Results cannot be pasted into a Conceptual Model."));
//			return;
//		}
//		
//		pasteCellsIntoProject(list, factorCommandHelper);
//		clipboard.incrementPasteCount();
//	}
//
//
//	public void pasteCellsIntoProject(TransferableEamList list, FactorCommandHelper factorCommandHelper) throws Exception 
//	{
//		factorCommandHelper.pasteFactorsAndLinksIntoProject(list, getLocation());
//	}
}
