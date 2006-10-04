/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.project;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.views.interview.InterviewView;
import org.json.JSONObject;
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
		info.obtainRealNodeId(new BaseId(27));
		info.obtainRealLinkageId(new BaseId(55));
		info.getAnnotationIdAssigner().takeNextId();
		
		ProjectInfo loaded = new ProjectInfo();
		loaded.fillFrom(info.toJson());
		verifyLoadedData(loaded, info);
	}

	private void verifyLoadedData(ProjectInfo loaded, ProjectInfo info)
	{
		assertEquals("Didn't keep current view?", info.getCurrentView(), loaded.getCurrentView());
		assertEquals("Didn't keep next id?", info.getNodeIdAssigner().getHighestAssignedId(), loaded.getNodeIdAssigner().getHighestAssignedId());
		assertEquals("Didn't keep next annotation id?", info.getAnnotationIdAssigner().getHighestAssignedId(), loaded.getAnnotationIdAssigner().getHighestAssignedId());
	}
	
	public void testEmptyJson() throws Exception
	{
		ProjectInfo info = new ProjectInfo();
		
		ProjectInfo loaded = new ProjectInfo();
		loaded.fillFrom(new JSONObject());
		verifyLoadedData(loaded, info);
	}
		
	public void testClear()
	{
		ProjectInfo info = new ProjectInfo();
		info.setCurrentView("super-view");
		info.obtainRealNodeId(new BaseId(27));
		info.getAnnotationIdAssigner().takeNextId();
		
		info.clear();
		assertEquals("Didn't clear currentView", InterviewView.getViewName(), info.getCurrentView());
		assertEquals("didn't clear id?", new BaseId(0), info.getNodeIdAssigner().takeNextId());
		assertEquals("didn't clear annotation id?", new BaseId(0), info.getAnnotationIdAssigner().takeNextId());
	}

	String testKey = "TestKey";

}
