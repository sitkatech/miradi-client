/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.interview;

import org.conservationmeasures.eam.testall.EAMTestCase;
import org.conservationmeasures.eam.views.interview.elements.HtmlElementData;
import org.conservationmeasures.eam.views.interview.elements.InputElementData;
import org.martus.util.UnicodeStringReader;

public class TestWizardStepLoader extends EAMTestCase
{
	public TestWizardStepLoader(String name)
	{
		super(name);
	}

	public void testEmptyTemplate() throws Exception
	{
		String data = "";
		InterviewStepModel step = createStepFromData(data);
		assertNull("had a default step name?", step.getStepName());
		assertEquals(0, step.getElementCount());
	}

	public void testHtmlElements() throws Exception
	{
		String stepName = "step name";
		String element0 = "This is\nsome boring html\ntext\n";
		String element1 = "\n\n\nMore boring text";
		String data = stepName + "\n" + ":html:\n" + element0 + ":html:\n" + element1;
		InterviewStepModel step = createStepFromData(data);
		assertEquals(stepName, step.getStepName());
		assertEquals(2, step.getElementCount());
		HtmlElementData label0 = (HtmlElementData)step.getElement(0);
		assertEquals("wrong element0?", htmlStart + element0 + htmlEnd, label0.toString());
		HtmlElementData label1 = (HtmlElementData)step.getElement(1);
		assertEquals("wrong element1?", htmlStart + element1 + "\n" + htmlEnd, label1.toString());
	}
	
	public void testInputField() throws Exception
	{
		String prompt = ":html:\nPlease enter some data\n";
		String inputField = ":input:\n";
		String template = "name\n" + prompt + inputField;
		InterviewStepModel step = createStepFromData(template);
		assertEquals(2, step.getElementCount());
		InputElementData inputComponent = (InputElementData)step.getElement(1);
		assertNotNull(inputComponent);
	}
	
	private InterviewStepModel createStepFromData(String data) throws Exception
	{
		return StepLoader.load(new UnicodeStringReader(data));
	}

	final static String htmlStart = "<html>";
	final static String htmlEnd = "</html>";
}
