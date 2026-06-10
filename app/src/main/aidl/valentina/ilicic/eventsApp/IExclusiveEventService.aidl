package valentina.ilicic.eventsApp;

interface IExclusiveEventService {
    int getEventStatus();
    boolean isRegistrationOpen();
    long getRemainingTime();
}