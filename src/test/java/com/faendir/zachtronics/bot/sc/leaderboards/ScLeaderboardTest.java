package com.faendir.zachtronics.bot.sc.leaderboards;

import com.faendir.zachtronics.bot.BotTest;
import com.faendir.zachtronics.bot.model.UpdateResult;
import com.faendir.zachtronics.bot.sc.SpaceChemMarker;
import com.faendir.zachtronics.bot.sc.model.ScCategory;
import com.faendir.zachtronics.bot.sc.model.ScPuzzle;
import com.faendir.zachtronics.bot.sc.model.ScRecord;
import com.faendir.zachtronics.bot.sc.model.ScScore;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.*;

@BotTest(SpaceChemMarker.SpaceChemConfiguration.class)
public class ScLeaderboardTest {

    @Autowired
    private ScRedditLeaderboard scLeaderboard;

    @Test
    public void testGoodRecords() {
        ScRecord goodRecord = scLeaderboard.get(ScPuzzle.research_example_1, ScCategory.C).block();
        System.out.println(goodRecord);
        goodRecord = scLeaderboard.get(ScPuzzle.published_1_1, ScCategory.S).block();
        System.out.println(goodRecord);
        goodRecord = scLeaderboard.get(ScPuzzle.published_101_3, ScCategory.RC).block();
        System.out.println(goodRecord);
        assertNotNull(goodRecord);
    }

    @Test
    public void testBadRecord() {
        ScRecord badRecord = scLeaderboard.get(ScPuzzle.research_example_1, ScCategory.RC).block();
        assertNull(badRecord);
        badRecord = scLeaderboard.get(ScPuzzle.bonding_7, ScCategory.RCNB).block();
        assertNull(badRecord);
    }

    @Test
    @Disabled("Massive test only for manual testing")
    public void testFullIO() {
        for (ScPuzzle p : ScPuzzle.values()) {
            for (ScCategory c : ScCategory.values()) {
                if (c.supportsPuzzle(p)) {
                    ScRecord r = scLeaderboard.get(p, c).block();
                    if (r != null)
                        scLeaderboard.update(p, r);
                }
            }
            System.out.println("Done " + p.getDisplayName());
        }
    }

    @Test
    public void testUpdate() {
        // (**817**/2/104) | ← | (**819**/2/44)
        ScPuzzle p = ScPuzzle.sensing_4;
        ScScore s = new ScScore(819, 2, 43);
        ScRecord r = new ScRecord(s, "auth", "lnk", false);

        UpdateResult ur = scLeaderboard.update(p, r).block();
        assertTrue(ur instanceof UpdateResult.BetterExists);

        s.setPrecognitive(false);
        ur = scLeaderboard.update(p, r).block();
        assertTrue(ur instanceof UpdateResult.Success);
    }

}