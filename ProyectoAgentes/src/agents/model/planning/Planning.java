package agents.model.planning;

import agents.config.AreaNames;

public class Planning extends cFramework.nodes.area.Area {

	public Planning () {
		this.ID = AreaNames.Planning;
		this.namer = AreaNames.class;
		addProcess(PLANNINGProcess1.class);
		addProcess(PLANNINGProcess2.class);
	}

	@Override
	public void init() {
	}

	@Override
	public void receive(long nodeID, byte[] data) {
            
            String message = new String(data);
            
            System.out.println(this.getClass().getName()+" received message: "+message);
            System.out.println(this.getClass().getName()+" sending to PLANNINGProcess1");
            
            send(AreaNames.PLANNINGProcess1, data);
            
	}
}
