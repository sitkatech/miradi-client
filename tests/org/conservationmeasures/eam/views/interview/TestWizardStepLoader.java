/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.interview;

import java.util.Vector;

import org.conservationmeasures.eam.testall.EAMTestCase;
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
		MockWizardStep step = createStepFromData(data);
		assertNull("had a default step name?", step.getStepName());
		assertEquals(0, step.getElementCount());
	}

	public void testHtmlElements() throws Exception
	{
		String stepName = "step name";
		String element0 = "This is\nsome boring html\ntext\n";
		String element1 = "\n\n\nMore boring text";
		String data = stepName + "\n" + ":html:\n" + element0 + ":html:\n" + element1;
		MockWizardStep step = createStepFromData(data);
		assertEquals(stepName, step.getStepName());
		assertEquals(2, step.getElementCount());
		assertEquals("wrong element0?", element0, step.getElement(0));
		assertEquals("wrong element1?", element1 + "\n", step.getElement(1));
	}
	
	private MockWizardStep createStepFromData(String data) throws Exception
	{
		MockWizardStep step = new MockWizardStep();
		UnicodeStringReader reader = new UnicodeStringReader(data);
		WizardStepLoader.load(step, reader);
		return step;
	}

	static class MockWizardStep extends WizardStep
	{
		public MockWizardStep()
		{
			elements = new Vector();
		}
		
		public void addText(String text)
		{
			elements.add(text);
		}

		public int getElementCount()
		{
			return elements.size();
		}
		
		public String getElement(int index)
		{
			return (String)elements.get(index);
		}
		
		Vector elements;
	}
	
}
