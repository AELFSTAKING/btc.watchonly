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
public class MsgQueuePo extends BasePo{
    private static final long serialVersionUID = 1L;
    public static final String TABLE_ALIAS = "MsgQueue";


    //columns START
    /**
     * isCallback       db_column: is_callback
     */
    private Boolean isCallback;
    /**
     * msg       db_column: msg
     */
    private String msg;
    /**
     * msgType       db_column: msg_type
     */
    private Integer msgType;
    /**
     * lastCallbackTime       db_column: last_callback_time
     */
    private Date lastCallbackTime;
    /**
     * createTime       db_column: create_time
     */
    private Date createTime;
    /**
     * updateTime       db_column: update_time
     */
    private Date updateTime;
    /**
     * height       db_column: height
     */
    private Long height;
    //columns END
    //冗余一个字段方便记录blockcache值 但不必存入db
    private String blockHash;

    @Builder
    public MsgQueuePo(Long id, Date queryBeginDate, Date queryEndDate, int pageIndex, int pageSize, List<Order> orderBy, Boolean isCallback, String msg, Integer msgType, Date lastCallbackTime, Date createTime, Date updateTime, Long height, String blockHash) {
        super(id, queryBeginDate, queryEndDate, pageIndex, pageSize, orderBy);
        this.isCallback = isCallback;
        this.msg = msg;
        this.msgType = msgType;
        this.lastCallbackTime = lastCallbackTime;
        this.createTime = createTime;
        this.updateTime = updateTime;
        this.height = height;
        this.blockHash = blockHash;
    }
}

