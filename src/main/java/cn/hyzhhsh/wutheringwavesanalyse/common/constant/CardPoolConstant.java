package cn.hyzhhsh.wutheringwavesanalyse.common.constant;

import java.util.HashMap;

public class CardPoolConstant {
    public static HashMap<Integer, String> indexToName = new HashMap<>();
    public static HashMap<String, Integer> nameToIndex = new HashMap<>();

    static {
        indexToName.put(1, "角色精准调谐");
        indexToName.put(2, "武器精准调谐");
        indexToName.put(3, "角色调谐（常驻池）");
        indexToName.put(4, "武器调谐（常驻池）");
        indexToName.put(5, "新手调谐");
        indexToName.put(6, "6");
        indexToName.put(7, "7");

        nameToIndex.put("角色精准调谐", 1);
        nameToIndex.put("武器精准调谐", 2);
        nameToIndex.put("角色调谐（常驻池）", 3);
        nameToIndex.put("武器调谐（常驻池）", 4);
        nameToIndex.put("新手调谐", 5);
        nameToIndex.put("6", 6);
        nameToIndex.put("7", 7);
    }
}
