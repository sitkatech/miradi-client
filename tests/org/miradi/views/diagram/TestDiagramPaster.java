/* 
Copyright 2005-2014, Foundations of Success, Bethesda, Maryland
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
import java.util.HashMap;
import java.util.Vector;

import org.miradi.commands.CommandBeginTransaction;
import org.miradi.commands.CommandEndTransaction;
import org.miradi.commands.CommandSetObjectData;
import org.miradi.diagram.DiagramModel;
import org.miradi.diagram.MemoryDiagramModel;
import org.miradi.diagram.cells.EAMGraphCell;
import org.miradi.diagram.cells.FactorCell;
import org.miradi.diagram.cells.LinkCell;
import org.miradi.ids.BaseId;
import org.miradi.ids.IdList;
import org.miradi.main.AbstractTransferableMiradiList;
import org.miradi.main.CommandExecutedEvent;
import org.miradi.main.CommandExecutedListener;
import org.miradi.main.EAM;
import org.miradi.main.TestCaseWithProject;
import org.miradi.main.TransferableMiradiListVersion4;
import org.miradi.objectdata.BooleanData;
import org.miradi.objecthelpers.*;
import org.miradi.objectpools.TaxonomyAssociationPool;
import org.miradi.objects.*;
import org.miradi.project.FactorDeleteHelper;
import org.miradi.project.Project;
import org.miradi.project.ProjectForTesting;
import org.miradi.schemas.*;
import org.miradi.utils.CodeList;
import org.miradi.views.umbrella.UndoDoer;

public class TestDiagramPaster extends TestCaseWithProject 
{
	public TestDiagramPaster(String name)
	{
		super(name);
	}
	
	public void testPasteThreatIntoResultsChain() throws Exception
	{
		FactorCell threatCell = getProject().createFactorCell(CauseSchema.getObjectType());
		DiagramFactor threatDiagramFactor = threatCell.getDiagramFactor();
		Cause threat = Cause.find(getProject(), threatDiagramFactor.getWrappedORef());
		threat.setData(Cause.TAG_IS_DIRECT_THREAT, BooleanData.BOOLEAN_TRUE);
		getProject().populateBaseObject(threat);
	
		DiagramObject conceptualModel = getProject().getMainDiagramObject();
		TransferableMiradiListVersion4 miradiList = new TransferableMiradiListVersion4(getProject(), conceptualModel.getRef());
		miradiList.storeData(new EAMGraphCell[] {threatCell});

		ORef resultsChainRef = getProject().createResultsChainDiagram();
		DiagramObject resultsChain = (DiagramObject)DiagramObject.find(getProject(), resultsChainRef);
		DiagramModel model = new MemoryDiagramModel(getProject());
		model.fillFrom(resultsChain);
		
		DiagramCopyPaster paster = new DiagramCopyPaster(null, model, miradiList);
		paster.pasteFactors(new Point());
		
		ORefList resultsChainDiagramFactorRefs = resultsChain.getAllDiagramFactorRefs();
		assertEquals(1, resultsChainDiagramFactorRefs.size());
		DiagramFactor trrDiagramFactor = DiagramFactor.find(getProject(), resultsChainDiagramFactorRefs.get(0));
		ThreatReductionResult trr = ThreatReductionResult.find(getProject(), trrDiagramFactor.getWrappedORef());
		assertEquals("", trr.getData(trr.TAG_TAXONOMY_CLASSIFICATION_CONTAINER));
	}
	
	public void testPseudoRefListNotPasted() throws Exception
	{
		DiagramFactor strategyDiagramFactorWithObjective = getProject().createAndAddFactorToDiagram(StrategySchema.getObjectType());
		getProject().addObjective(strategyDiagramFactorWithObjective.getWrappedFactor());
		
		DiagramFactor strategyDiagramFactor = getProject().createAndAddFactorToDiagram(StrategySchema.getObjectType());
		DiagramLink diagramLink = getProject().createDiagramLinkAndAddToDiagramModel(strategyDiagramFactorWithObjective, strategyDiagramFactor);

		AbstractTransferableMiradiList transferableList = cut(strategyDiagramFactorWithObjective, diagramLink);
		paste(transferableList);
	
		try
		{
			UndoDoer.undo(getProject());
		}
		catch (Exception e)
		{
			EAM.logException(e);
			fail("Command to set pseudoReflist on undostack, paste should not have called set?");
		}
	}

	private void paste(AbstractTransferableMiradiList transferableList) throws Exception
	{
		getProject().executeCommand(new CommandBeginTransaction());
		DiagramPaster paster = new DiagramCopyPaster(null, getProject().getTestingDiagramModel(), transferableList);
		paster.pasteFactorsAndLinks(new Point(0, 0));
		getProject().executeCommand(new CommandEndTransaction());
	}

	private AbstractTransferableMiradiList cut(DiagramFactor strategyDiagramFactorWithObjective, DiagramLink diagramLink) throws Exception
	{
		getProject().executeCommand(new CommandBeginTransaction());
		DiagramModel model = getDiagramModel();
		FactorCell strategyCell = model.getFactorCellByRef(strategyDiagramFactorWithObjective.getRef());
	
		EAMGraphCell[] dataCells = new EAMGraphCell[]{strategyCell, };
		ORef diagramObjectRef = getDiagramModel().getDiagramObject().getRef();
		AbstractTransferableMiradiList transferableList = new TransferableMiradiListVersion4(getProject(), diagramObjectRef);
		transferableList.storeData(dataCells);
	
		LinkDeletor linkDeletor = new LinkDeletor(getProject());
		linkDeletor.deleteDiagramLink(diagramLink);
		FactorDeleteHelper factorDeletor = FactorDeleteHelper.createFactorDeleteHelperForNonSelectedFactors(DiagramObject.findDiagramObject(getProject(), diagramObjectRef));
		factorDeletor.deleteFactorAndDiagramFactor(strategyDiagramFactorWithObjective);
		getProject().executeCommand(new CommandEndTransaction());

		return transferableList;
	}
	
	public void testBudgetItemPasteIntoDifferentProject() throws Exception
	{
		DiagramFactor strategyDiagramFactor = getProject().createAndAddFactorToDiagram(StrategySchema.getObjectType());
		Task activity = getProject().createTask(strategyDiagramFactor.getWrappedFactor());
		getProject().fillObjectUsingCommand(activity, Task.TAG_LEADER_RESOURCE, getProject().createProjectResource().getRef());
		
		ResourceAssignment resourceAssignment = getProject().createAndPopulateResourceAssignment();
		ExpenseAssignment expenseAssignment = getProject().createExpenseAssignment();
		getProject().fillObjectUsingCommand(activity, Task.TAG_RESOURCE_ASSIGNMENT_IDS, new IdList(resourceAssignment));
		getProject().fillObjectUsingCommand(activity, Task.TAG_EXPENSE_ASSIGNMENT_REFS, new ORefList(expenseAssignment));
		
		Vector<DiagramFactor> diagramFactorsToPaste = new Vector<DiagramFactor>();
		diagramFactorsToPaste.add(strategyDiagramFactor);
		
		ProjectForTesting projectToPasteInto = createNewProject();
		paste(projectToPasteInto, diagramFactorsToPaste, new Vector<DiagramLink>());
		
		Vector<Task> activities = projectToPasteInto.getTaskPool().getAllActivities();
		assertEquals("Incorrect activity count?", 1, activities.size());
		Task pastedActitivy = activities.get(0);
		verifyEmptyTag(pastedActitivy, BaseObject.TAG_LEADER_RESOURCE);
		
		ORefList resourceAssignmentRefs = projectToPasteInto.getAssignmentPool().getRefList();
		assertEquals("ResourceAssignment was not pasted?", 1, resourceAssignmentRefs.size());
		ResourceAssignment pastedResourceAssignment = ResourceAssignment.find(projectToPasteInto, resourceAssignmentRefs.getFirstElement());
		verifyEmptyTag(pastedResourceAssignment, ResourceAssignment.TAG_RESOURCE_ID);
		verifyEmptyTag(pastedResourceAssignment, ResourceAssignment.TAG_FUNDING_SOURCE_ID);
		verifyEmptyTag(pastedResourceAssignment, ResourceAssignment.TAG_ACCOUNTING_CODE_ID);
		verifyEmptyTag(pastedResourceAssignment, ResourceAssignment.TAG_CATEGORY_ONE_REF);
		verifyEmptyTag(pastedResourceAssignment, ResourceAssignment.TAG_CATEGORY_TWO_REF);
		
		ORefList expenseAssignmentRefs = projectToPasteInto.getExpenseAssignmentPool().getRefList();
		assertEquals("ExpenseAssignment was not pasted", 1, expenseAssignmentRefs.size());
		ExpenseAssignment pastedExpenseAssignment = ExpenseAssignment.find(projectToPasteInto, expenseAssignmentRefs.getFirstElement());
		verifyEmptyTag(pastedExpenseAssignment, ExpenseAssignment.TAG_FUNDING_SOURCE_REF);
		verifyEmptyTag(pastedExpenseAssignment, ExpenseAssignment.TAG_ACCOUNTING_CODE_REF);
		verifyEmptyTag(pastedExpenseAssignment, ExpenseAssignment.TAG_CATEGORY_ONE_REF);
		verifyEmptyTag(pastedExpenseAssignment, ExpenseAssignment.TAG_CATEGORY_TWO_REF);
	}
	
	private void verifyEmptyTag(BaseObject baseObject, String tag)
	{
		assertEquals("tag was not cleared?", "", baseObject.getData(tag));
	}

	public void testThreatStressRatingPasteIntoDifferentProject() throws Exception
	{
		DiagramFactor threatDiagramFactor = getProject().createDiagramFactorAndAddToDiagram(CauseSchema.getObjectType());
		getProject().enableAsThreat(threatDiagramFactor.getWrappedORef());
		DiagramFactor targetDiagramFactor = getProject().createDiagramFactorAndAddToDiagram(TargetSchema.getObjectType());
		ORef diagramLinkRef = getProject().createDiagramLinkAndAddToDiagram(threatDiagramFactor, targetDiagramFactor);
		
		Stress stress = getProject().createStress();
		ORefList stressRefs = new ORefList(stress);
		getProject().fillObjectUsingCommand(targetDiagramFactor.getWrappedFactor(), Target.TAG_STRESS_REFS, stressRefs);
		
		ThreatTargetVirtualLinkHelper helper = new ThreatTargetVirtualLinkHelper(getProject());
		ORef threatStressRatingRef = helper.findThreatStressRating(threatDiagramFactor.getWrappedORef(), targetDiagramFactor.getWrappedORef(), stress.getRef());
		assertTrue("threatStressRating was not created?", threatStressRatingRef.isValid());
		
		Vector<DiagramFactor> diagramFactorsToPaste = new Vector<DiagramFactor>();
		diagramFactorsToPaste.add(threatDiagramFactor);
		diagramFactorsToPaste.add(targetDiagramFactor);
		
		Vector<DiagramLink> diagramLinksToPaste = new Vector<DiagramLink>();
		diagramLinksToPaste.add(DiagramLink.find(getProject(), diagramLinkRef));
		
		ProjectForTesting projectToPasteInto = createNewProject();
		
		ThreatStressRatingCommandListener listener = new ThreatStressRatingCommandListener();
		projectToPasteInto.addCommandExecutedListener(listener);
		projectToPasteInto.disableIsDoNothingCommandOptimization();
		DiagramPaster paster = null;
		try
		{
			paster = paste(projectToPasteInto, diagramFactorsToPaste, diagramLinksToPaste);
		}
		finally
		{
			projectToPasteInto.removeCommandExecutedListener(listener);
			projectToPasteInto.enableIsDoNothingCommandOptimization();
		}
		
		ORef pastedThreatRef = paster.getOldToNewObjectRefMap().get(threatDiagramFactor.getWrappedORef());
		ORef pastedTargetRef = paster.getOldToNewObjectRefMap().get(targetDiagramFactor.getWrappedORef());
		ORef pastedStressRef = paster.getOldToNewObjectRefMap().get(stress.getRef());
		ThreatTargetVirtualLinkHelper virtualLinkHelperForProjectPastedInto = new ThreatTargetVirtualLinkHelper(projectToPasteInto);
		ORef pastedThreatStressRatingRef = virtualLinkHelperForProjectPastedInto.findThreatStressRating(pastedThreatRef, pastedTargetRef, pastedStressRef);
		ThreatStressRating pastedThreatStressRating = ThreatStressRating.find(projectToPasteInto, pastedThreatStressRatingRef);
		assertEquals("wrong threat ref for pasted threat stress rating?", pastedThreatRef, pastedThreatStressRating.getThreatRef());
		assertEquals("wrong stress ref for pasted threat stress rating?", pastedStressRef, pastedThreatStressRating.getStressRef());
	}
	
	public class ThreatStressRatingCommandListener implements CommandExecutedListener
	{
		public void commandExecuted(CommandExecutedEvent event)
		{
			if (hasRefChanged(event, ThreatStressRating.TAG_STRESS_REF))
				fail("ThreatStressRating's stress ref should not be changed during paste");
			
			if (hasRefChanged(event, ThreatStressRating.TAG_THREAT_REF))
				fail("ThreatStressRating's threat ref should not be changed during paste");
		}

		public boolean hasRefChanged(CommandExecutedEvent event, final String tag)
		{
			if (!event.isSetDataCommandWithThisType(ThreatStressRatingSchema.getObjectType()))
				return false;
			
			CommandSetObjectData setCommand = event.getSetCommand();
			if (!setCommand.getFieldTag().equals(tag))
				return false;
			
			return !setCommand.getDataValue().equals(setCommand.getPreviousDataValue());
		}
	}
	
	public void testThreatReductionResultWithNonExistingRelatedThreatRef() throws Exception
	{
		DiagramFactor diagramFactor = getProject().createDiagramFactorAndAddToDiagram(ThreatReductionResultSchema.getObjectType());
		ThreatReductionResult threatReductionResult = (ThreatReductionResult) diagramFactor.getWrappedFactor(); 
		ORef nonExistingThreatRef = new ORef(CauseSchema.getObjectType(), new BaseId(99999));
		getProject().fillObjectUsingCommand(threatReductionResult, ThreatReductionResult.TAG_RELATED_DIRECT_THREAT_REF, nonExistingThreatRef.toString());
		
		ProjectForTesting projectToPasteInto = createNewProject();
		DiagramPaster paster = pasteDiagramFactor(projectToPasteInto, diagramFactor);
		HashMap<ORef, ORef> oldToNewRefMap = paster.getOldToNewObjectRefMap();
		ORef newThreatReductionResultRef = oldToNewRefMap.get(threatReductionResult.getRef());
		ThreatReductionResult newThreatReductionResult = ThreatReductionResult.find(projectToPasteInto, newThreatReductionResultRef);
		assertEquals("did not blank out related threat ref?", ORef.INVALID, newThreatReductionResult.getRelatedThreatRef());
	}

	private ProjectForTesting createNewProject() throws Exception
	{
		return ProjectForTesting.createProjectWithDefaultObjects("ProjectToPasteInto");
	}

	public void testFixupAllIndicatorRefs() throws Exception
	{
		fixupRefs(CauseSchema.getObjectType(), IndicatorSchema.getObjectType(), Factor.TAG_INDICATOR_IDS);
		fixupRefs(CauseSchema.getObjectType(), ObjectiveSchema.getObjectType(), Factor.TAG_OBJECTIVE_IDS);
		fixupRefs(TargetSchema.getObjectType(), GoalSchema.getObjectType(), AbstractTarget.TAG_GOAL_IDS);
		fixupRefs(TargetSchema.getObjectType(), KeyEcologicalAttributeSchema.getObjectType(), AbstractTarget.TAG_KEY_ECOLOGICAL_ATTRIBUTE_IDS);
		fixupRefs(HumanWelfareTargetSchema.getObjectType(), GoalSchema.getObjectType(), AbstractTarget.TAG_GOAL_IDS);
		fixupRefs(HumanWelfareTargetSchema.getObjectType(), KeyEcologicalAttributeSchema.getObjectType(), AbstractTarget.TAG_KEY_ECOLOGICAL_ATTRIBUTE_IDS);
	}
		
	public void fixupRefs(int factorType, int annotationType, String annotationFactorTag) throws Exception
	{
		DiagramFactor diagramFactor = getProject().createDiagramFactorAndAddToDiagram(factorType);
		ORef annotationRef1 = getProject().createObject(annotationType);
		ORef annotationRef2 = getProject().createObject(annotationType);
		
		IdList annotationIds = new IdList(annotationType);
		annotationIds.addRef(annotationRef1);
		annotationIds.addRef(annotationRef2);
		
		CommandSetObjectData setFactorAnnotationIds = new CommandSetObjectData(diagramFactor.getWrappedORef(), annotationFactorTag, annotationIds.toString());
		getProject().executeCommand(setFactorAnnotationIds);
		
		Factor factor = (Factor) getProject().findObject(diagramFactor.getWrappedORef());
		DiagramPaster paster = pasteDiagramFactor(getProject(), diagramFactor);
		
		HashMap oldToNewFactorRefMap = paster.getOldToNewObjectRefMap();
		ORef newRef = (ORef) oldToNewFactorRefMap.get(factor.getRef());
		Factor newFactor = (Factor) getProject().findObject(newRef);
		IdList newAnnotationIds = new IdList(annotationType, newFactor.getData(annotationFactorTag));
		assertFalse("contains wrong old id?", newAnnotationIds.contains(annotationRef1));
		assertFalse("contains wrong old id?", newAnnotationIds.contains(annotationRef2));
		
		ORef newAnnotation1 = (ORef) oldToNewFactorRefMap.get(annotationRef1);
		assertTrue("does not contain new id?", newAnnotationIds.contains(newAnnotation1));
		
		ORef newAnnotation2 = (ORef) oldToNewFactorRefMap.get(annotationRef2);
		assertTrue("does not contain new id?", newAnnotationIds.contains(newAnnotation2));
	}
	
	public void testFixTags() throws Exception
	{
		DiagramObject diagramObject = getDiagramModel().getDiagramObject();
		AbstractTransferableMiradiList transferableList = new TransferableMiradiListVersion4(getProject(), diagramObject.getRef());
		DiagramCopyPaster diagramPaster = new DiagramCopyPaster(null, getDiagramModel(), transferableList);
		Target target = getProject().createTarget();
		final String TAG_LABEL = "tagLabel1";
		getProject().createLabeledTaggedObjectSet(TAG_LABEL);
		getProject().createLabeledTaggedObjectSet("tagLabel2");
		
		assertEquals("has tagged object sets?", 2, getProject().getTaggedObjectSetPool().size());
		diagramPaster.fixTags(new CodeList(), target);
		assertEquals("has tagged object sets?", 2, getProject().getTaggedObjectSetPool().size());
		
		CodeList nonExistingTagNames = new CodeList(new String[]{"pastedTag1", });
		diagramPaster.fixTags(nonExistingTagNames, target);	
		assertEquals("should have created tag for non existing tag name?", 3, getProject().getTaggedObjectSetPool().size());
		
		CodeList existingTagNames = new CodeList(new String[]{TAG_LABEL});
		diagramPaster.fixTags(existingTagNames, target);	
		assertEquals("should have not created tag for existing tag name?", 3, getProject().getTaggedObjectSetPool().size());
	}
	
	public void testThreatStressRatingUndoPaste() throws Exception
	{
		DiagramFactor targetDiagramFactor = getProject().createDiagramFactorAndAddToDiagram(TargetSchema.getObjectType());
		Target target = (Target) targetDiagramFactor.getWrappedFactor();
		ORefList stressRefs = new ORefList(getProject().createStress().getRef());
		getProject().fillObjectUsingCommand(target, Target.TAG_STRESS_REFS, stressRefs.toString());
		
		DiagramFactor threatDiagramFactor = getProject().createDiagramFactorAndAddToDiagram(CauseSchema.getObjectType());
		getProject().enableAsThreat(threatDiagramFactor.getWrappedORef());
		
		ORef diagramLinkRef = getProject().createDiagramLinkWithCommand(threatDiagramFactor, targetDiagramFactor);
		CommandSetObjectData addToDiagram = CommandSetObjectData.createAppendIdCommand(getProject().getTestingDiagramObject(), DiagramObject.TAG_DIAGRAM_FACTOR_LINK_IDS, diagramLinkRef.getObjectId());
		getProject().executeCommand(addToDiagram);
		
		assertEquals("wrong threat stress ratings count?", 1, getProject().getThreatStressRatingPool().size());
		
		DiagramModel model = getDiagramModel();
		FactorCell targetFactorCell = model.getFactorCellByRef(targetDiagramFactor.getRef());
		FactorCell threatFactorCell = model.getFactorCellByRef(threatDiagramFactor.getRef());
		DiagramLink threatDiagramLink = DiagramLink.find(getProject(), diagramLinkRef);
		LinkCell linkCell = model.getLinkCell(threatDiagramLink);
		EAMGraphCell dataCells[] = {linkCell, threatFactorCell, targetFactorCell};
		
		ORef diagramObjectRef = model.getDiagramObject().getRef();
		AbstractTransferableMiradiList transferableList = new TransferableMiradiListVersion4(getProject(), diagramObjectRef);
		transferableList.storeData(dataCells);
	
		getProject().executeCommand(new CommandBeginTransaction());
		try
		{
			getProject().getDiagramClipboard().incrementPasteCount();
			DiagramCopyPaster paster = new DiagramCopyPaster(null, getDiagramModel(), transferableList);
			paster.pasteFactorsAndLinks(new Point(0, 0));
		
			assertEquals("wrong threat stress ratings count after paste?", 2, getProject().getThreatStressRatingPool().size());
		}
		finally
		{
			getProject().executeCommand(new CommandEndTransaction());
		}
		
		UndoDoer.undo(getProject());
		assertEquals("wrong threat stress ratings count after undo?", 1, getProject().getThreatStressRatingPool().size());
	}
	
	private DiagramPaster pasteDiagramFactor(ProjectForTesting projectToPasteInto, DiagramFactor diagramFactor) throws Exception
	{
		Vector<DiagramFactor> diagramFactors = new Vector<DiagramFactor>();
		diagramFactors.add(diagramFactor);
		
		return paste(projectToPasteInto, diagramFactors, new Vector<DiagramLink>());
	}
	
	private DiagramPaster paste(ProjectForTesting projectToPasteInto, Vector<DiagramFactor> diagramFactors, Vector<DiagramLink> diagramLinks) throws Exception
	{
		Vector<EAMGraphCell> cellsToCopy = new Vector<EAMGraphCell>();
		for(DiagramLink diagramLink : diagramLinks)
		{
			LinkCell linkCell = getDiagramModel().getLinkCell(diagramLink);
			cellsToCopy.add(linkCell);
		}
	
		for(DiagramFactor diagramFactor : diagramFactors)
		{
			FactorCell factorCell = getDiagramModel().getFactorCellByRef(diagramFactor.getRef());
			cellsToCopy.add(factorCell);
		}
		
		EAMGraphCell[] cellsToCopyAsArray = cellsToCopy.toArray(new EAMGraphCell[0]);
		return paste(projectToPasteInto, cellsToCopyAsArray);
	}

	private DiagramPaster paste(ProjectForTesting projectToPasteInto, EAMGraphCell[] dataCells) throws Exception
	{
		ORef diagramObjectRef = getDiagramModel().getDiagramObject().getRef();
		AbstractTransferableMiradiList transferableList = new TransferableMiradiListVersion4(getProject(), diagramObjectRef);
		transferableList.storeData(dataCells);
		
		DiagramPaster paster = new DiagramCopyPaster(null, projectToPasteInto.getTestingDiagramModel(), transferableList);
		paster.pasteFactorsAndLinks(new Point(0, 0));
		
		return paster;
	}

    public void testPasteFactorCompatibleTaxonomies() throws Exception
    {
        getProject().createAndPopulateMiradiShareTaxonomy();
        getProject().populateTaxonomyAssociationsForBaseObjectTypes();

        DiagramFactor strategyDiagramFactor = getProject().createAndAddFactorToDiagram(StrategySchema.getObjectType());
        Strategy strategy = Strategy.find(getProject(), strategyDiagramFactor.getWrappedORef());
        getProject().populateBaseObject(strategy);

        Vector<DiagramFactor> diagramFactorsToPaste = new Vector<DiagramFactor>();
        diagramFactorsToPaste.add(strategyDiagramFactor);

        ProjectForTesting projectToPasteInto = createNewProject();
        projectToPasteInto.createAndPopulateMiradiShareTaxonomy();
        projectToPasteInto.populateTaxonomyAssociationsForBaseObjectTypes();

        DiagramPaster diagramPaster = paste(projectToPasteInto, diagramFactorsToPaste, new Vector<DiagramLink>());
        assertFalse(diagramPaster.wasAnyDataLost());

        String fromProjectStrategyTaxonomyClassifications = strategy.getData(BaseObject.TAG_TAXONOMY_CLASSIFICATION_CONTAINER);

        ORefList toProjectStrategyRefs = projectToPasteInto.getAllRefsForType(ObjectType.STRATEGY);
        Strategy toProjectStrategy = (Strategy) projectToPasteInto.getObjectManager().findObject(toProjectStrategyRefs.get(0));
        String toProjectStrategyTaxonomyClassifications = toProjectStrategy.getData(BaseObject.TAG_TAXONOMY_CLASSIFICATION_CONTAINER);

        assertEquals(fromProjectStrategyTaxonomyClassifications, toProjectStrategyTaxonomyClassifications);
    }

    public void testPasteFactorIncompatibleTaxonomies() throws Exception
    {
        getProject().createAndPopulateMiradiShareTaxonomy();
        getProject().populateTaxonomyAssociationsForBaseObjectTypes();

        DiagramFactor strategyDiagramFactor = getProject().createAndAddFactorToDiagram(StrategySchema.getObjectType());
        Strategy strategy = Strategy.find(getProject(), strategyDiagramFactor.getWrappedORef());
        getProject().populateBaseObject(strategy);

        Vector<DiagramFactor> diagramFactorsToPaste = new Vector<DiagramFactor>();
        diagramFactorsToPaste.add(strategyDiagramFactor);

        ProjectForTesting projectToPasteInto = createNewProject();
        projectToPasteInto.createAndPopulateMiradiShareTaxonomy();
        projectToPasteInto.populateTaxonomyAssociationsForBaseObjectTypes();

        // remove taxonomy association in destination project for strategy (making pasted taxonomy classifications invalid)
        deleteTaxonomyAssociationsForObject(projectToPasteInto, StrategySchema.getObjectType());

        DiagramPaster diagramPaster = paste(projectToPasteInto, diagramFactorsToPaste, new Vector<DiagramLink>());
        assertTrue(diagramPaster.wasAnyDataLost());

        String fromProjectStrategyTaxonomyClassifications = strategy.getData(BaseObject.TAG_TAXONOMY_CLASSIFICATION_CONTAINER);

        ORefList toProjectStrategyRefs = projectToPasteInto.getAllRefsForType(ObjectType.STRATEGY);
        Strategy toProjectStrategy = (Strategy) projectToPasteInto.getObjectManager().findObject(toProjectStrategyRefs.get(0));
        String toProjectStrategyTaxonomyClassifications = toProjectStrategy.getData(BaseObject.TAG_TAXONOMY_CLASSIFICATION_CONTAINER);

        assertNotEquals(fromProjectStrategyTaxonomyClassifications, toProjectStrategyTaxonomyClassifications);
        assertEquals(toProjectStrategyTaxonomyClassifications, "");
    }

    private void deleteTaxonomyAssociationsForObject(Project project, int objectType) throws Exception
    {
        TaxonomyAssociationPool taxonomyAssociationPool = project.getTaxonomyAssociationPool();

        Vector<String> poolNamesForType = TaxonomyHelper.getTaxonomyAssociationPoolNamesForType(objectType);
        for(String taxonomyAssociationPoolName : poolNamesForType)
        {
            Vector<TaxonomyAssociation> taxonomyAssociationsForBaseObject = taxonomyAssociationPool.findTaxonomyAssociationsForPoolName(taxonomyAssociationPoolName);
            for (TaxonomyAssociation taxonomyAssociation : taxonomyAssociationsForBaseObject)
            {
                project.deleteObject(taxonomyAssociation);
            }
        }
    }




}
