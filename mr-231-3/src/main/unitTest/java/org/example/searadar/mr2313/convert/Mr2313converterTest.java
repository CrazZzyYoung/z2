package org.example.searadar.mr2313.convert;

import org.example.searadar.mr2313.message.Mr2313ttm;
import org.example.searadar.mr2313.station.Mr2313stationtype;
import org.junit.Test;
import ru.oogis.searadar.api.message.RadarSystemDataMessage;
import ru.oogis.searadar.api.message.SearadarStationMessage;
import ru.oogis.searadar.api.types.IFF;
import ru.oogis.searadar.api.types.TargetStatus;
import ru.oogis.searadar.api.types.TargetType;

import java.util.List;

import static org.junit.Assert.*;

public class Mr2313converterTest {

    String message;
    Mr2313stationtype mr2313 = new Mr2313stationtype();
    Mr2313converter converter = mr2313.createConverter();
    List<SearadarStationMessage> searadarMessages;
    @Test
    public void TTMtest() {
        message = "$RATTM,66,28.71,341.1,T,57.6,024.5,T,0.4,4.1,N,b,L,,457362,А*42";

        searadarMessages = converter.convert(message);
        Mr2313ttm messageToGetTime = (Mr2313ttm) searadarMessages.get(0);

        Mr2313ttm ttm = new Mr2313ttm();
        ttm.setMsgRecTime(searadarMessages.get(0).getMsgRecTime());
        ttm.setMsgTime(messageToGetTime.getMsgTime());
        ttm.setTargetNumber(66);
        ttm.setDistance(28.71);
        ttm.setBearing(341.1);
        ttm.setCourse(24.5);
        ttm.setSpeed(57.6);
        ttm.setType(TargetType.UNKNOWN);
        ttm.setIff(IFF.FRIEND);
        ttm.setStatus(TargetStatus.LOST);
        ttm.setInterval((long)457362);

        assertEquals(ttm.toString()
                ,searadarMessages.get(0).toString());
    }
    @Test
    public void RSDtest(){
        message = "$RARSD,50.5,309.9,64.8,132.3,,,,,52.6,155.0,48.0,K,N,S*28";
        searadarMessages = converter.convert(message);

        RadarSystemDataMessage rsd = new RadarSystemDataMessage();
        rsd.setInitialDistance(50.5);
        rsd.setInitialBearing(309.9);
        rsd.setMovingCircleOfDistance(64.8);
        rsd.setBearing(132.3);
        rsd.setDistanceFromShip(52.6);
        rsd.setBearing2(155.0);
        rsd.setDistanceScale(48.0);
        rsd.setDistanceUnit("K");
        rsd.setDisplayOrientation("N");
        rsd.setWorkingMode("S");

        assertEquals( rsd.toString()
                ,searadarMessages.get(0).toString());
    }
    @Test
    public void RAVHWtest(){
        message = "$RAVHW, 115.6,T,,,46.0,N,,*71";
        searadarMessages = converter.convert(message);

        assertThrows(IndexOutOfBoundsException.class, () ->{
            System.out.println(searadarMessages.get(0).toString());
        });
    }
}