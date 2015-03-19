package mc.alk.arena.objects.regions;

import mc.alk.arena.controllers.plugins.WorldGuardController;

import java.util.Map;

public class WorldGuardRegion implements ArenaRegion{
	protected String regionName;

	protected String regionWorld;

	public WorldGuardRegion(){}

	public WorldGuardRegion(String regionWorld, String regionName){
		this.regionWorld = regionWorld;
		this.regionName = regionName;
	}

	
	public Object yamlToObject(Map<String,Object> map, String value) {
		if (value == null)
			return null;
		String split[] = value.split(",");
		regionWorld = split[0];
		regionName = split[1];
		return new WorldGuardRegion(regionWorld, regionName);
	}

	
	public Object objectToYaml() {
		return regionWorld+","+regionName;
	}

	
	public boolean valid() {
		return regionName != null && regionWorld != null &&
				WorldGuardController.hasWorldGuard() &&
				WorldGuardController.hasRegion(regionWorld, regionName);
	}

	
    public String getID() {
		return regionName;
	}

	public void setID(String regionName) {
		this.regionName = regionName;
	}

	public String getRegionWorld() {
		return regionWorld;
	}

	public void setRegionWorld(String regionWorld) {
		this.regionWorld = regionWorld;
	}

	
    public String getWorldName(){
		return regionWorld;
	}
}
