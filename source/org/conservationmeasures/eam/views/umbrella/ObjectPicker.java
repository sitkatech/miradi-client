/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.umbrella;

import javax.swing.event.ListSelectionListener;

import org.conservationmeasures.eam.objects.EAMObject;

public interface ObjectPicker
{
	public EAMObject[] getSelectedObjects();
	public void clearSelection();
	public void addListSelectionListener(ListSelectionListener listener);
}
