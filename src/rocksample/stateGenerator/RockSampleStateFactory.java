package rocksample.stateGenerator;

import rocksample.RockSample;
import rocksample.state.RockSampleState;
import rocksample.state.RockSampleWall;
import rocksample.state.RoverAgent;
import rocksample.state.RockSampleRock;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by steph on 10/26/2017.
 */
public class RockSampleStateFactory {

    public static RockSampleState createClassicState(){
        RoverAgent rover = new RoverAgent(RockSample.CLASS_ROVER + 0, 0, 2);

        List<RockSampleRock> rocks = new ArrayList();
        rocks.add(new RockSampleRock(RockSample.CLASS_ROCK + 0, 0, 4, "Bad"));
        rocks.add(new RockSampleRock(RockSample.CLASS_ROCK + 1, 2, 3, "Good"));
        rocks.add(new RockSampleRock(RockSample.CLASS_ROCK + 2, 3, 4, "Bad"));
        rocks.add(new RockSampleRock(RockSample.CLASS_ROCK + 3, 2, 4, "Bad"));

        List<RockSampleWall> walls = new ArrayList();
        walls.add(new RockSampleWall(RockSample.CLASS_WALL + 0, 0, 0, 5, false));
        walls.add(new RockSampleWall(RockSample.CLASS_WALL + 1, 0, 0, 5, true));
        walls.add(new RockSampleWall(RockSample.CLASS_WALL + 2, 5, 0, 5, false));
        walls.add(new RockSampleWall(RockSample.CLASS_WALL + 3, 0, 5, 5, true));

        return new RockSampleState(rover, rocks, walls);
    }

    public static RockSampleState createCustomState(String qual_1, String qual_2, String qual_3, String qual_4){

        RoverAgent rover = new RoverAgent(RockSample.CLASS_ROVER + 0, 0, 2);

        List<RockSampleRock> rocks = new ArrayList();
        rocks.add(new RockSampleRock(RockSample.CLASS_ROCK + 0, 0, 4, qual_1));
        rocks.add(new RockSampleRock(RockSample.CLASS_ROCK + 1, 2, 3, qual_2));
        rocks.add(new RockSampleRock(RockSample.CLASS_ROCK + 2, 3, 4, qual_3));
        rocks.add(new RockSampleRock(RockSample.CLASS_ROCK + 3, 2, 4, qual_4));

        List<RockSampleWall> walls = new ArrayList();
        walls.add(new RockSampleWall(RockSample.CLASS_WALL + 0, 0, 0, 5, false));
        walls.add(new RockSampleWall(RockSample.CLASS_WALL + 1, 0, 0, 5, true));
        walls.add(new RockSampleWall(RockSample.CLASS_WALL + 2, 5, 0, 5, false));
        walls.add(new RockSampleWall(RockSample.CLASS_WALL + 3, 0, 5, 5, true));

        return new RockSampleState(rover, rocks, walls);

    }
}