package rocksample.state;

/**
 * Created by steph on 10/26/2017.
 */
import java.util.Arrays;
import java.util.List;

import burlap.mdp.core.oo.state.ObjectInstance;
import rocksample.RockSample;
import utilities.MutableObject;

public class RockSampleWall extends MutableObject {

    /**
     * contains startx and y and length and if it is horizontal
     */
    private final static List<Object> keys = Arrays.<Object>asList(
            RockSample.ATT_START_X,
            RockSample.ATT_START_Y,
            RockSample.ATT_LENGTH,
            RockSample.ATT_IS_HORIZONTAL
    );

    public RockSampleWall(String name,
                          int startX,
                          int startY,
                          int length,
                          boolean isHorizontal){
        this(name, (Object) startX, (Object) startY,
                (Object) length, (Object) isHorizontal);
    }

    public RockSampleWall(String name,
                          Object startX,
                          Object startY,
                          Object length,
                          Object isHorizontal){
        this.set(RockSample.ATT_START_X, startX);
        this.set(RockSample.ATT_START_Y, startY);
        this.set(RockSample.ATT_LENGTH, length);
        this.set(RockSample.ATT_IS_HORIZONTAL, isHorizontal);
        this.setName(name);
    }

    @Override
    public String className(){
        return RockSample.CLASS_WALL;
    }

    @Override
    public RockSampleWall copy(){
        return (RockSampleWall) copyWithName(name());
    }

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

    @Override
    public List<Object> variableKeys(){
        return keys;
    }
}
