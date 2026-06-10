package valentina.ilicic.eventsApp;

import android.os.RemoteException;

public class ExclusiveEventBinder extends IExclusiveEventService.Stub {

    private int eventStatus = 0;
    private boolean isRegistrationOpen = false;
    private long remainingTime = 0;

    @Override
    public int getEventStatus() throws RemoteException {
        return eventStatus;
    }
    public void setEventStatus(int status) {
        this.eventStatus = status;
    }

    @Override
    public boolean isRegistrationOpen() throws RemoteException {
        return isRegistrationOpen;
    }
    public void setRegistrationOpen(boolean open) {
        this.isRegistrationOpen = open;
    }

    @Override
    public long getRemainingTime() throws RemoteException {
        return remainingTime;
    }
    public void setRemainingTime(long remainingTime) {
        this.remainingTime = remainingTime;
    }
}