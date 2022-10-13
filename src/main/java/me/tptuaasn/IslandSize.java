package me.tptuaasn;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import com.bgsoftware.superiorskyblock.api.SuperiorSkyblockAPI;
import com.bgsoftware.superiorskyblock.api.events.IslandCreateEvent;
import com.bgsoftware.superiorskyblock.api.island.Island;
import com.bgsoftware.superiorskyblock.api.wrappers.SuperiorPlayer;

public class IslandSize extends JavaPlugin implements Listener {

	@Override
	public void onEnable() {
		Bukkit.getPluginManager().registerEvents(this, this);
	}

	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onIslandCreate(IslandCreateEvent e) {
		final SuperiorPlayer superiorPlayer = e.getPlayer();
		final Island island = superiorPlayer.getIsland();
		int islandSize = SuperiorSkyblockAPI.getSettings().getDefaultValues().getIslandSize();
		final int islandMaxSize = SuperiorSkyblockAPI.getSettings().getMaxIslandSize();

		for (String s : getValues(superiorPlayer.asPlayer())) {
			int size = s.matches("^[0-9]+$") ? Integer.valueOf(s) : islandSize;
			islandSize = Math.max(islandSize, size);
		}

		if (islandSize % 2 != 0) islandSize--;
		if (islandSize > islandMaxSize) islandSize = islandMaxSize;
		island.setIslandSize(islandSize);
	}

	private List<String> getValues(Player p) {
		return Stream.of(p.getEffectivePermissions()).flatMap(Collection::stream)
				.filter(s -> s.getPermission().startsWith("superior.island.size."))
				.flatMap(s -> Arrays.stream(s.getPermission().split("superior.island.size.")))
				.collect(Collectors.toList());
	}
}
