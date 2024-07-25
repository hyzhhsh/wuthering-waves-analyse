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
public class ConveneItem implements Serializable {
    private String cardPoolType;
    private long resourceId;
    private long qualityLevel;
    private String resourceType;
    private String name;
    private long count;
    private LocalDateTime time;
    private long timeKey;
    private long playerId;
}
