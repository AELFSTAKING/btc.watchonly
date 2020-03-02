package io.seg.kofo.bitcoinwo.biz.service.impl;


import io.seg.kofo.bitcoinwo.common.config.WatchOnlyProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author gin
 */
@Service
public class WalletService {

    private static final Logger logger = LoggerFactory.getLogger(WalletService.class);


    @Autowired
    private WatchOnlyProperties properties;





    public WatchOnlyProperties getProperties() {
        return properties;
    }

























}
