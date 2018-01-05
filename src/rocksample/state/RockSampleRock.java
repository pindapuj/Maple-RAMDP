package rocksample.state;

import burlap.mdp.core.oo.state.ObjectInstance;

import java.util.Arrays;
import java.util.List;

import rocksample.RockSample;

/**
 *  Created by steph on 10/26/2017.
 *
 *  Rock class (extends point class) with x, y, and quality attributes.
 */

public class RockSampleRock extends RockSamplePt {

    /**
     *      x, y
     */
    private final static List<Object> keys = Arrays.<Object>asList(
            RockSample.ATT_X,
            RockSample.ATT_Y,
            RockSample.ATT_QUALITY
    );

    /**
     *      Constructors
     */
    // RockSampleRock
    // Given a name, an x, a y, and a quality, creates a rock object
    public RockSampleRock(String name, int x, int y, String quality) {
        this(name, (Object) x, (Object) y, (Object) quality);
    }

    // RockSampleRock
    // Given a name, an x, a y, and a quality, creates a rock object
    private RockSampleRock(String name, Object x, Object y, Object quality) {
        this.set(RockSample.ATT_X, x);
        this.set(RockSample.ATT_Y, y);
        this.set(RockSample.ATT_QUALITY, quality);
        this.setName(name);
    }

    // copy
    // Returns a copy of the rock
    @Override
    public RockSampleRock copy() {
        return (RockSampleRock) copyWithName(name());
    }

    // copyWithName
    // Given a rock, makes a copy of the rock and returns
    @Override
    public ObjectInstance copyWithName(String objectName) {
        return new RockSampleRock(
                objectName,
                get(RockSample.ATT_X),
                get(RockSample.ATT_Y),
                get(RockSample.ATT_QUALITY)
        );
    }

    /**
     *      Accessors & Mutators
     */
    // className
    // Returns the name of the class
    @Override
    public String className() {
        return RockSample.CLASS_ROCK;
    }

    // variableKeys
    // Returns all of the keys
    @Override
    public List<Object> variableKeys() {
        return keys;
    }
}
