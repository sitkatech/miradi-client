/* 
 * Copyright 2005-2007, Wildlife Conservation Society, 
 * Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
 * Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
 */ 
package org.conservationmeasures.eam.utils;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.swing.tree.TreePath;

import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.views.TreeTableNode;

import com.java.sun.jtreetable.TreeTableModel;

public class TreeTableModelExporter
{
	public TreeTableModelExporter(File fileToUse, TreeTableModel treeTableToUse)
	{
		fileToExportTo = fileToUse;
		treeTableModelToExport = treeTableToUse;	
	}

	public void export() throws Exception
	{
		PrintWriter printWriter = null;
		try
		{
			printWriter = new PrintWriter(fileToExportTo);
			writeOutTreeTablModel(printWriter);
		}
		catch (IOException e)
		{
			EAM.logException(e);
		}
		finally
		{
			if (printWriter == null)
				throw new IOException();
	
			printWriter.close();
		}
	}
	
	private void writeOutTreeTablModel(PrintWriter printWriter)
	{
		TreePath[] paths = getAllTreePaths(printWriter);
		int longestPath = getLongestPath(paths);
		
		writeAllPaths(printWriter, paths, longestPath);
	}
	
	private void writeAllPaths(PrintWriter printWriter, TreePath[] paths, int longestPath)
	{
		for (int i = 0; i < paths.length; i++)
			writeNode(paths[i], printWriter, longestPath);
	}

	private int getLongestPath(TreePath[] paths)
	{
		int longestPathCount = 0;
		for (int i = 0; i < paths.length; i++)
			longestPathCount = Math.max(paths[i].getPathCount(), longestPathCount);
		
		return longestPathCount;
	}

    public TreePath[] getAllTreePaths(PrintWriter printWriter) {
        TreeTableNode root = (TreeTableNode)treeTableModelToExport.getRoot();
        List list = new ArrayList();
        addPath(new TreePath(root), list, printWriter);

        return (TreePath[])list.toArray(new TreePath[list.size()]);
    }
    
    public void addPath(TreePath parent, List list, PrintWriter printWriter) 
    {
        list.add(parent);
        TreeTableNode node = (TreeTableNode)parent.getLastPathComponent();
        if (node.getChildCount() >= 0)
        {
        	for (int i  = 0; i < node.getChildCount(); i++)
            {
        		TreeTableNode n = node.getChild(i);
                TreePath path = parent.pathByAddingChild(n);
                addPath(path, list, printWriter);
            }
        }
    }

    private void writeNode(TreePath path, PrintWriter printWriter, int longestPath)
	{
    	int pathCount = path.getPathCount() - INVISIBLE_ROOT;
		padWithTabs(printWriter, pathCount);
        Object lastPathComponent = path.getLastPathComponent();

        if (lastPathComponent.toString() == null)
        	return;

        printWriter.print(lastPathComponent.toString());
        int diff = longestPath  - pathCount;
        padWithTabs(printWriter, diff - INVISIBLE_ROOT);
        appendTotalsColumns(printWriter, (TreeTableNode)lastPathComponent);

        printWriter.print(NEW_LINE);
	}

    private void appendTotalsColumns(PrintWriter printWriter, TreeTableNode node)
	{
    	int colCount = treeTableModelToExport.getColumnCount();
		for (int colCounter = 0; colCounter < colCount; colCounter++ )
		{
			String valueToWrite = treeTableModelToExport.getValueAt(node, colCounter).toString();
			printWriter.print(valueToWrite);
			printWriter.print(TAB_SEPERATOR);
		}
	}

	private void padWithTabs(PrintWriter printWriter, int tabCount)
	{
    	for (int i = 0; i < tabCount; i++)
    		printWriter.print(TAB_SEPERATOR);
	}

	private File fileToExportTo;
	private TreeTableModel treeTableModelToExport;
	private static final String TAB_SEPERATOR = "\t";
	private static final String NEW_LINE = "\n";
	private static final int INVISIBLE_ROOT = 1;
}
