/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.interview;

import java.util.Vector;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTextArea;

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
		JLabel label0 = (JLabel)step.getElement(0);
		assertEquals("wrong element0?", htmlStart + element0 + htmlEnd, label0.getText());
		JLabel label1 = (JLabel)step.getElement(1);
		assertEquals("wrong element1?", htmlStart + element1 + "\n" + htmlEnd, label1.getText());
	}
	
	public void testInputField() throws Exception
	{
		String prompt = ":html:\nPlease enter some data\n";
		String inputField = ":input:\n";
		String template = "name\n" + prompt + inputField;
		MockWizardStep step = createStepFromData(template);
		assertEquals(2, step.getElementCount());
		JTextArea inputComponent = (JTextArea)step.getElement(1);
		assertNotNull(inputComponent);
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
		
		public void addComponent(JComponent componentToAdd)
		{
			elements.add(componentToAdd);
		}

		public int getElementCount()
		{
			return elements.size();
		}
		
		public JComponent getElement(int index)
		{
			return (JComponent)elements.get(index);
		}
		
		Vector elements;
	}
	
	final static String htmlStart = "<html>";
	final static String htmlEnd = "</html>";
}
