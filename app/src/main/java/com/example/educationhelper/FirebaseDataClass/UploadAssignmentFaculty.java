package com.example.educationhelper.FirebaseDataClass;

public class UploadAssignmentFaculty {
    String assignmentName,defaultAssignment, assignmentCount;

    public UploadAssignmentFaculty() {
    }

    public UploadAssignmentFaculty(String assignmentName, String defaultAssignment, String assignmentCount) {
        this.assignmentName = assignmentName;
        this.defaultAssignment = defaultAssignment;
        this.assignmentCount = assignmentCount;
    }

    public String getAssignmentName() {
        return assignmentName;
    }

    public void setAssignmentName(String assignmentName) {
        this.assignmentName = assignmentName;
    }

    public String getDefaultAssignment() {
        return defaultAssignment;
    }

    public void setDefaultAssignment(String defaultAssignment) {
        this.defaultAssignment = defaultAssignment;
    }

    public String getAssignmentCount() {
        return assignmentCount;
    }

    public void setAssignmentCount(String assignmentCount) {
        this.assignmentCount = assignmentCount;
    }
}
