package com.github.ukkostaja.letterstat;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class DecoderTest {

    Logger logger = LoggerFactory.getLogger(this.getClass());
    @Test
    public void getUpdatedURLTest() throws IOException {
        logger.info(Decoder.getFileUrl());
    }
}
