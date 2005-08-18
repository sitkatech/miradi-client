/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.interview;

import java.io.IOException;

import org.martus.util.UnicodeReader;

public class WizardStepLoader
{
	static public void load(WizardStep step, UnicodeReader reader) throws IOException 
	{
		String name = reader.readLine();
		step.setStepName(name);
		
		StringBuffer elementData = new StringBuffer();
		while(true)
		{
			String line = reader.readLine();
			if(line == null)
				break;
			if(line.startsWith(":"))
			{
				append(step, elementData);
			}
			else
			{
				elementData.append(line);
				elementData.append("\n");
			}
		}
		append(step, elementData);
	}
	
	static public void append(WizardStep step, StringBuffer elementData)
	{
		if(elementData.length() == 0)
			return;

		step.addText(new String(elementData));
		elementData.setLength(0);
	}
}
