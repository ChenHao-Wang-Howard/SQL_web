public class BMIRecord {
    private int id;
    private String bmiRange;
    private String status;
    private int count;
    private String percentage;

    public BMIRecord(int id, String bmiRange, String status, int count, String percentage) {
        this.id = id;
        this.bmiRange = bmiRange;
        this.status = status;
        this.count = count;
        this.percentage = percentage;
    }
    // Java constructor overloading
    public BMIRecord(String bmiRange, String status, int count, String percentage) {
        this.bmiRange = bmiRange;
        this.status = status;
        this.count = count;
        this.percentage = percentage;
    }

    public int getId() {
        return id;
    }

    public String getBmiRange() {
        return bmiRange;
    }

    public String getStatus() {
        return status;
    }

    public int getCount() {
        return count;
    }

    public String getPercentage() {
        return percentage;
    }


    
}
