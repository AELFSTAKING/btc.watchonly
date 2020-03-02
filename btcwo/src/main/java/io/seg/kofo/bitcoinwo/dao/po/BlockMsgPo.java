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
public class BlockMsgPo extends BasePo{
    private static final long serialVersionUID = 1L;
    public static final String TABLE_ALIAS = "BlockMsg";


    //columns START
    /**
     * height       db_column: height
     */
    private Long height;
    /**
     * hash       db_column: hash
     */
    private String hash;
    /**
     * msg       db_column: msg
     */
    private String msg;
    /**
     * msgType       db_column: msg_type
     */
    private Integer msgType;
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
public BlockMsgPo(
        Long id,
        Date queryBeginDate,
        Date queryEndDate,
        int pageIndex,
        int pageSize,
        List<Order> orderBy,
        Long height,
        String hash,
        String msg,
        Integer msgType,
        Date createTime,
        Date updateTime
        ){
        super(id,queryBeginDate,queryEndDate,pageIndex,pageSize,orderBy);
        this.height=height;
        this.hash=hash;
        this.msg=msg;
        this.msgType=msgType;
        this.createTime=createTime;
        this.updateTime=updateTime;
    }

}

