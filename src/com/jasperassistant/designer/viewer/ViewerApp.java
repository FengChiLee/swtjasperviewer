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
package com.jasperassistant.designer.viewer;

import java.io.InputStream;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

import net.sf.jasperreports.view.JasperViewer;

import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;

import com.jasperassistant.designer.viewer.actions.ExitAction;
import com.jasperassistant.designer.viewer.actions.ExitComboContributionItem;
import com.jasperassistant.designer.viewer.actions.ExportAsCsvAction;
import com.jasperassistant.designer.viewer.actions.ExportAsHtmlAction;
import com.jasperassistant.designer.viewer.actions.ExportAsJasperReportsAction;
import com.jasperassistant.designer.viewer.actions.ExportAsMultiXlsAction;
import com.jasperassistant.designer.viewer.actions.ExportAsMultiXlsxAction;
import com.jasperassistant.designer.viewer.actions.ExportAsPdfAction;
import com.jasperassistant.designer.viewer.actions.ExportAsRtfAction;
import com.jasperassistant.designer.viewer.actions.ExportAsSingleXlsAction;
import com.jasperassistant.designer.viewer.actions.ExportAsSingleXlsxAction;
import com.jasperassistant.designer.viewer.actions.ExportAsTextAction;
import com.jasperassistant.designer.viewer.actions.ExportAsXmlAction;
import com.jasperassistant.designer.viewer.actions.ExportAsXmlWithImagesAction;
import com.jasperassistant.designer.viewer.actions.ExportMenuAction;
import com.jasperassistant.designer.viewer.actions.FirstPageAction;
import com.jasperassistant.designer.viewer.actions.LastPageAction;
import com.jasperassistant.designer.viewer.actions.NextPageAction;
import com.jasperassistant.designer.viewer.actions.PageNumberContributionItem;
import com.jasperassistant.designer.viewer.actions.PreviousPageAction;
import com.jasperassistant.designer.viewer.actions.PrintAction;
import com.jasperassistant.designer.viewer.actions.ReloadAction;
import com.jasperassistant.designer.viewer.actions.ZoomActualSizeAction;
import com.jasperassistant.designer.viewer.actions.ZoomComboContributionItem;
import com.jasperassistant.designer.viewer.actions.ZoomFitPageAction;
import com.jasperassistant.designer.viewer.actions.ZoomFitPageWidthAction;
import com.jasperassistant.designer.viewer.actions.ZoomInAction;
import com.jasperassistant.designer.viewer.actions.ZoomOutAction;

/**
 * Demo viewer based implemented as a standalone JFace application.
 * 
 * @author Peter Severin (peter_p_s@users.sourceforge.net)
 */
public class ViewerApp extends ApplicationWindow {

	private ReportViewer reportViewer = new ReportViewer(SWT.BORDER);

	private String shellText;
	private Point locationPoint;
	private Point sizePoint;
	private boolean bSizeMaximized = false;
	
	private IPosExportXlsHandler posExportXlsHandler;
	
	private ExportAsCsvAction exportCsv_Menu;
    private ExportAsCsvAction exportCsv_ToolBar;
	
	private ExportAsSingleXlsAction exporterXls_Menu;
	private ExportAsSingleXlsAction exporterXls_ToolBar;

    private ExportAsSingleXlsxAction exporterXlsx_Menu;
    private ExportAsSingleXlsxAction exporterXlsx_ToolBar;
    
	private ExportAsMultiXlsAction exporterMXls_Menu;
    private ExportAsMultiXlsAction exporterMXls_ToolBar;
	
    private ExportAsMultiXlsxAction exporterMXlsx_Menu;
    private ExportAsMultiXlsxAction exporterMXlsx_ToolBar;
    
    private ExportAsTextAction exportText_Menu;
    private ExportAsTextAction exportText_ToolBar;
    
    private ExportAsRtfAction exportRtf_Menu;
    private ExportAsRtfAction exportRtf_ToolBar;
    
    private ExportAsHtmlAction exportHtml_Menu;
    private ExportAsHtmlAction exportHtml_ToolBar;
	
    private Set<String> exportTypes = new HashSet<String>();
    
	/**
	 * Constructs the viewer
	 */
	public ViewerApp() {
		this(null);
	}

	public ViewerApp(Shell parent) {
		super(parent);
		
		addToolBar(SWT.FLAT | SWT.WRAP);
		addMenuBar();
		setBlockOnOpen(true);
		
		initExportTypes();
		
		initMenu();
		initToolBar();

	}

	public void setShellText(String shellText) {
		this.shellText = shellText;
	}

	public void setLocation(Point location) {
		this.locationPoint = location;
	}
	
	public void setSize(Point size) {
		this.sizePoint = size;
	}
	
	public void setSizeMaximized(boolean sizeMaximized) {
		this.bSizeMaximized = sizeMaximized;
	}
	
	private boolean enableType(String exportType) {
	    return exportTypes.contains(exportType);
	}
	
	private void initExportTypes() {
	    
	    exportTypes.add("PDF");
	    exportTypes.add("XLSX");
	    exportTypes.add("MXLSX");
	    exportTypes.add("CSV");
	    
	    Properties result = null;
	    InputStream in = null;
	    try {
            in = Thread.currentThread().getContextClassLoader()
                    .getResourceAsStream("jasperreports.properties");
            if (in != null) {
                result = new Properties();
                result.load(in); // Can throw IOException
                String ts = result.getProperty("com.jasperassistant.designer.viewer.export.types");
                if(ts != null) {
                    exportTypes.clear();
                    String[] aryTs = ts.split(",");
                    for(String t : aryTs) {
                        exportTypes.add(t.trim().toUpperCase());
                    }
                }
            }
	    } catch (Exception e) {            
        } finally {
            if (in != null)
                try {
                    in.close();
                } catch (Throwable ignore) {
                }
        }
	}
	
	private void initMenu() {
		MenuManager mm = getMenuBarManager();

		MenuManager export = new MenuManager(Messages.getString("ViewerApp.exportMenuLabel")); //$NON-NLS-1$
		if(enableType("PDF")) {
		    ExportAsPdfAction exportPdf_Menu = new ExportAsPdfAction(reportViewer);
		    export.add(exportPdf_Menu);
		}
		if(enableType("RTF")) {
		    exportRtf_Menu = new ExportAsRtfAction(reportViewer);
		    export.add(exportRtf_Menu);
		}
		if(enableType("JASPER")) {
		     export.add(new ExportAsJasperReportsAction(reportViewer));
		}
		if(enableType("HTML")) {
		    exportHtml_Menu = new ExportAsHtmlAction(reportViewer);
		    export.add(exportHtml_Menu);
		}
		if(enableType("XLS")) {
		    exporterXls_Menu = new ExportAsSingleXlsAction(reportViewer);
		    export.add(exporterXls_Menu);
		}
		if(enableType("XLSX")) {
		    exporterXlsx_Menu = new ExportAsSingleXlsxAction(reportViewer); 
		    export.add(exporterXlsx_Menu);
		}
        if(enableType("MXLS")) {
            exporterMXls_Menu = new ExportAsMultiXlsAction(reportViewer);
            export.add(exporterMXls_Menu);
        }
        if(enableType("MXLSX")) {
            exporterMXlsx_Menu = new ExportAsMultiXlsxAction(reportViewer);
            export.add(exporterMXlsx_Menu);
        }
        if(enableType("CSV")) {
            exportCsv_Menu = new ExportAsCsvAction(reportViewer);
            export.add(exportCsv_Menu);
        }
		if(enableType("XML")) {
		    export.add(new ExportAsXmlAction(reportViewer));
		}
		if(enableType("XML")) {
		    export.add(new ExportAsXmlWithImagesAction(reportViewer));
		}
		if(enableType("TEXT")) {
		    exportText_Menu = new ExportAsTextAction(reportViewer); 
		    export.add(exportText_Menu);
		}

		MenuManager file = new MenuManager(Messages.getString("ViewerApp.fileMenuLabel")); //$NON-NLS-1$
		file.add(new ReloadAction(reportViewer));
		file.add(new Separator());
		file.add(export);
		file.add(new Separator());
		file.add(new PrintAction(reportViewer));
		file.add(new Separator());
		file.add(new ExitAction(this));
		mm.add(file);

		MenuManager view = new MenuManager(Messages.getString("ViewerApp.viewMenuLabel")); //$NON-NLS-1$
		view.add(new ZoomOutAction(reportViewer));
		view.add(new ZoomInAction(reportViewer));
		view.add(new Separator());
		view.add(new ZoomActualSizeAction(reportViewer));
		view.add(new ZoomFitPageAction(reportViewer));
		view.add(new ZoomFitPageWidthAction(reportViewer));
		mm.add(view);

		MenuManager nav = new MenuManager(Messages.getString("ViewerApp.navigateMenuLabel")); //$NON-NLS-1$
		nav.add(new FirstPageAction(reportViewer));
		nav.add(new PreviousPageAction(reportViewer));
		nav.add(new NextPageAction(reportViewer));
		nav.add(new LastPageAction(reportViewer));
		mm.add(nav);
	}

	/**
	 * @see org.eclipse.jface.window.ApplicationWindow#configureShell(org.eclipse.swt.widgets.Shell)
	 */
	protected void configureShell(Shell shell) {
	    
		super.configureShell(shell);
				
		if (shellText != null && shellText.length() != 0) {
			shell.setText(shellText);
		} else {
			shell.setText(Messages.getString("ViewerApp.title")); //$NON-NLS-1$
		}
		shell.setImage(new Image(null, JasperViewer.class.getResourceAsStream("images/jricon.GIF"))); //$NON-NLS-1$
		
		if(this.sizePoint != null) {
			shell.setSize(sizePoint);
			
		} else if(this.bSizeMaximized == true) {
			shell.setMaximized(true);
			
		} else {
			shell.setSize(1024, 768);
		}
		
		if(this.locationPoint != null) {
			shell.setLocation(locationPoint);
		} else {
			shell.setLocation(0, 0);
		}
				
		shell.getDisplay().addFilter(SWT.KeyDown, new Listener() {            
            @Override
            public void handleEvent(Event e) {
//                System.out.println("Key event: e.stateMask=" + e.stateMask + ", e.character=" +
//                        e.character);
                if(((e.stateMask & SWT.CTRL) == SWT.CTRL) && (e.character == '+')) {
                    // System.out.println("Key down: Ctrl + '+'");
                    if(reportViewer != null && reportViewer.canZoomIn()) {
                        reportViewer.zoomIn();
                    }
                } else if(((e.stateMask & SWT.CTRL) == SWT.CTRL) && (e.character == '-')) {
                    // System.out.println("Key down: Ctrl + '-'");
                    if(reportViewer != null && reportViewer.canZoomOut()) {
                        reportViewer.zoomOut();
                    }
                }
            }
		});
		
		shell.layout();
	}

	private void initToolBar() {
		ToolBarManager tbManager = getToolBarManager();
		ExportMenuAction exportMenu = new ExportMenuAction(reportViewer);
		
		ExportAsPdfAction exportPdf_ToolBar = null;
		if(enableType("PDF")) {
		    exportPdf_ToolBar = new ExportAsPdfAction(reportViewer);
		    exportMenu.getMenuManager().add(exportPdf_ToolBar);
		}
		if(enableType("RTF")) {
		    exportRtf_ToolBar = new ExportAsRtfAction(reportViewer);
		    exportMenu.getMenuManager().add(exportRtf_ToolBar);
		}
		if(enableType("JASPER")) {
		    exportMenu.getMenuManager().add(new ExportAsJasperReportsAction(reportViewer));
		}
		if(enableType("HTML")) {
		    exportHtml_ToolBar = new ExportAsHtmlAction(reportViewer);
		    exportMenu.getMenuManager().add(exportHtml_ToolBar);
		}
		if(enableType("XLS")) {
		    this.exporterXls_ToolBar = new ExportAsSingleXlsAction(reportViewer);
		    exportMenu.getMenuManager().add(this.exporterXls_ToolBar);
		}
		if(enableType("XLSX")) {
		    this.exporterXlsx_ToolBar = new ExportAsSingleXlsxAction(reportViewer);
		    exportMenu.getMenuManager().add(this.exporterXlsx_ToolBar);
		}
        if(enableType("MXLS")) {
            exporterMXls_ToolBar = new ExportAsMultiXlsAction(reportViewer);
            exportMenu.getMenuManager().add(exporterMXls_ToolBar);
        }
        if(enableType("MXLSX")) {
            exporterMXlsx_ToolBar = new ExportAsMultiXlsxAction(reportViewer);
            exportMenu.getMenuManager().add(exporterMXlsx_ToolBar);
        }
        if(enableType("CSV")) {
            exportCsv_ToolBar = new ExportAsCsvAction(reportViewer);
            exportMenu.getMenuManager().add(exportCsv_ToolBar);
        }
		if(enableType("TEXT")) {
		    exportText_ToolBar = new ExportAsTextAction(reportViewer);
		    exportMenu.getMenuManager().add(exportText_ToolBar);
		}
		if(enableType("XML")) {
		    exportMenu.getMenuManager().add(new ExportAsXmlAction(reportViewer));
		}
		if(enableType("XML")) {
		    exportMenu.getMenuManager().add(new ExportAsXmlWithImagesAction(reportViewer));
		}
		if(exportPdf_ToolBar != null) {
		    exportMenu.setDefaultAction(exportPdf_ToolBar);
		}

		tbManager.add(exportMenu);
		tbManager.add(new PrintAction(reportViewer));
		tbManager.add(new ReloadAction(reportViewer));
		tbManager.add(new Separator());
		tbManager.add(new FirstPageAction(reportViewer));
		tbManager.add(new PreviousPageAction(reportViewer));
		if (SWT.getPlatform().equals("win32")) { //$NON-NLS-1$
			tbManager.add(new PageNumberContributionItem(reportViewer));
		}
		tbManager.add(new NextPageAction(reportViewer));
		tbManager.add(new LastPageAction(reportViewer));
		tbManager.add(new Separator());
		tbManager.add(new ZoomActualSizeAction(reportViewer));
		tbManager.add(new ZoomFitPageAction(reportViewer));
		tbManager.add(new ZoomFitPageWidthAction(reportViewer));
		tbManager.add(new Separator());
		tbManager.add(new ZoomOutAction(reportViewer));
		tbManager.add(new ZoomComboContributionItem(reportViewer));
		tbManager.add(new ZoomInAction(reportViewer));
		tbManager.add(new Separator());
		tbManager.add(new ExitComboContributionItem(this));
	}

	/**
	 * @see org.eclipse.jface.window.Window#createContents(org.eclipse.swt.widgets.Composite)
	 */
	protected Control createContents(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout(1, false);
		layout.marginWidth = layout.marginHeight = 0;
		container.setLayout(layout);

		Control reportViewerControl = reportViewer.createControl(container);
		reportViewerControl.setLayoutData(new GridData(GridData.FILL_BOTH));

		StatusBar statusBar = new StatusBar();
		statusBar.setReportViewer(reportViewer);
		Control statusBarControl = statusBar.createControl(container);
		statusBarControl.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		reportViewer.addHyperlinkListener(new DefaultHyperlinkHandler());

		return container;
	}

	/**
	 * Returns the report viewer used for viewing reports.
	 * 
	 * @return the report viewer
	 */
	public IReportViewer getReportViewer() {
		return reportViewer;
	}

	/**
	 * @return the posExportXlsHandler
	 */
	public IPosExportXlsHandler getPosExportXlsHandler() {
		return posExportXlsHandler;
	}

	/**
	 * @param posExportXlsHandler the posExportXlsHandler to set
	 */
	public void setPosExportXlsHandler(IPosExportXlsHandler posExportXlsHandler) {
		
		this.posExportXlsHandler = posExportXlsHandler;
		
		if(this.enableType("XLS")) {
		    this.exporterXls_Menu.setIPosExportXlsHandler(this.posExportXlsHandler);
		    this.exporterXls_ToolBar.setIPosExportXlsHandler(this.posExportXlsHandler);
		    
		}
		
		if(this.enableType("XLSX")) {
		    this.exporterXlsx_Menu.setIPosExportXlsHandler(this.posExportXlsHandler);
		    this.exporterXlsx_ToolBar.setIPosExportXlsHandler(this.posExportXlsHandler);
		    
		}

        if(this.enableType("MXLS")) {
            this.exporterMXls_Menu.setIPosExportXlsHandler(this.posExportXlsHandler);
            this.exporterMXls_ToolBar.setIPosExportXlsHandler(this.posExportXlsHandler);
        }
        
        if(this.enableType("MXLSX")) {
            this.exporterMXlsx_Menu.setIPosExportXlsHandler(this.posExportXlsHandler);
            this.exporterMXlsx_ToolBar.setIPosExportXlsHandler(this.posExportXlsHandler);
        }
        
		if(this.enableType("CSV")) {
            this.exportCsv_Menu.setIPosExportXlsHandler(this.posExportXlsHandler);
            this.exportCsv_ToolBar.setIPosExportXlsHandler(this.posExportXlsHandler);
        }
		
		if(this.enableType("TEXT")) {
            this.exportText_Menu.setIPosExportXlsHandler(this.posExportXlsHandler);
            this.exportText_ToolBar.setIPosExportXlsHandler(this.posExportXlsHandler);
        }
		
		if(this.enableType("RTF")) {
            this.exportRtf_Menu.setIPosExportXlsHandler(this.posExportXlsHandler);
            this.exportRtf_ToolBar.setIPosExportXlsHandler(this.posExportXlsHandler);
        }
		
		if(this.enableType("HTML")) {
            this.exportHtml_Menu.setIPosExportXlsHandler(this.posExportXlsHandler);
            this.exportHtml_ToolBar.setIPosExportXlsHandler(this.posExportXlsHandler);
        }		
		
	}

	public void setExportFileName(String exportFileName) {
		reportViewer.setExportFileName(exportFileName);
	}
	
	/**
	 * Main entry point
	 * 
	 * @param args
	 *            arguments
	 */
	public static void main(String[] args) {
		String fileName = null;
		boolean isXMLFile = false;

		if (args.length == 0) {
			usage();
			return;
		}

		int i = 0;
		while (i < args.length) {
			if (args[i].startsWith("-F")) //$NON-NLS-1$
				fileName = args[i].substring(2);
			if (args[i].startsWith("-XML")) //$NON-NLS-1$
				isXMLFile = true;

			i++;
		}

		if (fileName == null) {
			usage();
			return;
		}

		openViewer(fileName, isXMLFile);

		System.exit(0);
	}

	private static void openViewer(String fileName, boolean isXMLFile) {
		ViewerApp app = new ViewerApp();
		app.getReportViewer().loadDocument(fileName, isXMLFile);
		app.open();
	}

	private static void usage() {
		System.out.println(Messages.getString("ViewerApp.usageLabel")); //$NON-NLS-1$
		System.out.println(Messages.getString("ViewerApp.usage")); //$NON-NLS-1$
	}
}