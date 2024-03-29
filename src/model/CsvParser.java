/*
 * Parses the CSV data and gets the data from pagine bianche
 */
package model;

import controler.Action;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Random;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import controler.Controler;
import java.net.URLEncoder;
import java.util.Date;
import java.util.Iterator;
import java.util.Queue;
import java.util.LinkedList;
import java.util.LinkedHashMap;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
/**
 *
 * Parses the CSV data and gets the data from pagine bianche
 * @author claudiofior
 */
public class CsvParser extends Thread {
	private JTable table;
	private JLabel label;
	private File file;
	private String WHITE_PAGES_URL;
	private static final Pattern REMOVE_TAGS = Pattern.compile("<.+?>");
	private Controler controler;
	private int pos=0;
	private long startDateTime;
	private long lastDateTime;
        private Integer nRecords;
        private String separator;
        
	/**
	 * Prepares the data
	 * @param file File to open
	 * @param table Table to add log items
	 * @param label Label with the progress
	 * @param controler Controller of the actions
	 */
	public CsvParser(File file, JTable table,JLabel label, Controler controler,Integer nRecors, String separator) {
		this.file = file;
		this.table = table;
		this.controler = controler;
		this.label = label;
                this.nRecords = nRecors;
                this.separator = separator;
		this.WHITE_PAGES_URL = this.controler.get_config().getProperty("WHITE_PAGES_URL");
	}
	/**
	 * Action to be taken
	 */
	public void run() {
		this.parseFile();
    }
	/**
	 * Parses the file and saved the data
	 */
	private void parseFile() {
		this.startDateTime = new Date().getTime();
		DefaultTableModel model = (DefaultTableModel) this.table.getModel();
		BufferedReader br=null;
		BufferedWriter bw=null;
		File write_file=null;
		int lineNumber = 0;
                int recordNumber = 0;
		double charCount = 0;
		try {
			br = new BufferedReader( new FileReader(this.file));
		} catch (FileNotFoundException e ) {
			model.insertRow(0,new Object[] {"File non trovato"});
		}
		try{
		write_file = new File(this.file.getParent()+"/out_"+this.file.getName());
		if (write_file.isFile()) {
			int status = JOptionPane.showConfirmDialog(
				this.table,
				"Il file è già presente, lo cancello?",
				"Cancellazione file ?",
				JOptionPane.YES_NO_OPTION
			);
			if (status == JOptionPane.YES_OPTION) {
				write_file.delete();
				model.insertRow(0,new Object[] {"Il file di output e già presente, lo cancello"});
			}
			else if (status == JOptionPane.NO_OPTION) {
				controler.Action <String,Object> action = new controler.Action <String,Object> ();
				action.put("task", "openfile");
				this.controler.task_checkout(action);
				return;
			}
		}
		FileWriter filew = new FileWriter(write_file,true);
		bw = new BufferedWriter(filew);
		} catch (IOException e ) {
			model.insertRow(0,new Object[] {"File di output non disponibile"});
		}
		String strLine = "";
		StringTokenizer st=null;
		int tokenNumber = 0;
		String[] input = new String[2];
		try{
			while ((strLine = br.readLine()) != null) {
				charCount += strLine.length();
				strLine.replace("\"", "").replace("'", "");
				st = new StringTokenizer(strLine, this.separator);
				while (st.hasMoreTokens()) {
					try{
					input[tokenNumber]=st.nextToken();
					} catch (ArrayIndexOutOfBoundsException e) {}
					tokenNumber++;
				}
				this.pos = (int) Math.ceil(charCount/this.file.length()*100);
				this.lastDateTime = new Date().getTime();
				String text = this.getDateTime(this.startDateTime,this.lastDateTime)+" Ricerca di "+input[0]+" "+input[1];
				model.insertRow(0, new Object[]{text});
				Queue results = null;
				try {
				results = this.getData(input[0], input[1]);
				} catch (MalformedURLException e ) {
					model.insertRow(0,new Object[] {"Problema nella creazione dell'URL"});
				} catch (IOException e) {
					model.insertRow(0,new Object[] {"Errore nell'apertura del file"});
				} catch (Exception e) {
                                results = new LinkedList();
					model.insertRow(0,new Object[] {"Errore nel file in input"});
				}
                                LinkedHashMap result ;
                                while (!results.isEmpty()) {

                                 result = (LinkedHashMap) results.poll();
                                 Iterator keys = null;
                                 if (lineNumber == 0 && recordNumber ==0) {
                                         keys = result.keySet().iterator();
                                         while (keys.hasNext()) {
                                           bw.write(keys.next().toString());
                                           if (keys.hasNext())
                                                   bw.write(this.separator);
                                           else
                                                   bw.newLine();
                                         }

                                 }
                                 keys = result.values().iterator();
                                 while (keys.hasNext()) {
                                   bw.write("\""+keys.next().toString()+"\"");
                                   if (keys.hasNext())
                                           bw.write(this.separator);
                                   else
                                           bw.newLine();
                                 }
                                 bw.flush();
                                 recordNumber++;
                                }
				input = new String[2];
				tokenNumber = 0;
				lineNumber++;
			}
		}
		catch (IOException e) {
			model.insertRow(0,new Object[] {"Il file non può essere aperto"});
		}
		try{
		br.close();
		bw.close();
		}
		catch (IOException e) {
			model.insertRow(0,new Object[] {"Il file non può essere salvato"});
		}
		Action <String,Object>  action = new Action <String,Object>  ();
		action.put("task", "endparse");
		action.put("file", write_file.getAbsolutePath());
		action.put("count", new Integer(lineNumber).toString());
		action.put("time", this.getDateTime(this.startDateTime,new Date().getTime()));
		this.controler.task_checkout(action);
	}
	/**
	 * Retrives the data from pagine bianche
	 * @param name Name to be found
	 * @param province Place to search
	 * @return
	 * @throws MalformedURLException
	 * @throws IOException 
	 */
	private Queue getData(String name, String province) throws Exception, MalformedURLException , IOException {
		LinkedList <LinkedHashMap> results = new LinkedList<LinkedHashMap>();
		int sleep = 5+new Random().nextInt(5);
        URL url=null;
        try{
		 url = new URL(
				this.WHITE_PAGES_URL+"&qs="+URLEncoder.encode(name,"UTF-8")+
				"&dv="+URLEncoder.encode(province,"UTF-8")
				);
        }
		catch (NullPointerException e) {
            throw new Exception("Wrong input file");
        }
		String eta =this.getDateTime(this.startDateTime, this.startDateTime+(this.lastDateTime-this.startDateTime)*(100-this.pos)/this.pos);
		String common_text = this.pos+"% Finirò tra "+eta+" ";
		this.label.setText(common_text);
		BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
                LinkedHashMap <String,String>  result;
                String line = "";
               String content = "";

               while ((line = reader.readLine()) != null) {
                       content = content.concat(line);
               }
               Matcher name_pattern = Pattern.compile(this.controler.get_config().getProperty("NAME_PATTERN")).matcher(content);
               Matcher zip_pattern = Pattern.compile(this.controler.get_config().getProperty("ZIP_PATTERN")).matcher(content);
               Matcher city_pattern = Pattern.compile(this.controler.get_config().getProperty("CITY_PATTERN")).matcher(content);
               Matcher province_pattern = Pattern.compile(this.controler.get_config().getProperty("PROVINCE_PATTERN")).matcher(content);                  
               Matcher address_pattern = Pattern.compile(this.controler.get_config().getProperty("ADDRESS_PATTERN")).matcher(content);
               Matcher tel_pattern = Pattern.compile(this.controler.get_config().getProperty("PHONE_PATTERN")).matcher(content);
                for(int i=0; i<this.nRecords; i++){
                result = new LinkedHashMap <String,String> ();
		result.put("searched_name",name);
		result.put("searched_place",province);
		
		
                  
                  if(name_pattern.find()) {
                          result.put("name", this.removeTags(name_pattern.group()));
                  }
                  else
                          result.put("name", "");

                  
                  if(zip_pattern.find())
                          result.put("zip", this.removeTags(zip_pattern.group()));
                  else
                          result.put("zip", "");


                  if(city_pattern.find())
                          result.put("city", this.removeTags(city_pattern.group()));
                  else
                          result.put("city", "");

                  
                  if(province_pattern.find())
                          result.put("province", this.removeTags(province_pattern.group()));
                  else
                          result.put("province", "");

                  
                  if(address_pattern.find())
                          result.put("address", this.removeTags(address_pattern.group()));
                  else
                          result.put("address", "");

                  if(tel_pattern.find())
                          result.put("tel", this.removeTags(tel_pattern.group()).replaceAll("[a-zA-Z:]", "").trim());
                  else
                          result.put("tel", "");
                  results.add(result);
                }
                  String dots = "";
                  try{
                          for (int c = 0; c < sleep; c++) {
                                  dots += ".";
                                  this.label.setText(common_text+" "+dots);
                                  Thread.sleep(1000);
                          }
                  }
                  catch (InterruptedException e) {}
		return results;
	}
	/**
	 * Strips html tages
	 * @param string
	 * @return 
	 */
	private static String removeTags(String string) {
		if (string == null || string.length() == 0) {
			return string;
		}
		Matcher m = REMOVE_TAGS.matcher(string);
		return m.replaceAll("").trim();
	}
	/**
	 * Create date time in human readable way
	 * @param start datetime start
	 * @param last datetime end
	 * @return 
	 */
	private String getDateTime(long start , long last) {
		String datetime = "";
		long milliseconds = last-start;
		if ( milliseconds > 24*60*60*1000) {
			int days = (int) Math.ceil(milliseconds/(24*60*60*1000));
			milliseconds -= days*(24*60*60*1000);
			datetime += days+" g ";
		}
		else
			datetime += "0 g ";
		
		if ( milliseconds > 60*60*1000) {
			int hours = (int) Math.ceil(milliseconds/(60*60*1000));
			milliseconds = milliseconds - hours*(60*60*1000);
			datetime += hours+":";
		}
		else
			datetime += "0:";
		
		if ( milliseconds > 60*1000) {
			int minutes = (int) Math.ceil(milliseconds/(60*1000));
			milliseconds = milliseconds - minutes*(60*1000);
			datetime += minutes+":";
		}
		else
			datetime += "0:";
		
		if ( milliseconds > 1000) {
			int seconds = (int) Math.ceil(milliseconds/(1000));
			milliseconds = milliseconds - seconds*(1000);
			datetime += seconds;
		}
		else
			datetime += "0";
		return datetime;
	}
}
