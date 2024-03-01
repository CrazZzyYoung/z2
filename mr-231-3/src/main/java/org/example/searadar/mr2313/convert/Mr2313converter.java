package org.example.searadar.mr2313.convert;


import org.apache.camel.Exchange;
import org.example.searadar.mr2313.message.Mr2313ttm;
import ru.oogis.searadar.api.convert.SearadarExchangeConverter;
import ru.oogis.searadar.api.message.*;
import ru.oogis.searadar.api.types.IFF;
import ru.oogis.searadar.api.types.TargetStatus;
import ru.oogis.searadar.api.types.TargetType;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;
/**
 * класс mr2313converter конвертирует NMEA в TTM и RSD
 */
public class Mr2313converter implements SearadarExchangeConverter {
    /**
     * в поле хранится шкала делений дистанции
     */
    private static final Double[] DISTANCE_SCALE = {0.125, 0.25, 0.5, 1.5, 3.0, 6.0, 12.0, 24.0, 48.0, 96.0};
    /**
     * массив полей NMEA
     */
    private String[] fields;
    /**
     * поле типа NMEA
     */
    private String msgType;
    /**
     * метод преобразования NMEA-предложения типа Exchange в лист сообщений msgList
     * @param exchange NMEA-предложение
     * @return лист полей и значений NMEA
     */
    @Override
    public List<SearadarStationMessage> convert(Exchange exchange) {
        return convert(exchange.getIn().getBody(String.class));
    }
    /**
     * метод преобразования NMEA-предложения строкового типа в лист сообщений msgList
     * идёт проверка на соответствие сообщения одному из двух типов TTM или RSD
     * @param message NMEA-предложение
     * @return лист полей и значений NMEA
     */
    public List<SearadarStationMessage> convert(String message) {

        List<SearadarStationMessage> msgList = new ArrayList<>();

        readFields(message);

        switch (msgType) {

            case "TTM" : msgList.add(getTTM());
                break;

            case "RSD" : {

                RadarSystemDataMessage rsd = getRSD();
                InvalidMessage invalidMessage = checkRSD(rsd);

                if (invalidMessage != null)  msgList.add(invalidMessage);
                else msgList.add(rsd);
                break;
            }

        }

        return msgList;
    }
    /**
     * метод чтения полей NMEA
     * изменение сообщение в массив подстрок
     * вырезая первые три символа и символы после знака *
     * и использует знак запятой как разделитель
     * @param msg NMEA строкового типа
     */

    private void readFields(String msg) {

        String temp = msg.substring( 3, msg.indexOf("*") ).trim();

        fields = temp.split(Pattern.quote(","));
        msgType = fields[0];

    }
    /**
     * метод получения сообщения TTM из NMEA
     *
     * @return массив полей и значений TTM
     */


    private SearadarStationMessage getTTM() {

        Mr2313ttm ttm = new Mr2313ttm();
        Long msgRecTimeMillis = System.currentTimeMillis();

        ttm.setMsgTime(msgRecTimeMillis);
        TargetStatus status = TargetStatus.UNRELIABLE_DATA;
        IFF iff = IFF.UNKNOWN;
        TargetType type = TargetType.UNKNOWN;

        switch (fields[12]) {
            case "L" : status = TargetStatus.LOST;
                break;

            case "Q" : status = TargetStatus.UNRELIABLE_DATA;
                break;

            case "T" : status = TargetStatus.TRACKED;
                break;
        }

        switch (fields[11]) {
            case "b" : iff = IFF.FRIEND;
                break;

            case "p" : iff = IFF.FOE;
                break;

            case "d" : iff = IFF.UNKNOWN;
                break;
        }

        ttm.setMsgRecTime(new Timestamp(System.currentTimeMillis()));
        ttm.setTargetNumber(Integer.parseInt(fields[1]));
        ttm.setDistance(Double.parseDouble(fields[2]));
        ttm.setBearing(Double.parseDouble(fields[3]));
        ttm.setCourse(Double.parseDouble(fields[6]));
        ttm.setSpeed(Double.parseDouble(fields[5]));
        ttm.setStatus(status);
        ttm.setIff(iff);

        ttm.setType(type);
        ttm.setInterval(Long.parseLong(fields[14]));
        return ttm;
    }
    /**
     * метод получения сообщения RSD из NMEA
     * @return массив полей и значений RSD-сообщения
     */

    private RadarSystemDataMessage getRSD() {

        RadarSystemDataMessage rsd = new RadarSystemDataMessage();

        rsd.setMsgRecTime(new Timestamp(System.currentTimeMillis()));
        rsd.setInitialDistance(Double.parseDouble(fields[1]));
        rsd.setInitialBearing(Double.parseDouble(fields[2]));
        rsd.setMovingCircleOfDistance(Double.parseDouble(fields[3]));
        rsd.setBearing(Double.parseDouble(fields[4]));
        rsd.setDistanceFromShip(Double.parseDouble(fields[9]));
        rsd.setBearing2(Double.parseDouble(fields[10]));
        rsd.setDistanceScale(Double.parseDouble(fields[11]));
        rsd.setDistanceUnit(fields[12]);
        rsd.setDisplayOrientation(fields[13]);
        rsd.setWorkingMode(fields[14]);

        return rsd;
    }
    /**
     * метод проверки RSD-сообщения
     * если шкала дальности в сообщении
     * не соотетствует ни одному из значений DISTANCE_SCALE,
     * то возвращается сообщение об ошибке InvalidMessage
     * @param rsd RSD-сообщение
     * @return сообщение об ошибке типа InvalidMessage либо null
     */
    private InvalidMessage checkRSD(RadarSystemDataMessage rsd) {

        InvalidMessage invalidMessage = new InvalidMessage();
        String infoMsg = "";

        if (!Arrays.asList(DISTANCE_SCALE).contains(rsd.getDistanceScale())) {

            infoMsg = "RSD message. Wrong distance scale value: " + rsd.getDistanceScale();
            invalidMessage.setInfoMsg(infoMsg);
            return invalidMessage;
        }

        return null;
    }

}
