/* 
Copyright 2005-2009, Foundations of Success, Bethesda, Maryland 
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

package org.miradi.objecthelpers;

import org.miradi.diagram.cells.FactorCell;
import org.miradi.main.TestCaseWithProject;
import org.miradi.objects.Cause;
import org.miradi.objects.Factor;
import org.miradi.objects.IntermediateResult;
import org.miradi.project.ResultsChainCreatorHelper;

public class TestResultsChainCreatorHelper extends TestCaseWithProject
{
	public TestResultsChainCreatorHelper(String name)
	{
		super(name);
	}
	
	public void testTransferAnnotationsToNewlyCreatedFactor() throws Exception
	{
		Cause cause = getProject().createCause();
		ORef causeRef = cause.getRef();
		ORef intermediateResultRef = getProject().createObject(IntermediateResult.getObjectType());
		IntermediateResult intermediateResult = IntermediateResult.find(getProject(), intermediateResultRef);
		
		ResultsChainCreatorHelper helper = new ResultsChainCreatorHelper(getProject(), getDiagramModel(), new FactorCell[0]);
		helper.transferAnnotationsToNewFactor(causeRef, intermediateResultRef, Factor.TAG_INDICATOR_IDS);
		assertEquals("no indicators should have been transferred?", 0, intermediateResult.getOnlyDirectIndicatorRefs().size());
		
		helper.transferAnnotationsToNewFactor(causeRef, intermediateResultRef, Factor.TAG_OBJECTIVE_IDS);
		assertEquals("no objectives should have been transferred?", 0, intermediateResult.getObjectiveRefs().size());
		
		getProject().createIndicator(cause);
		getProject().createObjective(cause);
		helper.transferAnnotationsToNewFactor(causeRef, intermediateResultRef, Factor.TAG_INDICATOR_IDS);
		assertEquals("indicators was not transferred", 1, intermediateResult.getOnlyDirectIndicatorRefs().size());
		assertEquals("indicators were not removed from cause", 0, cause.getOnlyDirectIndicatorRefs().size());
		
		helper.transferAnnotationsToNewFactor(causeRef, intermediateResultRef, Factor.TAG_OBJECTIVE_IDS);
		assertEquals("objective was not transferred", 1, intermediateResult.getObjectiveRefs().size());
		assertEquals("objectives were not removed from cause", 0, cause.getObjectiveRefs().size());
	}
}
