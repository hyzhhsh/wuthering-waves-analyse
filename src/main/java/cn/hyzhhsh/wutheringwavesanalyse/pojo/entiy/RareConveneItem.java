package cn.hyzhhsh.wutheringwavesanalyse.pojo.entiy;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RareConveneItem implements Serializable {
    private String cardPoolType;
    private String resourceType;
    private String name;
    private int cost;
    private boolean target = true;
    private LocalDateTime time;
    private long timeKey;
    private long playerId;
}
