package agents.model.planning;

import agents.config.AreaNames;

public class PLANNINGProcess1 extends cFramework.nodes.process.Process {

    public PLANNINGProcess1() {
        this.ID = AreaNames.PLANNINGProcess1;
        this.namer = AreaNames.class;
    }

    @Override
    public void receive(long nodeID, byte[] data) {

        String message = new String(data);

        System.out.println(this.getClass().getName() + " received message: " + message);
    }
}
