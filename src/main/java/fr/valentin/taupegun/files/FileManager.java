package fr.valentin.taupegun.files;

import fr.valentin.taupegun.TaupeGun;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.HashMap;

/**
 * Created by Valentin on 07/08/2015.
 */
public class FileManager {

    private static FileManager instance = new FileManager();
    public static FileManager getInstance(){return instance;}
    private FileManager(){}

    private TaupeGun plugin = TaupeGun.getInstance();

    private HashMap<String, File> files = new HashMap<String, File>();

    public void loadFiles(){
        String dataFolder = plugin.getDataFolder().toString();

        loadFile("teams.yml", dataFolder);

        //loadFile("translations." + plugin.getPluginConfig().getLanguage + ".yml", dataFolder + File.separator + "language");
        if (loadFile("translations." + plugin.getPluginConfig().getLanguage + ".yml", dataFolder + File.separator + "language")) return;
        else loadFile("translations.fr.yml", dataFolder + File.separator + "language");

        if (!plugin.getPluginConfig().randomLocations){
            loadFile("locations.yml", dataFolder);
        }

        if (plugin.getPluginConfig().lobbyIsEnable){
            loadFile(plugin.getPluginConfig().getLobbyName, dataFolder + File.separator + "schematic");
        }
    }

    public boolean loadFile(String filename, String dataFolder){
        boolean statement = false;
        File file;

        if (!files.containsKey(filename)){
            file = new File(dataFolder, filename);

            if (!file.exists()){
                plugin.log("The file " + filename + " was not found, trying to get it from the plugin file...");
                try {
                    if (filename.endsWith(".schematic")){
                        plugin.saveResource("schematic" + File.separator + filename, false);
                    }
                    else if (filename.startsWith("translation")){
                        plugin.saveResource("language" + File.separator + filename, false);
                    }
                    else {
                        plugin.saveResource(filename, false);
                    }
                    plugin.log("The file " + filename + " was loaded correctly.");
                    statement = true;

                } catch (IllegalArgumentException e){
                    plugin.log("The file " + filename + " could not be loaded.");
                }
            }

            files.put(filename, file);
        }
        return statement;
    }

    public FileConfiguration getFileConfiguration(String filename){
        return YamlConfiguration.loadConfiguration(files.get(filename));
    }

    public File getFile(String filename){
        return files.get(filename);
    }

}
