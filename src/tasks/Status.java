package tasks;

public class Status {
    private final static String newStatus = "NEW";
    private final static String inProgressStatus = "IN_PROGRESS";
    private final static String doneStatus = "DONE";

    public String getNewStatus() {
        return newStatus;
    }

    public String getInProgressStatus() {
        return inProgressStatus;
    }

    public String getDoneStatus() {
        return doneStatus;
    }
}