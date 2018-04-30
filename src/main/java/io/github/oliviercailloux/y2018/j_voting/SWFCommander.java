package io.github.oliviercailloux.y2018.j_voting;

import java.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SWFCommander{
    private static SocialWelfareFunction result ; 
	static Logger log = LoggerFactory.getLogger(SWFCommander.class.getName());
    
    /**
     * Asks the user to enter a StrictPreference 
     * @return the entered StrictPreference
     */
    public static StrictPreference askPreference(){
    	log.debug("askPreference\n");
        System.out.println("Enter a StrictPreference complete");
        
        List<Alternative> list = new ArrayList<>();
        
        try(Scanner scan = new Scanner(System.in)){
        	log.debug("Scanner OK");
        	
        	String vote = scan.nextLine();
        	String[] preference = vote.split(",");
        	for(int i = 0 ; i < preference.length ; i++){
                list.add(new Alternative(Integer.parseInt(preference[i].trim())));
                log.debug("added alternative : {}\n", Integer.parseInt(preference[i].trim()));
            }
        }
        
        log.debug("list of alternatives : {}\n", list);
        return new StrictPreference(list);
    }   
    
    /**
     * Asks the user to enter StrictPreferences while it doesn't enter an empty one.
     * Each time the user enters a StrictPreference, it displays the current state of the StrictProfile.
     */
    public static void createProfileIncrementally(){
    	StrictProfile prof = new StrictProfile();
        boolean keepGoing = true;
        int voterId = 1;

        while(keepGoing){
        	log.debug("new voter id  : {}\n", voterId);
        	
            Voter v = new Voter(voterId);
            StrictPreference strictPreference = askPreference();
            log.debug("StrictPreference(s) : ");
            
            if(strictPreference.size() >= 1){
            	log.debug(strictPreference.getPreferences().toString());//not sure of this
            	
            	prof.addProfile(v, strictPreference);
            }
            else{
            	keepGoing = false;
            }
            voterId++;
            System.out.println(result.getSocietyStrictPreference(prof));
        }
    }
    
    public static void main(String[] args){
    	log.debug("main");
    	createProfileIncrementally();
    }
}