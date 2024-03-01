package org.example.searadar.mr2313.station;

import org.example.searadar.mr2313.convert.Mr2313converter;
import org.apache.mina.filter.codec.textline.LineDelimiter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.example.searadar.mr231.convert.Mr231Converter;

import java.nio.charset.Charset;

/**
 * класс для станции MR-231-3
 */
public class Mr2313stationtype {
    private static final String STATION_TYPE = "МР-231-3";
    private static final String CODEC_NAME = "mr2313";


    protected void doInitialize() {
        TextLineCodecFactory textLineCodecFactory = new TextLineCodecFactory(
                Charset.defaultCharset(),
                LineDelimiter.UNIX,
                LineDelimiter.CRLF
        );

    }

    /**
     * Создание конвертера
     * @return  объект класса mr2313converter
     */

    public Mr2313converter createConverter() {
        return new Mr2313converter();
    }
}
