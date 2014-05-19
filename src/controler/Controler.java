/*
 * Manages the action between windows and CSV parser
 */
package controler;

import java.io.File;
import java.io.IOException;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import model.CsvParser;
import panel.EndParse;
import panel.OpenFile;
import panel.Welcome;
import panel.ParseFile;
import java.util.Properties;

/**
 * Controler for CMV model
 * @author claudiofior
 * 
 */
public class Controler {
		/**
		 * Main farme
		 */
		private JFrame frame;
		/**
		 * Config data
		 */
		private Properties config;
		/**
		 * Loads controller and the config data
		 */
		public Controler() {
			Properties config = new Properties();
			try{
				config.load(this.getClass().getClassLoader().getResourceAsStream("config.ini"));
				this.config=config;
			}
			catch (IOException e) {
				JOptionPane.showMessageDialog(null,
				"Manca il file di configurazione",
				"Errore",
				JOptionPane.ERROR_MESSAGE);
				System.exit(1);
			}
			catch (NullPointerException e) {
				JOptionPane.showMessageDialog(null,
				"Manca il file di configurazione",
				"Errore",
				JOptionPane.ERROR_MESSAGE);
				System.exit(1);
			};
			this.frame = new frame.Mainpage(this);
			this.task_checkout(new Action());
		}
		/**
		 * Mnages the action
		 * @param action 
		 */
		public void task_checkout(Action action) {
			if (action.containsKey("task")) {
				String task = (String) action.get("task");
				if (task == "exit") {
					int status = JOptionPane.showConfirmDialog(
					this.frame,
					"Vuoi uscire?",
					"Vuoi uscire?",
					JOptionPane.YES_NO_OPTION
					);
					if (status == JOptionPane.YES_OPTION) {
						this.frame.setVisible(false);
						this.frame.dispose();
					}
					return;
				}
				else if (task == "openfile") {
					JPanel panel = new OpenFile(this);
					this.frame.setContentPane(panel);
					this.frame.pack();
					return;
				}
				else if (task == "reset") {
					JPanel panel = new Welcome(this);
					this.frame.setContentPane(panel);
					this.frame.pack();
					return;
				}
				else if (task == "parsefile") {
					File file = (File) action.get("file");
                                        Integer nRecords = (Integer) action.get("nRecords");
                                        String separator = (String) action.get("separator");
					ParseFile panel = new ParseFile(this);
					this.frame.setContentPane(panel);
					this.frame.pack();
					CsvParser csvparser = new CsvParser(file, panel.getTableLog(),panel.getLabelProgress(),this,nRecords,separator);
					csvparser.start();
					return;
				}
				else if (task == "endparse") {
					EndParse panel = new EndParse(this);
					panel.getJLabelFile().setText((String) action.get("file"));
					panel.getJLabelCount().setText((String) action.get("count"));
					panel.getJLabelTime().setText((String) action.get("time"));
					this.frame.setContentPane(panel);
					this.frame.pack();
					return;
				}
			}
			JPanel panel = new Welcome(this);
			this.frame.setContentPane(panel);
			this.frame.pack();
			this.frame.setVisible(true);
		}
		/**
		 * Gets config data
		 * @return 
		 */
		public Properties get_config() {
			return this.config;
		}
}
