/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.interview;

import java.util.Map;
import java.util.Set;

import org.conservationmeasures.eam.testall.EAMTestCase;
import org.conservationmeasures.eam.views.interview.elements.InputElementData;

public class TestInterviewStepModel extends EAMTestCase
{
	public TestInterviewStepModel(String name)
	{
		super(name);
	}

	public void testSaveData() throws Exception
	{
		String sampleData = "This is some text";
		
		InputElementData inputField = new InputElementData();
		String sampleFieldName = "fieldname";
		inputField.appendLine(sampleFieldName);
		inputField.createComponent();
		inputField.setFieldData(sampleData);

		InterviewStepModel step = new InterviewStepModel("step name");
		step.addElement(inputField);
		
		Map data = step.getData();
		Set fieldNames = data.keySet();
		assertEquals("wrong field count?", 1, fieldNames.size());
		assertContains("missing our field?", sampleFieldName, fieldNames);
		assertEquals("missing our data?", sampleData, data.get(sampleFieldName));
	}
	
}
