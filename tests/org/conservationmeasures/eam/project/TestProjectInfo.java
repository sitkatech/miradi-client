/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.project;

import org.conservationmeasures.eam.views.NoProjectView;
import org.martus.util.TestCaseEnhanced;

public class TestProjectInfo extends TestCaseEnhanced
{
	public TestProjectInfo(String name)
	{
		super(name);
	}
	
	public void testJson() throws Exception
	{
		ProjectInfo info = new ProjectInfo();
		info.setCurrentView("super-view");
		info.obtainRealNodeId(27);
		info.obtainRealLinkageId(55);
		info.getAnnotationIdAssigner().takeNextId();
		String testKey = "TestKey";
		info.getProjectData().put(testKey, "sample data");
		
		ProjectInfo loaded = new ProjectInfo();
		loaded.fillFrom(info.toJson());
		assertEquals("Didn't keep current view?", info.getCurrentView(), loaded.getCurrentView());
		assertEquals("Didn't keep next id?", info.getIdAssigner().getHighestAssignedId(), loaded.getIdAssigner().getHighestAssignedId());
		assertEquals("Didn't keep next annotation id?", info.getAnnotationIdAssigner().getHighestAssignedId(), loaded.getAnnotationIdAssigner().getHighestAssignedId());
		assertEquals("Didn't keep project data?", info.getProjectData().getString(testKey), loaded.getProjectData().getString(testKey));
	}
		
	public void testClear()
	{
		ProjectInfo info = new ProjectInfo();
		info.setCurrentView("super-view");
		info.obtainRealNodeId(27);
		info.getAnnotationIdAssigner().takeNextId();
		
		info.clear();
		assertEquals("Didn't clear currentView", NoProjectView.getViewName(), info.getCurrentView());
		assertEquals("didn't clear id?", 0, info.getIdAssigner().takeNextId());
		assertEquals("didn't clear annotation id?", 0, info.getAnnotationIdAssigner().takeNextId());
	}

}
