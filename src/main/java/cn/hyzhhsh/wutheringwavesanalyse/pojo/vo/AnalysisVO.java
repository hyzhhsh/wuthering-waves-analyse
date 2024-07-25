package cn.hyzhhsh.wutheringwavesanalyse.pojo.vo;

import cn.hyzhhsh.wutheringwavesanalyse.pojo.entiy.ConveneSummary;
import cn.hyzhhsh.wutheringwavesanalyse.pojo.entiy.RareConveneItem;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AnalysisVO {
    //各卡池出五星的记录
    Map<Integer, List<RareConveneItem>> rareConveneItems;
    //唤取总结
    ConveneSummary conveneSummary;
    private Long playerId;

}
