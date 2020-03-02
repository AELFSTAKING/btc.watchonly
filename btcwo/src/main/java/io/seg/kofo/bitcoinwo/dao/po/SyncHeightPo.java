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
public class SyncHeightPo extends BasePo{
    private static final long serialVersionUID = 1L;
    public static final String TABLE_ALIAS = "SyncHeight";


    //columns START
    /**
     * 区块高度       db_column: sync_height
     */
    private Long syncHeight;
    /**
     * 对应高度的区块hash       db_column: block_hash
     */
    private String blockHash;
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
public SyncHeightPo(
        Long id,
        Date queryBeginDate,
        Date queryEndDate,
        int pageIndex,
        int pageSize,
        List<Order> orderBy,
        Long syncHeight,
        String blockHash,
        Date createTime,
        Date updateTime
        ){
        super(id,queryBeginDate,queryEndDate,pageIndex,pageSize,orderBy);
        this.syncHeight=syncHeight;
        this.blockHash=blockHash;
        this.createTime=createTime;
        this.updateTime=updateTime;
    }



}

