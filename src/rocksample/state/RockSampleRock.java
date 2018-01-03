package rocksample.state;

import burlap.mdp.core.oo.state.ObjectInstance;

import java.util.Arrays;
import java.util.List;

import rocksample.RockSample;

/**
 * Created by steph on 10/26/2017.
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
    public RockSampleRock(String name, int x, int y, String quality) {
        this(name, (Object) x, (Object) y, (Object) quality);
    }

    private RockSampleRock(String name, Object x, Object y, Object quality) {
        this.set(RockSample.ATT_X, x);
        this.set(RockSample.ATT_Y, y);
        this.set(RockSample.ATT_QUALITY, quality);
        this.setName(name);
    }

    @Override
    public String className() {
        return RockSample.CLASS_ROCK;
    }

    @Override
    public RockSampleRock copy() {
        return (RockSampleRock) copyWithName(name());
    }

    @Override
    public ObjectInstance copyWithName(String objectName) {
        return new RockSampleRock(
                objectName,
                get(RockSample.ATT_X),
                get(RockSample.ATT_Y),
                get(RockSample.ATT_QUALITY)
        );
    }

    @Override
    public List<Object> variableKeys() {
        return keys;
    }
}
