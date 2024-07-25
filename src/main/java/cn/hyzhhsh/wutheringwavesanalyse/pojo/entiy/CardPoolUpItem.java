package cn.hyzhhsh.wutheringwavesanalyse.pojo.entiy;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CardPoolUpItem {
    private String resourceType;
    private String name;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

}
