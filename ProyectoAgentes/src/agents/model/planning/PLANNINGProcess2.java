package agents.model.planning;

import agents.config.AreaNames;

public class PLANNINGProcess2 extends cFramework.nodes.process.Process {

	public PLANNINGProcess2 () {
		this.ID = AreaNames.PLANNINGProcess2;
		this.namer = AreaNames.class;
	}

	@Override
	public void receive(long nodeID, byte[] data) {
	}
}
