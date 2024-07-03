package org.example;

public class ReferenceVariables {
    private long id;

    private static ReferenceVariables referenceVariables;
    public static ReferenceVariables getInstance() {
        if(referenceVariables==null)
        {
            referenceVariables = new ReferenceVariables();
            return referenceVariables;
        }
        else {
            return referenceVariables;
        }
    }

    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }



}
