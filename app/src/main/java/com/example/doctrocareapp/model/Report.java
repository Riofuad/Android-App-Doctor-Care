package com.example.doctrocareapp.model;

import java.util.Date;

public class Report {
    private String reportID;
    private Date dateCreation;

    public Report(String reportID, Date dateCreation) {
        this.reportID = reportID;
        this.dateCreation = dateCreation;
    }

    public String getReportID() {
        return reportID;
    }

    public void setReportID(String reportID) {
        this.reportID = reportID;
    }

    public Date getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(Date dateCreation) {
        this.dateCreation = dateCreation;
    }
}
