package cn.hyzhhsh.wutheringwavesanalyse.pojo.dto;

import cn.hyzhhsh.wutheringwavesanalyse.pojo.entiy.ConveneItem;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse implements Serializable {
    private int code;
    private String message;
    private List<ConveneItem> data;
}
