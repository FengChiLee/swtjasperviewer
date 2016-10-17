/*
 * SWTJasperViewer - Free SWT/JFace report viewer for JasperReports.
 * Copyright (C) 2004  Peter Severin (peter_p_s@users.sourceforge.net)
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307, USA.
 */
package com.jasperassistant.designer.viewer.actions;

import org.eclipse.jface.action.ContributionItem;
import org.eclipse.jface.util.Assert;
import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;

import com.jasperassistant.designer.viewer.ReportViewerEvent;

/**
 * Exit contribution item
 * 
 * @author Chapel Lee
 */
public class ExitComboContributionItem extends ContributionItem implements Listener {

	private Button btn;

	private ToolItem toolitem;

	private ApplicationWindow viewerApp;
	
	/**
	 * Constructs the action by specifying the report viewer to associate with
	 * the item.
	 * 
	 * @param viewer
	 *            the report viewer
	 */
	public ExitComboContributionItem(ApplicationWindow viewerApp) {

		this.viewerApp = viewerApp;

		refresh();
	}
	
	private void refresh() {
		if (btn == null || btn.isDisposed())
			return;
	}

//	private static int getTextWidth(String string, Control control) {
//		GC gc = new GC(control);
//		try {
//			return gc.textExtent(string).x;
//		} finally {
//			gc.dispose();
//		}
//	}

	private Control createControl(Composite parent) {
		btn = new Button(parent, SWT.NULL);
		btn.addListener(SWT.MouseDown, this);
		btn.setText(Messages.getString("ExitAction.label"));
		btn.setToolTipText(Messages.getString("ExitAction.tooltip"));
		
		btn.pack();

		refresh();
		return btn;
	}

	/**
	 * @see org.eclipse.jface.action.ContributionItem#dispose()
	 */
	public void dispose() {

		btn = null;

	}

	/**
	 * @see org.eclipse.jface.action.IContributionItem#fill(org.eclipse.swt.widgets.Composite)
	 */
	public final void fill(Composite parent) {
		createControl(parent);
	}

	/**
	 * @see org.eclipse.jface.action.IContributionItem#fill(org.eclipse.swt.widgets.Menu,
	 *      int)
	 */
	public final void fill(Menu parent, int index) {
		Assert.isTrue(false, Messages.getString("ZoomComboContributionItem.cannotAddToMenu")); //$NON-NLS-1$
	}

	/**
	 * @see org.eclipse.jface.action.IContributionItem#fill(org.eclipse.swt.widgets.ToolBar,
	 *      int)
	 */
	public void fill(ToolBar parent, int index) {
		toolitem = new ToolItem(parent, SWT.SEPARATOR, index);
		Control control = createControl(parent);
		toolitem.setWidth(btn.getSize().x);
		toolitem.setControl(control);
	}

	private void onSelection() {
		if (viewerApp != null)
			viewerApp.close();
	}

	/**
	 * @see com.jasperassistant.designer.viewer.IReportViewerListener#viewerStateChanged(com.jasperassistant.designer.viewer.ReportViewerEvent)
	 */
	public void viewerStateChanged(ReportViewerEvent evt) {
		refresh();
	}

	/**
	 * @see org.eclipse.swt.widgets.Listener#handleEvent(org.eclipse.swt.widgets.Event)
	 */
	public void handleEvent(Event event) {
		switch (event.type) {
		case SWT.MouseDown:
			onSelection();
			break;
		}
	}

}
