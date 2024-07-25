package cn.hyzhhsh.wutheringwavesanalyse.pojo.entiy;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.HashMap;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConveneSummary implements Serializable {
    private long playerId;
    /**
     * 总抽数
     */
    private HashMap<Integer, Integer> cardPoolCost;
    /**
     * 出五星数
     */
    private HashMap<Integer, Integer> cardPoolGet;
    /**
     * 不歪数（只对角色精准调谐池有意义）
     */
    private HashMap<Integer, Integer> cardPoolTarget;
    /**
     * 已垫抽数
     */
    private HashMap<Integer, Integer> cardPoolPrepared;

}
