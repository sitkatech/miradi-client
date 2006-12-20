/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.dialogs;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.LayoutManager2;

import javax.swing.JScrollPane;
import javax.swing.JSplitPane;

abstract public class VerticalSplitPanel extends ModelessDialogPanel
{
	public VerticalSplitPanel()
	{
		this(new BorderLayout());
	}
	
	public VerticalSplitPanel(LayoutManager2 layoutToUse)
	{
		super(layoutToUse);
	}

	public void createVerticalSplitPane(Component top, Component bottom)
	{
		JScrollPane treeComponentScroll = new JScrollPane(top);
		JScrollPane propertiesScroll = new JScrollPane(bottom);
		
		JSplitPane splitter = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		splitter.setOneTouchExpandable(true);
		splitter.setDividerSize(15);
		splitter.setResizeWeight(0.5);
		splitter.setTopComponent(treeComponentScroll);
		splitter.setBottomComponent(propertiesScroll);
		splitter.setDividerLocation(200);
		add(splitter, BorderLayout.CENTER);
	}
}
