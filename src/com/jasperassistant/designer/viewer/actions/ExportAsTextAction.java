package com.jasperassistant.designer.viewer.actions;

import java.io.File;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRExportProgressMonitor;
import net.sf.jasperreports.engine.export.JRTextExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleTextReportConfiguration;
import net.sf.jasperreports.export.SimpleWriterExporterOutput;

import org.eclipse.jface.resource.ImageDescriptor;

import com.jasperassistant.designer.viewer.IPosExportXlsHandler;
import com.jasperassistant.designer.viewer.IReportViewer;

public class ExportAsTextAction extends AbstractExportAction {

	private static final ImageDescriptor ICON = ImageDescriptor.createFromFile(
			ExportAsTextAction.class, "images/save.gif"); //$NON-NLS-1$

	private static final ImageDescriptor DISABLED_ICON = ImageDescriptor
			.createFromFile(ExportAsTextAction.class, "images/saved.gif"); //$NON-NLS-1$

	private IPosExportXlsHandler posExportSingleXlsHandler;
	
	/**
	 * @see AbstractExportAction#AbstractExportAction(IReportViewer)
	 */
	public ExportAsTextAction(IReportViewer viewer) {
		super(viewer);

		setText(Messages.getString("ExportAsTextAction.label")); //$NON-NLS-1$
		setToolTipText(Messages.getString("ExportAsTextAction.tooltip")); //$NON-NLS-1$
		setImageDescriptor(ICON);
		setDisabledImageDescriptor(DISABLED_ICON);

		setFileExtensions(new String[] { "*.txt" }); //$NON-NLS-1$
		setFilterNames(new String[] { Messages.getString("ExportAsTextAction.filterName") }); //$NON-NLS-1$
		setDefaultFileExtension("txt"); //$NON-NLS-1$
	}

	public void setIPosExportXlsHandler(IPosExportXlsHandler posExportSingleXlsHandler) {
        
        this.posExportSingleXlsHandler = posExportSingleXlsHandler;
    }
	
	/**
	 * @see com.jasperassistant.designer.viewer.actions.AbstractExportAction#exportWithProgress(java.io.File, net.sf.jasperreports.engine.export.JRExportProgressMonitor)
	 */
	protected void exportWithProgress(File file, JRExportProgressMonitor monitor)
			throws JRException {
	    JasperPrint reportFile = null;
        
        if(posExportSingleXlsHandler != null) {
            reportFile = posExportSingleXlsHandler.getJasperPrint();
        }
        
        if(reportFile == null) {
            reportFile = getReportViewer().getDocument();
        }
        
		JRTextExporter exporter = new JRTextExporter();
		
		SimpleTextReportConfiguration configuration = new SimpleTextReportConfiguration();
		configuration.setProgressMonitor(monitor);
		
		exporter.setConfiguration(configuration);
		
		exporter.setExporterInput(new SimpleExporterInput(reportFile));
		exporter.setExporterOutput(new SimpleWriterExporterOutput(file));
		
//		exporter.setParameter(JRExporterParameter.JASPER_PRINT,
//				getReportViewer().getDocument());
//		exporter.setParameter(JRExporterParameter.OUTPUT_FILE, file);
//		exporter.setParameter(JRExporterParameter.PROGRESS_MONITOR, monitor);
		
		exporter.exportReport();
	}

}
