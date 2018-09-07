package net.livefootball.util;

import net.livefootball.model.LeagueTable;

import java.util.Comparator;

public class TableComporatorImpl implements Comparator<LeagueTable> {
    @Override
    public int compare(LeagueTable o1, LeagueTable o2) {
        return o2.getPoints() - o1.getPoints();
    }
}
