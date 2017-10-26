package rocksample.stateGenerator;

import rocksample.RockSample;
import rocksample.state.RockSampleState;
import rocksample.state.RockSampleWall;
import rocksample.state.RoverAgent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by steph on 10/26/2017.
 */
public class RockSampleStateFactory {

    public static RockSampleState createClassicState(){
        RoverAgent rover = new RoverAgent(RockSample.CLASS_ROVER + 0, 0, 3);

        List<RockSampleWall> walls = new ArrayList();
        walls.add(new RockSampleWall(RockSample.CLASS_WALL + 0, 0, 0, 5, false));
        walls.add(new RockSampleWall(RockSample.CLASS_WALL + 1, 0, 0, 5, true));
        walls.add(new RockSampleWall(RockSample.CLASS_WALL + 2, 5, 0, 5, false));
        walls.add(new RockSampleWall(RockSample.CLASS_WALL + 3, 0, 5, 5, true));

        return new RockSampleState(rover, walls);
    }
}

