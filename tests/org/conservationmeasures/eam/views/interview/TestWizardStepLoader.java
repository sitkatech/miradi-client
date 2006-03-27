/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.interview;

import java.util.Vector;

import org.conservationmeasures.eam.testall.EAMTestCase;
import org.conservationmeasures.eam.views.interview.elements.HtmlElementData;
import org.conservationmeasures.eam.views.interview.elements.InputElementData;
import org.conservationmeasures.eam.views.interview.elements.ListElementData;
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
		String nextStepName = "next step";
		String previousStepName = "previous step";
		String element0 = "This is\nsome boring html\ntext\n";
		String element1 = "\n\n\nMore boring text";
		String data = stepName + "\n" + nextStepName + "\n" + previousStepName + "\n" + 
			":html:\n" + element0 + ":html:\n" + element1;
		InterviewStepModel step = createStepFromData(data);
		assertEquals(stepName, step.getStepName());
		assertEquals(nextStepName, step.getNextStepName());
		assertEquals(previousStepName, step.getPreviousStepName());
		assertEquals(2, step.getElementCount());
		HtmlElementData label0 = (HtmlElementData)step.getElement(0);
		assertEquals("wrong element0?", element0, label0.toString());
		HtmlElementData label1 = (HtmlElementData)step.getElement(1);
		assertEquals("wrong element1?", element1 + "\n", label1.toString());
	}
	
	public void testInputField() throws Exception
	{
		String prompt = ":html:\nPlease enter some data\n";
		String inputField = ":input:\nDataName\n";
		String template = "name\nnext\nprevious\n" + prompt + inputField;
		InterviewStepModel step = createStepFromData(template);
		assertEquals(2, step.getElementCount());
		InputElementData inputComponent = (InputElementData)step.getElement(1);
		assertNotNull(inputComponent);
		assertEquals("didn't set field name?", "DataName", inputComponent.getFieldName());
	}
	
	public void testListField() throws Exception
	{
		String prompt = ":html:\nPlease choose from the list\n";
		String listName = "List Name"; 
		String choice1 = "Choice 1"; 
		String choice2 = "Choice 2"; 
		String choice3 = "Choice 3"; 
		
		String listField = ":list:"+listName+"\n"+choice1+"\n"+choice2+"\n"+choice3+"\n";
		String template = "name\nnext\nprevious\n" + prompt + listField;
		InterviewStepModel step = createStepFromData(template);
		assertEquals(2, step.getElementCount());
		ListElementData listComponent = (ListElementData)step.getElement(1);
		assertNotNull(listComponent);
		listComponent.createComponent();
		assertEquals("didn't set field name?", listName, listComponent.getFieldName());
		Vector retrievedList = listComponent.getList();
		assertContains(choice1, retrievedList);
		assertContains(choice2, retrievedList);
		assertContains(choice3, retrievedList);
		assertEquals("Should return empty string when no data is selected", "", listComponent.getFieldData());
		listComponent.setFieldData(choice2);
		assertEquals(choice2, listComponent.getFieldData());
		assertEquals("<<list>>", listComponent.toString());
	}

	private InterviewStepModel createStepFromData(String data) throws Exception
	{
		return StepLoader.load(new UnicodeStringReader(data));
	}

	final static String htmlStart = "<html>";
	final static String htmlEnd = "</html>";
}
