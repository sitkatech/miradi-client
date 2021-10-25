/* 
Copyright 2005-2021, Foundations of Success, Bethesda, Maryland
on behalf of the Conservation Measures Partnership ("CMP").
Material developed between 2005-2013 is jointly copyright by Beneficent Technology, Inc. ("The Benetech Initiative"), Palo Alto, California.

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

import org.miradi.utils.FileUtilities;

abstract public class AbstractConverter
{
	protected String getVersionEntryPath() throws Exception
	{
		return FileUtilities.join(getJsonPrefix(), "version");
	}
	
	protected String getProjectInfoEntryPath() throws Exception
	{
		return FileUtilities.join(getJsonPrefix(), "project");
	}
	
	protected String getManifestFileName(int objectType) throws Exception
	{
		return FileUtilities.join(getObjectsDirectoryPrefix(objectType), "manifest");
	}
	
	protected String getObjectsDirectoryPrefix(int objectType) throws Exception
	{
		return FileUtilities.join(getJsonPrefix(), "objects-" + objectType);
	}
	
	protected String getJsonPrefix() throws Exception
	{
		return FileUtilities.join(getProjectPrefix(), "json");
	}
	
	protected String getThreatFrameworkEntryPath() throws Exception
	{
		return FileUtilities.join(getJsonPrefix(), "threatframework");
	}
	
	protected String getThreatRatingsDirectoryEntryPath() throws Exception
	{
		return FileUtilities.join(getJsonPrefix(), "threatratings");
	}
	
	abstract protected String getProjectPrefix();
}
