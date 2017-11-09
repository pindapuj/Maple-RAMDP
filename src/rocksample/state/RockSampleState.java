package rocksample.state;

import burlap.mdp.core.oo.state.MutableOOState;
import burlap.mdp.core.oo.state.OOStateUtilities;
import burlap.mdp.core.oo.state.OOVariableKey;
import burlap.mdp.core.oo.state.ObjectInstance;
import burlap.mdp.core.state.MutableState;
import rocksample.RockSample;

import java.util.*;

/**
 * Created by steph on 10/26/2017.
 */
public class RockSampleState implements MutableOOState {

    /**
     *      Getters & setters for RockSampleState
     */

    // get
    // Given a variable key, returns the object that corresponds to that key
    @Override
    public Object get(Object variableKey) {
        return OOStateUtilities.get(this, variableKey);
    }

    // set
    // Given a variable key and a value, sets the value of the object corresponding to the key and returns
    @Override
    public MutableState set(Object variableKey, Object value) {
        OOVariableKey key = OOStateUtilities.generateKey(variableKey);

        if(key.obName.equals(rover.name())) {
            touchRover().set(variableKey, value);
        }else if(rocks.get(key.obName) != null) {
            touchRock(key.obName).set(variableKey, value);
        }else if(walls.get(key.obName) != null){
            touchWall(key.obName).set(variableKey, value);
        } else {
            throw new RuntimeException("ERROR: unable to set value for " + variableKey);
        }
        return this;
    }

    // getRover
    // Returns the rover agent
    public RoverAgent getRover() { return rover; }

    // setRover
    // Given a rover agent, sets the rover
    public void setRover (RoverAgent rover) { this.rover = rover; }

    // getRoverAtt
    // Given an attribute name, returns the corresponding rover attribute
    public Object getRoverAtt(String attName){
        return rover.get(attName);
    }

    // getRoverName
    // Returns the name of the rover
    public String getRoverName(){
        return rover.name();
    }

    // setWalls
    // Given a map of walls, sets the walls
    public void setWalls(Map<String, RockSampleWall> walls) { this.walls = walls; }

    /**
     *     Private variables
     */
    private RoverAgent rover;                       // the agent operating in the domain
    private Map<String, RockSampleWall> walls;      // the walls of the domain
    private Map<String, RockSampleRock> rocks;      // the rocks in the domain

    /**
     *      Constructors
     */

    // RockSampleState
    // Given a rover agent and a list of walls, constructs a RockSampleState
    public RockSampleState(RoverAgent rover,
                           List<RockSampleRock> rocks,
                           List<RockSampleWall> walls) {
        this.rover = rover;

        this.rocks = new HashMap<String, RockSampleRock>();
        for(RockSampleRock r : rocks) {
            this.rocks.put(r.name(), r);
        }

        this.walls = new HashMap<String, RockSampleWall>();
        for(RockSampleWall w : walls) {
            this.walls.put(w.name(), w);
        }
    }

    // RockSampleState
    // Given a rover agent and a map of walls, constructs a rocksample state
    public RockSampleState(RoverAgent r,
                           Map<String, RockSampleRock> rocks,
                           Map<String, RockSampleWall> walls) {
        this.rover = r;
        this.rocks = rocks;
        this.walls = walls;
    }

    // copy
    // Makes a copy of the state
    @Override
    public RockSampleState copy() {
        return new RockSampleState(rover, rocks, walls);
    }

    // numObjects
    // Returns the number of objects in the domain
    @Override
    public int numObjects() { return 1 + rocks.size() + walls.size(); }

    // object
    // If object name matches object in domain, return that object. Return null otherwise
    @Override
    public ObjectInstance object(String oname) {
        if(rover.name().equals(oname))
            return rover;

        // change this ordering once more stuff is added
        ObjectInstance o = rocks.get(oname);
        if(o != null)
            return o;

        o = walls.get(oname);
        if(o != null)
            return o;

        return null;
    }

    // objects
    // Aggregates all objects in domain and returns the aggregation
    @Override
    public List<ObjectInstance> objects() {
        List<ObjectInstance> objs = new ArrayList<ObjectInstance>();
        objs.add(rover);
        objs.addAll(rocks.values());
        objs.addAll(walls.values());
        return objs;
    }

    // objectsOfClass
    // Given an object class, returns the objects in the domain of that class. Otherwise, throws exception
    @Override
    public List<ObjectInstance> objectsOfClass(String oclass) {
        if(oclass.equals(RockSample.CLASS_ROVER))
            return Arrays.<ObjectInstance>asList(rover);
        else if(oclass.equals(RockSample.CLASS_ROCK))
            return new ArrayList<ObjectInstance>(rocks.values());
       else if(oclass.equals(RockSample.CLASS_WALL))
            return new ArrayList<ObjectInstance>(walls.values());
        throw new RuntimeException("No object class " + oclass);
    }

    // variableKeys
    @Override
    public List<Object> variableKeys() {
        return OOStateUtilities.flatStateKeys(this);
    }

    // addObject
    // Given an object instance, adds the object to the state. Otherwise, throws exception
    @Override
    public MutableOOState addObject(ObjectInstance o) {
        if(o instanceof RoverAgent || o.className().equals(RockSample.CLASS_ROVER)) {
            touchRover();
            rover = (RoverAgent) o;
        }else if(o instanceof RockSampleRock || o.className().equals(RockSample.CLASS_ROCK)){
            touchRocks().put(o.name(), (RockSampleRock) o);
        }else if(o instanceof RockSampleWall || o.className().equals(RockSample.CLASS_WALL)){
            touchWalls().put(o.name(), (RockSampleWall) o);
        }else{
            throw new RuntimeException("Can only add certain objects to state.");
        }
        return this;
    }

    // removeObject
    // Throws exception
    @Override
    public MutableOOState removeObject(String oname) {
        throw new RuntimeException("Remove not implemented");
    }

    // renameObject
    // Throws exception
    @Override
    public MutableOOState renameObject(String objectName, String newName) {
        throw new RuntimeException("Rename not implemented");
    }


    /**
     *      Touch methods - allow shallow copy of states and copy of objects only when modified
     */

    // touchRover
    // Returns a shallow copy of the rover agent
    public RoverAgent touchRover(){
        this.rover = rover.copy();
        return rover;
    }

    public RockSampleWall touchWall(String wallName){
        RockSampleWall w = walls.get(wallName).copy();
        touchWalls().remove(wallName);
        walls.put(wallName, w);
        return w;
    }

    public Map<String, RockSampleWall> touchWalls(){
        this.walls = new HashMap<String, RockSampleWall>(walls);
        return walls;
    }

    public RockSampleRock touchRock(String rockName){
        RockSampleRock r = rocks.get(rockName).copy();
        touchRocks().remove(rockName);
        rocks.put(rockName, r);
        return r;
    }

    public Map<String, RockSampleRock> touchRocks(){
        this.rocks = new HashMap<String, RockSampleRock>(rocks);
        return rocks;
    }

    public String[] getWalls(){
        String[] ret = new String[walls.size()];
        int i = 0;
        for(String name: walls.keySet())
            ret[i++] = name;
        return ret;
    }

    public RockSampleRock getRockAtPoint(int x, int y){
        List<ObjectInstance> rocks = objectsOfClass(RockSample.CLASS_ROCK);
        for(ObjectInstance r : rocks){
            int rx = (int) r.get(RockSample.ATT_X);
            int ry = (int) r.get(RockSample.ATT_Y);
            if(rx == x && ry == y){
                return (RockSampleRock) r;
            }
        }
        return null;
    }

    public Object getRockAtt(String name, String attName) {
        return rocks.get(name).get(attName);
    }

    // test to see if there is a wall on either side of the taxi
    public boolean wallNorth(){
        int tx = (int) rover.get(RockSample.ATT_X);
        int ty = (int) rover.get(RockSample.ATT_Y);
        for(RockSampleWall w : walls.values()){
            boolean ish = (boolean) w.get(RockSample.ATT_IS_HORIZONTAL);
            int wx = (int) w.get(RockSample.ATT_START_X);
            int wy = (int) w.get(RockSample.ATT_START_Y);
            int wlen = (int) w.get(RockSample.ATT_LENGTH);
            if(ish){
                // wall in above line
                if(ty == wy - 1){
                    // x value in wall bounds
                    if(tx >= wx && tx < wx + wlen){
                        return true;
                    }
                }
            }
        }

        return false;
    }

    public boolean wallEast(){
        int tx = (int) rover.get(RockSample.ATT_X);
        int ty = (int) rover.get(RockSample.ATT_Y);
        for(RockSampleWall w : walls.values()){
            boolean ish = (boolean) w.get(RockSample.ATT_IS_HORIZONTAL);
            int wx = (int) w.get(RockSample.ATT_START_X);
            int wy = (int) w.get(RockSample.ATT_START_Y);
            int wlen = (int) w.get(RockSample.ATT_LENGTH);
            if(!ish){
                if(tx == wx - 1){
                    if(ty >= wy && ty < wy + wlen){
                        return true;
                    }
                }
            }
        }

        return false;
    }

    public boolean wallSouth(){
        int tx = (int) rover.get(RockSample.ATT_X);
        int ty = (int) rover.get(RockSample.ATT_Y);
        for(RockSampleWall w : walls.values()){
            boolean ish = (boolean) w.get(RockSample.ATT_IS_HORIZONTAL);
            int wx = (int) w.get(RockSample.ATT_START_X);
            int wy = (int) w.get(RockSample.ATT_START_Y);
            int wlen = (int) w.get(RockSample.ATT_LENGTH);
            if(ish){
                if(ty == wy){
                    if(tx >= wx && tx < wx + wlen){
                        return true;
                    }
                }
            }
        }

        return false;
    }

    public boolean wallWest(){
        int tx = (int) rover.get(RockSample.ATT_X);
        int ty = (int) rover.get(RockSample.ATT_Y);
        for(RockSampleWall w : walls.values()){
            boolean ish = (boolean) w.get(RockSample.ATT_IS_HORIZONTAL);
            int wx = (int) w.get(RockSample.ATT_START_X);
            int wy = (int) w.get(RockSample.ATT_START_Y);
            int wlen = (int) w.get(RockSample.ATT_LENGTH);
            if(!ish){
                if(tx == wx){
                    if(ty >= wy && ty < wy + wlen){
                        return true;
                    }
                }
            }
        }
        return false;
    }

    // toString
    @Override
    public String toString(){
        return OOStateUtilities.ooStateToString(this);
    }
}
