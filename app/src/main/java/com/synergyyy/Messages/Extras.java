package com.synergyyy.Messages;

public class Extras{

       private String workspace;

       private String id;

        String message;

       public String getWorkspace ()
       {
           return workspace;
       }

       public void setWorkspace (String workspace)
       {
           this.workspace = workspace;
       }

       public String getId ()
       {
           return id;
       }

       public void setId (String id)
       {
           this.id = id;
       }

       public String getMessage ()
       {
           return message;
       }

       public void setMessage (String message)
       {
           this.message = message;
       }

       @Override
       public String toString()
       {
           return "ClassPojo [workspace = "+workspace+", id = "+id+", message = "+message+"]";
       }


}
