package agents.model.perception;

import agents.config.AreaNames;

public class PERCEPTIONProcess2 extends cFramework.nodes.process.Process {

	public PERCEPTIONProcess2 () {
		this.ID = AreaNames.PERCEPTIONProcess2;
		this.namer = AreaNames.class;
	}

	@Override
	public void receive(long nodeID, byte[] data) {
	}
}
