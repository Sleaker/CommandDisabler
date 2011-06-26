package net.milkbowl.commanddisabler;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.event.Event;
import org.bukkit.event.Event.Priority;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.config.Configuration;

public class CommandDisabler extends JavaPlugin {

	protected static final Logger log = Logger.getLogger("Minecraft");
	public static final String plugName = "[CommandDisabler]";
	private CDPlayerListener playerListener = new CDPlayerListener(this);
	protected static Configuration config;
	protected Set<String> disabledCmds = new HashSet<String>();

	@Override
	public void onDisable() {
		log.info(plugName + " - Disabled successfully!");

	}

	@Override
	public void onEnable() {
		PluginManager pm = this.getServer().getPluginManager();

		//Try to enable the plugin, disable if there was an error.
		if (!getConfig()) {
			pm.disablePlugin(this);
			log.info(plugName + " - Config could not be created, or was empty.  Please specific commands to disable.");
			return;
		}

		pm.registerEvent(Event.Type.PLAYER_COMMAND_PREPROCESS, playerListener, Priority.Monitor, this);
		
		//Get a string representation of the disabled commands
		String disabled = null;
		for (String msg : disabledCmds)
			disabled += msg + "  ";
		
		log.info(plugName + " - Disabled commands: " + ChatColor.BLUE + disabled);
		log.info(plugName + this.getDescription().getName() + " v" + this.getDescription().getVersion() + " enabled successfully.");
	}

	private boolean getConfig() {
		//Check to see if there is a configuration file.
		File yml = new File(getDataFolder()+"/config.yml");

		if (!yml.exists()) {
			new File(getDataFolder().toString()).mkdir();
			try {
				yml.createNewFile();
			}
			catch (IOException ex) {
				log.info(plugName + " - Cannot create configuration file. And none to load, disabling plugin.");
				return false;
			}
		}	

		config = getConfiguration();
		if (!config.getKeys(null).contains("disabledcommands")) {
			config.setProperty("disabledcommands", null);
			config.save();
		}
		
		disabledCmds.addAll(config.getStringList("disabledcommands", null));
		if (disabledCmds.isEmpty())
			return false;
		else
			return true;

	}
}
