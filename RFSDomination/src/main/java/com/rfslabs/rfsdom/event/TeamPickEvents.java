package com.rfslabs.rfsdom.event;

import java.util.List;















import java.util.Random;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Sign;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Result;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import com.rfslabs.rfsdom.proj.ItemProjectile;
import com.rfslabs.rfsdom.proj.Particles;
import com.rfslabs.rfsdom.util.ConfigUtil;
import com.rfslabs.rfsdom.util.GeneralUtil;
import com.rfslabs.rfsdom.util.Generator;
import com.rfslabs.rfsdom.util.MainDAO;
import com.rfslabs.rfsdom.util.Outpost;
import com.rfslabs.rfsdom.util.PointsLink;
import com.rfslabs.rfsdom.util.ShieldHunger;
import com.rfslabs.rfsdom.util.Sunderer;

public class TeamPickEvents implements Listener{

	@SuppressWarnings("deprecation")
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onRightClick(final PlayerInteractEvent e){
		
		if(e.getAction() == Action.RIGHT_CLICK_BLOCK){
			
			if(e.getClickedBlock().getType() == Material.WALL_SIGN || e.getClickedBlock().getType() == Material.SIGN_POST){
				
				final Sign s = (Sign) e.getClickedBlock().getState();
				
				if(s.getLine(0).equals("Join TR")){
					
					MainDAO.guns.put(e.getPlayer().getUniqueId(), MainDAO.tr_smg);
					MainDAO.ammos.put(e.getPlayer().getUniqueId(), MainDAO.tr_smg_a);
					MainDAO.classes.put(e.getPlayer().getUniqueId(), "engineer");
					
					TeamPickEvents.this.join(e.getPlayer(), "terran republic");
					
					e.getPlayer().setDisplayName(ChatColor.RED + "[TR] " + ChatColor.RESET + e.getPlayer().getName());
					
				}else if(s.getLine(0).equals("Join VANU")){
					
					MainDAO.guns.put(e.getPlayer().getUniqueId(), MainDAO.vn_smg);
					MainDAO.ammos.put(e.getPlayer().getUniqueId(), MainDAO.vn_smg_a);
					MainDAO.classes.put(e.getPlayer().getUniqueId(), "engineer");
					
					TeamPickEvents.this.join(e.getPlayer(), "vanu sovereignty");
					
					e.getPlayer().setDisplayName(ChatColor.LIGHT_PURPLE + "[VANU] " + ChatColor.RESET + e.getPlayer().getName());
					
				}else if(s.getLine(0).equals("Join NC")){
					
					MainDAO.guns.put(e.getPlayer().getUniqueId(), MainDAO.nc_smg);
					MainDAO.ammos.put(e.getPlayer().getUniqueId(), MainDAO.nc_smg_a);
					MainDAO.classes.put(e.getPlayer().getUniqueId(), "engineer");
					
					TeamPickEvents.this.join(e.getPlayer(), "new conglomerate");
					
					e.getPlayer().setDisplayName(ChatColor.BLUE + "[NC] " + ChatColor.RESET + e.getPlayer().getName());
					
				}else if(s.getLine(0).equals("Generator")){
							Location genl = new Location(MainDAO.world, e.getClickedBlock().getLocation().getBlockX() , e.getClickedBlock().getLocation().getBlockY() - 1, e.getClickedBlock().getLocation().getBlockZ());
							final Generator g = GeneralUtil.getGenbyLoc(genl);//get generator
							
							//e.getPlayer().sendMessage("Gen x : " + genl.getX() + " y : " + genl.getY() + " z : " + genl.getZ());
							
							if(g == null){
								e.getPlayer().sendMessage(ChatColor.RED + "No generator found! Please contact the server administrators!");
								return;
							}
							
							if(g.captured.equals(MainDAO.players.get(e.getPlayer().getUniqueId()))){//if repair(g down)
								
								if(g.down){//if down , and engi repair
									
										if(MainDAO.classes.get(e.getPlayer().getUniqueId()).equals("engineer")){//if engi
									
										g.down = false;
										g.who = MainDAO.players.get(e.getPlayer().getUniqueId());
										s.setLine(1, ChatColor.GREEN + "Up");
										s.update(true);
										
										g.post.broadcast(ChatColor.GREEN + "Generator " + g.n + " got fixed!");
										
										ConfigUtil.addCerts(e.getPlayer().getUniqueId() , 20);
										
										g.colora(g.captured);
										
										g.overloaded = false;
										
										e.getPlayer().sendMessage(ChatColor.AQUA + "Generator fixed + 20 certs");
										
										//fixed
										
										}else{//only engis
											
											e.getPlayer().sendMessage(ChatColor.RED + "Only engineers can repair!");
											
										}
										
										
										
								}else if(g.t != null && g.t.getState() != Thread.State.TERMINATED){//if not yet destroyed
									
									if(MainDAO.classes.get(e.getPlayer().getUniqueId()).equals("engineer")){//if engi
									
									g.t.interrupt();
									g.t = null;
									g.down = false;
									g.overloaded = false;
									g.who = MainDAO.players.get(e.getPlayer().getUniqueId());
									s.setLine(1, ChatColor.GREEN + "Up");
									
									s.update(true);
									
									g.post.broadcast(ChatColor.GREEN + "Generator " + g.n + " was not overloaded!");
									
									//blocked destroy
									
									}else{//only engis
										
										e.getPlayer().sendMessage(ChatColor.RED + "Only engineers can stop an overload!");
										
									}
									
								}else{	
									
									e.getPlayer().sendMessage(ChatColor.RED + "Generator is already fine!");
									
								}
								
							}else if(!g.captured.equals(MainDAO.players.get(e.getPlayer().getUniqueId()))){//if opponents
								
								if(g.down == true){
									
									e.getPlayer().sendMessage(ChatColor.RED + "Generator is already destroyed!");
									
								}else if(g.overloaded){
									
									e.getPlayer().sendMessage(ChatColor.RED + "Generator is already overloaded!");
									
								}else if((g.t == null || g.t.getState() == Thread.State.TERMINATED || g.t.isInterrupted()) && g.down != true){//else , start overloading
									
									g.t = new Thread(){
										
									@Override
									public void run(){
									
									g.post.broadcast(ChatColor.RED + "Generator " + g.n + " got overloaded!");
									
									g.overloaded = true;
									
									g.who = MainDAO.players.get(e.getPlayer().getUniqueId());
									
									s.setLine(1, ChatColor.GOLD + "Overloaded");
									
									s.update(true);
									
									ConfigUtil.addCerts(e.getPlayer().getUniqueId() , 20);
									
									e.getPlayer().sendMessage(ChatColor.AQUA + "Generator overloaded + 20 certs");
										
									try {
										sleep(MainDAO.gen_rld * 1000);
										
										g.down = true;
										
										g.who = MainDAO.players.get(e.getPlayer().getUniqueId());
										
										g.overloaded = false;//
										
										s.setLine(1, ChatColor.RED + "Down");
										s.update(true);
										
										g.post.broadcast("Generator " + g.n + " got destroyed!");
										
										ConfigUtil.addCerts(e.getPlayer().getUniqueId() , 20);
										
										g.colora("none");
										
										e.getPlayer().sendMessage(ChatColor.AQUA + "Generator destroyed + 20 certs");//destroyed
										
									} catch (InterruptedException e) {
										
										//e.printStackTrace();
										
										return;
										
									}

									}
									
									};
									
									g.t.start();
									
								}
								
							}
							
				}else if(s.getLine(0).equals("Choose Gun")){
					
					if(MainDAO.players.get(e.getPlayer().getUniqueId()) == null){
						return;
					}
					
					if(s.getLine(3) != null && !s.getLine(3).equals("") && !MainDAO.players.get(e.getPlayer().getUniqueId()).equals(s.getLine(3))){
						e.getPlayer().sendMessage(ChatColor.RED + "You can't do that!");
						return;
					}
					
					if(s.getLine(1).equals("Sniper")){
						
						if(MainDAO.players.get(e.getPlayer().getUniqueId()).equals("terran republic")){
							
							MainDAO.guns.put(e.getPlayer().getUniqueId(), MainDAO.tr_sn);
							MainDAO.ammos.put(e.getPlayer().getUniqueId(), MainDAO.tr_sn_a);
							
							MainDAO.classes.put(e.getPlayer().getUniqueId(), "infiltrator");
							
						}else if(MainDAO.players.get(e.getPlayer().getUniqueId()).equals("vanu sovereignty")){
							
							MainDAO.guns.put(e.getPlayer().getUniqueId(), MainDAO.vn_sn);
							MainDAO.ammos.put(e.getPlayer().getUniqueId(), MainDAO.vn_sn_a);
							
							MainDAO.classes.put(e.getPlayer().getUniqueId(), "infiltrator");
							
						}else if(MainDAO.players.get(e.getPlayer().getUniqueId()).equals("new conglomerate")){
							
							MainDAO.guns.put(e.getPlayer().getUniqueId(), MainDAO.nc_sn);
							MainDAO.ammos.put(e.getPlayer().getUniqueId(), MainDAO.nc_sn_a);
							
							MainDAO.classes.put(e.getPlayer().getUniqueId(), "infiltrator");
						}
						
					}else if(s.getLine(1).equals("SMG")){
						
						if(MainDAO.players.get(e.getPlayer().getUniqueId()).equals("terran republic")){
							
							MainDAO.guns.put(e.getPlayer().getUniqueId(), MainDAO.tr_smg);
							MainDAO.ammos.put(e.getPlayer().getUniqueId(), MainDAO.tr_smg_a);
							
							MainDAO.classes.put(e.getPlayer().getUniqueId(), "engineer");
							
						}else if(MainDAO.players.get(e.getPlayer().getUniqueId()).equals("vanu sovereignty")){
							
							MainDAO.guns.put(e.getPlayer().getUniqueId(), MainDAO.vn_smg);
							MainDAO.ammos.put(e.getPlayer().getUniqueId(), MainDAO.vn_smg_a);
							
							MainDAO.classes.put(e.getPlayer().getUniqueId(), "engineer");
							
						}else if(MainDAO.players.get(e.getPlayer().getUniqueId()).equals("new conglomerate")){
							
							MainDAO.guns.put(e.getPlayer().getUniqueId(), MainDAO.nc_smg);
							MainDAO.ammos.put(e.getPlayer().getUniqueId(), MainDAO.nc_smg_a);
							
							MainDAO.classes.put(e.getPlayer().getUniqueId(), "engineer");
							
						}
						
					}else if(s.getLine(1).equals("Shotgun")){
						
						if(MainDAO.players.get(e.getPlayer().getUniqueId()).equals("terran republic")){
							
							MainDAO.guns.put(e.getPlayer().getUniqueId(), MainDAO.tr_shot);
							MainDAO.ammos.put(e.getPlayer().getUniqueId(), MainDAO.tr_shot_a);
							
							MainDAO.classes.put(e.getPlayer().getUniqueId(), "medic");
							
						}else if(MainDAO.players.get(e.getPlayer().getUniqueId()).equals("vanu sovereignty")){
							
							MainDAO.guns.put(e.getPlayer().getUniqueId(), MainDAO.vn_shot);
							MainDAO.ammos.put(e.getPlayer().getUniqueId(), MainDAO.vn_shot_a);
							
							MainDAO.classes.put(e.getPlayer().getUniqueId(), "medic");
							
						}else if(MainDAO.players.get(e.getPlayer().getUniqueId()).equals("new conglomerate")){
							
							MainDAO.guns.put(e.getPlayer().getUniqueId(), MainDAO.nc_shot);
							MainDAO.ammos.put(e.getPlayer().getUniqueId(), MainDAO.nc_shot_a);
							
							MainDAO.classes.put(e.getPlayer().getUniqueId(), "medic");
							
						}
						
					}else if(s.getLine(1).equals("Bazooka")){
						
						if(MainDAO.players.get(e.getPlayer().getUniqueId()).equals("terran republic")){	
							
							MainDAO.guns.put(e.getPlayer().getUniqueId(), MainDAO.tr_baz);
							MainDAO.ammos.put(e.getPlayer().getUniqueId(), MainDAO.tr_baz_a);
							
							MainDAO.classes.put(e.getPlayer().getUniqueId(), "heavy");
							
						}else if(MainDAO.players.get(e.getPlayer().getUniqueId()).equals("vanu sovereignty")){
							
							MainDAO.guns.put(e.getPlayer().getUniqueId(), MainDAO.vn_baz);
							MainDAO.ammos.put(e.getPlayer().getUniqueId(), MainDAO.vn_baz_a);
							
							MainDAO.classes.put(e.getPlayer().getUniqueId(), "heavy");
							
						}else if(MainDAO.players.get(e.getPlayer().getUniqueId()).equals("new conglomerate")){
							
							MainDAO.guns.put(e.getPlayer().getUniqueId(), MainDAO.nc_baz);
							MainDAO.ammos.put(e.getPlayer().getUniqueId(), MainDAO.nc_baz_a);
							
							MainDAO.classes.put(e.getPlayer().getUniqueId(), "heavy");
							
						}
						
					}	

					Bukkit.getScheduler().scheduleSyncDelayedTask(MainDAO.plugin, new Runnable(){

						@Override
						public void run() {
							
							if(!e.getPlayer().isDead()){
								GeneralUtil.addArmor(e.getPlayer(), MainDAO.players.get(e.getPlayer().getUniqueId()));
							}
							
						}
						
						
						
						
					}, 40L);
					
					
				}else if(s.getLine(0).equals("Resupply")){
					if(!MainDAO.players.get(e.getPlayer().getUniqueId()).equals("none")){
						
						
						if(s.getLine(3) != null && !s.getLine(3).equals("") && !MainDAO.players.get(e.getPlayer().getUniqueId()).equals(s.getLine(3))){
							e.getPlayer().sendMessage(ChatColor.RED + "You can't do that!");
							return;
						}
						
						if(s.getLine(1).equals("Free")){
							
								e.getPlayer().getInventory().setItem(7, MainDAO.mag);
								e.getPlayer().updateInventory();
								
						}else{
							
							int price = Integer.parseInt(s.getLine(1));
							
							if(ConfigUtil.getCerts(e.getPlayer().getUniqueId()) >= price){
								
								e.getPlayer().sendMessage(ChatColor.RED + "" + price + " certs have been removed from your account!");
								
								ConfigUtil.rmCerts(e.getPlayer().getUniqueId(), price);
								
								e.getPlayer().getInventory().setItem(7, MainDAO.mag);
								
								e.getPlayer().updateInventory();
								
							}else{
								e.getPlayer().sendMessage(ChatColor.RED + "Not enough certs");
							}
							
						}
						
						
					}
				}else if(s.getLine(0).equals("Sunderer")){
					
					int price = Integer.parseInt(s.getLine(1));
					
					if(ConfigUtil.getCerts(e.getPlayer().getUniqueId()) >= price){

						e.getPlayer().sendMessage(ChatColor.RED + "" + price + " certs have been removed from your account!");
						
						ConfigUtil.rmCerts(e.getPlayer().getUniqueId(), price);
						
						e.getPlayer().getInventory().setItem(4, MainDAO.sunderer);
						
						e.getPlayer().updateInventory();
					}else{
						e.getPlayer().sendMessage(ChatColor.RED + "Not enough certs");
					}
					
				}else if(s.getLine(0).equals("Grenade")){
					
					if(s.getLine(3) != null && !s.getLine(3).equals("") && !MainDAO.players.get(e.getPlayer().getUniqueId()).equals(s.getLine(3))){
						e.getPlayer().sendMessage(ChatColor.RED + "You can't do that!");
						return;
					}
					
					if(s.getLine(1).equals("Free")){
						e.getPlayer().getInventory().setItem(2, MainDAO.grenade);
						e.getPlayer().updateInventory();
						return;
					}
					
					int price = Integer.parseInt(s.getLine(1));
					
					if(ConfigUtil.getCerts(e.getPlayer().getUniqueId()) >= price){
						
						e.getPlayer().sendMessage(ChatColor.RED + "" + price + " certs have been removed from your account!");
						
						ConfigUtil.rmCerts(e.getPlayer().getUniqueId(), price);
						
						e.getPlayer().getInventory().setItem(2, MainDAO.grenade);
						
						e.getPlayer().updateInventory();
					}else{
						e.getPlayer().sendMessage(ChatColor.RED + "Not enough certs");
					}
					
				}else if(s.getLine(0).equals("Instant Action")){							
						
					e.getPlayer().sendMessage(ChatColor.RED + "Instant Action in 5 seconds!");
					
					Bukkit.getScheduler().scheduleSyncDelayedTask(MainDAO.plugin, new Runnable(){

						@Override
						public void run() {
						
							e.getPlayer().teleport(MainDAO.outposts.get(new Random().nextInt(MainDAO.outposts.size())).getInstantActionLocation(MainDAO.players.get(e.getPlayer().getUniqueId())));
							
						}
						
						
					}, 100L);
					
				}else if(s.getLine(0).equals("RFSPoints ->") && s.getLine(1).equals("Certs")){
					
					String[] st = s.getLine(2).split(" for ");
					
					int rfsp = Integer.parseInt(st[0]);
					
					int certs = Integer.parseInt(st[1]);
					
					if(PointsLink.rmRFSPts(e.getPlayer(), rfsp)){
						
						ConfigUtil.addCerts(e.getPlayer().getUniqueId(), certs);
						
						e.getPlayer().sendMessage(ChatColor.GREEN + "" + certs + " certs have been added to your account!");
						
					}
					
				}else if(s.getLine(0).equals("Upgrade")){
					
					int amount = Integer.parseInt(s.getLine(2));
					
					if(ConfigUtil.getCerts(e.getPlayer().getUniqueId()) >= amount){
					
						e.getPlayer().sendMessage(ChatColor.AQUA + "" + amount + " certs have been removed from your account!");
						ConfigUtil.armorUpgrade(e.getPlayer().getUniqueId() , s.getLine(1) , amount);
						e.getPlayer().sendMessage(ChatColor.GREEN + "Please relog!");
					
					}
				}
				
			}else if(e.getClickedBlock().getType() == Material.LAPIS_BLOCK){
				
				if(!MainDAO.players.get(e.getPlayer().getUniqueId()).equals("new conglomerate")){
					e.getPlayer().sendMessage(ChatColor.RED + "You can't do that!");
					return;
				}
				
				Location pl = e.getPlayer().getLocation();
				Location l = e.getClickedBlock().getLocation();
				
				int px , pz;
				
				px = (int) pl.getX();
				pz = (int) pl.getZ();
				
				int x , z;
				
				x = (int) l.getX();
				z = (int) l.getZ();
				
				int nx , nz;
				
				nx = x - px;
				
				nz = z - pz;
				
				if(nx == 0){
					
				}else if(nx > 0){
					nx ++;
				}else if(nx < 0){
					nx --;
				}
				
				if(nz == 0){
					
				}else if(nz > 0){
					nz ++;
				}else if(nz < 0){
					nz --;
				}
				
				Location newl = new Location(MainDAO.world,pl.getX() + nx,pl.getY() , pl.getZ() + nz, pl.getYaw() , pl.getPitch());
				e.getPlayer().teleport(newl);		
				
			}else if(e.getClickedBlock().getType() == Material.REDSTONE_BLOCK){
				
				if(!MainDAO.players.get(e.getPlayer().getUniqueId()).equals("terran republic")){
					
					e.getPlayer().sendMessage(ChatColor.RED + "You can't do that!");
					return;
					
				}
				
				Location pl = e.getPlayer().getLocation();
				Location l = e.getClickedBlock().getLocation();
				
				int px , pz;
				
				px = (int) pl.getX();
				pz = (int) pl.getZ();
				
				int x , z;
				
				x = (int) l.getX();
				z = (int) l.getZ();
				
				int nx , nz;
				
				nx = x - px;
				
				nz = z - pz;
				
				if(nx == 0){
					
				}else if(nx > 0){
					nx ++;
				}else if(nx < 0){
					nx --;
				}
				
				if(nz == 0){
					
				}else if(nz > 0){
					nz ++;
				}else if(nz < 0){
					nz --;
				}
				
				Location newl = new Location(MainDAO.world,pl.getX() + nx,pl.getY() , pl.getZ() + nz, pl.getYaw() , pl.getPitch());
				e.getPlayer().teleport(newl);
				
			}else if(e.getClickedBlock().getType() == Material.EMERALD_BLOCK){
				
				if(!MainDAO.players.get(e.getPlayer().getUniqueId()).equals("vanu sovereignty")){
					e.getPlayer().sendMessage(ChatColor.RED + "You can't do that!");
					return;
				}
				
				Location pl = e.getPlayer().getLocation();
				Location l = e.getClickedBlock().getLocation();
				
				int px , pz;
				
				px = (int) pl.getX();
				pz = (int) pl.getZ();
				
				int x , z;
				
				x = (int) l.getX();
				z = (int) l.getZ();
				
				int nx , nz;
				
				nx = x - px;
				
				nz = z - pz;
				
				if(nx == 0){
					
				}else if(nx > 0){
					nx ++;
				}else if(nx < 0){
					nx --;
				}
				
				if(nz == 0){
					
				}else if(nz > 0){
					nz ++;
				}else if(nz < 0){
					nz --;
				}
				
				Location newl = new Location(MainDAO.world,pl.getX() + nx,pl.getY() , pl.getZ() + nz, pl.getYaw() , pl.getPitch());
				e.getPlayer().teleport(newl);
				
			}
			
		}else if(e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK){
				
				if(e.getPlayer().getItemInHand() != null && e.getPlayer().getItemInHand().getItemMeta().getDisplayName() != null){
				
				if(e.getPlayer().getItemInHand().getItemMeta().getDisplayName().equals("TR Sniper")){
					
						if(e.getPlayer().getInventory().contains(MainDAO.ammo.getType(), 1)){
							
							e.getPlayer().getLocation().setPitch((float) (e.getPlayer().getLocation().getPitch() + 8.5));
					
							rmAmmo(e.getPlayer(), 120);
							
							e.getPlayer().getWorld().playSound(e.getPlayer().getLocation(), Sound.SILVERFISH_KILL, 10 , 1);
							new ItemProjectile("tr_sniper", e.getPlayer(), MainDAO.coal , 4);
					
						} else {
							
							tryReload(e.getPlayer(), 120);
							
						}
					
				}else if(e.getPlayer().getItemInHand().getItemMeta().getDisplayName().equals("VN Sniper")){
					
					if(e.getPlayer().getInventory().contains(MainDAO.ammo.getType(), 1)){
					
						rmAmmo(e.getPlayer(), 120);
						
						e.getPlayer().getWorld().playSound(e.getPlayer().getLocation(), Sound.SILVERFISH_KILL, 10 , 1);
						new ItemProjectile("vn_sniper", e.getPlayer(), MainDAO.coal , 4);
					
					} else {
					
						tryReload(e.getPlayer(), 120);
					
					}
					
				}else if(e.getPlayer().getItemInHand().getItemMeta().getDisplayName().equals("NC Sniper")){
					
					if(e.getPlayer().getInventory().contains(MainDAO.ammo.getType(), 1)){
						
						e.getPlayer().getLocation().setPitch((float) (e.getPlayer().getLocation().getPitch() + 4.5));
						
						rmAmmo(e.getPlayer(), 120);
						
						e.getPlayer().getWorld().playSound(e.getPlayer().getLocation(), Sound.SILVERFISH_KILL, 10, 1);
						new ItemProjectile("nc_sniper", e.getPlayer(), MainDAO.coal , 4);
					
					} else {
					
						tryReload(e.getPlayer(), 120);
					
					}
					
				}else if(e.getPlayer().getItemInHand().getItemMeta().getDisplayName().equals("TR Shotgun")){
					
					if(e.getPlayer().getInventory().contains(MainDAO.ammo.getType(), 1)){
						
						if(MainDAO.cant_shoot.contains(e.getPlayer().getUniqueId())){
							return;
						}
						
						e.getPlayer().getLocation().setPitch((float) (e.getPlayer().getLocation().getPitch() + 4));
						
						rmAmmo(e.getPlayer(), 120);
						
						e.getPlayer().getWorld().playSound(e.getPlayer().getLocation(), Sound.ZOMBIE_INFECT, 10, 1);
					
						new ItemProjectile("tr_shotgun", e.getPlayer(), MainDAO.egg , 1);
						new ItemProjectile("tr_shotgun", e.getPlayer(), MainDAO.egg , 1);
						new ItemProjectile("tr_shotgun", e.getPlayer(), MainDAO.egg , 1);
						new ItemProjectile("tr_shotgun", e.getPlayer(), MainDAO.egg , 1);
						new ItemProjectile("tr_shotgun", e.getPlayer(), MainDAO.egg , 1);
						
						delayShoot(e.getPlayer().getUniqueId() , 10);
					
					} else {
					
						tryReload(e.getPlayer(), 120);
				
					}
					
				}else if(e.getPlayer().getItemInHand().getItemMeta().getDisplayName().equals("VN Shotgun")){
					
					if(e.getPlayer().getInventory().contains(MainDAO.ammo.getType(), 1)){
						
						if(MainDAO.cant_shoot.contains(e.getPlayer().getUniqueId())){
							return;
						}
						
						rmAmmo(e.getPlayer(), 80);
						
						e.getPlayer().getWorld().playSound(e.getPlayer().getLocation(), Sound.ZOMBIE_INFECT, 10, 1);
					
						new ItemProjectile("vn_shotgun", e.getPlayer(), MainDAO.egg , 1);
						new ItemProjectile("vn_shotgun", e.getPlayer(), MainDAO.egg , 1);
						new ItemProjectile("vn_shotgun", e.getPlayer(), MainDAO.egg , 1);
						new ItemProjectile("vn_shotgun", e.getPlayer(), MainDAO.egg , 1);
						new ItemProjectile("vn_shotgun", e.getPlayer(), MainDAO.egg , 1);
						
						delayShoot(e.getPlayer().getUniqueId() , 10);
						
					} else {
						
						tryReload(e.getPlayer(), 80);
				
					}	
					
				}else if(e.getPlayer().getItemInHand().getItemMeta().getDisplayName().equals("NC Shotgun")){
					
					if(e.getPlayer().getInventory().contains(MainDAO.ammo.getType(), 1)){
						
						if(MainDAO.cant_shoot.contains(e.getPlayer().getUniqueId())){
							return;
						}
						
						e.getPlayer().getLocation().setPitch((float) (e.getPlayer().getLocation().getPitch() + 1.5));
						
						rmAmmo(e.getPlayer(), 100);
						
						e.getPlayer().getWorld().playSound(e.getPlayer().getLocation(), Sound.ZOMBIE_INFECT, 10, 1);
					
						new ItemProjectile("nc_shotgun", e.getPlayer(), MainDAO.egg , 1);
						new ItemProjectile("nc_shotgun", e.getPlayer(), MainDAO.egg , 1);
						new ItemProjectile("nc_shotgun", e.getPlayer(), MainDAO.egg , 1);
						new ItemProjectile("nc_shotgun", e.getPlayer(), MainDAO.egg , 1);
						new ItemProjectile("nc_shotgun", e.getPlayer(), MainDAO.egg , 1);
						
						delayShoot(e.getPlayer().getUniqueId() , 10);
					
					} else {
					
						tryReload(e.getPlayer(), 100);
			
					}
					
				}else if(e.getPlayer().getItemInHand().getItemMeta().getDisplayName().equals("TR SMG")){
					
					if(e.getPlayer().getInventory().contains(MainDAO.ammo.getType(), 1)){
						
						rmAmmo(e.getPlayer(), 120);
						
						e.getPlayer().getLocation().setPitch((float) (e.getPlayer().getLocation().getPitch() + 3));
						
						e.getPlayer().getWorld().playSound(e.getPlayer().getLocation(), Sound.CHICKEN_EGG_POP, 10, 1);
						new ItemProjectile("tr_smg", e.getPlayer(), MainDAO.sb , 2);
						
					} else {
						
						tryReload(e.getPlayer() , 120);
			
					}	
					
				}else if(e.getPlayer().getItemInHand().getItemMeta().getDisplayName().equals("VN SMG")){
					
					if(e.getPlayer().getInventory().contains(MainDAO.ammo.getType(), 1)){
						
						rmAmmo(e.getPlayer(), 80);
						
						e.getPlayer().getWorld().playSound(e.getPlayer().getLocation(), Sound.CREEPER_DEATH, 10, 1);
						new ItemProjectile("vn_smg", e.getPlayer(), MainDAO.sb , 2);
					
					} else {
					
						tryReload(e.getPlayer(),80);
		
					}
					
				}else if(e.getPlayer().getItemInHand().getItemMeta().getDisplayName().equals("NC SMG")){

					if(e.getPlayer().getInventory().contains(MainDAO.ammo.getType(), 1)){
						
						e.getPlayer().getLocation().setPitch((float) (e.getPlayer().getLocation().getPitch() + 0.5));
						
						rmAmmo(e.getPlayer(), 100);
					
						e.getPlayer().getWorld().playSound(e.getPlayer().getLocation(), Sound.CHICKEN_EGG_POP, 10, 1);
						new ItemProjectile("nc_smg", e.getPlayer(), MainDAO.sb , 2);
					
					} else {
					
						tryReload(e.getPlayer() , 100);
		
					}
					
				}else if(e.getPlayer().getItemInHand().getItemMeta().getDisplayName().equals("TR Bazooka")){
					
					if(e.getPlayer().getInventory().contains(MainDAO.ammo.getType(), 1)){
						
						e.getPlayer().getLocation().setPitch((float) (e.getPlayer().getLocation().getPitch() + 10));
						
						e.getPlayer().getWorld().playSound(e.getPlayer().getLocation(), Sound.GHAST_DEATH, 10, 1);
						
						rmAmmo(e.getPlayer(), 120);
					
						shootBall(e.getPlayer());
					
					} else {
					
						tryReload(e.getPlayer() , 120);
		
					}
					
				}else if(e.getPlayer().getItemInHand().getItemMeta().getDisplayName().equals("VN Bazooka")){
					
					if(e.getPlayer().getInventory().contains(MainDAO.ammo.getType(), 1)){
						
						e.getPlayer().getWorld().playSound(e.getPlayer().getLocation(), Sound.ZOMBIE_REMEDY, 10, 1);
						
						rmAmmo(e.getPlayer(), 120);
					
						shootBall(e.getPlayer());
					
					} else {
					
						tryReload(e.getPlayer(), 120);
		
					}
					
				}else if(e.getPlayer().getItemInHand().getItemMeta().getDisplayName().equals("NC Bazooka")){
					
					if(e.getPlayer().getInventory().contains(MainDAO.ammo.getType(), 1)){
						
						e.getPlayer().getWorld().playSound(e.getPlayer().getLocation(), Sound.HORSE_DEATH, 10, 1);
						
						e.getPlayer().getLocation().setPitch((float) (e.getPlayer().getLocation().getPitch() + 3));
						
						rmAmmo(e.getPlayer(), 120);
					
						shootBall(e.getPlayer());
					
					} else {
					
						tryReload(e.getPlayer(), 120);
		
					}
					
				}else if(e.getPlayer().getItemInHand().getItemMeta().getDisplayName().equals("Grenade")){
				
				if(e.getPlayer().getInventory().contains(MainDAO.grenade.getType() , 1)){
					
					rmGrenade(e.getPlayer());
					
					shootGrenade(e.getPlayer());
					
				}
				
				}else if(e.getPlayer().getItemInHand().getType() == Material.PAPER){
						
						if(MainDAO.can_sandy.contains(e.getPlayer().getUniqueId())){
							return;
						}
						
						boolean in = false;
						
						for(Outpost outp : MainDAO.outposts){
							
							if(outp.inBetween(e.getPlayer())){
								in = true;
								break;
							}
							
						}
						
						if(in){
							e.getPlayer().sendMessage(ChatColor.RED + "You can't spawn a sunderer inside an outpost!");
							return;
						}
							
						MainDAO.can_sandy.add(e.getPlayer().getUniqueId());
						
							final int curX = (int) e.getPlayer().getLocation().getX();
							final int curY = (int) e.getPlayer().getLocation().getY();
							final int curZ = (int) e.getPlayer().getLocation().getZ();
							
							Sunderer.spawnSandy(e.getPlayer());
							e.getPlayer().getInventory().setItem(4, null);
											
							e.getPlayer().sendMessage(ChatColor.RED + "You can't spawn another sunderer for 6 minutes!");
							
							Bukkit.getScheduler().scheduleSyncDelayedTask(MainDAO.plugin , new Runnable(){

								@Override
								public void run() {
									
									Sunderer.rmSandy(new Location(MainDAO.world , curX , curY , curZ));
									MainDAO.sandy.remove(e.getPlayer().getUniqueId());
									
								}
				
							}, 5 * 60* 20);
						
							
							Bukkit.getScheduler().scheduleSyncDelayedTask(MainDAO.plugin , new Runnable(){

								@Override
								public void run() {
								
									MainDAO.can_sandy.remove(e.getPlayer().getUniqueId());
								
								}
				
							}, 6 * 60* 20);
							
							return;
							
						}else if(e.getPlayer().getInventory().getItemInHand().getType() == Material.WHEAT){
							
							if(MainDAO.cant_ability.contains(e.getPlayer().getUniqueId())){
								e.getPlayer().sendMessage(ChatColor.RED + "Ability is on cooldown!");
								return;
							}
							
							e.getPlayer().setHealth(20);
							
							int reward = 0;
							
							List<Entity> es = e.getPlayer().getNearbyEntities(5, 2, 5);
							
							for(Entity en : es){
								
								if(en instanceof Player){
									
									if(MainDAO.players.get(e.getPlayer().getUniqueId()).equals(MainDAO.players.get(en.getUniqueId()))){
										
										if(((Player) en).getHealth() + 10 > 20){
											((Player) en).setHealth(20);
										}else{
										
										((Player) en).setHealth(((Player) en).getHealth() + 10);
										
										}
										
										((Player) en).playEffect(((Player) en).getLocation(), Effect.ENDER_SIGNAL, 1);
										
										((Player) en).updateInventory();
										
										reward++;
										
									}
									
								}
								
							}
							
							if(reward != 0){
							ConfigUtil.addCerts(e.getPlayer().getUniqueId(), reward);
							e.getPlayer().sendMessage(ChatColor.AQUA + "" +reward + " certs have been added to your account!");
							}
							
							MainDAO.cant_ability.add(e.getPlayer().getUniqueId());
							
							Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(MainDAO.plugin, new Runnable(){

								@Override
								public void run() {
									
									MainDAO.cant_ability.remove(e.getPlayer().getUniqueId());
									
								}			
								
								
							}, 60 * 20);
							
						}
				
				
				e.getPlayer().updateInventory();
				
			}
				
				
				
			}
			
	}

	private void delayShoot(final UUID name, int duration) {
		
		MainDAO.cant_shoot.add(name);
		
		
		Bukkit.getScheduler().scheduleSyncDelayedTask(MainDAO.plugin, new Runnable(){

			@Override
			public void run() {
				
				MainDAO.cant_shoot.remove(name);
				
			}			
			
		}, duration);
		
	}

	private void tryReload(final Player p, final long ticks) {
			
		
		if(!p.isDead()){
			
		if(!MainDAO.reloading.contains(p.getUniqueId())){

			if(p.getInventory().getItem(7) == null){
				
				p.sendMessage(ChatColor.RED + "No magazines!");
				return;
				
			}
			
			p.sendMessage(ChatColor.RED + "Reloading...");
			MainDAO.reloading.add(p.getUniqueId());
			
			Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(MainDAO.plugin, new Runnable(){

				@SuppressWarnings("deprecation")
				@Override
				public void run() {
					
					if(!p.isDead()){

					if(p.getInventory().getItem(7) != null)	{
						
					if(p.getInventory().getItem(7).getAmount() > 1){
					
					p.getInventory().getItem(7).setAmount(p.getInventory().getItem(7).getAmount() - 1);
					}else{
						p.getInventory().setItem(7, null);
					}

					p.getInventory().setItem(8 , MainDAO.ammos.get(p.getUniqueId()));
					
					p.getWorld().playSound(p.getLocation(), Sound.ORB_PICKUP, 1, 1);
					
					MainDAO.reloading.remove(p.getUniqueId());
					
					p.updateInventory();

					}
					
					}else{
						
						MainDAO.reloading.remove(p.getUniqueId());

					}
					
				}
				
				
				
			}, ticks);
		
		}else{
			
			p.sendMessage(ChatColor.RED + "Already reloading!");
			
		}
		
		
		}else{
			
		MainDAO.reloading.remove(p.getUniqueId());
		
		}
		
	}

	private void join(Player p, String team) {
		if(MainDAO.players.get(p.getUniqueId()).equals("none")){
			
			MainDAO.players.put(p.getUniqueId(), team);
			ConfigUtil.setPlayerTeam(p.getUniqueId(), team);
			p.sendMessage(ChatColor.GREEN + "You joined team " + team + "!");
			
			GeneralUtil.tpAndArmor(p.getPlayer() , team);		
			
			return;
		}
		p.sendMessage(ChatColor.RED + "You can not choose a team again!");
	}
	
	private void rmAmmo(final Player p, long timer){
		
		if(p.getInventory().getItem(8).getAmount() != 1){
		p.getInventory().getItem(8).setAmount(p.getInventory().getItem(8).getAmount() - 1);
		}else{
			p.getInventory().clear(8);
			tryReload(p , timer);
		}
	}
	
	private void rmGrenade(Player p){
		
		if(p.getInventory().getItem(2).getAmount() != 1){
		p.getInventory().getItem(2).setAmount(p.getInventory().getItem(2).getAmount() - 1);
		}else{
			p.getInventory().clear(2);
		}
		
	}
	
	private void shootBall(final Player p){
		
		//p.playSound(p.getLocation(), Sound.GHAST_FIREBALL, 1, 1);
		
		final Vector direction = p.getEyeLocation().getDirection().multiply(3);
		Fireball fireball = p.getWorld().spawn(p.getEyeLocation().add(direction.getX(), direction.getY(), direction.getZ()), Fireball.class);
		fireball.setShooter(p);
		fireball.setYield(0F);
		fireball.setIsIncendiary(false);
		
	}
	
	private void shootGrenade(final Player p){
		
		final Item i = p.getWorld().dropItem(p.getEyeLocation(), MainDAO.grenade);
		
		i.setVelocity(p.getEyeLocation().getDirection());
		
		Bukkit.getScheduler().scheduleSyncDelayedTask(MainDAO.plugin, new Runnable(){
			
			@Override
			public void run(){	
				
				List<Entity> es = i.getNearbyEntities(3, 3, 3);
				
				
				Particles.HUGE_EXPLOSION.display(i.getLocation(), 0, 0, 0, 1, 1);
				i.getWorld().playSound(i.getLocation(), Sound.EXPLODE, 10 , 1);
				i.remove();
				
				for(Entity e : es){
					
					if(e instanceof Player){
						
						if(((Player) e).getUniqueId().equals(p.getUniqueId()) || !MainDAO.players.get(((Player) e).getUniqueId()).equals(MainDAO.players.get(p.getUniqueId()))){
								  ShieldHunger.damagePlayer(((Player) e), p ,18);						
						}
					}
				}
				
			}
			
		}, 40);
		
	}
	
	@EventHandler
	public void onASD(PlayerInteractEvent e){
		
		if(e.getAction() == Action.RIGHT_CLICK_BLOCK){
		
		if(e.getPlayer().getItemInHand() != null && e.getPlayer().getItemInHand().getType() == Material.STICK){
			
			
			MainDAO.ls.add((int) e.getClickedBlock().getLocation().getX() +"-" + (int)e.getClickedBlock().getLocation().getY() +"-" + (int)e.getClickedBlock().getLocation().getZ());
			
			e.getClickedBlock().setType(Material.AIR);
			
			e.getPlayer().sendMessage("registered");
		}
		
		}
		
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerBuild(BlockBreakEvent e){
		if(!e.getPlayer().isOp()){
			e.setCancelled(true);
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerBuild(BlockPlaceEvent e){
		if(!e.getPlayer().isOp()){
			e.setCancelled(true);
		}
	}
	
}
