package org.example.searadar.mr2313.message;

import ru.oogis.searadar.api.message.TrackedTargetMessage;
/**
 * Класс TTM
 */
public class Mr2313ttm extends TrackedTargetMessage {

    /**
     * поле интервал
     */
    private Long interval;


    public Mr2313ttm() { super(); }

    public void setInterval(Long interval) { this.interval = interval; }

    public Long getInterval() { return interval; }

    /**
     * метод возвращает информацию о TTM-сообщении
     * выводит его значение
     * @return возвращает строку с информацией о TTM-сообщении
     */
    @Override
    public String toString(){
        return "TrackedTargetMessage{" +
                "msgRecTime=" + getMsgRecTime() +
                ", msgTime=" + getMsgTime() +
                ", targetNumber=" + getTargetNumber() +
                ", distance=" + getDistance() +
                ", bearing=" + getBearing() +
                ", course=" + getCourse() +
                ", speed=" + getSpeed() +
                ", type=" + getType() +
                ", status=" + getStatus() +
                ", iff=" + getIff() +
                ", interval=" + getInterval() +
                '}';
    }
}
