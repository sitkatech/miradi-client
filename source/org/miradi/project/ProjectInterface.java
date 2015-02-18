/* 
Copyright 2005-2015, Foundations of Success, Bethesda, Maryland
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

import java.util.Collection;

import org.miradi.migrations.VersionRange;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.project.threatrating.ThreatRatingBundle;

public interface ProjectInterface
{
	public ORef createObject(int objectType) throws Exception;
	public ORef createObject(ORef ref) throws Exception;
	public boolean containsObject(ORef ref);
	public Collection<ThreatRatingBundle> getSimpleThreatRatingBundles();
	public ORefList getAllRefsForType(int objectType);
	public VersionRange getCurrentVersionRange() throws Exception;
	public void setObjectData(ORef ref, String tag, String data) throws Exception;
}
