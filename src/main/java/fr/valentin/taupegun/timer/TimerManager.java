package fr.valentin.taupegun.timer;

import fr.valentin.taupegun.TaupeGun;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;

import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * Created by Valentin on 26/07/2015.
 */
public class TimerManager implements Runnable {

    private static TimerManager instance = new TimerManager();
    public static TimerManager getInstance(){return instance;}
    private TimerManager(){}

    private TaupeGun plugin = TaupeGun.getInstance();

    private NumberFormat numberFormat = new DecimalFormat("00");
    private int heures = 00;
    private int minutes = 00;
    private int secondes = 00;

    private BukkitScheduler scheduler = Bukkit.getScheduler();
    private BukkitTask task = null;//scheduler.runTask(plugin, this);

    public void startTimer(){
        task = scheduler.runTaskTimer(plugin, this, 0L, 20L);
    }

    public void stopTimer(){
        task.cancel();
    }

    public int getHeures(){
        return heures;
    }

    public String getHeuresFormat(){
        return numberFormat.format(heures);
    }

    public void setHeures(int heures){
        this.heures = heures;
    }

    public int getMinutes(){
        return minutes;
    }

    public String getMinutesFormat(){
        return numberFormat.format(minutes);
    }

    public void setMinutes(int minutes){
        this.minutes = minutes;
    }

    public int getSecondes(){
        return secondes;
    }

    public String getSecondesFormat(){
        return numberFormat.format(secondes);
    }

    public void setSecondes(int secondes){
        this.secondes = secondes;
    }

    // return time format -> HH:mm:ss
    public String getTime(){
        return String.format("%02d:%02d:%02d", heures, minutes, secondes);
    }

    @Override
    public void run() {
        if (minutes == 59 && secondes == 59){
            minutes = -01;
            heures++;
        }
        if (secondes == 59){
            secondes = -01;
            minutes++;
        }
        secondes++;

        plugin.getEpisodeManager().update();
        plugin.getScoreboardManager().updateScoreboards();
        plugin.getTaupeManager().testForTaupe();
        plugin.getGameManager().testForWin();

    }
}
