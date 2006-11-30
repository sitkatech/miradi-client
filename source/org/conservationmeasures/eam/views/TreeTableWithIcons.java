/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views;

import java.awt.Component;
import java.awt.Font;
import java.util.Vector;

import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableCellEditor;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreeSelectionModel;

import org.conservationmeasures.eam.icons.ActivityIcon;
import org.conservationmeasures.eam.icons.GoalIcon;
import org.conservationmeasures.eam.icons.IndicatorIcon;
import org.conservationmeasures.eam.icons.ObjectiveIcon;
import org.conservationmeasures.eam.icons.StrategyIcon;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objectpools.EAMObjectPool;
import org.conservationmeasures.eam.objects.EAMObject;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.views.umbrella.ObjectPicker;
import org.martus.swing.UiLabel;

import com.java.sun.jtreetable.JTreeTable;
import com.java.sun.jtreetable.TreeTableModel;

public class TreeTableWithIcons extends JTreeTable implements ObjectPicker
{
	public TreeTableWithIcons(Project projectToUse, TreeTableModel treeTableModel)
	{
		super(treeTableModel);
		project = projectToUse;
		setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		getTree().setShowsRootHandles(true);
		getTree().setRootVisible(false);
		getTree().setCellRenderer(new Renderer());
		getTree().setEditable(false);
		getColumnModel().getColumn(0).setPreferredWidth(200);
		TableCellEditor ce = new NonEditableTreeTableCellEditor();
		setDefaultEditor(TreeTableModel.class, ce);
	}

		public GenericTreeTableModel getTreeTableModel()
	{
		return (GenericTreeTableModel)getModel();
	}

	protected static class Renderer extends DefaultTreeCellRenderer
	{
		public Renderer()
		{
			Font uiLabelFont = new UiLabel().getFont();
			Font boldFont = uiLabelFont.deriveFont(Font.BOLD);
			Font italicFont = uiLabelFont.deriveFont(Font.ITALIC);
			
			interventionRenderer = new DefaultTreeCellRenderer();
			interventionRenderer.setClosedIcon(new StrategyIcon());
			interventionRenderer.setOpenIcon(new StrategyIcon());
			interventionRenderer.setLeafIcon(new StrategyIcon());
			
			objectiveRenderer = new DefaultTreeCellRenderer();
			objectiveRenderer.setClosedIcon(new ObjectiveIcon());
			objectiveRenderer.setOpenIcon(new ObjectiveIcon());
			objectiveRenderer.setLeafIcon(new ObjectiveIcon());
			objectiveRenderer.setFont(boldFont);

			indicatorRenderer = new DefaultTreeCellRenderer();
			indicatorRenderer.setClosedIcon(new IndicatorIcon());
			indicatorRenderer.setOpenIcon(new IndicatorIcon());
			indicatorRenderer.setLeafIcon(new IndicatorIcon());
			
			goalRenderer = new DefaultTreeCellRenderer();
			goalRenderer.setClosedIcon(new GoalIcon());
			goalRenderer.setOpenIcon(new GoalIcon());
			goalRenderer.setLeafIcon(new GoalIcon());
			goalRenderer.setFont(boldFont);
			
			
			activitiesRenderer = new DefaultTreeCellRenderer();
			activitiesRenderer.setClosedIcon(new ActivityIcon());
			activitiesRenderer.setOpenIcon(new ActivityIcon());
			activitiesRenderer.setLeafIcon(new ActivityIcon());
			activitiesRenderer.setFont(italicFont);

			stringNoIconRenderer = new DefaultTreeCellRenderer();
			stringNoIconRenderer.setClosedIcon(null);
			stringNoIconRenderer.setOpenIcon(null);
			stringNoIconRenderer.setLeafIcon(null);
			
			defaultRenderer = new DefaultTreeCellRenderer();
		}

		public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocusToUse)
		{
			TreeCellRenderer renderer = defaultRenderer;
			
			TreeTableNode node = (TreeTableNode) value;
			if (node.getType() == ObjectType.FAKE)
				renderer  = stringNoIconRenderer;
			else if(node.getType() == ObjectType.INDICATOR)
				renderer = indicatorRenderer;
			else if(node.getType() == ObjectType.MODEL_NODE)
				renderer = interventionRenderer;
			else if(node.getType() == ObjectType.OBJECTIVE)
				renderer = objectiveRenderer;
			else if(node.getType() == ObjectType.GOAL)
				renderer = goalRenderer;
			else if(node.getType() == ObjectType.TASK)
				renderer = activitiesRenderer;
			
			return renderer.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
		}

		DefaultTreeCellRenderer objectiveRenderer;
		DefaultTreeCellRenderer goalRenderer;
		DefaultTreeCellRenderer indicatorRenderer;
		DefaultTreeCellRenderer activitiesRenderer;
		DefaultTreeCellRenderer defaultRenderer;
		DefaultTreeCellRenderer interventionRenderer;
		DefaultTreeCellRenderer stringNoIconRenderer;
	}

	class NonEditableTreeTableCellEditor extends TreeTableCellEditor
	{
		public NonEditableTreeTableCellEditor() 
		{
		    super();
		}
		
		public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int r, int c)
		{
		    ((JTextField)getComponent()).setEditable(false);
			return super.getTableCellEditorComponent(table, value, isSelected, r, c);
		}
	}

	public void addListSelectionListener(ListSelectionListener listener)
	{
		getSelectionModel().addListSelectionListener(listener);
	}

	public EAMObject[] getSelectedObjects()
	{
		Vector selectedObjects = new Vector();
		TreeTableNode selectedNode = (TreeTableNode)getTree().getLastSelectedPathComponent();
		
		if (selectedNode == null)
			return (EAMObject[])selectedObjects.toArray(new EAMObject[0]);
		
		ORef oRef = selectedNode.getObjectReference();
		EAMObjectPool pool = project.getPool(oRef.getObjectType());
		
		if (pool == null)
			return (EAMObject[])selectedObjects.toArray(new EAMObject[0]);
		
		EAMObject foundObject = pool.findObject(oRef.getObjectId());
		selectedObjects.add(foundObject);
	
		return (EAMObject[])selectedObjects.toArray(new EAMObject[0]);
	}
	
	Project project;
}
