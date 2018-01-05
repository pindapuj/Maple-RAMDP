package rocksample.state;

import java.util.Arrays;
import java.util.List;

import burlap.mdp.core.oo.state.ObjectInstance;
import rocksample.RockSample;
import utilities.MutableObject;

/**
 * Created by steph on 10/26/2017.
 *
 * Wall class with x, y, length, and horizontal attributes.
 */

public class RockSampleWall extends MutableObject {

    /**
     *      Contains startx and y and length and if it is horizontal
     */
    private final static List<Object> keys = Arrays.<Object>asList(
            RockSample.ATT_START_X,
            RockSample.ATT_START_Y,
            RockSample.ATT_LENGTH,
            RockSample.ATT_IS_HORIZONTAL
    );

    /**
     *      Constructors
     */
    // RockSampleWall
    // Given a name, a start x, a start y, a length, and whether or not the wall
    // is horizontal, creates a wall object
    public RockSampleWall(String name, int startX,
                          int startY, int length,
                          boolean isHorizontal){
        this(name, (Object) startX, (Object) startY,
                (Object) length, (Object) isHorizontal);
    }

    // RockSampleWall
    // Given a name, a start x, a start y, a length, and whether or not the wall
    // is horizontal, creates a wall object
    public RockSampleWall(String name, Object startX,
                          Object startY, Object length,
                          Object isHorizontal){
        this.set(RockSample.ATT_START_X, startX);
        this.set(RockSample.ATT_START_Y, startY);
        this.set(RockSample.ATT_LENGTH, length);
        this.set(RockSample.ATT_IS_HORIZONTAL, isHorizontal);
        this.setName(name);
    }

    // copy
    // Returns a copy of the wall
    @Override
    public RockSampleWall copy(){
        return (RockSampleWall) copyWithName(name());
    }

    // copyWithName
    // Given a wall, makes a copy of the wall and returns
    @Override
    public ObjectInstance copyWithName(String objectName){
        return new RockSampleWall(
                objectName,
                get(RockSample.ATT_START_X),
                get(RockSample.ATT_START_Y),
                get(RockSample.ATT_LENGTH),
                get(RockSample.ATT_IS_HORIZONTAL)
        );
    }

    /**
     *      Accessors & Mutators
     */
    // className
    // Returns the name of the class
    @Override
    public String className(){
        return RockSample.CLASS_WALL;
    }

    // variableKeys
    // Returns all of the keys
    @Override
    public List<Object> variableKeys(){
        return keys;
    }
}
