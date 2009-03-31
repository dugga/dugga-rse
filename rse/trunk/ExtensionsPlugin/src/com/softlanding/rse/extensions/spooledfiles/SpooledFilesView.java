/*******************************************************************************
 * Copyright (c) 2005-2009 SoftLanding Systems, Inc. and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     SoftLanding - initial API and implementation
 *******************************************************************************/
package com.softlanding.rse.extensions.spooledfiles;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.GroupMarker;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.viewers.IOpenListener;
import org.eclipse.jface.viewers.OpenEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Item;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.part.ViewPart;

import com.softlanding.rse.extensions.ExtensionsPlugin;


public class SpooledFilesView extends ViewPart implements IMenuListener {
	private static SpooledFilesView view;
	private SpooledFilesTableViewer viewer;
	private SelectAllAction selectAllAction;
	private SaveColumnsAction saveColumnsAction;
	private static SpooledFileFilter filter;
	private static SpooledFileFactory spooledFileFactory;
	private boolean disposed;

	public SpooledFilesView() {
		super();
		view = this;
	}
	
	public void createPartControl(Composite parent) {
		if ((filter == null) || (filter.getDescription() == null))
			setPartName(ExtensionsPlugin.getResourceString("Spooled_Files_1")); //$NON-NLS-1$
		else
			setPartName(filter.getDescription());
		viewer = new SpooledFilesTableViewer(parent, SWT.NONE);
    	createMenus();
    	createToolbar();
		getSite().setSelectionProvider(viewer.getTableViewer());
		viewer.getTableViewer().addOpenListener(new IOpenListener() {
			public void open(OpenEvent e) {
				SpooledFileEditAction editAction = new SpooledFileEditAction();
				editAction.setActivePart(null, view);
				editAction.run(null);
			}
		});
	}
	
    void createMenus() {
		MenuManager menuMgr = new
		MenuManager("com.softlanding.turnover.wdsc5.subsystems.spooledfiles.SpooledFilesView"); //$NON-NLS-1$
		Menu menu = menuMgr.createContextMenu(viewer.getTableViewer().getControl());
		menuMgr.addMenuListener(this);
		menuMgr.setRemoveAllWhenShown(true);
		viewer.getTableViewer().getControl().setMenu(menu);
		getSite().registerContextMenu(menuMgr, viewer.getTableViewer());
		IMenuManager barMenuManager = getViewSite().getActionBars().getMenuManager();
		barMenuManager.addMenuListener(this);
		barMenuManager.setRemoveAllWhenShown(true);
		saveColumnsAction = new SaveColumnsAction();
		saveColumnsAction.setText(ExtensionsPlugin.getResourceString("Save_column_sizes_3")); //$NON-NLS-1$
		selectAllAction = new SelectAllAction();
		selectAllAction.setText(ExtensionsPlugin.getResourceString("Select_all_4")); //$NON-NLS-1$
		menuMgr.add(selectAllAction);
		barMenuManager.add(saveColumnsAction);
		barMenuManager.add(selectAllAction);
    }
	
	void createToolbar() {
		IToolBarManager toolbarManager = getViewSite().getActionBars().getToolBarManager();
		RefreshAction refreshAction = new RefreshAction();
		refreshAction.setToolTipText(ExtensionsPlugin.getResourceString("Refresh_5")); //$NON-NLS-1$
		refreshAction.setImageDescriptor(ExtensionsPlugin.getDefault().getImageDescriptor(ExtensionsPlugin.IMAGE_REFRESH));
		toolbarManager.add(refreshAction);
	}
	
	public void menuAboutToShow(IMenuManager menu) {	
		menu.add(saveColumnsAction);
		menu.add(selectAllAction);
		Item[] items = ((Table)viewer.getTableViewer().getControl()).getSelection();
		if (items.length > 0) {
			menu.add(new Separator());
			menu.add(new GroupMarker(IWorkbenchActionConstants.MB_ADDITIONS));
		}
	}
	
	public void setFocus() {
	}
	
	public Item[] getSelectedItems() {
		return viewer.getTableViewer().getTable().getSelection();
	}
	
	public void refresh() {
		if (disposed) return;
		SpooledFilesTableViewer.setSpooledFileFactory(spooledFileFactory);
		SpooledFilesTableViewer.setFilter(filter);
		if ((filter != null) && (filter.getDescription() != null)) 
			setPartName(filter.getDescription());
		viewer.refresh();
	}
	
	public void dispose() {
		disposed = true;
		view = null;  //clear current view PRH
		super.dispose();
	}
	
	private class RefreshAction extends Action {
		public void run() {
			viewer.refresh();
		}
	}
	
	private class SelectAllAction extends Action {
		public void run() {
			viewer.getTableViewer().getTable().selectAll();
		}
	}
	
	private class SaveColumnsAction extends Action {
		public void run() {
			IPreferenceStore prefStore = (IPreferenceStore)ExtensionsPlugin.getDefault().getPreferenceStore();
			TableColumn[] columns = viewer.getTableViewer().getTable().getColumns();
			for (int i = 0; i < columns.length; i++) {
				prefStore.setValue(SpooledFilesTableViewer.getTableID() + i, columns[i].getWidth());
			}
		}
	}

	/**
	 * Returns the filter.
	 * @return SpooledFileFilter
	 */
	public static SpooledFileFilter getFilter() {
		return filter;
	}

	/**
	 * Returns the spooledFileFactory.
	 * @return SpooledFileFactory
	 */
	public static SpooledFileFactory getSpooledFileFactory() {
		return spooledFileFactory;
	}

	/**
	 * Sets the filter.
	 * @param filter The filter to set
	 */
	public static void setFilter(SpooledFileFilter filter) {
		SpooledFilesView.filter = filter;
	}

	/**
	 * Sets the spooledFileFactory.
	 * @param spooledFileFactory The spooledFileFactory to set
	 */
	public static void setSpooledFileFactory(SpooledFileFactory spooledFileFactory) {
		SpooledFilesView.spooledFileFactory = spooledFileFactory;
	}

	/**
	 * Returns the view.
	 * @return SpooledFilesView
	 */
	public static SpooledFilesView getView() {
		return view;
	}

}
