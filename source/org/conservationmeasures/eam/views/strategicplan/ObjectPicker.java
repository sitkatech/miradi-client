/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.strategicplan;

import javax.swing.event.ListSelectionListener;

import org.conservationmeasures.eam.objects.EAMObject;

public interface ObjectPicker
{
	public EAMObject[] getSelectedObjects();
	public void addListSelectionListener(ListSelectionListener listener);
}
