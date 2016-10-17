/*
 * Created 13.07.2007
 * (c) Orthagis GmbH
 * All Rights Reserved. This file is subject to license restrictions.
 */
package com.jasperassistant.designer.viewer.actions;

import java.io.File;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRExportProgressMonitor;
import net.sf.jasperreports.engine.export.JRRtfExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleRtfReportConfiguration;
import net.sf.jasperreports.export.SimpleWriterExporterOutput;

import org.eclipse.jface.resource.ImageDescriptor;

import com.jasperassistant.designer.viewer.IPosExportXlsHandler;
import com.jasperassistant.designer.viewer.IReportViewer;

/**
 * RTF export action
 * 
 * @author d.schier
 */
public class ExportAsRtfAction extends AbstractExportAction {

    private static final ImageDescriptor ICON = ImageDescriptor.createFromFile(
            ExportAsPdfAction.class, "images/save.gif"); //$NON-NLS-1$

    private static final ImageDescriptor DISABLED_ICON = ImageDescriptor.createFromFile(
            ExportAsPdfAction.class, "images/saved.gif"); //$NON-NLS-1$

    private IPosExportXlsHandler posExportSingleXlsHandler;
    
    public ExportAsRtfAction(IReportViewer viewer) {
        super(viewer);

        setText(Messages.getString("ExportAsRtfAction.label")); //$NON-NLS-1$
        setToolTipText(Messages.getString("ExportAsRtfAction.tooltip")); //$NON-NLS-1$
        setImageDescriptor(ICON);
        setDisabledImageDescriptor(DISABLED_ICON);

        setFileExtensions(new String[] { "*.rtf" }); //$NON-NLS-1$
        setFilterNames(new String[] { Messages.getString("ExportAsRtfAction.filterName") }); //$NON-NLS-1$
        setDefaultFileExtension("rtf"); //$NON-NLS-1$
    }

    public void setIPosExportXlsHandler(IPosExportXlsHandler posExportSingleXlsHandler) {
        
        this.posExportSingleXlsHandler = posExportSingleXlsHandler;
    }
    
    protected void exportWithProgress(File file, JRExportProgressMonitor monitor)
            throws JRException {
    	
        JasperPrint reportFile = null;
        
        if(posExportSingleXlsHandler != null) {
            reportFile = posExportSingleXlsHandler.getJasperPrint();
        }
        
        if(reportFile == null) {
            reportFile = getReportViewer().getDocument();
        }
        
        JRRtfExporter exporter = new JRRtfExporter();
        
        SimpleRtfReportConfiguration configuration = new SimpleRtfReportConfiguration();
		configuration.setProgressMonitor(monitor);
		
		exporter.setConfiguration(configuration);
		
		exporter.setExporterInput(new SimpleExporterInput(reportFile));
		exporter.setExporterOutput(new SimpleWriterExporterOutput(file));
        
//        exporter.setParameter(JRExporterParameter.JASPER_PRINT, getReportViewer().getDocument());
//        exporter.setParameter(JRExporterParameter.OUTPUT_FILE, file);
//        exporter.setParameter(JRExporterParameter.PROGRESS_MONITOR, monitor);
        
        exporter.exportReport();
    }
    
}
