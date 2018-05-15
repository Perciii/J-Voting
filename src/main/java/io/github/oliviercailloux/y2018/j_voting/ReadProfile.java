package io.github.oliviercailloux.y2018.j_voting;

import java.util.*;
import java.io.*;

import org.slf4j.*;

import com.google.common.base.Charsets;
import com.google.common.base.Preconditions;
import com.google.common.io.Files;


public class ReadProfile {
	
	private static Logger LOGGER = LoggerFactory.getLogger(ReadProfile.class.getName());	
	/**
	 * @param path a string <code>not null</code> : the path of the file to read (encoded in UTF-8)
	 * @return fileRead, a list of String where each element is a line of the SOC or SOI file read
	 */
	public List<String> readFile(String path) throws IOException {
		LOGGER.debug("ReadProfile : readFile : \n") ;
		Preconditions.checkNotNull(path);
		LOGGER.debug("parameter : path = {}\n", path);

		File fileToRead = new File(path);
		List<String> fileRead = Files.readLines(fileToRead, Charsets.UTF_8);	
		return fileRead;
	}
	
	/**
	 * @param path a String <code>not null</code> of the path to the file to read : data which was read in a SOC/SOI file
	 * This function prints strings from the read file which path is passed as an argument
	 * @throws IOException 
	 */
	public void displayProfileFromFile(String path) throws IOException{
		LOGGER.debug("ReadProfile : displayProfileFromFile : \n") ;
		Preconditions.checkNotNull(path);
		LOGGER.debug("parameter : path = {}\n", path);
		
		List<String> fileRead = readFile(path);
		
		Iterator<String> it = fileRead.iterator();
		while(it.hasNext()){
			System.out.println(it.next());
		}
	}
	
	/**
	 * @param nbAlternatives the number of alternatives in the profile
	 * @param file a list of strings each containing an alternative
	 * @return the alternatives in the profile given in a list of string.
	 */
	public StrictPreference getAlternatives(int nbAlternatives,List<String> file){
		LOGGER.debug("ReadProfile : GetAlternatives :");
		Preconditions.checkNotNull(nbAlternatives);
		Preconditions.checkNotNull(file);
		LOGGER.debug("parameters : nbAlternatives = {}, file = {}\n", nbAlternatives, file); 
		Iterator<String> it = file.iterator();
		String s1; 
		List<Alternative> alternatives= new ArrayList<>();
		for(int k = 1 ; k <= nbAlternatives ; k++){//we add each alternative to a list
			s1 = it.next();
			LOGGER.debug("next Alternative : {}\n", s1);
			if (s1.contains(",")){//line with alternative doesn't contain ,
				throw new Error("Error: nbAlternative is not correct");
			}
			alternatives.add(new Alternative(Integer.parseInt(s1)));
		}
		StrictPreference listAlternatives = new StrictPreference(alternatives);
		LOGGER.debug("returns listAlternatives : {}\n", listAlternatives);
		return listAlternatives;
	}
	
	
	/**
	 * @param s the line with the voters statistics (number, sum of count, number of unique alternatives)
	 * @return a list with the three computed statistics
	 */
	public List<Integer> getStatsVoters(String s){
		LOGGER.debug("ReadProfile : getNbVoters :");
		Preconditions.checkNotNull(s);
		LOGGER.debug("parameter : s = {}\n", s);
		List<Integer> list = new ArrayList<>();
		String[] line = s.split(",");
		list.add(Integer.parseInt(line[0].trim()));
		list.add(Integer.parseInt(line[1].trim()));
		list.add(Integer.parseInt(line[2].trim()));
		LOGGER.debug("returns list : {}\n", list);
		return list;
	}
	
	/**
	 * @param listeAlternatives the alternatives of the profile
	 * @param s1 a line of the profile containing the number of voters for a preference followed by the preference (list of alternatives)
	 * @return the StrictPreference given in the line s1
	 */
	public StrictPreference getPreferences(StrictPreference listeAlternatives, String s1){
		LOGGER.debug("ReadProfile : getPreferences\n");
		Preconditions.checkNotNull(listeAlternatives);
		Preconditions.checkNotNull(s1);
		LOGGER.debug("parameters : listeAlternatives {}, s1 {}\n", listeAlternatives, s1);
		String[] s2 = s1.split(",");
		List<Alternative> pref = new ArrayList<>();
		for(int i = 1 ; i < s2.length ; i++){//we collect all the alternatives, skipping the first element which is the nb of votes
			Alternative alter = new Alternative(Integer.parseInt(s2[i].trim()));
			LOGGER.debug("next alternative {}\n", alter.getId());
			if(listeAlternatives.contains(alter)) {
				LOGGER.debug("correct alternative");
				pref.add(alter);
			}
			else {
				LOGGER.debug("alternative not in the profile");
				throw new IllegalArgumentException("The line s1 contains an alternative that is not in the profile's alternatives");
			}
		}
		return new StrictPreference(pref);
	}
	
	/**
	 * @param pref a StrictPreference
	 * @param nbVoters the number of voters that voted for the preference as parameter
	 * @param profile the StrictProfile to which the votes will be added
	 */
	public void addVotes(StrictPreference pref, int nbVoters, StrictProfile profile){
		LOGGER.debug("ReadProfile : addVotes\n");
		Preconditions.checkNotNull(pref);
		Preconditions.checkNotNull(nbVoters);
		Preconditions.checkNotNull(profile);
		LOGGER.debug("parameters : nbVoters {} for the preference {}\n", nbVoters, pref); 
		for(int m = 0 ; m < nbVoters ; m++){//we create as many profiles as voters 
			Voter v =new Voter(profile.nextVoterId);
			LOGGER.debug("adds the voter {} and the pref as parameter to the profile\n", profile.nextVoterId);
			profile.nextVoterId++;
			profile.addProfile(v, pref);
		}
	}

	/**
	 * @param file the lines with the number of votes for each preference
	 * @param listAlternatives the alternatives of the profile
	 * @param nbVoters the number of voters
	 * @return the created StrictProfile
	 */
	public StrictProfile buildProfile(List<String> file, StrictPreference listAlternatives, int nbVoters){
		LOGGER.debug("ReadProfile : buildProfiles :\n");
		Preconditions.checkNotNull(file);
		Preconditions.checkNotNull(listAlternatives);
		Preconditions.checkNotNull(nbVoters);
		Iterator<String> it = file.iterator();
		StrictProfile profile = new StrictProfile();
		String s1; //where we store the current line
		while(it.hasNext()){
			s1 = it.next();
			LOGGER.debug("next line : {}\n",s1);
			if (!s1.contains(",")){// if the line doesn't contain , it's the line of an alternative
				throw new IllegalArgumentException("the first string of file is an alternative line.");
			}
			String[] line = s1.split(",");
			StrictPreference pref = getPreferences(listAlternatives,s1);
			LOGGER.debug("to add : {} votes for the StrictPreference {}\n",line[0].trim(),pref);
			addVotes(pref, Integer.parseInt(line[0].trim()), profile);
		}
		return profile;
	}
	

	/**
	 * Creates a StrictProfile with the information extracted from a file.
	 * @param path <code>not null</code> the path to the file to read
	 * @return sProfile a StrictProfile
	 * @throws IOException 
	 */

	public StrictProfile createProfileFromFile(String path) throws IOException{
		LOGGER.debug("ReadProfile : createProfileFromReadFile : \n");
		Preconditions.checkNotNull(path);
		
		List<String> fileRead = readFile(path);
		
		Iterator<String> it = fileRead.iterator();
		StrictProfile sProfile = new StrictProfile();
		String lineNbVoters;
		int nbAlternatives = Integer.parseInt(it.next());	//first number of the file is the number of alternatives
		LOGGER.debug("number of alternatives : {}\n", nbAlternatives);
		List<String> alternatives = new ArrayList<>();
		List<String> profiles = new ArrayList<>();
		for(int i = 1 ; i <= nbAlternatives ; i++){//get the lines with the alternatives
			alternatives.add(it.next());
		}
		LOGGER.debug("alternatives : {}\n", alternatives);
		lineNbVoters = it.next();//get the line with the nb of voters
		LOGGER.debug("line with stats about the votes ); {}\n", lineNbVoters);
		while(it.hasNext()){//get the rest of the file
			profiles.add(it.next());
		}
		LOGGER.debug("lines with the number of votes for each StrictPreference : {}\n", profiles);
		StrictPreference listeAlternatives = getAlternatives(nbAlternatives, alternatives);
		List<Integer> listInt = getStatsVoters(lineNbVoters);
		sProfile = buildProfile(profiles, listeAlternatives, listInt.get(0));
		return sProfile;
	}
	
	
	
	
	/**
	 * This function displays the contents of the profiles in the resources, if they exist 
	 **/
	public void main(String[] args) throws IOException {
		//read SOC file
		String socPath = getClass().getResource("profil.soc").getPath();
		LOGGER.debug("SOC Profile path : {}\n", socPath);
		//StrictProfile socProfile = createProfileFromFile(socPath);
		displayProfileFromFile(socPath);
		
		//read SOI file
		String soiPath = getClass().getResource("profil.soi").getPath();
		LOGGER.debug("SOI Profile path : {}\n", soiPath);
		//StrictProfile socProfile = createProfileFromFile(socPath);
		displayProfileFromFile(soiPath);
	}
}
