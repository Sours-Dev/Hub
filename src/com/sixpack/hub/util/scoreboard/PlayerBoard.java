package com.sixpack.hub.util.scoreboard;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import com.sixpack.hub.util.scoreboard.utils.ScoreboardProvider;
import com.sixpack.hub.util.scoreboard.utils.StringCache;
import com.sixpack.hub.util.scoreboard.utils.StringUtils;




public class PlayerBoard {

    private static final List<PlayerBoard> INSTANCES = new ArrayList<>();

    private volatile Player player;
    private volatile Scoreboard board;
    private volatile Objective objective;
    private volatile ScoreboardProvider provider;
    private volatile int old;
    private volatile boolean visible;

    public PlayerBoard(Player player) {
        this.player = player;

        Scoreboard board = player.getScoreboard();

        this.board = (board == null || board.equals(Bukkit.getScoreboardManager().getMainScoreboard()) ?
                Bukkit.getScoreboardManager().getNewScoreboard() : board);

        this.objective = this.board.getObjective(StringUtils.getScoreboardId(player) + "_sb");
        if (this.objective == null) {
            this.objective = this.board.registerNewObjective(
                    StringUtils.getScoreboardId(player) + "_sb", "dummy");
        }

        this.visible = true;

        INSTANCES.add(this);
    }

    public void updateTeam(Player player, String prefix, String suffix, boolean showInvis) {
        Team team = getTeam(player);

        if (prefix != null) team.setPrefix(prefix);
        else team.setPrefix("");

        if (suffix != null) team.setSuffix(suffix);
        else team.setSuffix("");

        team.setCanSeeFriendlyInvisibles(showInvis);
    }

    public ScoreboardProvider getProvider() {
        return provider;
    }

    public void setProvider(ScoreboardProvider provider) {
        this.provider = provider;
    }

    public void send() {
        if (player == null || !player.isOnline() || provider == null)
            return;

        String title = provider.getTitle(player);
        String header = provider.getHeader();
        String footer = provider.getFooter();

        if (!player.getScoreboard().equals(board))
            player.setScoreboard(board);

        if (objective.getDisplaySlot() != DisplaySlot.SIDEBAR)
            objective.setDisplaySlot(DisplaySlot.SIDEBAR);

        if (!objective.getDisplayName().equals(title))
            objective.setDisplayName(title);

        List<String> lines = provider.getLinesFor(player);

        if (lines.isEmpty()) {
            for (int i = 0; i <= old; i++) {
                Team t = getTeam(i);

                for (String s : t.getEntries())
                    board.resetScores(s);
            }

            return;
        }

        int size = lines.size();

        if (size < old) {
            for (int i = (size + 1 + (header != null ? 1 : 0) + (footer != null ? 1 : 0)); i <= old; i++) {
                Team t = getTeam(i);

                for (String s : t.getEntries())
                    board.resetScores(s);
            }
        }

        int max = 13 + (header == null ? 1 : 0) + (footer == null ? 1 : 0);
        if (size > max) {
            size = max;
        }

        int score = 1;
        int total = 0;

        for (int i = size - 1; i >= 0; i--) {
            String s = lines.get(i);

            handleEntry(s, (footer != null ? score + 1 : score));
            score++;
            total++;
        }

        if (header != null) {
            handleEntry(ChatColor.WHITE + header, score + 1);
            total++;
        }
        if (footer != null) {
            handleEntry(ChatColor.RESET + footer, 1);
            total++;
        }

        old = total;
    }

    private void handleEntry(String s, int index) {
        String[] array = StringCache.get(s);

        Team team = getTeam(index);
        if (!team.getPrefix().equals(array[0])) team.setPrefix(array[0]);
        if (!team.getSuffix().equals(array[1])) team.setSuffix(array[1]);

        for (String str : team.getEntries()) {
            objective.getScore(str).setScore(index);
        }
    }

    private Team getTeam(int index) {
        Team team = board.getTeam("Team-" + index);

        if (team == null) {
            team = board.registerNewTeam("Team-" + index);
            team.addEntry(ChatColor.values()[index] + "" + ChatColor.RESET);
        }

        return team;
    }

    private Team getTeam(Player player) {
        Team team = board.getTeam(/*"ETeam-" +*/ player.getName());

        if (team == null) {
            team = board.registerNewTeam(/*"ETeam-" +*/ player.getName());

            team.addEntry(player.getName());
//            extraTeamObjective.getScore(player.getName()).setScore(1);

        }
        return team;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean b) {
        this.visible = b;

        if (b) {
            objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        } else {
            board.clearSlot(DisplaySlot.SIDEBAR);//just delete it
        }
    }

    public static List<PlayerBoard> all1() {
        return INSTANCES;
    }

    public static PlayerBoard get1(Player player) {
        for (PlayerBoard board : INSTANCES) {
            if (board.player.getUniqueId().equals(player.getUniqueId()))
                return board;
        }

        return new PlayerBoard(player);
    }

    public static void dispose1(Player player) {
        PlayerBoard board = get1(player);

        for (int i = 0; i < 15; i++) {
            board.getTeam(i).unregister();
        }

        board.objective.unregister();

        INSTANCES.remove(board);
    }

    public static void dispose1() {
        for (PlayerBoard board : INSTANCES) {
            for (int i = 0; i < 15; i++) {
                board.getTeam(i).unregister();
            }

            board.objective.unregister();

            if (board.player != null && board.player.isOnline())
                board.player.setScoreboard(Bukkit.getScoreboardManager().getMainScoreboard());
        }

        INSTANCES.clear();
    }





    private Team getTeam1(Player player) {
        Team team = board.getTeam(/*"ETeam-" +*/ player.getName());

        if (team == null) {
            team = board.registerNewTeam(/*"ETeam-" +*/ player.getName());

            team.addEntry(player.getName());
//            extraTeamObjective.getScore(player.getName()).setScore(1);

        }
        return team;
    }

    public boolean isVisible1() {
        return visible;
    }

    public void setVisible1(boolean b) {
        this.visible = b;

        if (b) {
            objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        } else {
            board.clearSlot(DisplaySlot.SIDEBAR);
        }
    }

    public static List<PlayerBoard> all() {
        return INSTANCES;
    }

    public static PlayerBoard get(Player player) {
        for (PlayerBoard board : INSTANCES) {
            if (board.player.getUniqueId().equals(player.getUniqueId()))
                return board;
        }

        return new PlayerBoard(player);
    }

    public static void dispose(Player player) {
        PlayerBoard board = get1(player);

        for (int i = 0; i < 15; i++) {
            board.getTeam(i).unregister();
        }

        board.objective.unregister();

        INSTANCES.remove(board);
    }

    public static void dispose() {
        for (PlayerBoard board : INSTANCES) {
            for (int i = 0; i < 15; i++) {
                board.getTeam(i).unregister();
            }

            board.objective.unregister();

            if (board.player != null && board.player.isOnline())
                board.player.setScoreboard(Bukkit.getScoreboardManager().getMainScoreboard());
        }

        INSTANCES.clear();
    }


}
