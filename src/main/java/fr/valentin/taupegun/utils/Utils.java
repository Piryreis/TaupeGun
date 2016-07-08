package fr.valentin.taupegun.utils;

import org.bukkit.ChatColor;
import org.bukkit.DyeColor;

import org.apache.commons.lang.StringUtils;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Valentin on 17/07/2015.
 */
public class Utils {

    private static Utils instance = new Utils();
    public static Utils getInstance(){return instance;}
    private Utils(){}


    private static HashMap<ChatColor, DyeColor> chatColorToDyeColor = new HashMap<ChatColor, DyeColor>();
    static {
        chatColorToDyeColor.put(ChatColor.BLACK, DyeColor.BLACK);
        chatColorToDyeColor.put(ChatColor.DARK_BLUE, DyeColor.BLUE);
        chatColorToDyeColor.put(ChatColor.DARK_GREEN, DyeColor.GREEN);
        chatColorToDyeColor.put(ChatColor.DARK_AQUA, DyeColor.CYAN);
        chatColorToDyeColor.put(ChatColor.DARK_RED, DyeColor.RED);
        chatColorToDyeColor.put(ChatColor.DARK_PURPLE, DyeColor.PURPLE);
        chatColorToDyeColor.put(ChatColor.GOLD, DyeColor.ORANGE);
        chatColorToDyeColor.put(ChatColor.GRAY, DyeColor.SILVER);
        chatColorToDyeColor.put(ChatColor.DARK_GRAY, DyeColor.GRAY);
        chatColorToDyeColor.put(ChatColor.BLUE, DyeColor.LIGHT_BLUE);
        chatColorToDyeColor.put(ChatColor.GREEN, DyeColor.LIME);
        chatColorToDyeColor.put(ChatColor.AQUA, DyeColor.CYAN);
        chatColorToDyeColor.put(ChatColor.RED, DyeColor.PINK);
        chatColorToDyeColor.put(ChatColor.LIGHT_PURPLE, DyeColor.MAGENTA);
        chatColorToDyeColor.put(ChatColor.YELLOW, DyeColor.YELLOW);
        chatColorToDyeColor.put(ChatColor.WHITE, DyeColor.WHITE);
    }


    public DyeColor getDyeColor(ChatColor color){
        return chatColorToDyeColor.get(color);
    }

    /*public String convertingListToString(ArrayList list){
        return StringUtils.join(list, " ");
    }*/

    public String convertingListToString(List<String> list, char separator){
        return StringUtils.join(list, separator);
        /*StringBuilder stringBuilder = new StringBuilder();
        for (int result = 0; result < list.size(); result++){
            if (result > 1){
                if (result == 2){
                    stringBuilder.append(list.get(result));
                } else {
                    stringBuilder.append(" ");
                    stringBuilder.append(list.get(result));
                }
            }
        }
        return stringBuilder.toString();*/
    }
}
