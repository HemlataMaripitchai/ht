package dumplingyzr.hearthtracker;

import java.util.InputMismatchException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import timber.log.Timber;

/**
 * Created by dumplingyzr on 2016/11/16.
 */

public class Card implements Comparable<String> {
    public static final String classNameList[] = {"Warrior", "Shaman", "Rogue", "Paladin", "Hunter", "Druid", "Warlock", "Mage", "Priest"};

    public static final String RARITY_LEGENDARY="LEGENDARY";
    public static final String TYPE_HERO="HERO";
    public static final String TYPE_UNKNOWN="TYPE_UNKNOWN";

    public static final int CLASS_INDEX_WARRIOR = 0;
    public static final int CLASS_INDEX_SHAMAN = 1;
    public static final int CLASS_INDEX_ROGUE = 2;
    public static final int CLASS_INDEX_PALADIN = 3;
    public static final int CLASS_INDEX_HUNTER = 4;
    public static final int CLASS_INDEX_DRUID = 5;
    public static final int CLASS_INDEX_WARLOCK = 6;
    public static final int CLASS_INDEX_MAGE = 7;
    public static final int CLASS_INDEX_PRIEST = 8;
    public static final int CLASS_INDEX_NEUTRAL = 9;
    public static final String TYPE_SPELL = "SPELL";

    public String name;
    public String id;
    public String playerClass;
    public Integer cost;
    public Integer attack;
    public Integer health;
    public Integer durability;
    public String rarity = "common";
    public String type = TYPE_UNKNOWN;
    public String text;
    public String race;
    public String set;
    public Boolean collectible = false;

    @Override
    public int compareTo(String o) {
        return id.compareTo(o);
    }

    @Override
    public String toString() {
        return name + "(" + id + ")";
    }

    public static Card unknown() {
        Card card = new Card();
        card.name = "unknown";
        card.id = "unknown";
        card.type = TYPE_UNKNOWN;
        card.cost = Integer.MAX_VALUE;
        return card;
    }

    public static int heroIdToClassIndex(String heroId) {

        Pattern pattern = Pattern.compile("hero_([0-9]*)[a-zA-Z]*", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(heroId);
        if (matcher.matches()) {
            try {
                int i = Integer.parseInt(matcher.group(1));
                i--;

                if (i >= 0 && i < 9) {
                    return i;
                }

            } catch (Exception e) {
                Timber.e("wrong heroId" + heroId);
            }
        }

        return -1;
    }

    public static String classIndexToPlayerClass(int classIndex) {
        switch (classIndex) {
            case CLASS_INDEX_WARRIOR:
                return "WARRIOR";
            case CLASS_INDEX_ROGUE:
                return "ROGUE";
            case CLASS_INDEX_SHAMAN:
                return "SHAMAN";
            case CLASS_INDEX_PALADIN:
                return "PALADIN";
            case CLASS_INDEX_HUNTER:
                return "HUNTER";
            case CLASS_INDEX_DRUID:
                return "DRUID";
            case CLASS_INDEX_WARLOCK:
                return "WARLOCK";
            case CLASS_INDEX_MAGE:
                return "MAGE";
            case CLASS_INDEX_PRIEST:
                return "PRIEST";
            default:
                return "NEUTRAL";
        }
    }

    public static String classIndexToHeroId(int classIndex) {
        return String.format("hero_%02d", classIndex + 1);
    }

    public static int playerClassToClassIndex(String playerClass) {
        for (int i = 0; i <= CLASS_INDEX_NEUTRAL; i++) {
            if (classIndexToPlayerClass(i).equals(playerClass)){
                return i;
            }
        }

        return -1;
    }
}

