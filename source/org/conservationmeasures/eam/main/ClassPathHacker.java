/* 
 * Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
 * (on behalf of the Conservation Measures Partnership, "CMP") and 
 * Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
 */
package org.conservationmeasures.eam.main;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;

// This class is based on a forum posting:
//   http://forum.java.sun.com/thread.jspa?threadID=300557

public class ClassPathHacker
{
	public static void addFile(File f) throws Exception
	{
		addURL(f.toURI().toURL());
	}

	public static void addURL(URL urlToAdd) throws Exception
	{
		final Object[] urlAsObjectArray = new Object[] { urlToAdd };

		final URLClassLoader systemClassLoader = (URLClassLoader) ClassLoader.getSystemClassLoader();
		final Class systemClassLoaderClass = URLClassLoader.class;

		final Class[] parameters = new Class[] { URL.class };
		final Method method = systemClassLoaderClass.getDeclaredMethod("addURL", parameters);
		method.setAccessible(true);
		method.invoke(systemClassLoader, urlAsObjectArray);
	}

}
