package mc.alk.arena.objects.regions;

import mc.alk.arena.controllers.plugins.PylamoController;

import java.util.Map;

public class PylamoRegion implements ArenaRegion{
	String regionName;

	public PylamoRegion(){}

	public PylamoRegion(String regionName){
		this.regionName = regionName;
	}

	
	public Object yamlToObject(Map<String,Object> map, String value) {
		regionName = value;
		return new PylamoRegion(regionName);
	}

	
	public Object objectToYaml() {
		return regionName;
	}

	public void setID(String id) {
		regionName = id;
	}

	
    public String getID() {
		return regionName;
	}

    
    public String getWorldName() {
        return null;
    }

    
    public boolean valid(){
		return regionName != null && PylamoController.enabled();
	}
}
