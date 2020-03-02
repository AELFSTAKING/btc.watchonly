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
public class BlockCachePo extends BasePo{
    private static final long serialVersionUID = 1L;
    public static final String TABLE_ALIAS = "BlockCache";


    //columns START
    /**
     * blockHash       db_column: block_hash
     */
    private String blockHash;
    /**
     * height       db_column: height
     */
    private Long height;
    /**
     * blockTime       db_column: block_time
     */
    private Date blockTime;
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
public BlockCachePo(
        Long id,
        Date queryBeginDate,
        Date queryEndDate,
        int pageIndex,
        int pageSize,
        List<Order> orderBy,
        String blockHash,
        Long height,
        Date blockTime,
        Date createTime,
        Date updateTime
        ){
        super(id,queryBeginDate,queryEndDate,pageIndex,pageSize,orderBy);
        this.blockHash=blockHash;
        this.height=height;
        this.blockTime=blockTime;
        this.createTime=createTime;
        this.updateTime=updateTime;
    }



}

