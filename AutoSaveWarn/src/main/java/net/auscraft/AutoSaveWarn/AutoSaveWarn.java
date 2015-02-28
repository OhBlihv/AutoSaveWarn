package net.auscraft.AutoSaveWarn;

import java.io.File;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.regex.Pattern;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class AutoSaveWarn  extends JavaPlugin
{
	
	private File configFile = null;
	private FileConfiguration config = null;
	
	@Override
	public void onEnable()
	{
		saveDefaultConfig();
		getConfig();
		
		Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable()
		{
			public void run()
			{
				Bukkit.broadcastMessage(getCString("message"));
				Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "save-all");
			}
		}, 0L, getInt("autosave") * 20);
	}
	
	@Override
	public void onDisable()
	{
		
	}
	
	
	
	//Configuration
	public FileConfiguration getConfig() 
	{
	    if (config == null)
	    {
	        reloadConfig();
	    }
	    return config;
	}
	
	public void reloadConfig() 
	{
	    if (configFile == null) 
	    {
	    	configFile = new File(this.getDataFolder(), "config.yml");
	    }
	    config = YamlConfiguration.loadConfiguration(configFile);
	 
	    // Look for defaults in the jar
	    Reader defConfigStream = null;
		try 
		{
			defConfigStream = new InputStreamReader(this.getResource("config.yml"), "UTF8");
		} 
		catch (UnsupportedEncodingException e) 
		{
			e.printStackTrace();
		}
	    if (defConfigStream != null)
	    {
	        YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
	        config.setDefaults(defConfig);
	    }
	}
	
	public void saveDefaultConfig() 
	{
	    if (configFile == null)
	    {
	        configFile = new File(this.getDataFolder(), "config.yml");
	    }
	    if (!configFile.exists())
	    {            
	         this.saveResource("config.yml", false);
	    }
	}
	
	public int getInt(String path)
	{
		int value = this.config.getInt(path);
		return value;
	}
	
	public String getCString(String path)
	{
		String value = translateColours(this.config.getString(path));
		return value;
	}
	
	public static String translateColours(String toFix)
	{
		Pattern chatColourPattern = Pattern.compile("(?i)&([0-9A-Fa-fk-oK-OrR])");
		String fixedString = chatColourPattern.matcher(toFix).replaceAll("\u00A7$1");
		return fixedString;
	}
	
}
