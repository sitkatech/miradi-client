/* 
Copyright 2005-2010, Foundations of Success, Bethesda, Maryland 
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

package org.miradi.views.diagram;

import java.awt.Point;
import java.util.Vector;

import org.miradi.diagram.DiagramModel;
import org.miradi.diagram.PersistentDiagramModel;
import org.miradi.diagram.cells.EAMGraphCell;
import org.miradi.diagram.cells.FactorCell;
import org.miradi.diagram.cells.LinkCell;
import org.miradi.main.CommandExecutedEvent;
import org.miradi.main.CommandExecutedListener;
import org.miradi.main.TestCaseWithProject;
import org.miradi.main.TransferableMiradiList;
import org.miradi.objecthelpers.CreateDiagramFactorLinkParameter;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objects.Cause;
import org.miradi.objects.ConceptualModelDiagram;
import org.miradi.objects.DiagramFactor;
import org.miradi.objects.DiagramLink;
import org.miradi.objects.DiagramObject;
import org.miradi.objects.FactorLink;
import org.miradi.objects.GroupBox;
import org.miradi.objects.Target;
import org.miradi.project.FactorDeleteHelper;

public class TestDiagramAliasPaster extends TestCaseWithProject
{
	public TestDiagramAliasPaster(String name)
	{
		super(name);
	}
	
	public void testGroupBoxDiagramFactorNotCreatedDuringCutPasteAsAlias() throws Exception
	{
		DiagramFactor targetDiagramFactor = getProject().createDiagramFactorAndAddToDiagram(Target.getObjectType());
		DiagramFactor threatDiagramFactor = getProject().createDiagramFactorAndAddToDiagram(Cause.getObjectType());
		Cause threat = (Cause) threatDiagramFactor.getWrappedFactor();
		getProject().enableAsThreat(threatDiagramFactor.getWrappedORef());
		DiagramLink threatTargetDiagramLink = getProject().createDiagramLinkAndAddToDiagramModel(threatDiagramFactor, targetDiagramFactor);
		
		DiagramModel diagramModelToPasteInto = createDiagramModelToPasteInto();
		pasteAsAlias(getDiagramModel(), diagramModelToPasteInto, threatDiagramFactor);
		
		DiagramFactor groupBoxDiagramFactor = createGroupBoxDiagramFactor(threatDiagramFactor);
		DiagramLink groupBoxDiagramLink = createGroupBoxLinkWithChildren(groupBoxDiagramFactor, targetDiagramFactor, threatTargetDiagramLink);
		
		assertTrue("threat to target link is not covered by group box link?", threatTargetDiagramLink.isCoveredByGroupBoxLink());
		assertEquals("group box has incorrect child links?", 1, groupBoxDiagramLink.getGroupedDiagramLinkRefs().size());

		Vector<DiagramFactor> diagramFactorsToCutPaste = new Vector<DiagramFactor>();
		diagramFactorsToCutPaste.add(groupBoxDiagramFactor);
		diagramFactorsToCutPaste.add(targetDiagramFactor);
		diagramFactorsToCutPaste.add(threatDiagramFactor);
		
		Vector<DiagramLink> diagramLinksToCutPaste = new Vector<DiagramLink>();
		diagramLinksToCutPaste.add(groupBoxDiagramLink);
		diagramLinksToCutPaste.add(threatTargetDiagramLink);
		TransferableMiradiList transferableList = createTransferable(getDiagramModel(), diagramFactorsToCutPaste, diagramLinksToCutPaste);
		
		deleteDiagramLinkAndOrphandFactorLink(groupBoxDiagramLink);
		deleteDiagramFactors(diagramFactorsToCutPaste);
		paste(diagramModelToPasteInto, transferableList);
		
		verifyFactorLinkAfterPaste(threat, diagramModelToPasteInto);
	}

	public void testFactorLinkNotCreatedDuringCutPaste() throws Exception
	{
		DiagramFactor targetDiagramFactor = getProject().createDiagramFactorAndAddToDiagram(Target.getObjectType());
		DiagramFactor threatDiagramFactor = getProject().createDiagramFactorAndAddToDiagram(Cause.getObjectType());
		Cause threat = (Cause) threatDiagramFactor.getWrappedFactor();
		
		getProject().enableAsThreat(threatDiagramFactor.getWrappedORef());
		DiagramLink diagramLink = getProject().createDiagramLinkAndAddToDiagramModel(threatDiagramFactor, targetDiagramFactor);
		
		DiagramModel diagramModelToPasteInto = createDiagramModelToPasteInto();		
		pasteAsAlias(getDiagramModel(), diagramModelToPasteInto, threatDiagramFactor);
		
		Vector<DiagramFactor> diagramFactorsToCutPaste = new Vector<DiagramFactor>();
		diagramFactorsToCutPaste.add(targetDiagramFactor);
		diagramFactorsToCutPaste.add(threatDiagramFactor);
		
		Vector<DiagramLink> diagramLinksToCutPaste = new Vector<DiagramLink>();
		diagramLinksToCutPaste.add(diagramLink);
		TransferableMiradiList transferableList = createTransferable(getDiagramModel(), diagramFactorsToCutPaste, diagramLinksToCutPaste);
		
		deleteDiagramLinkAndOrphandFactorLink(diagramLink);
		deleteDiagramFactors(diagramFactorsToCutPaste);
		paste(diagramModelToPasteInto, transferableList);
		
		verifyFactorLinkAfterPaste(threat, diagramModelToPasteInto);
	}
	
	private void verifyFactorLinkAfterPaste(Cause threat, DiagramModel diagramModelToPasteInto)
	{
		ORef factorLinkRef = getProject().getFactorLinkPool().getRefList().getFirstElement();
		FactorLink factorLink = FactorLink.find(getProject(), factorLinkRef);
		DiagramLink pastedDiagramLink = diagramModelToPasteInto.getDiagramObject().getDiagramFactorLink(factorLinkRef);
		assertNotNull("Diagram Link not found?", pastedDiagramLink);
		assertEquals("FactorLink from is not the original threat?", threat.getRef(), factorLink.getFromFactorRef());
		assertTrue("FactorLink to is not a target?", Target.is(factorLink.getToFactorRef()));
	}

	private DiagramModel createDiagramModelToPasteInto() throws Exception
	{
		DiagramModel diagramModelToPasteInto = new PersistentDiagramModel(getProject());
		ORef diagramObjectRef = getProject().createObject(ConceptualModelDiagram.getObjectType());
		DiagramObject diagramObject = DiagramObject.findDiagramObject(getProject(), diagramObjectRef);
		diagramModelToPasteInto.fillFrom(diagramObject);
		new ModelUpdater(diagramModelToPasteInto);
		
		return diagramModelToPasteInto;
	}
	
	private void deleteDiagramFactors(Vector<DiagramFactor> diagramFactorsToCutPaste) throws Exception
	{
		FactorDeleteHelper helper = FactorDeleteHelper.createFactorDeleteHelperForNonSelectedFactors(getDiagramModel().getDiagramObject());
		for (int index = 0; index < diagramFactorsToCutPaste.size(); ++index)
		{
			helper.deleteFactorAndDiagramFactor(diagramFactorsToCutPaste.get(index));
		}
	}

	private void deleteDiagramLinkAndOrphandFactorLink(DiagramLink diagramLink) throws Exception
	{
		LinkDeletor linkDeletor = new LinkDeletor(getProject());
		linkDeletor.deleteDiagramLinkAndOrphandFactorLink(diagramLink);
	}
	
	private void pasteAsAlias(DiagramModel diagramModelToCopyFrom, DiagramModel diagramModelToPasteInto, DiagramFactor diagramFactor) throws Exception
	{
		TransferableMiradiList transferableList = createTransferable(diagramModelToCopyFrom, diagramFactor);
		paste(diagramModelToPasteInto, transferableList);
	}
	
	private TransferableMiradiList createTransferable(DiagramModel diagramModelToCopyFrom, DiagramFactor diagramFactor) throws Exception
	{
		Vector<DiagramFactor> diagramFactors = new Vector<DiagramFactor>();
		diagramFactors.add(diagramFactor);
		
		return createTransferable(diagramModelToCopyFrom, diagramFactors, new Vector<DiagramLink>());
	}
	
	private TransferableMiradiList createTransferable(DiagramModel diagramModelToCopyFrom, Vector<DiagramFactor> diagramFactors, Vector<DiagramLink> diagramLinks) throws Exception
	{
		Vector<EAMGraphCell> cellsToCopy = new Vector();
		for(DiagramLink diagramLink : diagramLinks)
		{
			LinkCell linkCell = diagramModelToCopyFrom.getLinkCell(diagramLink);
			cellsToCopy.add(linkCell);
		}
	
		for(DiagramFactor diagramFactor : diagramFactors)
		{
			FactorCell factorCell = diagramModelToCopyFrom.getFactorCellByRef(diagramFactor.getRef());
			cellsToCopy.add(factorCell);
		}
		
		EAMGraphCell[] cellsToCopyAsArray = cellsToCopy.toArray(new EAMGraphCell[0]);
		TransferableMiradiList transferableList = new TransferableMiradiList(getProject(), diagramModelToCopyFrom.getDiagramObject().getRef());
		transferableList.storeData(cellsToCopyAsArray);
		
		return transferableList;
	}

	private void paste(DiagramModel diagramModelToPasteInto, TransferableMiradiList transferableList) throws Exception
	{
		DiagramPaster paster = new DiagramAliasPaster(null, diagramModelToPasteInto, transferableList);
		paster.pasteFactorsAndLinks(new Point(0, 0));
	}
	
	private DiagramFactor createGroupBoxDiagramFactor(DiagramFactor groupBoxChild) throws Exception
	{
		DiagramFactor groupBox = getProject().createDiagramFactorAndAddToDiagram(GroupBox.getObjectType());
		ORefList groupBoxChildrenRefs = new ORefList(groupBoxChild);
		
		getProject().fillObjectUsingCommand(groupBox, DiagramFactor.TAG_GROUP_BOX_CHILDREN_REFS, groupBoxChildrenRefs.toString());
		
		return groupBox;
	}
	
	private DiagramLink createGroupBoxLinkWithChildren(DiagramFactor from, DiagramFactor to, DiagramLink diagramLinkToCover) throws Exception
	{
		ORefList coveredLinkRefs = new ORefList(diagramLinkToCover);
		CreateDiagramFactorLinkParameter extraInfoWithNoFactorLink = new CreateDiagramFactorLinkParameter(from.getRef(), to.getRef());
		LinkCreator linkCreator = new LinkCreator(getProject());
		ORef newGroupBoxDiagramLinkRef = linkCreator.createDiagramLink(getDiagramModel().getDiagramObject(), extraInfoWithNoFactorLink);
		linkCreator.updateGroupBoxChildrenRefs(coveredLinkRefs, newGroupBoxDiagramLinkRef);		
		
		return DiagramLink.find(getProject(), newGroupBoxDiagramLinkRef);
	}
	
	private class ModelUpdater implements CommandExecutedListener
	{
		public ModelUpdater(DiagramModel diagramModel)
		{
			getProject().addCommandExecutedListener(this);
			modelUpdater = new DiagramModelUpdater(getProject(), diagramModel);
		}

		public void commandExecuted(CommandExecutedEvent event)
		{
			if (event.isSetDataCommand())
				modelUpdater.commandSetObjectDataWasExecuted(event.getSetCommand());
		}
		
		private DiagramModelUpdater modelUpdater;
	}
}
