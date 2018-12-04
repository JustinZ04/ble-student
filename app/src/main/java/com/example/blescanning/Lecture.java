package com.example.blescanning;

public class Lecture
{
    public String db_id;
    public String course_id;
    private String name;
    private String startTime;
    private String endTime;
    private String createdByProfNID;

    public Lecture(String db_id, String course_id, String name, String startTime, String endTime, String createdByProfNID)
    {
        this.db_id = db_id;
        this.course_id = course_id;
        this.name = name;
        this.startTime = startTime;
        this.endTime = endTime;
        this.createdByProfNID = createdByProfNID;
    }

    public String getCourse_id()
    {
        return course_id;
    }

    public String getName()
    {
        return name;
    }

    public String getStartTime()
    {
        return startTime;
    }

    public String getEndTime()
    {
        return endTime;
    }

    public String getCreatedByProfNID()
    {
        return createdByProfNID;
    }
}
