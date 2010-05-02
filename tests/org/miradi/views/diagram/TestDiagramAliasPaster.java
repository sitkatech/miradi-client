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
import org.miradi.objecthelpers.ORefSet;
import org.miradi.objects.BaseObject;
import org.miradi.objects.Cause;
import org.miradi.objects.ConceptualModelDiagram;
import org.miradi.objects.DiagramFactor;
import org.miradi.objects.DiagramLink;
import org.miradi.objects.DiagramObject;
import org.miradi.objects.FactorLink;
import org.miradi.objects.GroupBox;
import org.miradi.objects.Target;
import org.miradi.project.FactorDeleteHelper;
import org.miradi.project.ProjectRepairer;

public class TestDiagramAliasPaster extends TestCaseWithProject
{
	public TestDiagramAliasPaster(String name)
	{
		super(name);
	}
	
	@Override
	public void setUp() throws Exception
	{
		super.setUp();
		
		targetDiagramFactor = getProject().createDiagramFactorAndAddToDiagram(Target.getObjectType());
		threatDiagramFactor = getProject().createDiagramFactorAndAddToDiagram(Cause.getObjectType());
		
		target = (Target) targetDiagramFactor.getWrappedFactor();
		threat = (Cause) threatDiagramFactor.getWrappedFactor();
		getProject().enableAsThreat(threatDiagramFactor.getWrappedORef());
		
		diagramModelToPasteInto = createDiagramModelToPasteInto();
	}

	public void testPasteSharedGroup() throws Exception
	{
		// get rid of target
		Vector<DiagramFactor> targetAsVector = new Vector<DiagramFactor>();
		targetAsVector.add(targetDiagramFactor);
		deleteDiagramFactors(getDiagramObject(), targetAsVector);

		wrapThreatWithGroupBox();

		// Copy/paste-shared grouped threat into other diagram
		TransferableMiradiList transferableListBeforeCut = createTransferable(getDiagramModel(), getDiagramModel().getAllDiagramFactors(), new Vector());
		pasteShared(diagramModelToPasteInto, transferableListBeforeCut);
		
		// Cut from second diagram, then paste-shared back into first diagram
		// where the threat and group still exist
		TransferableMiradiList transferableList = createTransferable(diagramModelToPasteInto, diagramModelToPasteInto.getAllDiagramFactors(), new Vector());
		deleteDiagramFactors(diagramModelToPasteInto.getDiagramObject(), diagramModelToPasteInto.getAllDiagramFactors());
		pasteShared(getDiagramModel(), transferableList);
		
		DiagramObject diagramObject = getDiagramObject();
		ORefList diagramFactorRefs = diagramObject.getAllDiagramFactorRefs();
		DiagramFactor pastedThreatDiagramFactor = null;
		DiagramFactor pastedGroupDiagramFactor = null;
		for(int i = 0; i < diagramFactorRefs.size(); ++i)
		{
			DiagramFactor diagramFactor = DiagramFactor.find(getProject(), diagramFactorRefs.get(i));
			if(diagramFactor.isGroupBoxFactor())
			{
				// FIXME: I think the actual desired end result is two groups, 
				// with the newly pasted one having no children
				assertNull("More than one group?", pastedGroupDiagramFactor);
				pastedGroupDiagramFactor = diagramFactor;
			}
			else if(Cause.is(diagramFactor.getWrappedORef()))
			{
				assertNull("More than one threat?", pastedThreatDiagramFactor);
				pastedThreatDiagramFactor = diagramFactor;
			}
			else
			{
				fail("Unexpected factor: " + diagramFactor.getRef() + " wraps " + diagramFactor.getWrappedORef());
			}
		}
		
		// FIXME: I think the actual desired end result is two groups, 
		// with the newly pasted one having no children
		assertEquals("Don't have threat+group diagram factors?", 2, diagramFactorRefs.size());

		assertEquals("Threat not shared?", threat.getRef(), pastedThreatDiagramFactor.getWrappedORef());
		assertEquals("Threat not shared twice?", 2, threat.findObjectsThatReferToUs(DiagramFactor.getObjectType()).size());

		// FIXME: I think the actual desired end result is two groups, 
		// with the newly pasted one having no children
		ORefSet children = pastedGroupDiagramFactor.getGroupBoxChildrenSet();
		assertEquals("Group doesn't contain one factor?", 1, children.size());
		assertContains("Group doesn't wrap threat?", pastedThreatDiagramFactor.getRef(), children);
		
		ProjectRepairer repairer = new ProjectRepairer(getProject());
		assertEquals("Orphaned threats?", 0, repairer.getFactorsWithoutDiagramFactors(Cause.getObjectType()).size());
		assertEquals("Orphaned groups?", 0, repairer.getFactorsWithoutDiagramFactors(GroupBox.getObjectType()).size());
	}
	
	public void testPasteSharedGroupWithLinkOnlyThreatExists() throws Exception
	{	
		threatTargetDiagramLink = createThreatTargetLink();
		wrapThreatAndThreatTargetLinkWithGroupBox();
		
		pasteShared(getDiagramModel(), diagramModelToPasteInto, threatDiagramFactor);
		cutPasteAll(diagramModelToPasteInto);
		verifyGroupContainingThreatLinkedToTarget(diagramModelToPasteInto);
	}
	
	public void testPasteSharedGroupWithLinkOnlyTargetExists() throws Exception
	{	
		threatTargetDiagramLink = createThreatTargetLink();
		wrapThreatAndThreatTargetLinkWithGroupBox();
		
		pasteShared(getDiagramModel(), diagramModelToPasteInto, targetDiagramFactor);
		cutPasteAll(diagramModelToPasteInto);
		verifyGroupContainingThreatLinkedToTarget(diagramModelToPasteInto);
	}
	
	public void testPasteSharedGroupWithLinkOnlyGroupExists() throws Exception
	{	
		threatTargetDiagramLink = createThreatTargetLink();
		wrapThreatAndThreatTargetLinkWithGroupBox();

		pasteShared(getDiagramModel(), diagramModelToPasteInto, groupBoxDiagramFactor);
		cutPasteAll(diagramModelToPasteInto);
		verifyGroupContainingThreatLinkedToTarget(diagramModelToPasteInto);
	}
	
	public void testPasteSharedGroupWithLinkOnlyGroupAndThreatExist() throws Exception
	{	
		threatTargetDiagramLink = createThreatTargetLink();
		wrapThreatAndThreatTargetLinkWithGroupBox();
	
		pasteShared(getDiagramModel(), diagramModelToPasteInto, threatDiagramFactor);
		pasteShared(getDiagramModel(), diagramModelToPasteInto, groupBoxDiagramFactor);
		cutPasteAll(diagramModelToPasteInto);
		verifyGroupContainingThreatLinkedToTarget(diagramModelToPasteInto);
	}
	
	public void testPasteSharedGroupWithLinkOnlyThreatAndTargetExist() throws Exception
	{	
		threatTargetDiagramLink = createThreatTargetLink();
		wrapThreatAndThreatTargetLinkWithGroupBox();
	
		pasteShared(getDiagramModel(), diagramModelToPasteInto, threatDiagramFactor);
		pasteShared(getDiagramModel(), diagramModelToPasteInto, targetDiagramFactor);
		cutPasteAll(diagramModelToPasteInto);
		verifyGroupContainingThreatLinkedToTarget(diagramModelToPasteInto);
	}
	
	public void testPasteSharedGroupWithLinkOnlyGroupAndTargetExist() throws Exception
	{	
		threatTargetDiagramLink = createThreatTargetLink();
		wrapThreatAndThreatTargetLinkWithGroupBox();
	
		pasteShared(getDiagramModel(), diagramModelToPasteInto, targetDiagramFactor);
		pasteShared(getDiagramModel(), diagramModelToPasteInto, groupBoxDiagramFactor);
		cutPasteAll(diagramModelToPasteInto);
		verifyGroupContainingThreatLinkedToTarget(diagramModelToPasteInto);
	}
	
	public void testPasteSharedGroupWithLinkAllExist() throws Exception
	{	
		threatTargetDiagramLink = createThreatTargetLink();
		wrapThreatAndThreatTargetLinkWithGroupBox();

		Vector<DiagramFactor> diagramFactorsToCutPaste = createDiagramFactorList();
		Vector<DiagramLink> diagramLinksToCutPaste = createDiagramLinkList();
		TransferableMiradiList transferableList = createTransferable(getDiagramModel(), diagramFactorsToCutPaste, diagramLinksToCutPaste);		
		pasteShared(diagramModelToPasteInto, transferableList);
		
		wrapThreatAndThreatTargetLinkWithGroupBox();
		cutPasteAll(diagramModelToPasteInto);
		verifyGroupContainingThreatLinkedToTarget(diagramModelToPasteInto);
	}

	public void testFactorLinkNotCreatedDuringCutPaste() throws Exception
	{					
		threatTargetDiagramLink = createThreatTargetLink();
		pasteShared(getDiagramModel(), diagramModelToPasteInto, threatDiagramFactor);
		
		Vector<DiagramFactor> diagramFactorsToCutPaste = new Vector<DiagramFactor>();
		diagramFactorsToCutPaste.add(targetDiagramFactor);
		diagramFactorsToCutPaste.add(threatDiagramFactor);
		
		Vector<DiagramLink> diagramLinksToCutPaste = new Vector<DiagramLink>();
		diagramLinksToCutPaste.add(threatTargetDiagramLink);
		TransferableMiradiList transferableList = createTransferable(getDiagramModel(), diagramFactorsToCutPaste, diagramLinksToCutPaste);
		
		deleteDiagramLinkAndOrphandFactorLink(threatTargetDiagramLink);
		deleteDiagramFactors(getDiagramObject(), diagramFactorsToCutPaste);
		pasteShared(diagramModelToPasteInto, transferableList);
		
		verifyFactorLinkAfterPaste(FactorLink.FROM, threat, diagramModelToPasteInto);
	}
	
	private void verifyFactorLinkAfterPaste(int direction, BaseObject sharedFactor, DiagramModel diagramModel)
	{
		ORef factorLinkRef = getProject().getFactorLinkPool().getRefList().getFirstElement();
		FactorLink factorLink = FactorLink.find(getProject(), factorLinkRef);
		DiagramLink pastedDiagramLink = diagramModel.getDiagramObject().getDiagramFactorLink(factorLinkRef);
		assertNotNull("Diagram Link not found?", pastedDiagramLink);
		assertEquals("FactorLink end is not the original factor?", sharedFactor.getRef(), factorLink.getFactorRef(direction));
		assertTrue("FactorLink from is not a cause?", Cause.is(factorLink.getFromFactorRef()));
		assertTrue("FactorLink to is not a target?", Target.is(factorLink.getToFactorRef()));
	}
	
	private void verifyGroupContainingThreatLinkedToTarget(DiagramModel diagramModel)
	{
		assertEquals("Not one group?", 1, diagramModel.getAllGroupBoxCells().size());
		DiagramFactor gdf = ((FactorCell)(diagramModel.getAllGroupBoxCells().get(0))).getDiagramFactor();
		assertEquals("Group not in diagram?", new ORefList(diagramModel.getDiagramObject().getRef()), gdf.findObjectsThatReferToUs(ConceptualModelDiagram.getObjectType()));
		assertEquals("Group doesn't have one child?", 1, gdf.getGroupBoxChildrenSet().size());

		DiagramFactor cdf = DiagramFactor.find(getProject(), gdf.getGroupBoxChildrenRefs().get(0));
		assertEquals("Cause not in diagram?", new ORefList(diagramModel.getDiagramObject().getRef()), cdf.findObjectsThatReferToUs(ConceptualModelDiagram.getObjectType()));

		assertEquals("Not one target?", 1, diagramModel.getAllDiagramTargets().size());
		DiagramFactor tdf = (diagramModel.getAllDiagramTargets().get(0)).getDiagramFactor();
		assertEquals("Target not in diagram?", new ORefList(diagramModel.getDiagramObject().getRef()), tdf.findObjectsThatReferToUs(ConceptualModelDiagram.getObjectType()));

		assertTrue("No threat-target link?", diagramModel.getDiagramObject().areDiagramFactorsLinkedFromToNonBidirectional(cdf.getRef(), tdf.getRef()));
		assertTrue("No group-target link?", diagramModel.getDiagramObject().areDiagramFactorsLinkedFromToNonBidirectional(gdf.getRef(), tdf.getRef()));

		DiagramLink gdl = DiagramLink.find(getProject(), gdf.findObjectsThatReferToUs().getRefForType(DiagramLink.getObjectType()));
		assertTrue("Isn't a group link?", gdl.isGroupBoxLink());
		assertNull("Group link has an FL?", gdl.getWrappedFactorLink());
		assertEquals("Group link not from group?", GroupBox.getObjectType(), gdl.getFromDiagramFactor().getWrappedType());
		assertEquals("Group link not to target?", Target.getObjectType(), gdl.getToDiagramFactor().getWrappedType());
		assertEquals("Group link doesn't have child?", 1, gdl.getGroupedDiagramLinkRefs().size());

		DiagramLink tdl = DiagramLink.find(getProject(), gdl.getGroupedDiagramLinkRefs().getFirstElement());
		assertEquals("Threat link not from threat?", Cause.getObjectType(), tdl.getFromDiagramFactor().getWrappedType());
		assertEquals("Threat link not to target?", Target.getObjectType(), tdl.getToDiagramFactor().getWrappedType());
	}

	private DiagramObject getDiagramObject()
	{
		return getDiagramModel().getDiagramObject();
	}

	private void cutPasteAll(DiagramModel diagramModelToPasteIntoToUse) throws Exception
	{
		Vector<DiagramFactor> diagramFactorsToCutPaste = createDiagramFactorList();
		Vector<DiagramLink> diagramLinksToCutPaste = createDiagramLinkList();
		TransferableMiradiList transferableList = createTransferable(getDiagramModel(), diagramFactorsToCutPaste, diagramLinksToCutPaste);
		
		deleteDiagramLinkAndOrphandFactorLink(groupBoxDiagramLink);
		deleteDiagramFactors(getDiagramObject(), diagramFactorsToCutPaste);
		pasteShared(diagramModelToPasteIntoToUse, transferableList);
	}

	private Vector<DiagramLink> createDiagramLinkList()
	{
		Vector<DiagramLink> diagramLinksToCutPaste = new Vector<DiagramLink>();
		diagramLinksToCutPaste.add(groupBoxDiagramLink);
		diagramLinksToCutPaste.add(threatTargetDiagramLink);
		
		return diagramLinksToCutPaste;
	}

	private Vector<DiagramFactor> createDiagramFactorList()
	{
		Vector<DiagramFactor> diagramFactorsToCutPaste = new Vector<DiagramFactor>();
		diagramFactorsToCutPaste.add(groupBoxDiagramFactor);
		diagramFactorsToCutPaste.add(targetDiagramFactor);
		diagramFactorsToCutPaste.add(threatDiagramFactor);
		
		return diagramFactorsToCutPaste;
	}

	private void wrapThreatAndThreatTargetLinkWithGroupBox() throws Exception
	{
		wrapThreatWithGroupBox();
		groupBoxDiagramLink = createGroupBoxLinkWithChildren(groupBoxDiagramFactor, targetDiagramFactor, threatTargetDiagramLink);

		assertTrue("threat to target link is not covered by group box link?", threatTargetDiagramLink.isCoveredByGroupBoxLink());
		assertEquals("group box has incorrect child links?", 1, groupBoxDiagramLink.getGroupedDiagramLinkRefs().size());
	}

	private void wrapThreatWithGroupBox() throws Exception
	{
		groupBoxDiagramFactor = createGroupBoxDiagramFactor(threatDiagramFactor);
	}

	private DiagramModel createDiagramModelToPasteInto() throws Exception
	{
		PersistentDiagramModel diagramModel = new PersistentDiagramModel(getProject());
		ORef diagramObjectRef = getProject().createObject(ConceptualModelDiagram.getObjectType());
		DiagramObject diagramObject = DiagramObject.findDiagramObject(getProject(), diagramObjectRef);
		diagramModel.fillFrom(diagramObject);
		new ModelUpdater(diagramModel);
		
		return diagramModel;
	}
	
	private void deleteDiagramFactors(DiagramObject diagramObject, Vector<DiagramFactor> diagramFactorsToCutPaste) throws Exception
	{
		FactorDeleteHelper helper = FactorDeleteHelper.createFactorDeleteHelperForNonSelectedFactors(diagramObject);
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
	
	private void pasteShared(DiagramModel diagramModelToCopyFrom, DiagramModel diagramModelToPasteIntoToUse, DiagramFactor diagramFactor) throws Exception
	{
		TransferableMiradiList transferableList = createTransferable(diagramModelToCopyFrom, diagramFactor);
		pasteShared(diagramModelToPasteIntoToUse, transferableList);
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

	private void pasteShared(DiagramModel diagramModelToPasteIntoToUse, TransferableMiradiList transferableList) throws Exception
	{
		DiagramPaster sharedPaster = new DiagramAliasPaster(null, diagramModelToPasteIntoToUse, transferableList);
		sharedPaster.pasteFactorsAndLinks(new Point(0, 0));
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
		ORef newGroupBoxDiagramLinkRef = linkCreator.createDiagramLink(getDiagramObject(), extraInfoWithNoFactorLink);
		linkCreator.updateGroupBoxChildrenRefs(coveredLinkRefs, newGroupBoxDiagramLinkRef);		
		
		return DiagramLink.find(getProject(), newGroupBoxDiagramLinkRef);
	}
	
	private DiagramLink createThreatTargetLink() throws Exception
	{
		return getProject().createDiagramLinkAndAddToDiagramModel(threatDiagramFactor, targetDiagramFactor);
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
	
	private DiagramModel diagramModelToPasteInto;
	private DiagramFactor targetDiagramFactor;
	private DiagramFactor threatDiagramFactor;
	private Target target;
	private Cause threat;
	private DiagramLink threatTargetDiagramLink;
	private DiagramFactor groupBoxDiagramFactor;
	private DiagramLink groupBoxDiagramLink;

}
