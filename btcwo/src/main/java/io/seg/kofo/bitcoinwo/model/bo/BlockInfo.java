package io.seg.kofo.bitcoinwo.model.bo;

import lombok.*;

import java.io.Serializable;
import java.util.List;

/**
 * 解析区块对象
 *
 * @author dongweizhao
 * @Date: 2018/9/27 下午3:28
 */
@Getter
@Setter
@NoArgsConstructor
@ToString(callSuper = true)
public class BlockInfo implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 0 分叉消息 1 区块消息
     */
    private int type;
    /**
     * 区块hash
     */
    private String hash;
    /**
     * 确认数
     */
    private int confirmations;
    private int size;
    /**
     * 区块高度
     */
    private int height;
    private int version;
    private String merkleroot;
    private long time;
    private int nonce;
    private String bits;
    private double difficulty;
    /**
     * 父级区块hash
     */
    private String previousblockhash;
    /**
     * 交易集合
     */
    private List<String> tx;

    private List<OmniTx> omniTx;

    @Builder
    public BlockInfo(int type, String hash, int confirmations, int size, int height, int version, String merkleroot, long time, int nonce, String bits, double difficulty, String previousblockhash, List<String> tx, List<OmniTx> omniTx) {
        this.type = type;
        this.hash = hash;
        this.confirmations = confirmations;
        this.size = size;
        this.height = height;
        this.version = version;
        this.merkleroot = merkleroot;
        this.time = time;
        this.nonce = nonce;
        this.bits = bits;
        this.difficulty = difficulty;
        this.previousblockhash = previousblockhash;
        this.tx = tx;
        this.omniTx = omniTx;
    }
}
