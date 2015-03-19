package mc.alk.arena.util;

import mc.alk.arena.Defaults;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Collection;
import java.util.HashMap;

public class EffectUtil {
	static final String version = "2.1.2";
	static final HashMap<PotionEffectType,String> effectToName = new HashMap<PotionEffectType,String>();
	static final HashMap<String,PotionEffectType> nameToEffect= new HashMap<String,PotionEffectType>();
	static{
		effectToName.put(PotionEffectType.FAST_DIGGING, "HASTE");
		effectToName.put(PotionEffectType.SLOW_DIGGING,"SLOWSWING");
		effectToName.put(PotionEffectType.SLOW,"SLOWNESS");
		effectToName.put(PotionEffectType.SLOW_DIGGING,"SLOWDIG");
		effectToName.put(PotionEffectType.INCREASE_DAMAGE, "STRENGTH");
		effectToName.put(PotionEffectType.REGENERATION, "REGEN");
		effectToName.put(PotionEffectType.DAMAGE_RESISTANCE, "RESISTANCE");
		effectToName.put(PotionEffectType.DAMAGE_RESISTANCE, "PROT");
		effectToName.put(PotionEffectType.CONFUSION, "NAUSEA");
		nameToEffect.put("HASTE", PotionEffectType.FAST_DIGGING);
		nameToEffect.put("SLOW", PotionEffectType.SLOW);
		nameToEffect.put("SLOWNESS", PotionEffectType.SLOW);
		nameToEffect.put("SLOWDIG", PotionEffectType.SLOW_DIGGING);
		nameToEffect.put("SLOWSWING", PotionEffectType.SLOW_DIGGING);
		nameToEffect.put("STRENGTH", PotionEffectType.INCREASE_DAMAGE);
		nameToEffect.put("REGEN", PotionEffectType.REGENERATION);
		nameToEffect.put("RESISTANCE", PotionEffectType.DAMAGE_RESISTANCE);
		nameToEffect.put("PROT", PotionEffectType.DAMAGE_RESISTANCE);
		nameToEffect.put("NAUSEA", PotionEffectType.CONFUSION);
	}

	public static PotionEffectType getEffect(String buffName){
		buffName = buffName.toUpperCase();
		PotionEffectType type = PotionEffectType.getByName(buffName);
		if (type != null){
			return type;
		}
		if (nameToEffect.containsKey(buffName))
			return nameToEffect.get(buffName);
		for (PotionEffectType pet : PotionEffectType.values()){
			if (pet == null)
				continue;
			if (pet.getName().replaceAll("_", "").equalsIgnoreCase(buffName))
				return pet;
		}
		buffName = buffName.replaceAll("_", "");
		type = PotionEffectType.getByName(buffName);
		if (type != null){
			return type;
		}
		if (nameToEffect.containsKey(buffName))
			return nameToEffect.get(buffName);
		return null;
	}

	public static String getCommonName(PotionEffect effect){
		if (effectToName.containsKey(effect.getType()))
			return effectToName.get(effect.getType()).toLowerCase();
		return effect.getType().getName().toLowerCase();
	}

	public static void enchantPlayer(Player player, Collection<PotionEffect> ewas){
		for (PotionEffect ewa : ewas){
            try {
                if (player.hasPotionEffect(ewa.getType())) {
                    player.removePotionEffect(ewa.getType());
                }
                player.addPotionEffect(ewa);
            } catch (Exception e ){
                if (!Defaults.DEBUG_VIRTUAL)
                    Log.printStackTrace(e);
            }
		}
	}

	public static String getEnchantString(Collection<PotionEffect> effects){
		StringBuilder sb = new StringBuilder();
		boolean first = true;
		for (PotionEffect pe : effects){
			if (!first) sb.append(", ");
			first = false;
			sb.append(getEnchantString(pe));
		}
		return sb.toString();
	}

	public static String getEnchantString(PotionEffect effect){
		int str = effect.getAmplifier();
		int tim = effect.getDuration();
		return getCommonName(effect) +":" + (str+1)+":"+tim/20;
	}


    public static PotionEffect parseArg(String arg, int defaultStrength, int defaultTimeSeconds) {
		if (arg.contains("{"))
			arg = arg.replaceFirst("=", " ");
		arg = arg.replaceAll("[}{]", "");
		arg = arg.replaceAll("(,|:)", " ");
		String split[] = arg.split(" ");
		PotionEffectType type = getEffect(split[0]);
		if (type == null)
			throw new IllegalArgumentException("PotionEffectType "+ arg +" not found");
		Integer strength = defaultStrength;
		Integer time = defaultTimeSeconds*20 /*convert to ticks*/;
		if (split.length > 1){try{strength = Integer.valueOf(split[1]) -1;} catch (Exception e){/* do nothing */}}
		if (split.length > 2){try{time = Integer.valueOf(split[2])*20/*ticks*/;} catch (Exception e){/* do nothing */}}
        if (time < 0) {
            time = Integer.MAX_VALUE;
        }
        return new PotionEffect(type,time, strength);
	}

	public static void deEnchantAll(Player p) {
		for (PotionEffectType pet: PotionEffectType.values()){
			if (pet == null)
				continue;
			if (p.hasPotionEffect(pet)){
				p.removePotionEffect(pet);
			}
		}
	}
}
