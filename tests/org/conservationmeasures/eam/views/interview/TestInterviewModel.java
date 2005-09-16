/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.interview;

import org.conservationmeasures.eam.testall.EAMTestCase;

public class TestInterviewModel extends EAMTestCase
{
	public TestInterviewModel(String name)
	{
		super(name);
	}

	public void testBasics() throws Exception
	{
		InterviewModel model = new InterviewModel();

		InterviewStepModel step1 = new InterviewStepModel("step1");
		InterviewStepModel step2 = new InterviewStepModel("step2");
		model.addStep(step1);
		model.addStep(step2);
		assertEquals("not 2 steps?", 2, model.getStepCount());
		assertEquals("step 1 not first?", step1.getStepName(), model.getStep(0).getStepName());
		assertEquals("step 1 not current?", step1.getStepName(), model.getCurrentStep().getStepName());
		assertEquals("stepname 1 not current?", step1.getStepName(), model.getCurrentStepName());
		model.setCurrentStepName(step2.getStepName());
		assertEquals("didn't switch to step 2?", step2.getStepName(), model.getCurrentStepName());
	}
	
	public void testRealSteps() throws Exception
	{
		InterviewModel model = new InterviewModel();
		model.loadSteps();
		assertTrue("Didn't load steps?", model.getStepCount() > 1);
		InterviewStepModel welcomeStep = model.getCurrentStep();
		assertEquals("wrong first step?", "welcome", welcomeStep.getStepName());
		InterviewStepModel secondStep = model.getStep(welcomeStep.getNextStepName());
		assertEquals("wrong next step?", "P1aT2S1", secondStep.getStepName());
		
	}
}
