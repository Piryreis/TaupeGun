package fr.valentin.taupegun.world;

import fr.valentin.taupegun.TaupeGun;
import fr.valentin.taupegun.schematic.Schematic;
import fr.valentin.taupegun.schematic.SchematicManager;
import org.bukkit.*;

import java.io.File;
import java.util.Random;

/**
 * Created by Valentin on 10/08/2015.
 */
public class WorldManager {

    private static WorldManager instance = new WorldManager();
    public static WorldManager getInstance(){return instance;}
    private WorldManager(){}

    private TaupeGun plugin = TaupeGun.getInstance();

    private static Random random = new Random();

    private String worldName = plugin.getPluginConfig().getWorldName;
    public World world = Bukkit.getWorld(worldName);
    public World nether;
    public World end;
    private int mapSize = plugin.getPluginConfig().getMapSize;

    public void initialise() {

        if (!Bukkit.getWorlds().contains(world)){

            for (World w : Bukkit.getWorlds()){
                Bukkit.unloadWorld(w, true);
            }

            world = new WorldCreator(worldName).environment(World.Environment.NORMAL).createWorld();
            Bukkit.getWorlds().add(world);

            nether = new WorldCreator(worldName + "_nether").environment(World.Environment.NETHER).createWorld();
            Bukkit.getWorlds().add(nether);

            end = new WorldCreator(worldName + "_the_end").environment(World.Environment.THE_END).createWorld();
            Bukkit.getWorlds().add(end);

        }

        int spawnX = plugin.getPluginConfig().getSpawnX;
        int spawnY = plugin.getPluginConfig().getSpawnY;
        int spawnZ = plugin.getPluginConfig().getSpawnZ;
        world.setSpawnLocation(spawnX, spawnY, spawnZ);

        world.getWorldBorder().setCenter(spawnX, spawnZ);
        world.getWorldBorder().setSize(plugin.getPluginConfig().getMapSize);
        world.getWorldBorder().setWarningDistance(25);
        world.setDifficulty(Difficulty.HARD);


        if (plugin.getPluginConfig().lobbyIsEnable) {
            File schematicFile = plugin.getFileManager().getFile(plugin.getPluginConfig().getLobbyName);
            if (schematicFile.exists()) {
                try {
                    Schematic schematic = SchematicManager.loadSchematic(schematicFile);

                    double locX = plugin.getPluginConfig().getLobbyX;
                    double locY = plugin.getPluginConfig().getLobbyY;
                    double locZ = plugin.getPluginConfig().getLobbyZ;
                    Location location = new Location(world, locX, locY, locZ);

                    SchematicManager.pasteSchematic(world, location, schematic);

                    plugin.log("The lobby schematic " + schematicFile.getName() + " was correctly pasted.");
                } catch (Exception e) {
                    e.printStackTrace();
                    plugin.log("The lobby schematic could not be loaded.");
                }
            }
        }
    }

    public Location randomMapLocation(){
        int finanMapSize = mapSize / 2 - 10;
        int xRandom = random.nextInt(finanMapSize - (-finanMapSize) + 1) - finanMapSize;
        int zRandom = random.nextInt(finanMapSize - (-finanMapSize) + 1) - finanMapSize;
        Location location = new Location(world, xRandom, 120, zRandom);
        return location;
    }

}
