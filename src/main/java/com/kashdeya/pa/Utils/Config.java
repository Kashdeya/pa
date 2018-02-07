package com.kashdeya.pa.Utils;

import java.io.File;

import net.minecraftforge.common.config.Configuration;

public class Config {
	
	public static Configuration config;
	public static final File configDir = new File("config/Passive Aggression");	
	
	public static void initMain()
	{
		
		File f = new File(configDir, "Main Config.cfg");
        config = new Configuration(f);
        
        config.load();
        
		String category;
		
		category = "Main Configs";
		
		// AI
        config.addCustomCategoryComment(category + " Passive Mob AI", "Blame Jordan Kappa");
        Events.LONG_TERM_MEMORY = config.getBoolean("Passive Mob Long Term Memory", category + " Passive Mob AI", true, "Enable Long Term Memory?");
        Events.CALL_FOR_HELP = config.getBoolean("Passive Mob Call for Help", category + " Passive Mob AI", true, "Enable Call for Help?");
        Events.ATTACK_DAMAGE = config.getFloat("Passive Mob Attack Damage", category + " Passive Mob AI", 2F, 1F, 10F, "Sets Attack Damage");
        
		if (config.hasChanged() == true){
        config.save();
        }
	}
}
