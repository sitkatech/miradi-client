/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.interview;

import java.awt.Dimension;
import java.io.IOException;

import javax.swing.JComponent;
import javax.swing.JLabel;

import org.martus.swing.UiTextArea;
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

		step.addComponent(elementData.createComponent());
	}
	
	static abstract class ElementData
	{
		abstract public boolean hasData();
		abstract public void appendLine(String text);
		abstract public JComponent createComponent();
		abstract public String toString();
	}
	
	static class TextElementData extends ElementData
	{
		public TextElementData()
		{
			data = new StringBuffer("");
		}
		
		public boolean hasData()
		{
			return data.length() > 0;
		}
		
		public void appendLine(String text)
		{
			data.append(text);
			data.append("\n");
		}
		
		public String toString()
		{
			return data.toString();
		}
		
		public JComponent createComponent()
		{
			return new JLabel(toString());
		}

		private StringBuffer data;

	}
	
	static class NullElementData extends ElementData
	{
		public boolean hasData()
		{
			return false;
		}

		public void appendLine(String text)
		{
			throw new RuntimeException();
		}

		public String toString()
		{
			return null;
		}

		public JComponent createComponent()
		{
			throw new RuntimeException();
		}
	}
	
	static class HtmlElementData extends TextElementData
	{
		public String toString()
		{
			return "<html>" + super.toString() + "</html>";
		}
	}
	
	static class InputElementData extends ElementData
	{
		public boolean hasData()
		{
			return true;
		}
		
		public String toString()
		{
			return "<<input>>";
		}

		public void appendLine(String text)
		{
			throw new RuntimeException();
		}

		public JComponent createComponent()
		{
			NotHugeTextArea field = new NotHugeTextArea();
			field.setLineWrap(true);
			field.setWrapStyleWord(true);
			return field;
		}
	}
	
	static class NotHugeTextArea extends UiTextArea
	{
		public NotHugeTextArea()
		{
			super(1, 80);
		}
		
		public Dimension getMaximumSize()
		{
			return getPreferredSize();
		}
	}
}
