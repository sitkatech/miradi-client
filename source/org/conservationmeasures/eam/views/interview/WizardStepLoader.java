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
import org.conservationmeasures.eam.views.interview.elements.NullElementData;
import org.martus.util.UnicodeReader;

public class WizardStepLoader
{
	static public void load(WizardStep step, UnicodeReader reader) throws IOException 
	{
		String name = reader.readLine();
		step.setStepName(name);
		
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
				if(line.equals(":html:"))
					elementData = new HtmlElementData();
				else if(line.equals(":input:"))
					elementData = new InputElementData();
			}
			else
			{
				elementData.appendLine(line);
			}
		}
		append(step, elementData);
	}
	
	static public void append(WizardStep step, ElementData elementData)
	{
		if(!elementData.hasData())
			return;

		step.addComponent(elementData);
	}
}
