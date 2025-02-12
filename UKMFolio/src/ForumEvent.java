

public class ForumEvent {
    private String type;
    private Object data;

    public ForumEvent(String type, Object data) {
        this.type = type;
        this.data = data;
    }

    public String getType() { 
    	return type; 
    }
    public Object getData() { 
    	return data; 
    }
}