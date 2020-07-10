package agents.model.perception;

import agents.config.AreaNames;
import java.util.Timer;
import java.util.TimerTask;

public class Perception extends cFramework.nodes.area.Area {

        private int count = 0;
        
	public Perception () {
		this.ID = AreaNames.Perception;
		this.namer = AreaNames.class;
		addProcess(PERCEPTIONProcess1.class);
		addProcess(PERCEPTIONProcess2.class);
	}

	@Override
	public void init() {
            
            
            TimerTask task =new TimerTask() {
                @Override
                public void run() {
                    
                    String message = "Hola Mundo! "+count;
                    
                    System.out.println(this.getClass().getName()+" "+message);
                    
                    send(AreaNames.Planning, message.getBytes());
                    
                    count++;
                    
                }
            };
            
            Timer timer = new Timer();
            
            timer.scheduleAtFixedRate(task, 2000, 1000);
            
	}

	@Override
	public void receive(long nodeID, byte[] data) {
	}
}
