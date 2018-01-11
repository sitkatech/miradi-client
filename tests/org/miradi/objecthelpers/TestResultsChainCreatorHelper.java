/* 
Copyright 2005-2018, Foundations of Success, Bethesda, Maryland
on behalf of the Conservation Measures Partnership ("CMP").
Material developed between 2005-2013 is jointly copyright by Beneficent Technology, Inc. ("The Benetech Initiative"), Palo Alto, California.

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

package org.miradi.objecthelpers;

import java.util.HashMap;
import java.util.HashSet;

import org.miradi.diagram.cells.FactorCell;
import org.miradi.main.TestCaseWithProject;
import org.miradi.objects.*;
import org.miradi.project.ResultsChainCreatorHelper;
import org.miradi.schemas.*;

public class TestResultsChainCreatorHelper extends TestCaseWithProject
{
	public TestResultsChainCreatorHelper(String name)
	{
		super(name);
	}
	
	public void testTransferCauseAnnotationsToNewlyCreatedFactorCoveredByGroupBox() throws Exception
	{
		DiagramFactor causeDiagramFactor = getProject().createDiagramFactorAndAddToDiagram(CauseSchema.getObjectType());

        HashSet<DiagramFactor> diagramFactorsToClone = getTestDiagramFactorsToClone(causeDiagramFactor);
        ResultsChainCreatorHelper helper = new ResultsChainCreatorHelper(getProject(), getDiagramModel(), new FactorCell[0]);
        HashMap<DiagramFactor, DiagramFactor> originalToClonedMap = helper.cloneDiagramFactors(diagramFactorsToClone);

        assertEquals("incorrect number of cloned diagramFactors?", 3, originalToClonedMap.size());

        ORefList intermediateResultRefs = getProject().getIntermediateResultPool().getRefList();
        assertEquals("Incorrect number of intermediate results created?", 1, intermediateResultRefs.size());
        assertEquals("incorrect number of objectives?", 1, getProject().getObjectivePool().size());

        DiagramFactor intermediateResultDiagramFactor = originalToClonedMap.get(causeDiagramFactor);
        final Factor intermediateResult = intermediateResultDiagramFactor.getWrappedFactor();
        assertEquals("the objective was not transferred?", 1, intermediateResult.getObjectiveIds().size());
	}
	
	public void testTransferBiophysicalFactorAnnotationsToNewlyCreatedFactorCoveredByGroupBox() throws Exception
	{
		DiagramFactor biophysicalFactorDiagramFactor = getProject().createDiagramFactorAndAddToDiagram(BiophysicalFactorSchema.getObjectType());

        HashSet<DiagramFactor> diagramFactorsToClone = getTestDiagramFactorsToClone(biophysicalFactorDiagramFactor);
        ResultsChainCreatorHelper helper = new ResultsChainCreatorHelper(getProject(), getDiagramModel(), new FactorCell[0]);
        HashMap<DiagramFactor, DiagramFactor> originalToClonedMap = helper.cloneDiagramFactors(diagramFactorsToClone);

        assertEquals("incorrect number of cloned diagramFactors?", 3, originalToClonedMap.size());

        ORefList biophysicalResultRefs = getProject().getBiophysicalResultPool().getRefList();
        assertEquals("Incorrect number of biophysical results created?", 1, biophysicalResultRefs.size());
        assertEquals("incorrect number of objectives?", 1, getProject().getObjectivePool().size());

        DiagramFactor biophysicalResultDiagramFactor = originalToClonedMap.get(biophysicalFactorDiagramFactor);
        final Factor bioPhysicalResult = biophysicalResultDiagramFactor.getWrappedFactor();
        assertEquals("the objective was not transferred?", 1, bioPhysicalResult.getObjectiveIds().size());
	}

	public HashSet<DiagramFactor> getTestDiagramFactorsToClone(DiagramFactor diagramFactor) throws Exception
	{
		getProject().addObjective(diagramFactor.getWrappedFactor());
		DiagramFactor groupBoxDiagramFactor = getProject().createAndPopulateDiagramFactorGroupBox(diagramFactor);
		DiagramFactor strategyDiagramFactor = getProject().createDiagramFactorAndAddToDiagram(StrategySchema.getObjectType());
		getProject().createDiagramLinkAndAddToDiagram(strategyDiagramFactor, groupBoxDiagramFactor);

		HashSet<DiagramFactor> diagramFactorToClone = new HashSet<DiagramFactor>();
		diagramFactorToClone.add(strategyDiagramFactor);
		diagramFactorToClone.add(diagramFactor);
		diagramFactorToClone.add(groupBoxDiagramFactor);

		return diagramFactorToClone;
	}

	public void testTransferCauseAnnotationsToNewlyCreatedIntermediateResult() throws Exception
	{
		Cause cause = getProject().createCause();
		ORef intermediateResultRef = getProject().createObject(IntermediateResultSchema.getObjectType());
		IntermediateResult intermediateResult = IntermediateResult.find(getProject(), intermediateResultRef);

        checkTransferAnnotationsToNewlyCreatedFactor(cause, intermediateResult);
	}

	public void testTransferBiophysicalFactorAnnotationsToNewlyCreatedBiophysicalResult() throws Exception
	{
        BiophysicalFactor biophysicalFactor = getProject().createBiophysicalFactor();
		ORef biophysicalResultRef = getProject().createObject(BiophysicalResultSchema.getObjectType());
        BiophysicalResult biophysicalResult = BiophysicalResult.find(getProject(), biophysicalResultRef);

        checkTransferAnnotationsToNewlyCreatedFactor(biophysicalFactor, biophysicalResult);
	}

	private void checkTransferAnnotationsToNewlyCreatedFactor(Factor fromFactor, Factor toFactor) throws Exception
	{
		ORef fromFactorRef = fromFactor.getRef();
		ORef toFactorRef = toFactor.getRef();

		ResultsChainCreatorHelper helper = new ResultsChainCreatorHelper(getProject(), getDiagramModel(), new FactorCell[0]);
		helper.transferAnnotationsToNewFactor(fromFactorRef, toFactorRef, Factor.TAG_INDICATOR_IDS);
		assertEquals("no indicators should have been transferred?", 0, toFactor.getOnlyDirectIndicatorRefs().size());

		helper.transferAnnotationsToNewFactor(fromFactorRef, toFactorRef, Factor.TAG_OBJECTIVE_IDS);
		assertEquals("no objectives should have been transferred?", 0, toFactor.getObjectiveRefs().size());

		getProject().createIndicator(fromFactor);
		getProject().createObjective(fromFactor);
		helper.transferAnnotationsToNewFactor(fromFactorRef, toFactorRef, Factor.TAG_INDICATOR_IDS);
		assertEquals("indicators was not transferred", 1, toFactor.getOnlyDirectIndicatorRefs().size());
		assertEquals("indicators were not removed from cause", 0, fromFactor.getOnlyDirectIndicatorRefs().size());

		helper.transferAnnotationsToNewFactor(fromFactorRef, toFactorRef, Factor.TAG_OBJECTIVE_IDS);
		assertEquals("objective was not transferred", 1, toFactor.getObjectiveRefs().size());
		assertEquals("objectives were not removed from cause", 0, fromFactor.getObjectiveRefs().size());
	}
}
