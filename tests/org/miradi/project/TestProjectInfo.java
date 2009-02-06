/* 
Copyright 2005-2009, Foundations of Success, Bethesda, Maryland 
(on behalf of the Conservation Measures Partnership, "CMP") and 
Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 

This file is part of Miradi

Miradi is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License version 3, 
as published by the Free Software Foundation.

Miradi is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Miradi.  If not, see <http://www.gnu.org/licenses/>. 
*/
package org.miradi.project;

import org.json.JSONObject;
import org.martus.util.TestCaseEnhanced;
import org.miradi.ids.BaseId;
import org.miradi.project.ProjectInfo;

public class TestProjectInfo extends TestCaseEnhanced
{
	public TestProjectInfo(String name)
	{
		super(name);
	}
	
	public void testJson() throws Exception
	{
		ProjectInfo info = new ProjectInfo();
		info.obtainRealFactorId(new BaseId(27));
		info.obtainRealLinkId(new BaseId(55));
		info.getNormalIdAssigner().takeNextId();
		info.setMetadataId(new BaseId(79));
		
		ProjectInfo loaded = new ProjectInfo();
		loaded.fillFrom(info.toJson());
		verifyLoadedData(loaded, info);
	}

	private void verifyLoadedData(ProjectInfo loaded, ProjectInfo info)
	{
		assertEquals("Didn't keep next id?", info.getFactorAndLinkIdAssigner().getHighestAssignedId(), loaded.getFactorAndLinkIdAssigner().getHighestAssignedId());
		assertEquals("Didn't keep next annotation id?", info.getNormalIdAssigner().getHighestAssignedId(), loaded.getNormalIdAssigner().getHighestAssignedId());
		assertEquals("Didn't keep metadata id?", info.getMetadataId(), loaded.getMetadataId());
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
		info.obtainRealFactorId(new BaseId(27));
		info.getNormalIdAssigner().takeNextId();
		assertTrue("MetadataId not invalid?", info.getMetadataId().isInvalid());
		BaseId metadataId = new BaseId(525);
		info.setMetadataId(metadataId);
		assertEquals("set/get metadata not working?", metadataId, info.getMetadataId());
		
		info.clear();
		assertEquals("didn't clear id?", -1, info.getFactorAndLinkIdAssigner().getHighestAssignedId());
		assertEquals("didn't clear annotation id?", -1, info.getNormalIdAssigner().getHighestAssignedId());
		assertTrue("didn't clear metadata id?", info.getMetadataId().isInvalid());
	}

	String testKey = "TestKey";

}
