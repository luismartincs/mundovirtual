package agents.main;
import cFramework.nodes.service.Igniter;

public class Main extends Igniter{

	public Main(){
		String[] areaNames = {
			agents.model.perception.Perception.class.getName(),
			agents.model.planning.Planning.class.getName(),
		};
		setAreas(areaNames);
		configuration.setLocal(false);
		configuration.setDebug(null);
		run();
	}

	public static void main(String[] arg){
		new Main();
	}
}
