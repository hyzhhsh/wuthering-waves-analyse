package cn.hyzhhsh.wutheringwavesanalyse.pojo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetByFileDto implements Serializable {
    private Long playerId;
    private String url;
}
