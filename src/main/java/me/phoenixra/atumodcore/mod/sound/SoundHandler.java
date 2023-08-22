package me.phoenixra.atumodcore.mod.sound;

import net.minecraft.client.Minecraft;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.FloatControl.Type;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SoundHandler {

    private static final Logger LOGGER = LogManager.getLogger();

    private static Map<String, Clip> sounds = new HashMap<>();
    private static boolean init = false;

    private static volatile boolean volumeHandling = true;
    private static List<String> unsupportedFormatAudios = new ArrayList<>();

    public static void init() {
        if (FMLCommonHandler.instance().getSide() != Side.CLIENT) {
            LOGGER.error("[AtumModCore] Tried to initialize SoundHandler server-side!");
            new Throwable().printStackTrace();
            return;
        }
        if (!init) {
            //Observation thread to check if Minecraft's master volume was changed and set the new volume to all registered sounds
            new Thread(() -> {
                float lastMaster = 0.0F;
                while (volumeHandling) {
                    try {
                        float currentMaster = Minecraft.getMinecraft().gameSettings.getSoundLevel(SoundCategory.MASTER);
                        if (lastMaster != currentMaster) {
                            SoundHandler.updateVolume();
                        }
                        lastMaster = currentMaster;
                        Thread.sleep(100);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();

            init = true;
        }
    }

    public static void registerSound(String key, String path) {
        if (unsupportedFormatAudios.contains(key)) {
            return;
        }
        if (FMLCommonHandler.instance().getSide() != Side.CLIENT) {
            LOGGER.error("[AtumModCore] Tried to register sound server-side!");
            new Throwable().printStackTrace();
            return;
        }
        if (!sounds.containsKey(key)) {
            AudioInputStream in = null;
            try {
                Clip c = AudioSystem.getClip();
                in = AudioSystem.getAudioInputStream(new File(path));
                c.open(in);
                in.close();
                sounds.put(key, c);
                setVolume(key, getMinecraftMasterVolume());
            } catch (IllegalArgumentException e) {
                LOGGER.error("[AtumModCore] Unable to register sound! Format not supported or invalid!");
                printErrorLog(e, key, 0, "registering audio");
                unsupportedFormatAudios.add(key);
                if (in != null) {
                    try {
                        in.close();
                    } catch (Exception e2) {
                        e2.printStackTrace();
                    }
                }
                sounds.remove(key);
            } catch (Exception e) {
                e.printStackTrace();
                if (in != null) {
                    try {
                        in.close();
                    } catch (Exception e2) {
                        e2.printStackTrace();
                    }
                }
                sounds.remove(key);
            }
        }
    }

    public static void unregisterSound(String key) {
        Clip sound = sounds.get(key);
        if (sound == null) {
            return;
        }
        sound.stop();
        sound.close();
        sounds.remove(key);
    }

    public static void playSound(String key) {
        Clip sound = sounds.get(key);
        if (sound == null) {
            return;
        }
        sound.start();
    }

    public static void stopSound(String key) {
        Clip sound = sounds.get(key);
        if (sound == null) {
            return;
        }
        sound.stop();
    }

    public static void resetSound(String key) {
        Clip sound = sounds.get(key);
        if (sound == null) {
            return;
        }
        sound.setMicrosecondPosition(0);
    }

    public static boolean soundExists(String key) {
        return sounds.containsKey(key);
    }

    public static void setLooped(String key, boolean looped) {
        Clip sound = sounds.get(key);
        if (sound == null) {
            return;
        }
        if (looped) {
            sound.setLoopPoints(0, -1);
            sound.loop(-1);
        } else {
            sound.loop(0);
        }
    }

    public static boolean isPlaying(String key) {
        Clip sound = sounds.get(key);
        if (sound == null) {
            return false;
        }
        return sound.isRunning();
    }

    private static void updateVolume() {
        for (String s : sounds.keySet()) {
            setVolume(s, getMinecraftMasterVolume());
        }
    }

    private static void setVolume(String key, int percentage) {
        if (!volumeHandling) {
            return;
        }
        try {
            Clip clip = sounds.get(key);
            if (clip == null) {
                return;
            }
            if (clip.isOpen()) {
                if (clip.isControlSupported(Type.MASTER_GAIN)) {
                    FloatControl f = ((FloatControl) sounds.get(key).getControl(Type.MASTER_GAIN));
                    int gain = (int) ((int) f.getMinimum() + ((f.getMaximum() - f.getMinimum()) / 100 * percentage));
                    f.setValue(gain);
                } else {
                    volumeHandling = false;
                    LOGGER.error("[AtumModCore] Unable to set sound volume! MASTER_GAIN control not supported! Disabling volume handling!");
                    printErrorLog(new Throwable(), key, percentage, "setting volume");
                }
            } else {
                LOGGER.error("[AtumModCore] Unable to set sound volume! Clip not open: " + key);
            }

        } catch (Exception e) {
            volumeHandling = false;
            LOGGER.error("[AtumModCore] Unable to set sound volume! Disabling volume handling!");
            printErrorLog(e, key, percentage, "setting volume");
        }
    }

    private static void printErrorLog(Throwable throwable, String audioKey, int audioVolume, String action) {
        CrashReport c = CrashReport.makeCrashReport(throwable, "Exception in AtumMod while " + action);
        CrashReportCategory cat = c.makeCategory("Audio Information");
        cat.addDetail("Key", () -> audioKey);
        cat.addDetail("Volume", () -> "" + audioVolume);
        LOGGER.error(c.getCompleteReport());
    }

    private static int getMinecraftMasterVolume() {
        return (int) (Minecraft.getMinecraft().gameSettings.getSoundLevel(SoundCategory.MASTER) * 100);
    }

}
