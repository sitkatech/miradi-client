/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.interview.elements;

import javax.swing.JComponent;

public abstract class ElementData
{
	abstract public boolean hasData();
	abstract public void appendLine(String text);
	abstract public JComponent createComponent();
	abstract public String toString();
}