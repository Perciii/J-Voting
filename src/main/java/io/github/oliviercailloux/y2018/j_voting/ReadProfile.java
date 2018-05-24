package io.github.oliviercailloux.y2018.j_voting;

import java.util.*;
import java.io.*;

import org.slf4j.*;

import com.google.common.base.Charsets;
import com.google.common.base.Preconditions;
import com.google.common.collect.Iterables;
import com.google.common.io.CharStreams;



public class ReadProfile {
	
	private static Logger LOGGER = LoggerFactory.getLogger(ReadProfile.class.getName());
	
	/**
	 * Creates a StrictProfile with the information extracted from the InputStream given as parameter.
	 * @param is <code>not null</code> the InputStream from which the data has to be extracted. InputStream is closed in this method.
	 * @return sProfile a StrictProfile
	 * @throws IOException 
	 */

	public StrictProfileI createProfileFromStream(InputStream is) throws IOException{
		LOGGER.debug("CreateProfileFromReadFile : ");
		Preconditions.checkNotNull(is);
		
		try(InputStreamReader isr = new InputStreamReader(is, Charsets.UTF_8)){
			List<String> fileRead =  CharStreams.readLines(isr);

			Iterator<String> it = fileRead.iterator();
			//StrictProfileBuilder sProfile = new StrictProfileBuilder();
			String lineNbVoters;
			int nbAlternatives = Integer.parseInt(it.next());	//first number of the file is the number of alternatives
			LOGGER.debug("number of alternatives : {}", nbAlternatives);
			List<String> alternatives = new ArrayList<>();
			List<String> profiles = new ArrayList<>();
			for(int i = 1 ; i <= nbAlternatives ; i++){//get the lines with the alternatives
				alternatives.add(it.next());
			}
			LOGGER.debug("alternatives : {}", alternatives);
			lineNbVoters = it.next();//get the line with the nb of voters
			LOGGER.debug("line with stats about the votes ); {}", lineNbVoters);
			while(it.hasNext()){//get the rest of the file
				profiles.add(it.next());
			}
			LOGGER.debug("lines with the number of votes for each StrictPreference : {}", profiles);
			StrictPreference listeAlternatives = getAlternatives(alternatives);
			List<Integer> listInt = getStatsVoters(lineNbVoters);
			return buildProfile(profiles, listeAlternatives, listInt.get(0));
		}
	}
	
	/**
	 * This method prints strings from the read file which InputStream is passed as an argument. It uses an InputStreamReader using UTF-8 to read the Stream
	 * @param is an InputStream <code>not null</code> to read from the desired file<br>
	 * InputStream is closed in this method
	 * @throws IOException 
	 */
	public void displayProfileFromStream(InputStream is) throws IOException{
		LOGGER.debug("DisplayProfileFromFile : ") ;
		Preconditions.checkNotNull(is);
		LOGGER.debug("parameter : InputStream = {}", is);
		
		
		try(InputStreamReader isr = new InputStreamReader(is, Charsets.UTF_8)){
			List<String> fileRead =  CharStreams.readLines(isr);
			for(String line : fileRead){
				System.out.println(line);
			}
		}
		
	}
	
	/**
	 * @param nbAlternatives <code>not null</code> the number of alternatives in the profile
	 * @param file <code>not null</code> a list of strings each containing an alternative
	 * @return the Alternatives in the profile given as a StrictPreference.
	 */
	public StrictPreference getAlternatives(List<String> file){
		LOGGER.debug("GetAlternatives :");
		Preconditions.checkNotNull(file);
		LOGGER.debug("parameter : file = {}", file); 
		List<Alternative> alternatives= new ArrayList<>();
		for(String alternative : file){
			LOGGER.debug("next Alternative : {}", alternative);
			alternatives.add(new Alternative(Integer.parseInt(alternative)));
		}
		StrictPreference listAlternatives = new StrictPreference(alternatives);
		LOGGER.debug("returns listAlternatives : {}", listAlternatives);
		return listAlternatives;
	}
	
	
	/**
	 * @param s <code>not null</code> the line with the voters statistics (number, sum of count, number of unique alternatives)
	 * @return a List with the three computed statistics
	 */
	public List<Integer> getStatsVoters(String s){
		LOGGER.debug("GetNbVoters :");
		Preconditions.checkNotNull(s);
		LOGGER.debug("parameter : s = {}", s);
		List<Integer> list = new ArrayList<>();
		String[] line = s.split(",");
		list.add(Integer.parseInt(line[0].trim()));
		list.add(Integer.parseInt(line[1].trim()));
		list.add(Integer.parseInt(line[2].trim()));
		LOGGER.debug("returns list : {}\n", list);
		return list;
	}
	
	/**
	 * @param listeAlternatives <code>not null</code> the alternatives of the profile
	 * @param s1 <code>not null</code> a line of the profile containing the number of voters for a preference followed by the preference (list of alternatives)
	 * @return the StrictPreference given in the line s1
	 */
	public StrictPreference getPreferences(StrictPreference listeAlternatives, String s1){
		LOGGER.debug("GetPreferences");
		Preconditions.checkNotNull(listeAlternatives);
		Preconditions.checkNotNull(s1);
		LOGGER.debug("parameters : listeAlternatives {}, s1 {}", listeAlternatives, s1);
		
		String[] alternatives = s1.split(",");
		List<Alternative> pref = new ArrayList<>();
		for(String alternative : Iterables.skip(Arrays.asList(alternatives), 1)){
			Alternative alter = new Alternative(Integer.parseInt(alternative.trim()));
			LOGGER.debug("next alternative {}", alter.getId());
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
	 * 
	 * @param s1 not <code>null</null>
	 * @return the strictPreference in the string. The string only contains the alternatives.
	 */
	public StrictPreference getPreferences(String s1){
		LOGGER.debug("GetPreferences");
		Preconditions.checkNotNull(s1);
		LOGGER.debug("parameters : s1 {}",s1);
		String[] s2 = s1.split(",");
		List<Alternative> pref = new ArrayList<>();
		for(String strAlt : s2){
			Alternative alter = new Alternative(Integer.parseInt(strAlt.trim()));
			LOGGER.debug("next alternative {}", alter.getId());
			pref.add(alter);
		}
		if(pref.size() == 0) {
			throw new IllegalArgumentException("The preference is empty.");
		}
		return new StrictPreference(pref);
	}
	
	/**
	 * @param pref <code>not null</code> a StrictPreference
	 * @param nbVoters <code>not null</code> the number of voters that voted for the preference as parameter
	 * @param profile <code>not null</code> the StrictProfile to which the votes will be added
	 */
	public void addVotes(StrictPreference pref, int nbVoters, StrictProfileBuilder profile){
		LOGGER.debug("AddVotes");
		Preconditions.checkNotNull(pref);
		Preconditions.checkNotNull(nbVoters);
		Preconditions.checkNotNull(profile);
		LOGGER.debug("parameters : nbVoters {} for the preference {}", nbVoters, pref); 
		for(int m = 0 ; m < nbVoters ; m++){//we create as many profiles as voters 
			Voter v =new Voter(profile.nextVoterId);
			LOGGER.debug("adds the voter {} and the pref as parameter to the profile", profile.nextVoterId);
			profile.nextVoterId++;
			profile.addProfile(v, pref);
		}
	}

	/**
	 * @param file <code>not null</code> the lines with the number of votes for each preference
	 * @param listAlternatives <code>not null</code> the alternatives of the profile
	 * @param nbVoters <code>not null</code> the number of voters
	 * @return the created StrictProfile
	 */
	public StrictProfileI buildProfile(List<String> file, StrictPreference listAlternatives, int nbVoters){
		LOGGER.debug("BuildProfiles :");
		Preconditions.checkNotNull(file);
		Preconditions.checkNotNull(listAlternatives);
		Preconditions.checkNotNull(nbVoters);

		StrictProfileBuilder profile = new StrictProfileBuilder();
		for(String line : file){
			LOGGER.debug("next line : {}", line);
			if (!line.contains(",")){// if the line doesn't contain "," it's the line of an alternative
				throw new IllegalArgumentException("the first string of file is an alternative line.");
			}
			String[] lineAsArray = line.split(",");
			StrictPreference pref = getPreferences(listAlternatives, line);
			LOGGER.debug("to add : {} votes for the StrictPreference {}", lineAsArray[0].trim(), pref);
			addVotes(pref, Integer.parseInt(lineAsArray[0].trim()), profile);
		}
		return profile.createStrictProfileI();
	}
	
	
	/**
	 * This method displays the contents of the profiles in the resources, if they exist 
	 **/
	public void main(String[] args) throws IOException {
		//read SOC file
		try(InputStream socStream = getClass().getResourceAsStream("profil.soc")){
			LOGGER.debug("SOC Profile stream : {}", socStream);
			//StrictProfile socProfile = createProfileFromFile(socPath);
			displayProfileFromStream(socStream);
		}
		
		//read SOI file
		try(InputStream soiStream = getClass().getResourceAsStream("profil.soi")){
			LOGGER.debug("SOI Profile stream : {}", soiStream);
			//StrictProfile socProfile = createProfileFromFile(socPath);
			displayProfileFromStream(soiStream);
		}
	}
}
