/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.treetables;

import java.awt.Component;
import java.awt.Font;
import java.awt.font.TextAttribute;
import java.util.Map;
import java.util.Vector;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableCellEditor;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import org.conservationmeasures.eam.dialogs.fieldComponents.PanelTreeTable;
import org.conservationmeasures.eam.dialogs.tablerenderers.RowColumnBaseObjectProvider;
import org.conservationmeasures.eam.icons.ActivityIcon;
import org.conservationmeasures.eam.icons.ConceptualModelIcon;
import org.conservationmeasures.eam.icons.DirectThreatIcon;
import org.conservationmeasures.eam.icons.GoalIcon;
import org.conservationmeasures.eam.icons.IndicatorIcon;
import org.conservationmeasures.eam.icons.KeyEcologicalAttributeIcon;
import org.conservationmeasures.eam.icons.MeasurementIcon;
import org.conservationmeasures.eam.icons.MethodIcon;
import org.conservationmeasures.eam.icons.ObjectiveIcon;
import org.conservationmeasures.eam.icons.ResultsChainIcon;
import org.conservationmeasures.eam.icons.StrategyIcon;
import org.conservationmeasures.eam.icons.TargetIcon;
import org.conservationmeasures.eam.icons.TaskIcon;
import org.conservationmeasures.eam.icons.ThreatReductionResultIcon;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objecthelpers.ORefList;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objectpools.EAMObjectPool;
import org.conservationmeasures.eam.objects.BaseObject;
import org.conservationmeasures.eam.objects.ConceptualModelDiagram;
import org.conservationmeasures.eam.objects.Factor;
import org.conservationmeasures.eam.objects.ResultsChainDiagram;
import org.conservationmeasures.eam.objects.Task;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.views.umbrella.ObjectPicker;

import com.java.sun.jtreetable.TreeTableModel;
import com.java.sun.jtreetable.TreeTableModelAdapter;

public class TreeTableWithIcons extends PanelTreeTable implements ObjectPicker, RowColumnBaseObjectProvider
{
	public TreeTableWithIcons(Project projectToUse, GenericTreeTableModel treeTableModelToUse)
	{
		super(treeTableModelToUse);
		treeTableModel = treeTableModelToUse;
		project = projectToUse;
		selectionListeners = new Vector();

		setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		getTree().setShowsRootHandles(true);
		getTree().setRootVisible(false);
		getTree().setCellRenderer(new Renderer());
		getTree().setEditable(false);
		getColumnModel().getColumn(0).setPreferredWidth(200);
		TableCellEditor ce = new NonEditableTreeTableCellEditor();
		setDefaultEditor(TreeTableModel.class, ce);
		if (getRowCount()>0)
			setRowSelectionInterval(0,0);
		
		final int CUSTOM_HEIGHT_TO_AVOID_ICON_CROPPING = getRowHeight() + 1;
		setRowHeight(CUSTOM_HEIGHT_TO_AVOID_ICON_CROPPING);
	}

	public Project getProject()
	{
		return project;
	}

	public GenericTreeTableModel getTreeTableModel()
	{
		return treeTableModel;
	}
	
	public static Font createFristLevelFont(Font defaultFontToUse)
	{
		Map map = defaultFontToUse.getAttributes();
	    map.put(TextAttribute.SIZE, new Float(defaultFontToUse.getSize2D() + 2));
	    map.put(TextAttribute.WEIGHT, TextAttribute.WEIGHT_BOLD);
	    Font customFont = new Font(map);
		return customFont;
	}
	
	public static Font getSharedTaskFont(Task task)
	{
		int style = Font.PLAIN;
		if (task.isShared())
			style |= Font.ITALIC;

		if (task.isMethod())
			style |= Font.BOLD;
				
		return Renderer.deriveFont(style);
	}
	
	public static Font getSharedTaskFont(TreeTableNode node)
	{
		if (node.getType() != Task.getObjectType())
			return Renderer.getPlainFont();
			
		return getSharedTaskFont((Task) node.getObject());	
	}

	public static class Renderer extends DefaultTreeCellRenderer
	{		
		public Renderer()
		{	
			targetRenderer = new DefaultTreeCellRenderer();
			targetRenderer.setClosedIcon(new TargetIcon());
			targetRenderer.setOpenIcon(new TargetIcon());
			targetRenderer.setLeafIcon(new TargetIcon());
			targetRenderer.setFont(getBoldFont());

			directThreatRenderer = new DefaultTreeCellRenderer();
			directThreatRenderer.setClosedIcon(new DirectThreatIcon());
			directThreatRenderer.setOpenIcon(new DirectThreatIcon());
			directThreatRenderer.setLeafIcon(new DirectThreatIcon());
			directThreatRenderer.setFont(getPlainFont());

			threatReductionResultRenderer = new DefaultTreeCellRenderer();
			threatReductionResultRenderer.setClosedIcon(new ThreatReductionResultIcon());
			threatReductionResultRenderer.setOpenIcon(new ThreatReductionResultIcon());
			threatReductionResultRenderer.setLeafIcon(new ThreatReductionResultIcon());
			threatReductionResultRenderer.setFont(getPlainFont());

			strategyRenderer = new DefaultTreeCellRenderer();
			strategyRenderer.setClosedIcon(new StrategyIcon());
			strategyRenderer.setOpenIcon(new StrategyIcon());
			strategyRenderer.setLeafIcon(new StrategyIcon());
			strategyRenderer.setFont(getBoldFont());

			objectiveRenderer = new DefaultTreeCellRenderer();
			objectiveRenderer.setClosedIcon(new ObjectiveIcon());
			objectiveRenderer.setOpenIcon(new ObjectiveIcon());
			objectiveRenderer.setLeafIcon(new ObjectiveIcon());
			objectiveRenderer.setFont(getBoldFont());

			indicatorRenderer = new DefaultTreeCellRenderer();
			indicatorRenderer.setClosedIcon(new IndicatorIcon());
			indicatorRenderer.setOpenIcon(new IndicatorIcon());
			indicatorRenderer.setLeafIcon(new IndicatorIcon());
			indicatorRenderer.setFont(getBoldFont());
			
			goalRenderer = new DefaultTreeCellRenderer();
			goalRenderer.setClosedIcon(new GoalIcon());
			goalRenderer.setOpenIcon(new GoalIcon());
			goalRenderer.setLeafIcon(new GoalIcon());
			goalRenderer.setFont(getBoldFont());
			
			activityRenderer = new DefaultTreeCellRenderer();
			activityRenderer.setClosedIcon(new ActivityIcon());
			activityRenderer.setOpenIcon(new ActivityIcon());
			activityRenderer.setLeafIcon(new ActivityIcon());
			activityRenderer.setFont(getPlainFont());

			keyEcologicalAttributeRenderer = new DefaultTreeCellRenderer();
			keyEcologicalAttributeRenderer.setClosedIcon(new KeyEcologicalAttributeIcon());
			keyEcologicalAttributeRenderer.setOpenIcon(new KeyEcologicalAttributeIcon());
			keyEcologicalAttributeRenderer.setLeafIcon(new KeyEcologicalAttributeIcon());
			keyEcologicalAttributeRenderer.setFont(getPlainFont());
			
			methodRenderer = new DefaultTreeCellRenderer();
			methodRenderer.setClosedIcon(new MethodIcon());
			methodRenderer.setOpenIcon(new MethodIcon());
			methodRenderer.setLeafIcon(new MethodIcon());
			methodRenderer.setFont(getPlainFont());

			taskRenderer = new DefaultTreeCellRenderer();
			taskRenderer.setClosedIcon(new TaskIcon());
			taskRenderer.setOpenIcon(new TaskIcon());
			taskRenderer.setLeafIcon(new TaskIcon());
			taskRenderer.setFont(getPlainFont());
			
			conceptualModelRenderer = new DefaultTreeCellRenderer();
			conceptualModelRenderer.setClosedIcon(new ConceptualModelIcon());
			conceptualModelRenderer.setOpenIcon(new ConceptualModelIcon());
			conceptualModelRenderer.setLeafIcon(new ConceptualModelIcon());
			conceptualModelRenderer.setFont(getBoldFont());

			resultsChainRenderer = new DefaultTreeCellRenderer();
			resultsChainRenderer.setClosedIcon(new ResultsChainIcon());
			resultsChainRenderer.setOpenIcon(new ResultsChainIcon());
			resultsChainRenderer.setLeafIcon(new ResultsChainIcon());
			resultsChainRenderer.setFont(getBoldFont());

			stringNoIconRenderer = new DefaultTreeCellRenderer();
			stringNoIconRenderer.setClosedIcon(null);
			stringNoIconRenderer.setOpenIcon(null);
			stringNoIconRenderer.setLeafIcon(null);
			Font customFont = createFristLevelFont(getPlainFont());
			stringNoIconRenderer.setFont(customFont);
			
			defaultRenderer = new DefaultTreeCellRenderer();
			defaultRenderer.setFont(getPlainFont());
			
			measurementRenderer = new DefaultTreeCellRenderer();
			measurementRenderer.setClosedIcon(new MeasurementIcon());
			measurementRenderer.setOpenIcon(new MeasurementIcon());
			measurementRenderer.setLeafIcon(new MeasurementIcon());
		}

		public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocusToUse)
		{
			TreeCellRenderer renderer = defaultRenderer;
			
			TreeTableNode node = (TreeTableNode) value;
			if (node.getType() == ObjectType.FAKE)
				renderer  = stringNoIconRenderer;
			else if(node.getType() == ConceptualModelDiagram.getObjectType())
				renderer = conceptualModelRenderer;
			else if(node.getType() == ResultsChainDiagram.getObjectType())
				renderer = resultsChainRenderer;
			else if(node.getType() == ObjectType.TARGET)
				renderer = targetRenderer;
			else if(node.getType() == ObjectType.CAUSE)
				renderer = directThreatRenderer;
			else if(node.getType() == ObjectType.THREAT_REDUCTION_RESULT)
				renderer = threatReductionResultRenderer;
			else if(node.getType() == ObjectType.INDICATOR)
				renderer = indicatorRenderer;
			else if(node.getType() == ObjectType.STRATEGY)
				renderer = getStrategyRenderer((Factor)node.getObject());
			else if(node.getType() == ObjectType.OBJECTIVE)
				renderer = objectiveRenderer;
			else if(node.getType() == ObjectType.GOAL)
				renderer = goalRenderer;
			else if(node.getType() == ObjectType.TASK)
				renderer = getTaskRenderer((Task)node.getObject());
			else if(node.getType() == ObjectType.KEY_ECOLOGICAL_ATTRIBUTE)
				renderer = keyEcologicalAttributeRenderer;
			else if(node.getType() == ObjectType.MEASUREMENT)
				renderer = measurementRenderer;
			
			return renderer.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
		}
		
		private TreeCellRenderer getTaskRenderer(Task task)
		{
			if(task.isActivity())
				return getRendererWithSetSharedTaskItalicFont(activityRenderer, task);
			if(task.isMethod())
				return getRendererWithSetSharedTaskItalicFont(methodRenderer, task);
			
			return taskRenderer;
		}
		
		private DefaultTreeCellRenderer getRendererWithSetSharedTaskItalicFont(DefaultTreeCellRenderer renderer, Task task)
		{
			renderer.setFont(getSharedTaskFont(task));
			return renderer;
		}
		
		protected DefaultTreeCellRenderer getStrategyRenderer(Factor factor)
		{
			return strategyRenderer;
		}
		
		public static Font getBoldFont()
		{
			return deriveFont(Font.BOLD);
		}

		public static Font getPlainFont()
		{
			return deriveFont(Font.PLAIN);
		}
		
		public static Font deriveFont(int style)
		{
			Font defaultFont = EAM.getMainWindow().getUserDataPanelFont();
			return defaultFont.deriveFont(style);
		}

		private DefaultTreeCellRenderer targetRenderer;
		private DefaultTreeCellRenderer strategyRenderer;
		private DefaultTreeCellRenderer objectiveRenderer;
		protected DefaultTreeCellRenderer goalRenderer;
		protected DefaultTreeCellRenderer indicatorRenderer;
		private DefaultTreeCellRenderer activityRenderer;
		private DefaultTreeCellRenderer methodRenderer;
		private DefaultTreeCellRenderer taskRenderer;
		private DefaultTreeCellRenderer conceptualModelRenderer;
		private DefaultTreeCellRenderer resultsChainRenderer;
		private DefaultTreeCellRenderer defaultRenderer;
		private DefaultTreeCellRenderer stringNoIconRenderer;
		private DefaultTreeCellRenderer keyEcologicalAttributeRenderer;
		private DefaultTreeCellRenderer directThreatRenderer;
		private DefaultTreeCellRenderer threatReductionResultRenderer;
		private	DefaultTreeCellRenderer measurementRenderer;
	}

	class NonEditableTreeTableCellEditor extends TreeTableCellEditor
	{
		public NonEditableTreeTableCellEditor() 
		{
		    super();
		}
		
		public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int r, int c)
		{
			JTextField textField = (JTextField) super.getTableCellEditorComponent(table, value, isSelected, r, c); 
			return convertToJLabelAvoidingEditMode(textField);
		}

		private JLabel convertToJLabelAvoidingEditMode(JTextField component2)
		{
			return new JLabel(component2.getText());
		}
	}
	
	public TreeTableNode[] getSelectedTreeNodes()
	{
		return new TreeTableNode[] {(TreeTableNode)getTree().getLastSelectedPathComponent()};
	}

	 //TODO: This method needs review as it seems a bit complex
	public BaseObject[] getSelectedObjects()
	{
		TreeTableNode selectedNode = (TreeTableNode)getTree().getLastSelectedPathComponent();
		
		if (selectedNode == null)
			return new BaseObject[0];
		
		ORef oRef = selectedNode.getObjectReference();
		EAMObjectPool pool = project.getPool(oRef.getObjectType());
		
		if (pool == null)
			return new BaseObject[0];
		
		BaseObject foundObject = pool.findObject(oRef.getObjectId());
		
		if (foundObject == null)
			return new BaseObject[0];
		
		return new BaseObject[] {foundObject};
	}
	
	public ORefList getSelectionHierarchy()
	{
		TreePath selectionPath = getTree().getSelectionModel().getSelectionPath();
		if (selectionPath == null)
			return new ORefList();
		
		ORefList selectionHierarchyNodeRefs = new ORefList();
		for(int i = selectionPath.getPathCount() - 1; i >=0 ; --i)
		{			
			TreeTableNode node = (TreeTableNode) selectionPath.getPathComponent(i);
			selectionHierarchyNodeRefs.add(node.getObjectReference());
		}
		
		return selectionHierarchyNodeRefs;
	}

	public ORefList[] getSelectedHierarchies()
	{
		TreePath[] selectionPaths = getTree().getSelectionModel().getSelectionPaths();
		if (selectionPaths == null)
			return new ORefList[] {new ORefList(getRootNodeRef())};
		
		ORefList[] selectionHierarchies = new ORefList[selectionPaths.length];
		for (int i = 0; i < selectionPaths.length; ++i)
		{
			selectionHierarchies[i] = convertPath(selectionPaths[i]);
		}
		
		return selectionHierarchies;
	}

	private ORef getRootNodeRef()
	{
		return getTreeTableModel().getRootNode().getObjectReference();
	}
	
	private ORefList convertPath(TreePath treePath)
	{
		ORefList selectionHierarchyNodeRefs = new ORefList();
		for(int i = treePath.getPathCount() - 1; i >=0 ; --i)
		{			
			TreeTableNode node = (TreeTableNode) treePath.getPathComponent(i);
			selectionHierarchyNodeRefs.add(node.getObjectReference());
		}
		
		return selectionHierarchyNodeRefs;	
	}

	public void ensureObjectVisible(ORef ref)
	{
		// NOTE: This code hasn't been proven to work...we believe it needs to be called
		// from inside invokeLater and that it will work if we do that
		TreePath path = getTreeTableModel().findObject(getTreeTableModel().getPathToRoot(), ref);
		getTree().scrollPathToVisible(path);
	}

	public void addSelectionChangeListener(ListSelectionListener listener)
	{
		selectionListeners.add(listener);
	}

	public void removeSelectionChangeListener(ListSelectionListener listener)
	{
		selectionListeners.remove(listener);
	}

	public void valueChanged(ListSelectionEvent e)
	{
		super.valueChanged(e);
		
		if(selectionListeners == null)
			return;
		
		for(int i = 0; i < selectionListeners.size(); ++i)
		{
			ListSelectionListener listener = (ListSelectionListener)selectionListeners.get(i);
			listener.valueChanged(null);
		}
	}

	public String getColumnTag(int tableColumn)
	{
		int modelColumn = convertColumnIndexToModel(tableColumn);
		return getTreeTableModel().getColumnTag(modelColumn);
	}
	
	public TreeTableModelAdapter getTreeTableAdapter()
	{
		return (TreeTableModelAdapter)getModel();
	}
	
	public TreeTableNode getNodeForRow(int row)
	{
		return (TreeTableNode)getTreeTableAdapter().nodeForRow(row);
	}
	
	public BaseObject getBaseObjectForRowColumn(int row, int column)
	{
		return getNodeForRow(row).getObject();
	}
	
	public void selectObject(ORef ref)
	{
		TreePath path = getTreeTableModel().getPathOfNode(ref.getObjectType(), ref.getObjectId());
		tree.setSelectionPath(path);
	}

	public void selectObjectAfterSwingClearsItDueToTreeStructureChange(ORef selectedRef)
	{
		SwingUtilities.invokeLater(new Reselecter(this, selectedRef));
	}
	
	static class Reselecter implements Runnable
	{
		public Reselecter(TreeTableWithIcons treeTableToUse, ORef refToSelect)
		{
			treeTable = treeTableToUse;
			ref = refToSelect;
		}
		
		public void run()
		{
			treeTable.selectObject(ref);
		}
		
		private TreeTableWithIcons treeTable;
		private ORef ref;
	}

	private GenericTreeTableModel treeTableModel;
	Project project;
	Vector selectionListeners;
}
