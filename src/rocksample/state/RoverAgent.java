package rocksample.state;

import burlap.mdp.core.oo.state.ObjectInstance;

import java.util.Arrays;
import java.util.List;

import rocksample.RockSample;

/**
 * Created by steph on 10/26/2017.
 *
 * Rover agent class (extends point class) with x and y attributes.
 */

public class RoverAgent extends RockSamplePt {

    /**
     *      x, y
     */
    private final static List<Object> keys = Arrays.<Object>asList(
            RockSample.ATT_X,
            RockSample.ATT_Y
    );

    /**
     *      Constructors
     */
    // RoverAgent
    // Given a name, an x, and a y, constructs a new rover agent
    public RoverAgent(String name, int x, int y){
        this(name, (Object) x, (Object) y);
    }

    // RoverAgent
    // Given a name, an x, and a y, constructs a new rover agent
    private RoverAgent(String name, Object x, Object y){
        this.set(RockSample.ATT_X, x);
        this.set(RockSample.ATT_Y, y);
        this.setName(name);
    }

    // copy
    // Returns a copy of the agent
    @Override
    public RoverAgent copy(){
        return (RoverAgent) copyWithName(name());
    }

    // copyWithName
    // Given the name of the agent, return a copy of that agent
    @Override
    public ObjectInstance copyWithName(String objectName){
        return new RoverAgent(
                objectName,
                get(RockSample.ATT_X),
                get(RockSample.ATT_Y)
        );
    }

    /**
     *      Accessors & Mutators
     */

    // className
    // Returns the name of the class
    @Override
    public String className(){
        return RockSample.CLASS_ROVER;
    }

    // variableKeys
    // Returns all of the keys
    @Override
    public List<Object> variableKeys(){
        return keys;
    }
}
