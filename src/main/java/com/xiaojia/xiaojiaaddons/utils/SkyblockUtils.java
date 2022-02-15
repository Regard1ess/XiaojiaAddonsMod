package com.xiaojia.xiaojiaaddons.utils;

import com.xiaojia.xiaojiaaddons.Events.TickEndEvent;
import com.xiaojia.xiaojiaaddons.Features.QOL.AutoLobby;
import com.xiaojia.xiaojiaaddons.Objects.ScoreBoard;
import com.xiaojia.xiaojiaaddons.XiaojiaAddons;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.xiaojia.xiaojiaaddons.utils.MinecraftUtils.getPlayer;

public class SkyblockUtils {
    private static final ArrayList<String> maps = new ArrayList<>(Arrays.asList(
            "Ruins", "Forest", "Mountain", "High Level", "Wilderness",
            "Dungeon Hub",
            "Hub", "Spider's Den", "Gunpowder Mines", "Void Sepulture", "Dragon's Nest",
            "The End", "The Mist", "Blazing Fortress", "The Catacombs", "Howling Cave",
            "Your Island", "None"
    ));
    private static final ArrayList<String> crystalHollowsMaps = new ArrayList<>(Arrays.asList(
            "Fairy Grotto",
            "Goblin Holdout", "Goblin Queen's Den",
            "Jungle", "Jungle Temple",
            "Mines of Divan", "Mithril Deposits",
            "Lost Precursor City", "Precursor Remnants",
            "Magma Fields", "Crystal Nucleus",
            "Khazad", "Dragon's Lair"
    ));
    public static int calculatedPing = -1;
    public static long[] pings = new long[]{-1, -1, -1, -1, -1};
    public static int pingsIndex = 0;
    private static String currentMap = "";
    private static String currentServer = "";
    private static boolean set = false;
    private static boolean isInCrystalHollows = false;
    private static String dungeon = "F6";
    private long lastPing;
    private Thread pingThread;

    private static String updateCurrentMap() {
        ArrayList<String> lines = ScoreBoard.getLines();
        if (lines.size() < 5) return "";
        String line = lines.get(lines.size() - 3) + lines.get(lines.size() - 4) + lines.get(lines.size() - 5);
        StringBuilder removeSkippingChar = new StringBuilder();
        line = ChatLib.removeFormatting(line);
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            if (c >= 'A' && c <= 'Z' || c >= 'a' && c <= 'z' || c >= '0' && c <= '9' || c == '\'' || c == ' '
                    || c == '(' || c == ')' || c == 'û')
                removeSkippingChar.append(c);
        }
        ChatLib.debug(line);
        line = removeSkippingChar.toString();
        ChatLib.debug(line);

        String result = "Others";
        for (String map : maps) {
            if (line.contains(map)) {
                result = map;
                break;
            }
        }
        isInCrystalHollows = false;
        if (result.equals("Others")) {
            for (String map : crystalHollowsMaps) {
                if (line.contains(map)) {
                    result = map;
                    isInCrystalHollows = true;
                }
            }
        }

        if (result.equals("The Catacombs")) {
            Pattern pattern = Pattern.compile("The Catacombs \\((.*)\\)");
            Matcher matcher = pattern.matcher(line);
            if (matcher.find()) {
                dungeon = matcher.group(1);
            }
        }
        return result;
    }

    public static String getDungeon() {
        return dungeon;
    }

    public static String getCurrentMap() {
        return currentMap;
    }

    public static void setCurrentMap(String map) {
        currentMap = map;
        set = true;
    }

    public static boolean isInSpiderDen() {
        return currentMap.equals("Spider's Den");
    }

    public static boolean isInDungeon() {
        return currentMap.equals("The Catacombs");
    }

    public static boolean isInMist() {
        return currentMap.equals("The Mist");
    }

    public static boolean isInDragon() {
        return currentMap.equals("Dragon's Nest");
    }

    public static boolean isInTheEnd() {
        return currentMap.equals("The End");
    }

    public static boolean isInVoidSepulture() {
        return currentMap.equals("Void Sepulture");
    }

    public static boolean isInEndIsland() {
        return isInDragon() || isInTheEnd() || isInVoidSepulture();
    }

    public static boolean isInGunpowderMines() {
        return currentMap.equals("Gunpowder Mines");
    }

    public static boolean isInHowlingCave() {
        return currentMap.equals("Howling Cave");
    }

    public static boolean isInRuin() {
        return currentMap.equals("Ruins");
    }

    public static boolean isInMountain() {
        return currentMap.equals("Mountain");
    }

    public static boolean isInForest() {
        return currentMap.equals("Forest");
    }

    public static boolean isInCrystalHollows() {
        return isInCrystalHollows;
    }

    public static String getCurrentServer() {
        return currentServer;
    }

    public static boolean isInSkyblock() {
        return ChatLib.removeFormatting(ScoreBoard.title).contains("SKYBLOCK");
    }

    public static int calcPing() {
        int cnt = 0;
        int sum = 0;
        for (long ping : pings) {
            if (ping != -1) {
                sum += ping;
                cnt++;
            }
        }
        if (cnt == 0) return -1;
        return sum / cnt;
    }

    public static int getPing() {
        return calculatedPing;
    }

    public static double getAccelerate() {
        return AutoLobby.getAccelerate();
    }

    public static double getMotionY() {
        return getPlayer().motionY;
    }

    public static double getVelocity() {
        return AutoLobby.getVelocity();
    }

    public static double getCurrentY() {
        return AutoLobby.getCurrentY();
    }

    @SubscribeEvent
    public void onTick(TickEndEvent event) {
        if (!set || !XiaojiaAddons.isDebug())
            currentMap = updateCurrentMap();
        if (isInSkyblock() && (pingThread == null || !pingThread.isAlive())) {
            long cur = TimeUtils.curTime();
            if (cur - lastPing > 1500) {
                lastPing = cur;
                pingThread = new Thread(() -> {
                    ChatLib.say("/whereami");
                    try {
                        Thread.sleep(60000);
                    } catch (InterruptedException e) {
                        long ping = TimeUtils.curTime() - lastPing;
                        pings[pingsIndex] = ping;
                        calculatedPing = calcPing();
                        pingsIndex = (pingsIndex + 1) % 5;
                    }
                });
                pingThread.start();
            }
        }
    }

    @SubscribeEvent
    public void pingChatReceived(ClientChatReceivedEvent event) {
        if (event.type != 0) return;
        if (ChatLib.removeFormatting(event.message.getUnformattedText()).startsWith("You are currently connected to server")
                && pingThread != null && pingThread.isAlive()) {
            pingThread.interrupt();
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void onChatReceived(ClientChatReceivedEvent event) {
        String message = event.message.getUnformattedText();
        Pattern pattern = Pattern.compile("^Sending to server (.*)\\.\\.\\..*");
        Matcher matcher = pattern.matcher(message);
        if (matcher.find()) {
            currentServer = matcher.group(1);
        }
        set = false;
    }
}
