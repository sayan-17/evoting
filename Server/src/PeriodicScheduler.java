import java.sql.Time;
import java.util.Timer;
import java.util.TimerTask;

public class PeriodicScheduler extends TimerTask {
	
	@Override
	public void run() {
		AdminData.updateEmailQueue();
	}
}
