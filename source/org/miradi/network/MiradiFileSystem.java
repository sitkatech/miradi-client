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
package org.miradi.network;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Vector;

public interface MiradiFileSystem
{
	public abstract void setDataLocation(String dataLocation) throws Exception;
	public abstract String getDataLocation();
	
	public abstract boolean doesProjectDirectoryExist(String projectName) throws Exception;
	public abstract void createProject(String projectName) throws Exception;
	public abstract void deleteProject(String projectName) throws Exception;
	
	public abstract void lockProject(String projectName) throws Exception;
	public abstract void unlockProject(String projectName) throws Exception;

	public abstract boolean doesFileExist(String projectName, File file) throws Exception;
	public abstract Map<Integer, String> readAllManifestFiles(String projectName) throws Exception;
	public abstract Map<File, String> readMultipleFiles(String projectName, Vector<File> filePathSet) throws Exception;
	public abstract String readFile(String projectName, File file) throws Exception;

	public abstract void beginTransaction(String projectName) throws Exception;
	public abstract void endTransaction() throws Exception;
	public abstract void writeFile(String projectName, File file, String contents) throws Exception;
	public abstract void writeMultipleFiles(String projectName, HashMap<File, String> pendingWrites) throws Exception;
	public abstract void deleteFile(String projectName, File file) throws Exception;
	public abstract void deleteMultipleFiles(String projectName, HashSet<File> pendingDeletes) throws Exception;

}
