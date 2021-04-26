package net.novaprison.Modules.Blood;

import net.minecraft.server.v1_8_R1.EnumParticle;
import net.minecraft.server.v1_8_R1.PacketPlayOutWorldParticles;
import net.novaprison.Modules.Toggle.ToggleManager;
import net.novaprison.Utils.SLAPI;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R1.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class BloodManager {

    public static HashMap<UUID, Blood> bloodData;
    public static HashMap<UUID, Power> bloodPower;

    public static enum Blood {
        regular(152, 0),
        america(152, 0, 80, 0, 22, 0),
        halloween(86, 0, 49, 0),
        christmas(133, 0, 152, 0),
        australia(41, 0, 133, 0);

        private int id1;
        private int data1;
        private int id2;
        private int data2;
        private int id3;
        private int data3;

        Blood(int xid1, int xdata1, int xid2, int xdata2, int xid3, int xdata3) {
            this.id1 = xid1;
            this.data1 = xdata1;
            this.id2 = xid2;
            this.data2 = xdata2;
            this.id3 = xid3;
            this.data3 = xdata3;
        }
        Blood(int xid1, int xdata1, int xid2, int xdata2) {
            this.id1 = xid1;
            this.data1 = xdata1;
            this.id2 = xid2;
            this.data2 = xdata2;
            this.id3 = 0;
            this.data3 = 0;
        }
        Blood(int xid1, int xdata1) {
            this.id1 = xid1;
            this.data1 = xdata1;
            this.id2 = 0;
            this.data2 = 0;
            this.id3 = 0;
            this.data3 = 0;
        }

        int getId(int i) {
            if(i == 1) {
                return id1;
            } else if(i == 2) {
                return id2;
            } else if(i == 3) {
                return id3;
            }
            return 0;
        }
        int getData(int i) {
            if(i == 1) {
                return data1;
            } else if(i == 2) {
                return data2;
            } else if(i == 3) {
                return data3;
            }
            return 0;
        }
    }
    public static enum Power {
        weak, regular, powerful
    }

    public static void load()
    {
        try {
            bloodData = SLAPI.load("plugins/NovaCore/blood/blooddata.bin");
        } catch (Exception e) {
            bloodData = new HashMap<UUID, Blood>();
        }
        try {
            bloodPower = SLAPI.load("plugins/NovaCore/blood/powerdata.bin");
        } catch (Exception e) {
            bloodPower = new HashMap<UUID, Power>();
        }
    }

    public static void save()
    {
        try {
            SLAPI.save(bloodData, "plugins/NovaCore/blood/blooddata.bin");
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            SLAPI.save(bloodPower, "plugins/NovaCore/blood/powerdata.bin");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void bleed(Player player)
    {
        Power power = bloodPower.get(player.getUniqueId());
        Blood blood = bloodData.get(player.getUniqueId());
        int blockID = 0;
        int blockID2 = 0;
        int blockID3 = 0;
        int blockData = 0;
        int block2Data = 0;
        int block3Data = 0;
        float xOff = 0;
        float yOff = 0;
        float zOff = 0;
        int amount = 0;

        blockID = blood.getId(1);
        blockID2 = blood.getId(2);
        blockID3 = blood.getId(3);
        blockData = blood.getData(1);
        block2Data = blood.getData(2);
        block3Data = blood.getData(3);
        switch(power) {
            case weak: {
                xOff = 0.3F;
                yOff = 0.7F;
                zOff = 0.3F;
                amount = 20;
            }
            case regular: {
                xOff = 0.5F;
                yOff = 0.9F;
                zOff = 0.5F;
                amount = 40;
            }
            case powerful: {
                xOff = 0.6F;
                yOff = 1F;
                zOff = 0.6F;
                amount = 60;
            }
        }
        Location loc = player.getLocation();
        PacketPlayOutWorldParticles packet1 = null;
        PacketPlayOutWorldParticles packet2 = null;
        PacketPlayOutWorldParticles packet3 = null;
        packet1 = new PacketPlayOutWorldParticles(EnumParticle.BLOCK_CRACK, true,
                (float) loc.getX(), (float) loc.getY(), (float) loc.getZ(), xOff, yOff, zOff, (float) blockData, amount, blockID, blockData);
        if(blockID2 != 0) {
            packet2 = new PacketPlayOutWorldParticles(EnumParticle.BLOCK_CRACK, true,
                    (float) loc.getX(), (float) loc.getY(), (float) loc.getZ(), xOff, yOff, zOff, (float) block2Data, amount, blockID2, block2Data);
        }
        if(blockID3 != 0) {
            packet3 = new PacketPlayOutWorldParticles(EnumParticle.BLOCK_CRACK, true,
                    (float) loc.getX(), (float) loc.getY(), (float) loc.getZ(), xOff, yOff, zOff, (float) block3Data, amount, blockID3, block3Data);
        }
        for(Entity e : player.getNearbyEntities(32, 500, 32)) {
            if(e instanceof Player) {
                Player p = (Player) e;
                if(ToggleManager.blood_toggle.get(p.getUniqueId())) {
                    (((CraftPlayer)p).getHandle().playerConnection).sendPacket(packet1);
                    if(packet2 != null) {
                        (((CraftPlayer)p).getHandle().playerConnection).sendPacket(packet2);
                    }
                    if(packet3 != null) {
                        (((CraftPlayer)p).getHandle().playerConnection).sendPacket(packet3);
                    }
                }
            }
        }
    }
}