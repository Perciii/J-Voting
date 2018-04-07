package io.github.oliviercailloux.y2018.j_voting;

import java.util.*;
import java.io.*;


public class ReadProfile {
	/**
	 * @param path a string : the path of the file to read 
	 * @return fileRead, a list of String where each element is a line of the SOC or SOI file read
	 *
	 */
	public static List<String> fromSOCorSOI(String path) throws IOException {
	    try (BufferedReader br = new BufferedReader(new FileReader(path))) {
	    	List<String> fileRead = new ArrayList<String>();
	    	String currentLine;
	    	while((currentLine = br.readLine()) != null){
	    		fileRead.add(currentLine);
	    	}
	        return fileRead;
	    }
	}
	
	public static void displayProfileFromReadFile(List<String> fileRead){
		Iterator<String> it = fileRead.iterator();
		while(it.hasNext()){
			System.out.println(it.next());
		}
	}

	public static void main(String[] args) throws IOException {
		// read SOC file
		List<String> socToRead = fromSOCorSOI("/Files/profil.soc"); 
		System.out.println("SOC file :");
		displayProfileFromReadFile(socToRead);
		
		// read SOI file
		List<String> soiToRead = fromSOCorSOI("/Files/profil.soi"); 
		System.out.println("SOI file :");
		displayProfileFromReadFile(soiToRead);
	}
}
