package cn.hyzhhsh.wutheringwavesanalyse.pojo.entiy;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Role implements Serializable {
    private Long playerId;
    private String roleName;
    private String cardPoolId;
    private String serverId;
    private String recordId;
}
