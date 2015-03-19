package mc.alk.arena.objects.spawns;

import org.bukkit.Material;
import org.bukkit.block.Block;


public class BlockSpawn extends SpawnInstance{
    Material mat;
    Material despawnMat = Material.AIR;

	public BlockSpawn(Block block, boolean setMaterial){
		super(block.getLocation());
        if (setMaterial){
            this.mat = block.getType();}
    }

    public void setMaterial(Material mat) {
        this.mat = mat;
    }

    public void setDespawnMaterial(Material mat) {
        this.despawnMat = mat;
    }

    
    public void spawn() {
        Block b = getLocation().getBlock();
        if (mat != null && b.getType() != mat)
            b.setType(mat);
    }

    
	public void despawn() {
        Block b = getLocation().getBlock();
        if (despawnMat!=null)
            b.setType(despawnMat);
	}

	
	public String toString(){
		return "[BS "+mat.name()+"]";
	}

    public Block getBlock() {
        return this.getLocation().getBlock();
    }
    public Material getMaterial() {
        return mat;
    }

    public Material getDespawnMaterial() {
        return despawnMat;
    }
}
