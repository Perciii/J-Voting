package io.github.oliviercailloux.y2018.j_voting;

import java.util.*;
import java.io.*;
import java.net.URL;
import java.nio.file.Paths;


public class ReadProfile {
	/**
	 * @param path a string : the path of the file to read 
	 * @return fileRead, a list of String where each element is a line of the SOC or SOI file read
	 *
	 */
	public static List<String> fromSOCorSOI(URL url) throws IOException {
	    try (BufferedReader br = new BufferedReader(new FileReader(url.toString()))) {
	    	List<String> fileRead = new ArrayList<String>();
	    	String currentLine;
	    	while((currentLine = br.readLine()) != null){
	    		fileRead.add(currentLine);
	    	}
	        return fileRead;
	    }
	}
	
	/**
	 * @param fileRead a list of strings : data which was read in a SOC/SOI file
	 * This function prints strings from the list passed as an argument
	 * 
	 */
	public static void displayProfileFromReadFile(List<String> fileRead){
		Iterator<String> it = fileRead.iterator();
		while(it.hasNext()){
			System.out.println(it.next());
		}
	}
	/**
	 * This function calls fromSOCorSOI function if there is a SOC or a SOI file in the FILES directory
	 * 
	 **/
	public static void main(String[] args) throws IOException {
		final String socFile = "/profil.soc";
		final String soiFile = "/profil.soi";
		URL socURL = ReadProfile.class.getResource(socFile);
		URL soiURL = ReadProfile.class.getResource(soiFile);
		
		// read SOC file
		List<String> socToRead = fromSOCorSOI(socURL); 
		System.out.println("SOC file :");
		displayProfileFromReadFile(socToRead);
		
		// read SOI file
		List<String> soiToRead = fromSOCorSOI(soiURL); 
		System.out.println("SOI file :");
		displayProfileFromReadFile(soiToRead);
	}
}
