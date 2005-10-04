/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.interview;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import org.conservationmeasures.eam.views.interview.elements.ElementData;

public class InterviewStepModel
{
	public InterviewStepModel(String stepNameToUse)
	{
		stepName = stepNameToUse;
		elements = new Vector();
	}
	
	public void addElement(ElementData elementToAdd)
	{
		elements.add(elementToAdd);
	}
	
	public int getElementCount()
	{
		return elements.size();
	}
	
	public ElementData getElement(int n)
	{
		return (ElementData)elements.get(n);
	}
	
	public String getStepName()
	{
		return stepName;
	}
	
	public String getNextStepName()
	{
		return nextStepName;
	}
	
	public void setNextStepName(String newNextStepName)
	{
		nextStepName = newNextStepName;
	}
	
	public String getPreviousStepName()
	{
		return previousStepName;
	}
	
	public void setPreviousStepName(String newPreviousStepName)
	{
		previousStepName = newPreviousStepName;
	}
	
	public boolean isPreviousAvailable()
	{
		return previousStepName.length() > 0;
	}
	
	public boolean isNextAvailable()
	{
		return nextStepName.length() > 0;
	}

	public Map getData()
	{
		HashMap stepData = new HashMap();
		for(int i=0; i < elements.size(); ++i)
		{
			ElementData element = (ElementData)elements.get(i);
			if(element.hasData())
			{
				stepData.put(element.getFieldName(), element.getFieldData());
			}
		}
		return stepData;
	}
	
	private String stepName;
	private String nextStepName;
	private String previousStepName;
	private Vector elements;
}
