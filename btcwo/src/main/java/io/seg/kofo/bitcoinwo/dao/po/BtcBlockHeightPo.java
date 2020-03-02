package io.seg.kofo.bitcoinwo.dao.po;

import io.seg.framework.dao.model.BasePo;
import io.seg.framework.dao.model.Order;
import lombok.*;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@ToString(callSuper = true)
public class BtcBlockHeightPo extends BasePo{
    private static final long serialVersionUID = 1L;
    public static final String TABLE_ALIAS = "BtcBlockHeight";


    //columns START
    /**
     * nodeLatestBlockHeight       db_column: node_latest_block_height
     */
    private Long nodeLatestBlockHeight;
    /**
     * latestBlockHeight       db_column: latest_block_height
     */
    private Long latestBlockHeight;
    /**
     * lastCallBackHeight       db_column: last_call_back_height
     */
    private Long lastCallBackHeight;
    /**
     * lastCallBackTime       db_column: last_call_back_time
     */
    private Date lastCallBackTime;
    /**
     * createTime       db_column: create_time
     */
    private Date createTime;
    /**
     * updateTime       db_column: update_time
     */
    private Date updateTime;
    //columns END



@Builder
public BtcBlockHeightPo(
        Long id,
        Date queryBeginDate,
        Date queryEndDate,
        int pageIndex,
        int pageSize,
        List<Order> orderBy,
        Long nodeLatestBlockHeight,
        Long latestBlockHeight,
        Long lastCallBackHeight,
        Date lastCallBackTime,
        Date createTime,
        Date updateTime
        ){
        super(id,queryBeginDate,queryEndDate,pageIndex,pageSize,orderBy);
        this.nodeLatestBlockHeight=nodeLatestBlockHeight;
        this.latestBlockHeight=latestBlockHeight;
        this.lastCallBackHeight=lastCallBackHeight;
        this.lastCallBackTime=lastCallBackTime;
        this.createTime=createTime;
        this.updateTime=updateTime;
    }



}

