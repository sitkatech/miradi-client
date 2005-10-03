/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.interview;

import java.io.IOException;


import org.conservationmeasures.eam.views.interview.elements.ElementData;
import org.conservationmeasures.eam.views.interview.elements.HtmlElementData;
import org.conservationmeasures.eam.views.interview.elements.InputElementData;
import org.conservationmeasures.eam.views.interview.elements.ListElementData;
import org.conservationmeasures.eam.views.interview.elements.NullElementData;
import org.martus.util.UnicodeReader;

public class StepLoader
{
	static public InterviewStepModel load(UnicodeReader reader) throws IOException 
	{
		String stepName = reader.readLine();
		InterviewStepModel step = new InterviewStepModel(stepName);
		
		String nextStepName = reader.readLine();
		step.setNextStepName(nextStepName);
		
		String previousStepName = reader.readLine();
		step.setPreviousStepName(previousStepName);
		
		ElementData elementData = new NullElementData();
		while(true)
		{
			String line = reader.readLine();
			if(line == null)
				break;
			if(line.startsWith(":"))
			{
				append(step, elementData);
				elementData = null;
				if(line.equals(htmlElementTag))
					elementData = new HtmlElementData();
				else if(line.equals(inputElementTag))
					elementData = new InputElementData();
				else if(line.startsWith(listElementTag))
					elementData = new ListElementData(line.substring(listElementTag.length()));
				else 
					System.out.println("Unknown element->"+line);
			}
			else
			{
				elementData.appendLine(line);
			}
		}
		append(step, elementData);
		
		return step;
	}
	
	static public void append(InterviewStepModel step, ElementData elementData)
	{
		if(elementData.isEmpty())
			return;

		step.addElement(elementData);
	}
	static final String htmlElementTag = ":html:";
	static final String inputElementTag = ":input:";
	static final String listElementTag = ":list:";
}
