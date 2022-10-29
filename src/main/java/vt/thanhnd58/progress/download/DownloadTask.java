package vt.thanhnd58.progress.download;

import vt.thanhnd58.cmd.AppCmd;
import vt.thanhnd58.constant.PluginConstant;
import vt.thanhnd58.utils.FileUtils;
import vt.thanhnd58.views.AppView;

import javax.swing.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Execute file download in a background thread and update the progress. 
 * @author www.codejava.net
 *
 */
public class DownloadTask extends SwingWorker<Void, Void> {
	private static final int BUFFER_SIZE = 4096;	
	private String downloadURL;
	private String saveDirectory;
	private AppView gui;
	
	public DownloadTask(AppView gui, String downloadURL, String saveDirectory) {
		this.gui = gui;
		this.downloadURL = downloadURL;
		this.saveDirectory = saveDirectory;
	}
	
	/**
	 * Executed in background thread
	 */	
	@Override
	protected Void doInBackground() throws Exception {
		try {
			HTTPDownloadUtil util = new HTTPDownloadUtil();
			util.downloadFile(downloadURL, PluginConstant.TEMP_APP_JAR_FILE);
			
			// set file information on the GUI
//			gui.setFileInfo(util.getFileName(), util.getContentLength());
			
			String saveFilePath = saveDirectory + File.separator + util.getFileName();

			InputStream inputStream = util.getInputStream();
			// opens an output stream to save into file
			FileOutputStream outputStream = new FileOutputStream(saveFilePath);

			byte[] buffer = new byte[BUFFER_SIZE];
			int bytesRead = -1;
			long totalBytesRead = 0;
			int percentCompleted = 0;
			long fileSize = util.getContentLength();

			while ((bytesRead = inputStream.read(buffer)) != -1) {
				outputStream.write(buffer, 0, bytesRead);
				totalBytesRead += bytesRead;
				percentCompleted = (int) (totalBytesRead * 100 / fileSize);

				setProgress(percentCompleted);			
			}

			outputStream.close();

			util.disconnect();
		} catch (IOException ex) {
			JOptionPane.showMessageDialog(gui, PluginConstant.ERROR_DOWNLOAD_UPDATE_FILE + ex.getMessage(),
					"Error", JOptionPane.ERROR_MESSAGE);
			ex.printStackTrace();
			ex.printStackTrace();
			setProgress(0);
			cancel(true);
		}
		return null;
	}

	/**
	 * Executed in Swing's event dispatching thread
	 */
	@Override
	protected void done() {
		if (!isCancelled()) {
			FileUtils.updateProjectProps();
            JOptionPane.showMessageDialog(gui, PluginConstant.START_PROCESS_UPDATE_MESSAGE, PluginConstant.UPDATE_TITLE, JOptionPane.INFORMATION_MESSAGE);
			gui.setVisible(true);
			AppCmd.runCmd("java -jar ./" + PluginConstant.UPDATER_JAR_FILE);
			AppCmd.exit();
		}
	}	
}