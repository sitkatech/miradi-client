/* 
Copyright 2005-2011, Foundations of Success, Bethesda, Maryland 
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

package org.miradi.views.planning;

import org.miradi.dialogs.planning.WorkPlanRowColumnProvider;
import org.miradi.main.TestCaseWithProject;
import org.miradi.objects.ProjectMetadata;
import org.miradi.questions.DiagramObjectDataInclusionQuestion;

public class TestWorkPlanRowColumnProvider extends TestCaseWithProject
{
	public TestWorkPlanRowColumnProvider(String name)
	{
		super(name);
	}
	
	@Override
	public void setUp() throws Exception
	{
		super.setUp();
		
		provider = new WorkPlanRowColumnProvider(getProject());
	}
	
	public void testShouldIncludeConceptualModelPage() throws Exception
	{
		verifyConceptualModelDataInclusion(DiagramObjectDataInclusionQuestion.INCLUDE_BOTH_DIAGRAM_DATA_CODE, true);
		verifyConceptualModelDataInclusion(DiagramObjectDataInclusionQuestion.INCLUDE_CONCEPTUAL_MODEL_DATA_CODE, true);
		verifyConceptualModelDataInclusion(DiagramObjectDataInclusionQuestion.INCLUDE_RESULTS_CHAIN_DATA_CODE, false);
	}

	public void testShouldIncludeResultsChain() throws Exception
	{
		verifyResultsChainDataInclusion(DiagramObjectDataInclusionQuestion.INCLUDE_BOTH_DIAGRAM_DATA_CODE, true);
		verifyResultsChainDataInclusion(DiagramObjectDataInclusionQuestion.INCLUDE_CONCEPTUAL_MODEL_DATA_CODE, false);
		verifyResultsChainDataInclusion(DiagramObjectDataInclusionQuestion.INCLUDE_RESULTS_CHAIN_DATA_CODE, true);
	}
	
	private void verifyConceptualModelDataInclusion(final String diagramDataInclusionCode, boolean expectedValue) throws Exception
	{
		fillMetadataDataInclusionValue(diagramDataInclusionCode);
		assertEquals("should include conceptual model diagram data?", expectedValue, provider.shouldIncludeConceptualModelPage());
	}
	
	private void verifyResultsChainDataInclusion(final String diagramDataInclusionCode, boolean expectedValue) throws Exception
	{
		fillMetadataDataInclusionValue(diagramDataInclusionCode);
		assertEquals("should include resuls chain diagram data?", expectedValue, provider.shouldIncludeResultsChain());
	}

	private void fillMetadataDataInclusionValue(final String diagramDataInclusionCode) throws Exception
	{
		getProject().fillObjectUsingCommand(getProject().getMetadata(), ProjectMetadata.TAG_WORK_PLAN_DIAGRAM_DATA_INCLUSION, diagramDataInclusionCode);
	}
	
	private WorkPlanRowColumnProvider provider;
}
