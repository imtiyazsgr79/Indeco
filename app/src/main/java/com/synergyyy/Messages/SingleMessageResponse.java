package com.synergyyy.Messages;

public class SingleMessageResponse {

        private String createdDate;

        private String seenTime;

        private Extras extras;

        private String id;

        private String text;

        private String title;

        private String type;

        private String sent;

        private String seen;

        public String getCreatedDate ()
        {
            return createdDate;
        }

        public void setCreatedDate (String createdDate)
        {
            this.createdDate = createdDate;
        }

        public String getSeenTime ()
        {
            return seenTime;
        }

        public void setSeenTime (String seenTime)
        {
            this.seenTime = seenTime;
        }

        public Extras getExtras ()
        {
            return extras;
        }

        public void setExtras (Extras extras)
        {
            this.extras = extras;
        }

        public String getId ()
        {
            return id;
        }

        public void setId (String id)
        {
            this.id = id;
        }

        public String getText ()
        {
            return text;
        }

        public void setText (String text)
        {
            this.text = text;
        }

        public String getTitle ()
        {
            return title;
        }

        public void setTitle (String title)
        {
            this.title = title;
        }

        public String getType ()
        {
            return type;
        }

        public void setType (String type)
        {
            this.type = type;
        }

        public String getSent ()
        {
            return sent;
        }

        public void setSent (String sent)
        {
            this.sent = sent;
        }

        public String getSeen ()
        {
            return seen;
        }

        public void setSeen (String seen)
        {
            this.seen = seen;
        }

        @Override
        public String toString()
        {
            return "ClassPojo [createdDate = "+createdDate+", seenTime = "+seenTime+", extras = "+extras+", id = "+id+", text = "+text+", title = "+title+", type = "+type+", sent = "+sent+", seen = "+seen+"]";
        }
    }


