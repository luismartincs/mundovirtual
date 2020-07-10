package agents.model.perception;

import agents.config.AreaNames;

public class PERCEPTIONProcess1 extends cFramework.nodes.process.Process {

	public PERCEPTIONProcess1 () {
		this.ID = AreaNames.PERCEPTIONProcess1;
		this.namer = AreaNames.class;
	}

	@Override
	public void receive(long nodeID, byte[] data) {
	}
}
