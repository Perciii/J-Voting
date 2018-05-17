package io.github.oliviercailloux.y2018.j_voting;

import java.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SWFCommander{
    private static SocialWelfareFunction result ; 
	private static Logger LOGGER = LoggerFactory.getLogger(SWFCommander.class.getName());
    
	
	public SWFCommander(SocialWelfareFunction res) {
		result = res;
	}
	
    /**
     * Asks the user to enter a StrictPreference 
     * @return the entered StrictPreference
     */
    public static StrictPreference askPreference(){
    	LOGGER.debug("askPreference");
        System.out.println("Enter a StrictPreference complete");
        
        List<Alternative> list = new ArrayList<>();
        
        try(Scanner scan = new Scanner(System.in)){
        	LOGGER.debug("Scanner OK");
        	
        	String vote = scan.nextLine();
        	String[] preference = vote.split(",");
        	for(int i = 0 ; i < preference.length ; i++){
                list.add(new Alternative(Integer.parseInt(preference[i].trim())));
                LOGGER.debug("added alternative : {}", Integer.parseInt(preference[i].trim()));
            }
        }
        
        LOGGER.debug("list of alternatives : {}", list);
        return new StrictPreference(list);
    }   
    
    /**
     * Asks the user to enter StrictPreferences while it doesn't enter an empty one.
     * Each time the user enters a StrictPreference, it displays the current state of the StrictProfile.
     */
    public void createProfileIncrementally(){
    	StrictProfile prof = new StrictProfile();
        boolean keepGoing = true;
        int voterId = 1;

        while(keepGoing){
        	LOGGER.debug("new voter id  : {}", voterId);
        	
            Voter v = new Voter(voterId);
            StrictPreference strictPreference = askPreference();
            LOGGER.debug("StrictPreference(s) : ");
            
            if(strictPreference.size() >= 1){
            	LOGGER.debug("strictPreference :{}",strictPreference.getPreferences());
            	
            	prof.addProfile(v, strictPreference);
            }
            else{
            	keepGoing = false;
            }
            voterId++;
            System.out.println(result.getSocietyStrictPreference(prof));
        }
    }
    
}