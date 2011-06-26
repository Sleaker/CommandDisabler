package net.milkbowl.commanddisabler;

import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerListener;

public class CDPlayerListener extends PlayerListener {
	CommandDisabler plugin;

	CDPlayerListener(CommandDisabler plugin) {
		this.plugin = plugin;
	}

	@Override
	public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
		String cmd = event.getMessage().toLowerCase();
		for (String disabled : plugin.disabledCmds) {
			if (disabled.startsWith("/" + cmd)) {
				event.setCancelled(true);
				return;
			}
		}
	}
}
