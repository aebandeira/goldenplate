package goldenplate;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Hello extends HttpServlet{
	private static final long serialVersionUID = 1L;
	private static ArrayList<String> allData = new ArrayList<String>();
    public Hello() {}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		BufferedReader br = request.getReader();
		PrintWriter writer = response.getWriter();
		String strLine = "";
		String strFromDateTime = "", strToDateTime = "";
		
		while ((strLine = br.readLine()) != null) {
			if (strLine.startsWith("timestampfrom:")){
				strFromDateTime = strLine.substring(strLine.indexOf(":")+1, strLine.length());
			}else if (strLine.startsWith("timestampto:")) {
				strToDateTime = strLine.substring(strLine.indexOf(":")+1, strLine.length());
			}
		}
		
		strFromDateTime = strFromDateTime.replace("T", " ");
		strToDateTime = strToDateTime.replace("T", " ");
		
		// Set response content type
	    response.setContentType("text/html");
	    writer.println("Measurements from " + strFromDateTime + " to " + strToDateTime);
	    
	    	    
	    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        LocalDateTime fromDateTime = LocalDateTime.parse(strFromDateTime, formatter);
        LocalDateTime toDateTime =   LocalDateTime.parse(strToDateTime, formatter);

	    
	    LocalDateTime searchDateTime;
	    String strSearchDateTime = "";
	    for (String strSearch : allData) {
	    	strSearchDateTime = strSearch.substring(0, strSearch.indexOf("-"));	    	
	    	searchDateTime = LocalDateTime.parse(strSearchDateTime, formatter);

	    	if ((searchDateTime.isAfter(fromDateTime) && searchDateTime.isBefore(toDateTime)) || 
	    			searchDateTime.isEqual(fromDateTime) || searchDateTime.isEqual(toDateTime)) {
	    		writer.println(strSearch);
	    	}
	    	
	    }


	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		BufferedReader br = request.getReader();
		PrintWriter writer = response.getWriter();
		String strLine = "";
		String[] result;
		
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");  
	    Date date = new Date(); 
	    String strDateTime = formatter.format(date);
		
		while ((strLine = br.readLine()) != null) {
			result = strLine.split("&");
			for (int x=0; x<result.length; x++) {
		         writer.append(strDateTime).append("----").append(result[x]).append("\r\n");
			}
			
			saveToFile(result, strDateTime);	
			saveToVar(result, strDateTime);
		}     
    }
	
	
	private void saveToVar(String[] data, String timestamp) {
		
		for (int x=0; x<data.length; x++) {
			allData.add(timestamp + "----" + data[x]);
		}
		
		
	}

	private void saveToFile(String[] data, String timestamp) {
		BufferedWriter fileWriter = null;
		
		try {
			File measureFile = new File("src/goldenplate/measurement.txt");  
			fileWriter = new BufferedWriter(new FileWriter(measureFile,true));
			
			if (measureFile.createNewFile()){
				System.out.println("File is created!");
			}else {
				System.out.println("File already exists.");
			}
			
			for (int x=0; x<data.length; x++) {
				fileWriter.newLine();
				fileWriter.write(timestamp + "----" + data[x]);
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			try {
				// Close the writer regardless of what happens...
                fileWriter.close();
            } catch (Exception e) {
            	
            }
        }
		
	}

}
